# FinGuard - 금융 루머 검증 시스템

실시간 공시 정보와 루머 검증을 제공하는 금융 정보 서비스

## 📁 프로젝트 구조 (25.10.13 14:00)

```
rumor_study/
└── stockkkkk/                          # Spring Boot 프로젝트
    ├── src/main/
    │   ├── java/com/example/stockkkkk/
    │   │   ├── global/                 # 전역 설정 및 공통 기능
    │   │   │   ├── config/             # 설정 (Security, CORS, WebMvc)
    │   │   │   ├── auth/               # 인증 관련
    │   │   │   │   ├── dto/            # 인증 DTO
    │   │   │   │   └── jwt/            # JWT 토큰 처리
    │   │   │   └── exception/          # 예외 처리
    │   │   │       ├── custom/         # 커스텀 예외
    │   │   │       └── novel/          # 신규 예외
    │   │   │
    │   │   ├── auser/                  # 사용자 도메인
    │   │   │   ├── controller/         # 사용자 API 컨트롤러
    │   │   │   ├── domain/             # 사용자 엔티티
    │   │   │   ├── dto/                # 사용자 DTO
    │   │   │   ├── repository/         # 사용자 레포지토리
    │   │   │   └── service/            # 사용자 서비스
    │   │   │
    │   │   └── disclosure/             # 공시 도메인
    │   │       ├── config/             # 공시 관련 설정
    │   │       ├── controller/         # 공시 API 컨트롤러
    │   │       ├── domain/             # 공시 엔티티
    │   │       ├── dto/                # 공시 DTO
    │   │       ├── repository/         # 공시 레포지토리
    │   │       └── service/            # 공시 서비스 (DART API 연동)
    │   │
    │   └── resources/
    │       ├── application.yml         # Spring Boot 설정
    │       └── static/                 # 프론트엔드 (React 스타일 구조)
    │           ├── index.html
    │           ├── home-integration.js # 홈 통합 스크립트
    │           │
    │           ├── assets/             # 전역 리소스
    │           │   ├── styles/
    │           │   │   └── global.css
    │           │   └── scripts/
    │           │       └── main.js     # 라우터, 드로어
    │           │
    │           ├── features/           # 기능별 모듈 (React 스타일)
    │           │   ├── home/
    │           │   │   ├── home.js
    │           │   │   └── components/
    │           │   │       ├── home-chat.html
    │           │   │       ├── home-chat.css
    │           │   │       └── home-chat.js
    │           │   ├── disclosure/     # 공시 탭
    │           │   │   └── disclosure.js
    │           │   ├── rumor/          # 루머체크 탭
    │           │   │   ├── rumor.html
    │           │   │   └── rumor.js
    │           │   ├── detail/         # 루머 상세
    │           │   │   ├── detail.html
    │           │   │   └── detail.js
    │           │   └── profile/        # 프로필
    │           │       ├── profile.html
    │           │       └── profile.js
    │           │
    │           └── shared/             # 공유 컴포넌트 및 서비스
    │               ├── components/
    │               │   └── modals/
    │               │       ├── LoginModal.html
    │               │       ├── SubmitModal.html
    │               │       └── DetailModal.html
    │               └── services/       # 전역 서비스
    │                   ├── api-client.js
    │                   ├── auth-manager.js
    │                   └── modal-controller.js
    │
    ├── build.gradle                    # Gradle 빌드 설정
    └── gradlew                         # Gradle Wrapper
```

## 🛠 기술 스택

### Backend
- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Database**: MariaDB
- **Security**: Spring Security + JWT
- **Build Tool**: Gradle

### Frontend
- **Architecture**: Vanilla JS (React-like 폴더 구조)
- **Styling**: CSS3 (CSS Variables)
- **Router**: Hash-based SPA Router
- **Modals**: Native `<dialog>` API

## 🚀 시작하기

### 필수 요구사항
- Java 17 이상
- MariaDB 10.x
- (25.10.10 수정 -> 현재 서버 현황 -mysql from 11.8.3-MariaDB, client 15.2 for debian-linux-gnu (x86_64) using  EditLine wrapper)
### 데이터베이스 설정

```sql
CREATE DATABASE stock5_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'dtuser'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON stock5_db.* TO 'dtuser'@'localhost';
FLUSH PRIVILEGES;
```

### 애플리케이션 실행

```bash
cd stockkkkk
./gradlew bootRun
```

```bash
nohup java -jar build/libs/stockkkkk-0.0.1-SNAPSHOT.jar > app.log &
```
```
  주요 Gradle 명령어:
  ./gradlew clean       # 빌드 결과 삭제
  ./gradlew build       # 프로젝트 빌드 (테스트 포함)
  ./gradlew build -x test  # 테스트 없이 빌드
  ./gradlew bootRun     # 개발 모드로 실행

  빌드 결과물:
  - JAR 파일: stockkkkk/build/libs/stockkkkk-0.0.1-SNAPSHOT.jar 
```


서버가 `http://localhost:8080`에서 실행됩니다.


## 📱 주요 기능

### 1. 홈 (Home)
- 채팅 인터페이스로 금융 정보 조회
- 실시간 주가 및 공시 정보 확인

### 2. 공시 (Disclosure)
- 최근 7일간 DART 공시 자동 수집 및 조회
- 날짜별 캘린더 UI로 직관적인 조회
- 공시 상세 내용 조회 및 원문 링크
- 1시간마다 당일 공시 자동 갱신

> **📘 자세한 내용**: [공시 기능 가이드 (DISCLOSURE_GUIDE.md)](./DISCLOSURE_GUIDE.md)

### 3. 루머체크 (Rumor)
- 루머 검증 요청
- AI 패턴 분석 예측
- 유사 사례 분석
- 커뮤니티 의견

### 4. 프로필 (Profile)
- 사용자 정보 관리
- 로그인/로그아웃
- 검증 히스토리

## 🏗 아키텍처 특징

### Frontend 모듈화
- **Feature-based 구조**: 각 기능을 독립적인 폴더로 분리
- **Shared Services**: API 클라이언트, 인증 관리자를 전역 서비스로 분리
- **Dynamic Loading**: 모달 및 뷰를 동적으로 로드하여 초기 로딩 최적화

### Backend 보안
- **JWT 기반 인증**: Access Token + Refresh Token
- **Spring Security**: 경로별 권한 관리
- **CORS 설정**: 외부 도메인 접근 제어

## 📝 주요 설정 파일

### application.yml
```yaml
spring:
  datasource:
    url: jdbc:mariadb://localhost:3306/stock5_db
    username: dtuser
    password: 1234

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

server:
  address: 0.0.0.0
  port: 8080
```

### SecurityConfig.java
```java
// 정적 리소스 허용
.requestMatchers("/assets/**", "/features/**", "/shared/**",
                 "/*.html", "/*.css", "/*.js").permitAll()
```

## 🔧 개발 가이드

### 새로운 Feature 추가

1. `features/` 아래 새 폴더 생성
2. HTML, CSS, JS 파일 작성
3. `index.html`에 스크립트 태그 추가
4. 필요시 라우터에 경로 추가

### 새로운 Modal 추가

1. `shared/components/modals/` 에 HTML 파일 생성
2. `modal-controller.js`에서 초기화
3. `index.html`의 모달 로더에 추가

## 🐛 알려진 이슈

- **브라우저 캐시**: 정적 리소스 변경 시 하드 리프레시 필요 (`Ctrl+Shift+R`)
- **서버 재시작**: JS 파일 수정 후 Spring Boot 서버 재시작 필요

---

**FinGuard** - 신뢰할 수 있는 금융 정보 서비스
