package com.mnsoo.parkinglot.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnsoo.parkinglot.domain.dto.ParkingLotDTO;
import com.mnsoo.parkinglot.domain.persist.ParkingLotEntity;
import com.mnsoo.parkinglot.repository.ParkingLotRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OpenApiService {

    private static final Logger logger = LoggerFactory.getLogger(OpenApiService.class);

    @Value("${openapi.key}")
    private String apikey;

    private final ParkingLotRepository parkingLotRepository;

    public OpenApiService(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void autoSaveParkingLotData(){
        var result = getAllParkingLot(true);
        if(!result.isEmpty()){
            logger.info("Data updated successfully");
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
                    if (parkingLot != null) {
                        tmp.add(parkingLot);
                    } else {
                        logger.error("Failed to convert JSONObject to ParkingLotDTO: {}", object.toJSONString());
                    }
                }

                if (isUpdate) {
                    processApiResponse(tmp);
                }

                totalResponse.addAll(tmp);
                if(isEndOfData) break;
                startIndex = endIndex + 1;
                endIndex += 1000;
            } catch (Exception e) {
                logger.error("Failed to get response", e);
                throw new RuntimeException("Failed to get response", e);
            }
        }
        logger.info("Retrieved {} parking lots", totalResponse.size());
        return totalResponse;
    }

    private String getApiResponse(String apiUrl) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        return response.getBody();
    }

    public void processApiResponse(List<ParkingLotDTO> parkingLots) {
        try {
            for (ParkingLotDTO parkingLotDTO : parkingLots) {
                Optional<ParkingLotEntity> optionalParkingLot = parkingLotRepository.findByParkingCode(parkingLotDTO.getParkingCode());
                ParkingLotEntity parkingLot;
                parkingLot = optionalParkingLot.orElseGet(() -> ParkingLotEntity.builder()
                        .parkingCode(parkingLotDTO.getParkingCode())
                        .parkingName(parkingLotDTO.getParkingName())
                        .build());

                parkingLot.setAddress(parkingLotDTO.getAddress());
                parkingLot.setTel(parkingLotDTO.getTel());
                parkingLot.setLat(parkingLotDTO.getLat());
                parkingLot.setLng(parkingLotDTO.getLng());

                parkingLotRepository.save(parkingLot);
            }
        } catch (Exception e) {
            logger.error("Error processing API response", e);
            throw new RuntimeException(e);
        }
    }

    private ParkingLotDTO createParkingLotDTO(JSONObject object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);
        mapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            ParkingLotDTO parkingLotDTO = mapper.convertValue(object, ParkingLotDTO.class);
            return parkingLotDTO;
        } catch (Exception e) {
            logger.error("Error converting JSONObject to ParkingLotDTO", e);
            return null;
        }
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

    public Page<ParkingLotEntity> searchNearbyParkingLots(int page, int size, double userLat, double userLng, int radius){
        Pageable pageable = PageRequest.of(page, size);
        Page<ParkingLotEntity> nearbyParkingLots = parkingLotRepository.findNearbyParkingLots(userLat, userLng, radius, pageable);

        return nearbyParkingLots;
    }
}
