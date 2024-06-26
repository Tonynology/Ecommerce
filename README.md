# 물품 거래 플랫폼 서비스
유저간 물건을 등록하고 구매/판매를 하도록 하는 서비스 (11.13 ~ 12.15)

## 프로젝트 기능 및 설계

### 유저
* 회원가입 기능 - jwt 사용
  * 사용자는 자체 회원가입 기능으로 가입힌다.
  * 회원가입시 아이디, 패스워드, 이메일, 전화번 그리고 동네를 입력받으며, 아이디는 unique 해야한다.
  * 회원가입을 하지않으면, 물품 보기는 가능하나, 거래 및 채팅기능은 제한된다.


* 로그인 기능
  * 회원 가입시 아이디와 비밀번호가 일치해야 로그인이 가능하다.


  
### 물품
* 등록 기능
  *  로그인 한 사용자는 물품의 설명, 가격, 이미지와 카테고리(fashion, pets, etc.), 상태(new, good, normal, bad) 정보와 함께 물픔을 등록할 수 있다.
* 수정 기능
  * 로그인 한 사용자는 등록한 물품의 정보를 수정할 수 있다.
* 삭제 기능
  * 로그인 한 사용자는 등록한 물품을 삭제할 수 있다.
* 검색 기능
  * 로그인 한 사용자는 물품을 검색할 수 있다.
* 물품 리스트 조회 기능
  * 로그인하지 않은 사용자를 포함한 모든 사용자는 물품 리스트를 조회할 수 있다.
* 물품 디테일 조회 기능
  * 로그인하지 않은 사용자를 포함한 모든 사용자는 물품 디테일을 조회할 수 있다.
  * 물품 목록은 최신순으로 기본 정렬된다.


### 거래
* 관심 상품 등록 기능
  * 사용자는 관심 상품 등록이 가능하다.
* 알림 기능
  * 사용자가 관심 상품으로 등록한 물품의 가이나 상태가 변경되었을때 사용자에게 알림을 보낸다.
* 지갑 만들기 기능
  * 사용자는 결제를 위해 지갑을 먼저 만들어야한다.
* 결제 기능
  * 사용자는 금액을 지갑에 충전 후 결제 후 차감한다.
* 잔돈 확인 기능
  * 사용자는 지갑의 잔돈을 확인할 수 있다.




## ERD
![image](https://github.com/Tonynology/Ecommerce/assets/36941592/e4b5fe77-d6f3-4295-a262-6c172198babc)


## 사용 기술

![java](https://github.com/Tonynology/Ecommerce/assets/36941592/4355c541-60ff-4f6c-b790-46e6c99b9142)
![springboot](https://github.com/Tonynology/Ecommerce/assets/36941592/5d6e038d-a39d-49bf-9537-71d8d7898023)
![mysql](https://github.com/Tonynology/Ecommerce/assets/36941592/04d33c4d-3d69-461d-8082-3c7fc4588eb7)
![git](https://github.com/Tonynology/Ecommerce/assets/36941592/e214ae07-79c3-4ac3-91eb-b6760ca4984a)
![elasticsearch](https://github.com/Tonynology/Ecommerce/assets/36941592/2a822fc3-f7fb-469d-bf97-bda60d4ca78d)
![image](https://github.com/Tonynology/Ecommerce/assets/36941592/3b57a5f6-f185-4159-8e21-f12ab6d35b38)
![image](https://github.com/Tonynology/Ecommerce/assets/36941592/6d9e8842-4b0e-4838-ba44-dd20295cceb3)
![image](https://github.com/Tonynology/Ecommerce/assets/36941592/b2390b54-48c8-436a-9be0-1b9558987246)
![image](https://github.com/Tonynology/Ecommerce/assets/36941592/54649a35-c24e-4e35-ae9c-e656649772c8)
![image](https://github.com/Tonynology/Ecommerce/assets/36941592/eea4c105-c297-4713-89b0-10001fd92595)