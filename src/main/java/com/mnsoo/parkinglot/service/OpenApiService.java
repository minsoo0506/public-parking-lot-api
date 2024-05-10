package com.mnsoo.parkinglot.service;

import com.mnsoo.parkinglot.domain.persist.ParkingLotEntity;
import com.mnsoo.parkinglot.repository.ParkingLotRepository;
import io.swagger.models.auth.In;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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
        String result = getAllParkingLot(true);
        if(result != null){
            System.out.println("data updated successfully");
        }
    }

    public String getAllParkingLot(boolean isUpdate) {
        int startIndex = 1;
        int endIndex = 1000;  // API가 한 번에 반환할 수 있는 최대 결과 수
        JSONArray totalResponse = new JSONArray();

        while (true) {
            String apiUrl = "http://openapi.seoul.go.kr:8088/"
                    + apikey
                    + "/json/GetParkingInfo/"
                    + startIndex + "/"
                    + endIndex + "/";

            try {
                String result = getApiResponse(apiUrl);
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
                JSONObject parkingInfo = (JSONObject) jsonObject.get("GetParkingInfo");
                JSONArray infoArr = (JSONArray) parkingInfo.get("row");
                totalResponse.addAll(infoArr);
                if (!processApiResponse(result, isUpdate)) {
                    break;
                }
                startIndex = endIndex + 1;
                endIndex += 1000;
            } catch (Exception e) {
                return "failed to get response";
            }
        }

        return totalResponse.toString();
    }

    private String getApiResponse(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        BufferedReader br;
        if(responseCode == 200){
            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        } else {
            br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        String result = br.readLine();
        br.close();

        return result;
    }

    private boolean processApiResponse(String result, boolean isUpdate) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject parkingInfo = (JSONObject) jsonObject.get("GetParkingInfo");

            JSONObject subResult = (JSONObject) parkingInfo.get("RESULT");
            JSONArray infoArr = (JSONArray) parkingInfo.get("row");

            System.out.println(subResult);

            for (int i = 0; i < infoArr.size(); i++) {
                JSONObject tmp = (JSONObject) infoArr.get(i);

                String parkingCode = tmp.get("PARKING_CODE").toString();
                if (parkingCode == null || parkingCode.equals("")) {
                    return false;
                }

                if (isUpdate) {
                    ParkingLotEntity parkingLot = parkingLotRepository.findByParkingCode(Integer.parseInt(parkingCode));
                    if (parkingLot == null) {
                        parkingLot = ParkingLotEntity.builder()
                                .parkingCode(Integer.parseInt(parkingCode))
                                .build();
                    }

                    parkingLot.setParkingName(tmp.get("PARKING_NAME").toString());
                    parkingLot.setAddress(tmp.get("ADDR").toString());
                    parkingLot.setTel(tmp.get("TEL").toString());
                    parkingLot.setLat(Double.parseDouble(tmp.get("LAT").toString()));
                    parkingLot.setLng(Double.parseDouble(tmp.get("LNG").toString()));

                    parkingLotRepository.save(parkingLot);
                }
            }
            return true;
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public List<ParkingLotEntity> searchParkingLots(String query){
        return parkingLotRepository.findByParkingNameContaining(query);
    }

    public Object searchSpecifically(String code) {
        String allParkingLotData = getAllParkingLot(false);
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) jsonParser.parse(allParkingLotData);
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            Object item = jsonArray.get(i);
            if (!(item instanceof JSONObject)) {
                throw new IllegalArgumentException("Array item is not a JSONObject: " + item);
            }
            JSONObject object = (JSONObject) item;
            String parkingCode = object.get("PARKING_CODE").toString().trim();
            if (parkingCode.equals(code)) {
                return object;
            }
        }

        return "No data found for parking code: " + code;
    }
}
