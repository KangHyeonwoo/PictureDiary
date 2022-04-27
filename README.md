# PictureDiary

저장소(현재는 NAS)에 저장된 사진 데이터를 사용자에 따라 조회해 맵에 표출해주는 프로젝트

- 데이터 조회
- 메타데이터 수정
- 삭제
- 로컬에서 나스로 사진 추가 (TODO)
- Admin 은 사용자 관리만 할 수 있는 계정 (TODO)

의 기능이 있다.

## 프로젝트 진행하면서 공부할 기능들

- Database : PostgreSQL
- Docker Container 위에 서비스 올리기
- @Valid 및 Validation 자동화
- ResponseEntity
- 캐싱 (우선은 맵으로 구현하고 Spring Cache 적용 방안 고려 + 리팩토링 진행하고 흔적 정리하기)
- 파일 썸네일 가져올 때는 용량 축소해서 가져오기
- HEIC 확장자 방안 고려
- 카카오맵 사용
- 지도에서 사용할 마커 및 인포윈도우는 class 객체로 관리하기 (javascript es6 문법 공부하면서 하자)
- AOP
- Spring Cache

## 개발 방향 정리

> **가장 중요. 원본 파일은 건드리지 않음을 원칙으로 한다.**
> 
> 지도에 사진을 보여주는 것이 메인 기능이므로, 위치정보가 있는 사진만 가치를 지닌다.
> 
> 따로 정의한 요청이 아닌 기본 요청의 경우 DB 의 값 (캐싱된 값)을 리턴하는 것을 원칙으로 한다.
> 
> 위치정보가 있는 사진이 중허다.
> 
> 위치정보가 없는 사진 정보는 DB 에 저장하지 않는다.
> 고로, 새로고침 혹은 다시 로그인했을 때 목록에 보이지 않는다.
>   (새로고침할 때 좌표 정보가 없는 사진은 없어지고, 다시 조회해야 한다고 알려는 주자)


### 로그인 OR 새로고침 후 DB 에 저장된 데이터 조회

1. 캐싱된 데이터를 조회
   - 캐싱은 Map 객체로 관리하며 (HashMap vs LinkedHashMap) Map<UserId, List<PictureDto>> 의 형태로 관리
   - LinkedHashMap 은 맵에 저장된 개체들의 순서가 중요한 경우 사용하는게 유리하므로 HashMap 을 사용하도록 함.
     (나의 경우 저장 순서는 필요 없으므로)
2. 캐싱된 데이터가 있으면 캐싱된 데이터를 리턴하고, 없으면 DB 에서 조회한다.
3. DB 에 조회된 데이터를 캐싱하고 리턴한다. (deleteAt = N 인 것들만)


### 저장소에서 좌표 정보가 있는 데이터 조회

> 화면에서 [내 사진 가져오기] 라는 버튼 주변에 '위치정보가 있는 사진만 가져오기' 라는 체크박스를 만들자

### 저장소에서 전체 데이터 조회

1.비동기로 최신화된 데이터를 조회한다.
    - 비동기 작업은 알림으로 알려주며, 진행 상황도 같이 알려줄 수 있는 방안을 고려한다.
2. DB 에서 모든 데이터를 조회한다.
3. NAS 에 저장된 모든 데이터를 조회한다.
4. NAS 에는 있고 DB 에는 없는 데이터를 추린다. (최신화된 데이터)
    - 두 배열의 값을 비교하는 방법 있는지 찾아보기
5. NAS 에만 있는 데이터를 DB 에 저장하고 캐싱하고 리턴한다.
6. 화면에 최신화 작업이 완료됐다는 토스트창을 띄우고 화면에 새로운 데이터들을 추가한다.

### 메타데이터 수정

메타데이터 수정은 DB 수정만 한다.
- Geometry

- Description
  - 위치정보가 없는 사진은 설명을 작성할 수 없다.
  - 왜냐면 프로젝트 주제가 사진을 지도에 보여주는 것이기 때문에 지도에 표출할 수 없는 사진에는 설명이 필요가 없다.
  
- 날짜는 놉

캐싱된 데이터 처리

### 파일 삭제

1. deleteAt 컬럼을 Y 로.
2. 화면에서는 마커 지우기
3. 캐싱된 데이터 처리


### NAS 로그인하는 법
- 클라이언트에서 바로 요청 보내지 말고 서버에서 보내기
- 요청 IP 등 로그 쌓기


### 기타 메모

- 조회 > 수정 > 삭제 > 프론트앤드 순서로 구현하기
  (최상) (상)  (하)     (상)

- 프론트엔드에 시간을 너무 많이 소요해, 효율적인 시간 분배를 못했음
  - 프론트엔드 특히 CSS에 투자하는 시간을 줄이기 위해 화면 구성은 최소한으로.
    (그래도 결과물은 나와야하니 최소한은 하기)

- 테스트는 로컬 디렉토리로 한다.
- 로컬에서도 잘 할 수 있도록 (변화를 주어도 소스코드의 수정을 최소화할 수 있도록) 구현하자.
- TDD 로 구현

- 캐싱을 통한 새로고침 고려
- 메소드 주석 잘 달기
- 세션 타임아웃 설정하기
- 마커에 붙어있는 썸네일은 적은 용량으로 사진을 조회해야 한다.
- HEIC 이놈이 제일 문제.