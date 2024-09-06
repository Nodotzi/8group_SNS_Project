## <aside>🏁 **Goal:  "JWT 토큰을 활용한 SNS 뉴스피드 기능 만들기"**</aside>


### SNS_Project를 통해 얻을 수 있는 것 👍
1.  **데이터베이스와 ORM**
    - [ ]  데이터베이스 스키마를 설계할 수 있다.
    - [ ]  JPA를 이용해 데이터베이스와 연동할 수 있다.
    - [ ]  JPA를 통해 CRUD 작업을 할 수 있다.
2. **인증**
    - [ ]  사용자 인증과 인가의 기본 원리와 차이점을 이해하고 있다.
    - [ ]  JWT를 이해하고 활용할 수 있다.
3. **REST API**
    - [ ]  기능에 알맞게 REST API 설계를 할 수 있다.
    - [ ]  Spring Boot를 이용해 REST API를 구현할 수 있다.
4. **협업 및 버전 관리**
    - [ ]  Git을 사용해 소스 코드 버전 관리를 할 수 있다.
    - [ ]  Git branch를 이용하여 브랜치 관리 및 원활한 협업을 할 수 있다.
    - [ ]  Pull Request와 코드 리뷰 과정에 대해 이해할 수 있다.


<aside> 🔥 **다음을 고려하며 설계했습니다.** 🔥</aside>

### **1. 프로필 관리**

- **프로필 조회 기능**
    - 다른 사용자의 프로필 조회 시, 민감한 정보는 표시되지 않습니다.
- **프로필 수정 기능**
    
    로그인한 사용자는 본인의 사용자 정보를 수정할 수 있습니다.
    
    - 비밀번호 수정 조건
        - 비밀번호 수정 시, 본인 확인을 위해 현재 비밀번호를 입력하여 올바른 경우에만 수정할 수 있습니다.
        - 현재 비밀번호와 동일한 비밀번호로는 변경할 수 없습니다.
    - **⚠️ 예외처리**
        - 비밀번호 수정 시, 본인 확인을 위해 입력한 현재 비밀번호가 일치하지 않은 경우
        - 비밀번호 형식이 올바르지 않은 경우
        - 현재 비밀번호와 동일한 비밀번호로 수정하는 경우


### **2.  뉴스피드 게시물 관리**

- **게시물 작성, 조회, 수정, 삭제 기능**
    - 조건
        - 게시물 수정, 삭제는 작성자 본인만 처리할 수 있습니다.
    - **⚠️ 예외처리**
        - 작성자가 아닌 다른 사용자가 게시물 수정, 삭제를 시도하는 경우
- **뉴스피드 조회 기능**
    - 기본 정렬은 생성일자 ****기준으로 내림차순 정렬합니다.
    - 10개씩 페이지네이션하여, 각 페이지 당 뉴스피드 데이터가 10개씩 나오게 합니다.
    - **⚠️ 예외처리**
        - 다른 사람의 뉴스피드는 볼 수 없습니다.


### **3. 사용자 인증**

- **회원가입 기능**
    - 사용자 아이디
        - 사용자 아이디는 이메일 형식이어야 합니다.
    - 비밀번호
        - `Bcrypt`로 인코딩합니다.
            - 암호화를 위한 `PasswordEncoder`를 직접 만들어 사용합니다.
                - 참고 코드
                    1. `build.gradle` 에 아래의 의존성을 추가해주세요.
                        
                        ```java
                        implementation 'at.favre.lib:bcrypt:0.10.2'
                        ```
                        
                    2. `config` 패키지가 없다면 추가하고, 아래의 클래스를 추가해주세요.
                        
                        ```java
                        import at.favre.lib.crypto.bcrypt.BCrypt;
                        import org.springframework.stereotype.Component;
                        
                        @Component
                        public class PasswordEncoder {
                        
                            public String encode(String rawPassword) {
                                return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
                            }
                        
                            public boolean matches(String rawPassword, String encodedPassword) {
                                BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
                                return result.verified;
                            }
                        }
                        ```
                        
        - 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함합니다.
        - 비밀번호는 최소 8글자 이상이어야 합니다.
    - **⚠️ 예외처리**
        - 중복된 `사용자 아이디`로 가입하는 경우
        - `사용자 아이디` 이메일과 비밀번호 형식이 올바르지 않은 경우
- **회원탈퇴 기능**
    
    회원 탈퇴 방식을 어떻게 처리할지 고민해보세요.
    
    - 조건
        - 탈퇴 처리 시 `비밀번호`를 확인한 후 일치할 때 탈퇴 처리합니다.
        - 탈퇴한 사용자의 아이디는 재사용할 수 없고, 복구할 수 없습니다.
    - **⚠️ 예외처리**
        - `사용자 아이디`와 `비밀번호`가 일치하지 않는 경우
        - 이미 탈퇴한 `사용자 아이디`인 경우


### **4. 친구 관리**

- 특정 사용자를 친추 추가/삭제 할 수 있습니다.
- 친구 기능이 구현되었다면, 뉴스피드에 친구의 최신 게시물들을 최신순으로 볼 수 있습니다.
    - **⚠️ 주의사항**
        - 친구는 상대방의 수락 기능이 필요합니다. 만약 어렵다면, 관심 유저를 팔로우하는 기능으로 개발하셔도 좋습니다.
  

### **5. 댓글 관리**

- 댓글 작성, 조회, 수정, 삭제
    - 사용자는 게시물에 댓글을 작성할 수 있고, 본인의 댓글은 **수정 및 삭제**를 할 수 있습니다.
    - **내용**만 수정이 가능합니다.
    - 댓글 수정, 삭제는 댓글의 작성자 혹은 게시글의 작성자만 가능합니다.
 

- 댓글 수정, 삭제는 댓글의 작성자 혹은 게시글의 작성자만 가능합니다.

## 와이어프레임
![image](https://github.com/user-attachments/assets/025d7a1c-86ae-43af-8cd0-793f2d2780bb)
## 플로우차트
![8조 플로우 차트](https://github.com/user-attachments/assets/dfa29a18-8bd0-4c5f-93b1-1e3b3f14894b)




## ⚜API명세서 
### 회원가입/탈퇴 API, 로그인 API, 뉴스피드 API (박대현)
![image](https://github.com/user-attachments/assets/5b6455d9-6588-4306-8fc4-c57445ea3fb7)
![image](https://github.com/user-attachments/assets/10b0bedd-61b2-457c-ab68-a568591462ed)


### 친구요청 관련 API (김진비)
![image](https://github.com/user-attachments/assets/745eb8a4-debb-4eb0-9727-fc94ac223153)
![image](https://github.com/user-attachments/assets/5b2cb6cb-7595-45c6-b32a-a6e46af568cf)
![image](https://github.com/user-attachments/assets/72072768-df00-4c83-8eb6-5562327c937a)
![image](https://github.com/user-attachments/assets/240ee8e1-9d0d-429e-b0c9-fb6876192ac0)




### 게시물관련 API (노현지)
![image](https://github.com/user-attachments/assets/fdd08a53-27cc-4395-8825-7b3e7fa6c00b)
![image](https://github.com/user-attachments/assets/2d1f90c5-ef79-431b-8154-d929bea93ae3)



### 유저 관련 API (이현중)
![image](https://github.com/user-attachments/assets/11e0fd86-c4d3-42df-aaa5-564bbbada3c3)




## ERD 다이어그램
![image](https://github.com/user-attachments/assets/e7ecd466-8514-465f-8477-4f3175a59f02)


### [컬럼설명]
**user (사용자):사용자 정보를 저장합니다.**

    `id` (기본 키), `email`(이메일), `password`(비밀번호), `user_status`(활성화 상태), `nickname`(닉네임), `introduce`(자기소개)


**posting (게시물):사용자가 작성한 게시물 정보를 저장합니다.**
    
    `id`(기본 키), 'title'(제목), 'contents'(내용), `created_at`(생성일자), `modified_at`(수정일자)


**relationship(친구요청) : 친구 요청 기록을 저장합니다.**

    `id`(기본 키), `status`(요청상태), `created_at'(생성일자), `modified_at`(수정일자), `send_id`(보낸 유저, FK), `receive_id` (받은 유저, FK)


**friends (친구 관계):사용자 간의 친구 관계를 저장합니다.**
    
    `id`(기본 키), `friendA_id`(친구1, FK), `friendB_id`(친구2, FK)


**comment(댓글):댓글 정보를 저장합니다.**
     
    `id`(기본키), `content`(댓글 내용), `posting_id`(게시물, FK), `user_id`(작성자, FK)





