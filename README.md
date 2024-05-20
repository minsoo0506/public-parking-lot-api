# 🚗 public-parking-lot-api


서울시 공공 데이터를 활용한 __공영 주차장 실시간 자리 확인__ 서비스

### 📚 기술 스택

---

<div align=center> 
    <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
    <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
    <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
    <img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
    <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
    <img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
    <img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
    <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
</div>



### 🗒️ 프로젝트 기능 및 설계

---

#### 일반 사용자
- 회원가입 / 로그인 (JWT 사용)
- 실시간 주차 현황 조회 (OpenAPI 사용 / 전체 정보 조회 - 페이징 기능 사용)
- 가까운 주차장 찾기 (OpenAPI 사용 / 자치구 기준)
- 특정 주차장 검색 (DB 데이터 내에서 조회 / 키워드로 검색)
- 주차장 상세 정보 조회 (OpenAPI 사용 / 주차 가능한 자리 수, 요금, 운영시간)
- 주차장 즐겨 찾기 기능
- 리뷰(별점, 글쓰기) 작성 / 수정 / 삭제

#### 관리자
- 회원가입 / 로그인 (JWT 사용)
- 회원 관리
- 리뷰 관리


### ✏️ ERD

---

<img width="977" alt="스크린샷 2024-05-09 오후 11 09 16" src="https://github.com/minsoo0506/Weather-Diary/assets/68321360/afa8f3c5-fdba-4943-a52d-01265972501b">

### 📜 API DOC : SWAGGER

---

http://13.209.40.105:8080/swagger-ui.html


### ⚠️ Trouble Shooting

--- 

https://notch-lupin-fd0.notion.site/Trouble-Shooting-8d36975393824efd8b730d3a87853bd5
