## <aside>🏁 **Goal:  "JPA를 활용한 upgrade 일정 관리 앱 서버 만들기"**</aside>


-  DB_CRUD_JWT_Project를 통해 얻을 수 있는 것 👍
    1. JPA를 활용하여 CRUD를 구현하고, 이를 통해 객체 지향적으로 데이터를 다룰 수 있어요.
    2. JPA를 활용하여 데이터베이스를 관리하고 영속성에 대해서 이해할 수 있어요.
    3. 회원가입, 로그인을 통해 인증/인가를 이해하고 JWT를 활용할 수 있어요.
    4. RestTemplate을 통해 외부 정보를 호출하고 활용할 수 있어요.
 

 ### <DB를 사용하고, CRUD와 JWT를 사용하는 과제입니다>
개인 입문과제로 만든 Custom_Sceduler를 스케일업 하여, CRUD만 구현한 Custom_Sceduler에, JPA로 DB를 관리하고, '영속성'을 고려해서 만드는 Sceduler 프로젝트 입니다.


<aside> 🔥 **다음을 고려하며 설계했습니다.** </aside>

- 모든 테이블은 고유 식별자(ID)를 가집니다.
- `3 Layer Architecture` 에 따라 각 Layer의 목적에 맞게 개발합니다.
- CRUD 필수 기능은 모두 데이터베이스 연결 및  `JPA`를 사용해서 개발합니다.
- `JDBC`와 `Spring Security`는 사용하지 않습니다.
- 인증/인가 절차는 `JWT`를 활용하여 개발합니다.
- JPA의 연관관계는 **`양방향`**으로 구현합니다.
  

#### <입문주차 개인괴제(Custom_Sceduler)링크>
https://github.com/ilmechaJu/Custom_Sceduler/blob/master/ReadMe.md


## API명세서 (박대현)
### 회원가입/탈퇴 API, 로그인 API, 뉴스피드 API
![image](https://github.com/user-attachments/assets/5b6455d9-6588-4306-8fc4-c57445ea3fb7)
![image](https://github.com/user-attachments/assets/10b0bedd-61b2-457c-ab68-a568591462ed)




### 친구요청 관련 API (김진비)
![image](https://github.com/user-attachments/assets/745eb8a4-debb-4eb0-9727-fc94ac223153)
![image](https://github.com/user-attachments/assets/5b2cb6cb-7595-45c6-b32a-a6e46af568cf)
![image](https://github.com/user-attachments/assets/72072768-df00-4c83-8eb6-5562327c937a)
![image](https://github.com/user-attachments/assets/240ee8e1-9d0d-429e-b0c9-fb6876192ac0)




### 게시물관련 API (노현지)
![image](https://github.com/user-attachments/assets/fdd08a53-27cc-4395-8825-7b3e7fa6c00b)
![image](https://github.com/user-attachments/assets/43df2e55-3b61-45ba-85e5-c29460418bd7)
![image](https://github.com/user-attachments/assets/2d1f90c5-ef79-431b-8154-d929bea93ae3)



### 유저 관련 API (이현중)
![image](https://github.com/user-attachments/assets/11e0fd86-c4d3-42df-aaa5-564bbbada3c3)




## ERD 다이어그램
![image](https://github.com/user-attachments/assets/e7ecd466-8514-465f-8477-4f3175a59f02)

### 컬럼설명
**user (사용자):사용자 정보를 저장합니다.**

**필드: `id` (기본 키),`email`(이메일), `password`(비밀번호), `user_status`(활성화 상태), `nickname`(닉네임), `introduce`(자기소개)**

**posting (게시물):사용자가 작성한 게시물 정보를 저장합니다.**

**필드: `id`(기본 키), title(제목) , contents(내용) , `created_at` (생성일자), `modified_at` (수정일자)** 

**relationship(친구요청) : 친구 요청 기록을 저장합니다.**

**필드 :** **`id`(기본 키), `status`  (요청상태), `created_at` (생성일자), `modified_at` (수정일자)  , `send_id`(보낸 유저, FK), `receive_id` (받은 유저, FK)**

**friends (친구 관계):사용자 간의 친구 관계를 저장합니다.**

**필드: `id`, `friendA_id` (친구1, FK), `friendB_id` (친구2, FK)**

**comment(댓글):댓글 정보를 저장합니다.**

**필드 : `id` (기본키), `content`(댓글 내용), `posting_id`(게시물, FK), `user_id`(작성자, FK)**





