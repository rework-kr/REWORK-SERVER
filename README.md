# 패키지 구조
```bash
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── rework
    │   │               ├── ReworkApplication.java
    │   │               ├── auth
    │   │               │   ├── MemberRole.java
    │   │               │   ├── cookie
    │   │               │   │   └── CookieUtil.java
    │   │               │   ├── entity
    │   │               │   │   └── RefreshToken.java
    │   │               │   ├── jwt
    │   │               │   │   ├── JwtAuthenticationFilter.java
    │   │               │   │   ├── JwtAuthorizationFilter.java
    │   │               │   │   ├── JwtProvider.java
    │   │               │   │   ├── MemberDetails.java
    │   │               │   │   └── MemberDetailsService.java
    │   │               │   ├── repository
    │   │               │   │   └── RefreshTokenRepository.java
    │   │               │   └── service
    │   │               │       └── RefreshTokenService.java
    │   │               ├── config
    │   │               │   ├── bcrypt
    │   │               │   │   └── BcryptConfig.java
    │   │               │   ├── jpa
    │   │               │   │   └── JpaAuditingConfig.java
    │   │               │   ├── mail
    │   │               │   │   └── MailConfig.java
    │   │               │   ├── redis
    │   │               │   │   ├── EmbeddedRedisConfig.java
    │   │               │   │   └── RedisConfig.java
    │   │               │   ├── security
    │   │               │   │   ├── SecurityConfig.java
    │   │               │   │   └── SecurityUtils.java
    │   │               │   └── swagger
    │   │               │       └── SwaggerConfig.java
    │   │               ├── dailyagenda
    │   │               │   ├── application
    │   │               │   │   ├── DailyAgendaService.java
    │   │               │   │   ├── ScheduleService.java
    │   │               │   │   ├── dto
    │   │               │   │   │   ├── DailyAgendaRequestDto.java
    │   │               │   │   │   └── DailyAgendaResponseDto.java
    │   │               │   │   └── impl
    │   │               │   │       └── DailyAgendaServiceImpl.java
    │   │               │   ├── domain
    │   │               │   │   ├── DailyAgenda.java
    │   │               │   │   └── repository
    │   │               │   │       └── DailyAgendaRepository.java
    │   │               │   ├── presentation
    │   │               │   │   └── DailyAgendaController.java
    │   │               │   └── restapi
    │   │               │       └── DailyAgendaApi.java
    │   │               ├── discord
    │   │               │   └── WebhookService.java
    │   │               ├── global
    │   │               │   ├── ErrorCodes.java
    │   │               │   ├── ErrorResponse.java
    │   │               │   ├── GlobalExceptionHandler.java
    │   │               │   ├── GlobalExceptionHandlerFilter.java
    │   │               │   ├── base
    │   │               │   │   └── BaseTimeEntity.java
    │   │               │   ├── common
    │   │               │   │   └── CommonResDto.java
    │   │               │   ├── dto
    │   │               │   │   ├── CommonResponse.java
    │   │               │   │   └── ErrorResponse.java
    │   │               │   └── error
    │   │               │       ├── AlreadyPagingIdException.java
    │   │               │       ├── DuplicateAccountException.java
    │   │               │       ├── EncryptException.java
    │   │               │       ├── InvalidDiscordMessage.java
    │   │               │       ├── InvalidTokenException.java
    │   │               │       ├── NotFoundAccountException.java
    │   │               │       ├── NotFoundAgendaException.java
    │   │               │       ├── PasswordNotMatchException.java
    │   │               │       ├── PasswordUnchangedException.java
    │   │               │       └── UnAuthorizedException.java
    │   │               ├── mail
    │   │               │   ├── MailPurpose.java
    │   │               │   ├── application
    │   │               │   │   ├── MailSenderService.java
    │   │               │   │   ├── dto
    │   │               │   │   │   └── MailRequestDto.java
    │   │               │   │   └── impl
    │   │               │   │       └── MailSenderServiceImpl.java
    │   │               │   ├── infra
    │   │               │   │   └── MailSenderApi.java
    │   │               │   ├── presentation
    │   │               │   │   └── MailSenderController.java
    │   │               │   ├── restapi
    │   │               │   │   └── EmailApi.java
    │   │               │   └── utils
    │   │               │       └── GeneRateRandomPassword.java
    │   │               ├── member
    │   │               │   ├── application
    │   │               │   │   ├── MemberService.java
    │   │               │   │   ├── dto
    │   │               │   │   │   ├── MemberResponseDto.java
    │   │               │   │   │   └── MemeberRequestDto.java
    │   │               │   │   └── impl
    │   │               │   │       └── MemberServiceImpl.java
    │   │               │   ├── domain
    │   │               │   │   ├── Member.java
    │   │               │   │   └── repository
    │   │               │   │       └── MemberRepository.java
    │   │               │   ├── exception
    │   │               │   │   ├── dto
    │   │               │   │   └── error
    │   │               │   ├── infra
    │   │               │   ├── presentation
    │   │               │   │   └── MemberController.java
    │   │               │   ├── restapi
    │   │               │   │   └── MemberApi.java
    │   │               │   └── utils
    │   │               └── monthlyagenda
    │   │                   ├── application
    │   │                   │   ├── MonthlyAgendaService.java
    │   │                   │   ├── dto
    │   │                   │   │   ├── MonthlyAgendaRequestDto.java
    │   │                   │   │   └── MonthlyAgendaResponseDto.java
    │   │                   │   └── impl
    │   │                   │       └── MonthlyAgendaServiceImpl.java
    │   │                   ├── domain
    │   │                   │   ├── MonthlyAgenda.java
    │   │                   │   └── repository
    │   │                   │       └── MonthlyAgendaRepository.java
    │   │                   ├── presentation
    │   │                   │   └── MonthlyAgendaController.java
    │   │                   └── restapi
    │   │                       └── MonthlyAgendaApi.java
    │   └── resources
    │       ├── application-dev.yml
    │       ├── application-local.yml
    │       ├── application-test.yml
    │       ├── application.yml
    │       ├── db
    │       │   └── data.sql
    │       └── templates
    │           └── acceptEmail.html
    └── test
        └── java
            └── com
                └── example
                    └── rework
                        ├── MonthlyAgenda
                        │   ├── application
                        │   │   └── impl
                        │   │       └── MonthlyAgendaServiceImplTest.java
                        │   ├── fixture
                        │   │   └── MonthlyAgendaFixture.java
                        │   └── presentation
                        │       └── MonthlyAgendaControllerTest.java
                        ├── ReworkApplicationTests.java
                        ├── dailyagenda
                        │   ├── application
                        │   │   ├── ScheduleServiceTest.java
                        │   │   └── impl
                        │   │       └── DailyAgendaServiceImplTest.java
                        │   ├── fixture
                        │   │   └── DailyAgendaFixture.java
                        │   └── presentation
                        │       └── DailyAgendaControllerTest.java
                        ├── mail
                        │   └── application
                        │       └── impl
                        │           └── MailSenderServiceImplTest.java
                        ├── member
                        │   ├── application
                        │   │   └── impl
                        │   │       └── MemberServiceImplTest.java
                        │   ├── fixture
                        │   │   └── MemberFixture.java
                        │   └── presentation
                        │       └── MemberControllerTest.java
                        └── util
                            └── ControllerTestSupport.java
```


# 아키텍처 구조
<img width="753" alt="image" src="https://github.com/rework-kr/REWORK-SERVER/assets/79193811/aea022e2-37e3-4e89-8328-d0297fb507c9">

# git flow role
# Branch Naming Convention 브랜치명/이슈번호-기능명
![image](https://github.com/rework-kr/REWORK-SERVER/assets/79193811/b45ab380-f22a-49f0-b58a-f500abad1784)

# Commit Message Convention
# [브랜치명] 간단한 내용
## 태그명과 설명
####  Feat: 새로운 기능을 추가할 때 사용합니다.
#### Fix: 버그를 수정했을 때 사용합니다.
####  Design: 사용자 UI 디자인을 변경했을 때 사용합니다.
####  Style: 코드 포맷을 변경하거나 세미 콜론을 수정했을 때 사용합니다. 코드 로직 변경이 없는 경우에도 사용됩니다.
####  Refactor: 프로덕션 코드를 리팩토링했을 때 사용합니다.
####  Comment: 필요한 주석을 추가 또는 변경했을 때 사용합니다.
####  Docs: 문서를 수정했을 때 사용합니다.
####  Test: 테스트 코드를 추가하거나 리팩토링 테스트 코드를 작성했을 때 사용합니다. 실제 사용하는 코드에 변경이 없는 경우에도 사용됩니다.
####  Chore: 빌드 업무를 수정하거나 패키지 매니저를 변경했을 때 사용합니다. 프로덕션 코드에 변경이 없는 경우에 사용됩니다.
####  Rename: 파일 또는 폴더명을 수정하거나 옮겼을 때 사용합니다.
####  Remove: 파일을 삭제했을 때 사용합니다.
