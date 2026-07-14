# ParcelKR

한국에 거주하는 외국인을 위한 택배 조회 앱입니다. 별도 백엔드 없이 클라이언트에서 배송사 API를 직접 호출하고, 한글로 오는 배송 상태 메시지를 번역해서 보여줍니다.

Kotlin Multiplatform과 Compose Multiplatform 기반으로 만들고 있으며, 현재는 Android로 개발 중입니다.

<br>

**Stack**

![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin%20Multiplatform-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)
![Ktor](https://img.shields.io/badge/Ktor-087CFA?style=flat-square&logo=ktor&logoColor=white)
![SQLDelight](https://img.shields.io/badge/SQLDelight-333333?style=flat-square)
![Koin](https://img.shields.io/badge/Koin-56C1FF?style=flat-square)
![Coroutines](https://img.shields.io/badge/Coroutines-2B2B2B?style=flat-square)

<br>

**MVP 범위**

- 송장번호로 실시간 배송 조회 + 시간순 타임라인
- 택배사 자동 감지
- 배송 상태 메시지 번역 (고정 용어 사전 기반)
- 통관고유부호(개인통관부호) 저장
- 주문 확인 메일에서 송장번호 자동 인식
- 다국어 UI (영/중/베/한)

<br>

**Modules**

```
shared/     공통 로직 + UI (commonMain, 향후 멀티플랫폼 확장 대비)
androidApp/ Android 진입점
```

현재 Android MVP 단계로 개발 중입니다.
