# **DOSTicket**

## 🏷 **프로젝트 개요**
**DOSTicket**은 사용자들이 공연, 영화, 스포츠 경기 등 다양한 이벤트의 티켓을 신속하고 간편하게 예매할 수 있는 서비스를 제공합니다. 특히, 예매 트래픽이 급증하는 순간에도 안정적으로 좌석을 선택하고 결제를 완료할 수 있는 시스템을 목표로 개발되었습니다.
프로젝트 이름 **DOSTicket**은 팀명 도리도스(DoriDOS)에서 영감을 받았으며, 동시에 많은 트래픽이 몰리는 상황에서도 안정적인 예매 경험을 제공한다는 의미를 담고 있습니다.

</br>

## 🏢 **팀 소개**
- **팀명**: 도리도스 (DoriDOS)
- **팀명 의미**: 도리도스는 수많은 곳에서 발생하는 대량 트래픽을 효과적으로 처리하는 시스템을 의미하는 *DOS 공격*에서 착안하였습니다. 티켓 예매 과정에서 많은 사용자가 동시에 접속하는 상황을 빠르고 안정적으로 처리하는 것을 목표로 하고 있습니다.

</br>

## 🎯 **주요 기능**
1. **티켓 예매**: 실시간으로 좌석 상태를 확인하고, 원하는 좌석을 선택하여 예매할 수 있습니다.

     [-> 좌석 선택 시 분산 락을 이용한 동시성 문제 해결 과정 포스팅](https://alswns7984.tistory.com/92)
</br>

2. **결제 시스템 연동**: 토스 간편 결제 기능을 제공하여 빠르고 안전하게 결제를 진행합니다.

     [-> 토스 간편 결제 시스템 연동 포스팅](https://alswns7984.tistory.com/95)
</br>

3. **예매 내역 조회**: 사용자는 자신이 예매한 내역을 언제든지 확인하고 관리할 수 있습니다.
</br>

4. **소셜 로그인 지원**: OAuth2를 이용한 소셜 로그인 기능을 제공해 간편히 로그인을 진행할 수 있습니다.

     [-> OAuth2 이용한 소셜로그인 구현 1편](https://alswns7984.tistory.com/73)

     [-> OAuth2 이용한 소셜로그인 구현 2편](https://alswns7984.tistory.com/75)
</br>

5. **API 문서화 (RestDocs 활용)**: Spring RestDocs를 이용해 API를 문서화하였으며, 각 기능별 요청 및 응답 형식에 대한 상세한 정보를 제공합니다.

     [-> Spring RestDocs를 활용한 API 문서화 포스팅](https://alswns7984.tistory.com/26)
</br>


## ⚙️ 기술 스택 
![기술스택](https://github.com/minjun7984/readme-image/blob/main/KakaoTalk_Photo_2024-10-13-19-31-51.jpeg)
</br>
</br>
## 인프라
![인프라](https://github.com/minjun7984/readme-image/blob/main/KakaoTalk_Photo_2024-10-13-19-31-59.jpeg)
</br>
</br>

## 배포구성도
- Github Actions 이용한 CI 환경 구축 [(해당 과정 포스팅)](https://alswns7984.tistory.com/77)

![인프라](https://github.com/minjun7984/readme-image/blob/main/cicd.jpeg)
</br>
</br>
## 테스트
- 100개 이상의 테스트코드 작성 

![테스트](https://github.com/minjun7984/readme-image/blob/main/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202024-10-17%20%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB%201.52.09.png)
</br>
</br>
