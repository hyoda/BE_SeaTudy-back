<img src="https://springblog.s3.ap-northeast-2.amazonaws.com/readme/seatudy_banner.png" width="">

- <a href="https://youtu.be/BPsMbtXCyAg">시연영상 보기</a>

## 🐟 프로젝트 소개
**_함께 공부하며 물고기도 모아봐요!_** <br>
SeaTUDY는 함께 공부하며 체크인/체크아웃 시스템을 이용해 공부시간을 기록할 수 있는 서비스입니다.<br>

### [SeaTUDY 체험해보기](https://www.seatudy.com)

함께 공부하는 즐거움을 주어 공부에 **_지속적인 동기부여_** 를 주고,<br>
게임화(Gamification)를 더하여 경쟁심을 자극해 **_자발적인 공부를 유도_** 하며,<br>
자기주도 학습능력 향상을 통한 **_목표지향적 능력 개발 도모_** 하는 서비스를 만들고 싶었습니다.<br>
공부포인트를 쌓아 물고기캐릭터도 모으고, 랭킹 시스템을 통해 재밌게 공부해보세요!

<br>

## 🗓 프로젝트 기간
2022/8/26 - 2022/10/7(6주간)

<br>

## 👥 5조 '역전의 명수'__BACKEND
<table>
<tr>
    <td align="center" width="25%"><a href="https://github.com/PaulKim330" target='_blank'>
    <img src="https://springblog.s3.ap-northeast-2.amazonaws.com/myungsoo.png" width="100%" height="100%"></a></td>
    <td align="center" width="25%"><a href="https://github.com/hlim9022" target='_blank'>
    <img src="https://springblog.s3.ap-northeast-2.amazonaws.com/heylim.png" width="100%" height="100%"></a></td>
    <td align="center" width="25%"><a href="https://github.com/minjpark3" target='_blank'>
    <img src="https://springblog.s3.ap-northeast-2.amazonaws.com/minjung.png" width="100%" height="100%"></a></td>
    <td align="center" width="25%"><a href="https://github.com/ghwo68" target='_blank'>
    <img src="https://springblog.s3.ap-northeast-2.amazonaws.com/hojae.png" width="100%" height="100%"></a></td>
</tr>
<tr>
    <td align="center"><strong>김명수🔸</strong></td>
    <td align="center"><strong>김혜림</strong></td>
    <td align="center"><strong>박민정</strong></td>
    <td align="center"><strong>이호재</strong></td>
</tr>
<tr>
    <td align="center">Todo카테고리 조회/생성/수정/삭제,<br>TodoList 조회/생성/수정/삭제,<br> 물고기 위치 변경/조회</td>
    <td align="center">소셜로그인(카카오/네이버/구글),<br> 실시간채팅,<br> CI/CD,<br> 엔티티 연관관계설정</td>
    <td align="center">Todo카테고리 조회/생성/수정/삭제,<br> TodoList 조회/생성/수정/삭제</td>
    <td align="center">D-day 조회/생성/수정/삭제,<br> 체크인/체크아웃 기능,<br> 전체랭킹(주간/일간) & 개인랭킹(주간/일간) 조회</td>
</tr>
<tr>
    <td align="center"><a href="https://github.com/PaulKim330" target='_blank'>github</a></td>
    <td align="center"><a href="https://github.com/hlim9022" target='_blank'>github</a></td>
    <td align="center"><a href="https://github.com/minjpark3" target='_blank'>github</a></td>
    <td align="center"><a href="https://github.com/ghwo68" target='_blank'>github</a></td>
</tr>
</table>

<br>

## 📌 핵심기능


> **⏰ 체크인/체크아웃 시스템**
> - 공부시간 기록(총 공부시간 & 하루 공부시간)
> - 매일 오전 5시 리셋되어 새로운 하루 시작
>
> <img src="https://springblog.s3.ap-northeast-2.amazonaws.com/seatudy-gif/check-in%26out.gif" width="600">

<br>

> **📈 공부량 추적(통계페이지)**
> - 전체 회원의 랭킹을 일일별/주간별 확인가능
> - 내 포인트 확인가능 → 포인트는 총 공부량의 시간 = 1pt / 1hr
> - 한 주동안 요일별 내 공부량, 주차별 내 공부량 그래프로 확인가능
> - 1년동안 공부량 확인가능(= Github 잔디심기)
>
> <img src="https://springblog.s3.ap-northeast-2.amazonaws.com/seatudy-gif/rank.gif" width="600">

<br>

> **💬 실시간 채팅(Stomp)**
> - 바다테마에 맞춰 5대양의 이름을 가진 고정 채팅방
> - 상대방이 입장/퇴장시 확인 가능
> - 각 채팅방에서 실시간으로 유저들끼리의 랭킹 확인가능
>
> <img src="https://springblog.s3.ap-northeast-2.amazonaws.com/seatudy-gif/chatroom.gif" width="600">

<br>

> 📅 TodoList & D-day 관리
> - 달력에서 Todolist 완료율 확인 가능
> - 메인 페이지에서도 Todolist 완료 체크 및 확인가능
> - 가장 가까운 D-day는 메인 상단바에서 확인가능
>
> <img src="https://springblog.s3.ap-northeast-2.amazonaws.com/readme/todolist.gif" width="600">
> <img src="https://springblog.s3.ap-northeast-2.amazonaws.com/readme/dday.png" width="600">

<br>

> 🐡 물고기 꾸미기 기능
> - 공부량을 기반으로  unlock된 물고기를 메인페이지에 꾸밀 수 있음
> - 게임적 요소: 더 많은 물고기를 unlock하여 메인페이지를 꾸밀 수 있도록 동기부여
>
> <img src="https://springblog.s3.ap-northeast-2.amazonaws.com/seatudy-gif/fish-deco.gif" width="600">

<br>

> 🌊 ASMR 기능
> - 공부에 집중할 수 있도록 4가지 백색소음 제공(파도/숲속/모닥불/비)
>
> <img src="https://springblog.s3.ap-northeast-2.amazonaws.com/readme/asmr.png" width="600">


<br>

## 🔧 기술적 의사결정
<table border="2">
    <tbody>
        <tr align="center">
            <td width="20%"><b>사용기술</b></td>
            <td width="80%"><b>기술설명</b></td>
        </tr>
        <tr>
            <td align="center">Stomp</td>
            <td>http 프로토콜이 아닌 Stomp 프로토콜을 이용하여 실시간으로 유저들이 채팅을 할 수 있도록 하기위함</td>
        </tr>
        <tr>
            <td align="center">Redis</td>
            <td>각 채팅방에 입장한 유저들의 인원수를 카운트하기 위해 세션ID와 채팅방 정보를 저장해둘 필요가 있었고, RDS를 사용하기에는 잦은 채팅방 입장/퇴장 이벤트가 일어나기 때문에 RDS보다 속도가 빠른 in-memory DB ‘Redis’가 최적이라고 생각함</td>
        </tr>
        <tr>
            <td align="center">Oauth 로그인</td>
            <td>사용자들이 회원가입의 번거로움을 덜고, 편리하게 서비스를 이용할 수 있도록 하기위해 사용</td>
        </tr>
        <tr>
            <td align="center">NGINX</td>
            <td>DDos와 같은 공격으로부터 보호하고(Https SSL 인증서), 좀 더 빠른 응답을 위해 적용</td>
        </tr>
        <tr>
            <td align="center">GitHub Actions</td>
            <td>프론트엔드와 백엔드의 효율적인 협업을 위해, 자동배포를 진행</td>
        </tr>
        <tr>
            <td align="center">MySQL</td>
            <td>유저에 따라 다른 데이터가 형성되기 때문에 관계형 DB를 선택하였습니다. 그 중에서도 MySQL를 선택한 이유는 가장 널리 사용되고 있는 DB 중 하나이기에 많은 유저 경험과 레퍼런스가 있다는 장점이 있고, 저희도 그동안 계속해서 사용해와서 다루기에 좀 더 수월하여 선택</td>
        </tr>
    </tbody>
</table>

<br>


## 🎯 트러블슈팅

> Issue #1__채팅방 인원수 카운트 오류
<table border="2">
    <tbody>
        <tr>
            <td align="center" width="20%">문제</td>
            <td width="80%">각 채팅방에 입장한 인원수 카운트를 할 때, 만약 한 유저가 두 브라우저를 띄우고 같은 채팅방에 입장시 2명으로 인원수가 카운트 되어지는 문제</td>
        </tr>
        <tr>
            <td align="center">과정</td>
            <td>중복허용이 되지 않는 Set을 이용: 입장시 세션ID(키)와 닉네임(밸류)을 저장한 후, 닉네임(밸류)을 기준으로 Set에 저장하여 중복카운트 방지 → 입장은 중복없이 카운트가 되었으나, 퇴장시 아직 하나의 브라우저가 남아있음에도 인원이 -1명이 됨(유저가 여전히 채팅방에 있음에도 퇴장으로 확인)</td>
        </tr>
        <tr>
            <td align="center">해결</td>
            <td>입장시, 1번과 동일하게 value 기준으로 Set을 만들어 카운트(중복제거)하고, 퇴장시 세션ID(키)를 찾아 제거 후 다시 value기준 Set으로 카운트
                <img src="https://springblog.s3.ap-northeast-2.amazonaws.com/readme/ts_chatroom.png"></td>
        </tr>
    </tbody>
</table>

<br>

> Issue #2__시간세팅
<table border="2">
    <tbody>
        <tr>
            <td align="center" width="20%">문제</td>
            <td width="80%">공부시간 기록서비스이다보니 자정을 기준으로 하루를 초기화하게되면 유저가 공부하는 중간에 체크인/체크아웃을 다시 해줘야하는 번거로움이 발생하는 경우가 많음
(팀회의를 거쳐 항해의 시스템과 동일한 새벽5시에 하루초기화 결정)</td>
        </tr>
        <tr>
            <td align="center">과정</td>
            <td>만약 자정이 넘은 시간에 체크인을 하게 된 경우, 체크인 시간값에서 -5시간을하여  DB에 저장하여 같은 하루의 공부량으로 기록하도록 함.
하지만, 체크인/아웃을 할 때마다 -5시간을 적용해줘야해서 성능의 저하가 우려</td>
        </tr>
        <tr>
            <td align="center">해결</td>
            <td>서버시간자체를 자정에 하루를 초기화하는 것이 아닌 오전5시에 초기화되도록 로직 수정. 체크인/체크아웃 시간에서 -5시간을 할 필요없이 현재 시간 자체를 DB에 저장하여도 같은 하루로 설정되도록 함
                <img src="https://springblog.s3.ap-northeast-2.amazonaws.com/readme/ts_timesetting.png"></td>
        </tr>
    </tbody>
</table>

<br>

> Issue #3__CI/CD
<table border="2">
    <tbody>
        <tr>
            <td align="center" width="20%">문제</td>
            <td width="80%">자동배포 성공 후에도 서버가 작동하지 않는 문제</td>
        </tr>
        <tr>
            <td align="center">과정</td>
            <td>GitHub Action으로 자동배포시에 .gitignore 로 설정된 application.properties 파일은 커밋되있지 않기 때문에 빌드시에 값들이 지정되지 않아서 작동이 되지않음.
따라서, GitHub 시크릿에 값을 하나하나 설정하여 빌드 때에 이 설정값들이 지정될 수 있도록 하여 빌드성공. 하지만, 설정값이 많은데 일일이 값을 시크릿 설정으로 해줘야하는 번거로움이 있음</td>
        </tr>
        <tr>
            <td align="center">해결</td>
            <td>GitHub 시크릿에 하나하나 설정값을 지정하지 않고 전체 설정값을 GitHub 시크릿에 저장해두고, CI/CD를 진행할 때 application.properties 파일을 생성한 후 이 값 전체를 넣어주도록 설정함
                <img src="https://springblog.s3.ap-northeast-2.amazonaws.com/readme/ts_cicd.png"></td>
        </tr>
    </tbody>
</table>

<br>

## 🖥 기술스택

#### Language & Build Tool
<div>
    <img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white">
    <img src="https://img.shields.io/badge/GRADLE-02303A?style=for-the-badge&logo=GRADLE&logoColor=white">
</div>

#### Framework
<div>
    <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white">
    <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white">
</div>

#### Library
<div>
    <img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white">
    <img src="https://img.shields.io/badge/Swagger-25A162?style=for-the-badge&logo=Swagger&logoColor=white">
    <img src="https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=Junit5&logoColor=white">
    <img src="https://img.shields.io/badge/WebSocket-black?style=for-the-badge&logo=WebSocket&logoColor=white">
    <img src="https://img.shields.io/badge/Stomp-black?style=for-the-badge&logo=Stomp&logoColor=white">
</div>

#### API
<div>
    <img src="https://img.shields.io/badge/KAKAO-FFCD00?style=for-the-badge&logo=KAKAO&logoColor=black">
    <img src="https://img.shields.io/badge/NAVER-03C75A?style=for-the-badge&logo=NAVER&logoColor=white">
    <img src="https://img.shields.io/badge/GOOGLE-4285F4?style=for-the-badge&logo=GOOGLE&logoColor=white">
</div>


#### Database
<div>
    <img src="https://img.shields.io/badge/AWS%20RDS-232F3E?style=for-the-badge&logo=AmazonAWS&logoColor=FF9A00"/>
    <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white">
    <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"/>
</div>


#### Deploy
<div>
    <img src="https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=NGINX&logoColor=white"/>
    <img src="https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=GitHubActions&logoColor=white"/>
    <img src="https://img.shields.io/badge/Amazon%20EC2-232F3E?style=for-the-badge&logo=Amazon EC2&logoColor=FF9A00">
    <img src="https://img.shields.io/badge/AWS%20S3-232F3E?style=for-the-badge&logo=AmazonAWS&logoColor=FF9A00"/>
    <img src="https://img.shields.io/badge/AWS%20CodeDeploy-232F3E?style=for-the-badge&logo=AmazonAWS&logoColor=FF9A00"/>
</div>

#### Utilty
<div>
    <img src="https://img.shields.io/badge/Intellij%20Ultimate-000000?style=for-the-badge&logo=intellij idea&logoColor=white">
    <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white">
    <img src="https://img.shields.io/badge/workbench-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
</div>
<br>


## 🛠 아키텍쳐
<img src="https://springblog.s3.ap-northeast-2.amazonaws.com/readme/SEATUDY_architecture.jpg">

<br>

## 📌 ERD
<img src="https://springblog.s3.ap-northeast-2.amazonaws.com/readme/seatudy_erd.png">


