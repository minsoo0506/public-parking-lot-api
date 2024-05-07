package com.mnsoo.parkinglot.service;

import com.mnsoo.parkinglot.domain.persist.ParkingLotEntity;
import com.mnsoo.parkinglot.repository.ParkingLotRepository;
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
        String result = getParkingLotInfo();
        System.out.println(result);
    }

    public String getParkingLotInfo() {
        int startIndex = 1;
        int endIndex = 1000;  // API가 한 번에 반환할 수 있는 최대 결과 수
        StringBuilder totalResponse = new StringBuilder();

        while (true) {
            String apiUrl = "http://openapi.seoul.go.kr:8088/"
                    + apikey
                    + "/json/GetParkingInfo/"
                    + startIndex + "/"
                    + endIndex + "/";

            try {
                String result = getApiResponse(apiUrl);
                if (!processApiResponse(result)) {
                    break;
                }
                totalResponse.append(result);
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

    private boolean processApiResponse(String result) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject parkingInfo = (JSONObject) jsonObject.get("GetParkingInfo");
            Long totalCount = (Long) parkingInfo.get("list_total_count");

            JSONObject subResult = (JSONObject) parkingInfo.get("RESULT");
            JSONArray infoArr = (JSONArray) parkingInfo.get("row");

            System.out.println(subResult);
            System.out.println("불러온 데이터의 개수 : " + totalCount);

            System.out.println(infoArr.size());

            for (int i = 0; i < infoArr.size(); i++) {
                JSONObject tmp = (JSONObject) infoArr.get(i);

                String parkingCode = tmp.get("PARKING_CODE").toString();
                if (parkingCode == null || parkingCode.equals("")) {
                    return false;
                }

                ParkingLotEntity parkingLot = ParkingLotEntity.builder()
                        .parkingCode(Integer.parseInt(parkingCode))
                        .parkingName(tmp.get("PARKING_NAME").toString())
                        .address(tmp.get("ADDR").toString())
                        .tel(tmp.get("TEL").toString())
                        .lat(Double.parseDouble(tmp.get("LAT").toString()))
                        .lng(Double.parseDouble(tmp.get("LNG").toString()))
                        .build();

                parkingLotRepository.save(parkingLot);
            }
            return true;
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
