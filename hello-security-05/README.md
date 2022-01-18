# Hello Security 05

### 기능
- 동시 세션 제어

### 주요 내용
- maximumSessions: 최대 허용 가능 세션 수 (-1: 무제한 로그인 세션 허용)
- maxSessionsPreventsLogin: true: 동시 로그인 차단, false: 기존 세션 만료 (default)
- invalidSessionUrl: 세션이 유효하지 않을 때 이동 할 페이지
- expiredUrl: 세션이 만료된 경우 이동 할 페이지