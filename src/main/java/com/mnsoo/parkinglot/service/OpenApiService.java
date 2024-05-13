package com.mnsoo.parkinglot.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnsoo.parkinglot.distance.DistanceCalculator;
import com.mnsoo.parkinglot.domain.dto.ParkingLotDTO;
import com.mnsoo.parkinglot.domain.persist.ParkingLotEntity;
import com.mnsoo.parkinglot.repository.ParkingLotRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenApiService {
    @Value("${openapi.key}")
    private String apikey;

    private final ParkingLotRepository parkingLotRepository;

    public OpenApiService(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void autoSaveParkingLotData(){
        var result = getAllParkingLot(true);
        if(result != null){
            System.out.println("data updated successfully");
        }
    }

    public List<ParkingLotDTO> getAllParkingLot(boolean isUpdate) {
        int startIndex = 1;
        int endIndex = 1000;  // API가 한 번에 반환할 수 있는 최대 결과 수
        List<ParkingLotDTO> totalResponse = new ArrayList<>();

        boolean isEndOfData = false;
        while (true) {
            String apiUrl = "http://openapi.seoul.go.kr:8088/"
                    + apikey
                    + "/json/GetParkingInfo/"
                    + startIndex + "/"
                    + endIndex + "/";

            try {
                List<ParkingLotDTO> tmp = new ArrayList<>();
                String result = getApiResponse(apiUrl);
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
                JSONObject parkingInfo = (JSONObject) jsonObject.get("GetParkingInfo");
                JSONArray infoArr = (JSONArray) parkingInfo.get("row");
                for (Object item : infoArr) {
                    JSONObject object = (JSONObject) item;
                    String parkingCode = object.get("PARKING_CODE").toString().trim();
                    if(parkingCode.isEmpty()){
                        isEndOfData = true;
                        break;
                    }
                    ParkingLotDTO parkingLot = createParkingLotDTO(object);
                    tmp.add(parkingLot);
                }

                if (isUpdate) {
                    processApiResponse(tmp);
                }

                totalResponse.addAll(tmp);
                if(isEndOfData) break;
                startIndex = endIndex + 1;
                endIndex += 1000;
            } catch (Exception e) {
                throw new RuntimeException("Failed to get response", e);
            }
        }

        return totalResponse;
    }

    private String getApiResponse(String apiUrl) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        return response.getBody();
    }

    private void processApiResponse(List<ParkingLotDTO> parkingLots) {
        try {
            for (ParkingLotDTO parkingLotDTO : parkingLots) {
                ParkingLotEntity parkingLot = parkingLotRepository.findByParkingCode(parkingLotDTO.getParkingCode());
                if (parkingLot == null) {
                    parkingLot = ParkingLotEntity.builder()
                            .parkingCode(parkingLotDTO.getParkingCode())
                            .build();
                }

                parkingLot.setParkingName(parkingLotDTO.getParkingName());
                parkingLot.setAddress(parkingLotDTO.getAddress());
                parkingLot.setTel(parkingLotDTO.getTel());
                parkingLot.setLat(parkingLotDTO.getLat());
                parkingLot.setLng(parkingLotDTO.getLng());

                parkingLotRepository.save(parkingLot);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ParkingLotDTO createParkingLotDTO(JSONObject object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);
        mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ParkingLotDTO parkingLotDTO = mapper.convertValue(object, ParkingLotDTO.class);
        return parkingLotDTO;
    }

    public List<ParkingLotEntity> searchParkingLots(String query){
        return parkingLotRepository.findByParkingNameContaining(query);
    }

    public ParkingLotDTO searchSpecifically(String code) {
        List<ParkingLotDTO> allParkingLotData = getAllParkingLot(false);

        for (ParkingLotDTO parkingLot : allParkingLotData) {
            String parkingCode = String.valueOf(parkingLot.getParkingCode()).trim();
            if (parkingCode.equals(code)) {
                return parkingLot;
            }
        }

        return null;
    }

    public List<ParkingLotEntity> searchNearbyParkingLots(double userLat, double userLng, double radius){
        List<ParkingLotEntity> allParkingLots = parkingLotRepository.findAll();

        return allParkingLots.stream()
                .filter(parkingLot -> DistanceCalculator
                        // [radius]km 이내의 주차장만 필터링
                        .calculateDistance(userLat, userLng, parkingLot.getLat(), parkingLot.getLng()) <= radius)
                .collect(Collectors.toList());
    }
}
