# RabbitMQ를 이용한 상담 서비스 프로젝트&#128513;

### 프로젝트 내용
1. 고객(1~N)명이 상담 시작을 누른다.
2. 랜덤으로 생성 된 RoomId를 통해 Topic을 구독 한다.
3. 랜덤 생성된 RoomId를 Redis 에 발생 시간과 함께 insert 한다.
4. 상담사는 상담 시작 버튼을 누른다.
5. Redis에 있는 가장 먼저 등록 된 RoomId를 가져와 Topic을 구독 한다.
6. 고객이 상담을 종료 하면 해당 RoomId는 delete 되고 상담사는 상담 시작 버튼을 통해 다음 고객과 상담을 할 수 있다.

******************************************************
### 사용 기술
+ Spring Boot 3.0.1
+ java17
+ Redis
+ RebbitMQ
+ AWS EC2

******************************************************
### 사용하려면..
- application.properties 혹은 yaml 파일에 Redis, RabbitMQ host, port, id, password를 명시 해야 함
  - 개발 시 나는 localhost를 이용 하다가 배포 전에 EC2에 RabbitMQ와 Redis를 설치 하고 외부 접속을 허용 했었다.
