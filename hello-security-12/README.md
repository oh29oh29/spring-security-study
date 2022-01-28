# Hello Security 12

인증 저장소 필터

### 주요 내용

#### 익명사용자

|순서|BP|
|----|----|
|1|![익명사용자_01](images/IMG_12_01.png)|
|2|![익명사용자_02](images/IMG_12_02.png)|
|3|![익명사용자_03](images/IMG_12_03.png)|
|4|![익명사용자_04](images/IMG_12_04.png)|
|5|![익명사용자_05](images/IMG_12_05.png)|
|6|![익명사용자_06](images/IMG_12_06.png)|
|7|![익명사용자_07](images/IMG_12_07.png)|
|8|![익명사용자_08](images/IMG_12_08.png)|
|9|![익명사용자_09](images/IMG_12_09.png)|
|10|![익명사용자_10](images/IMG_12_10.png)|

#### 로그인

|순서|BP|
|----|----|
|1..7|익명사용자의 1..7 과정과 동일|
|8|`AbstractAuthenticationProcessingFilter` 를 통한 인증 처리|
|9|![로그인_08](images/IMG_12_11.png)|
|10|![로그인_09](images/IMG_12_12.png)|
|11|![로그인_10](images/IMG_12_13.png)|

#### 로그인 후

|순서|BP|
|----|----|
|1..2|익명사용자의 1..2 과정과 동일|
|3|![로그인후_03](images/IMG_12_14.png)|
|4|![로그인후_04](images/IMG_12_15.png)|
|5|![로그인후_05](images/IMG_12_16.png)|
|6|인가 처리|
|7|![로그인후_06](images/IMG_12_13.png)|