# :microphone: HYPE VOICE - 성우 지망생들을 위한 플랫폼

<img src="/uploads/b6b4ceafdd99ffd4ef35b80f46990371/image.png" width="500px" height="300px">

## 서비스 소개
> Hype 한 Voice로 성우가 되고 싶은 사람은 누구나! <br> 내가 올린 작업물들이 곧 내 포트폴리오! <br> 같은 꿈을 키워나가는 사람들과 함께 연습과 피드백도 가능합니다! 😎
<br>


## 프로젝트 진행 기간
2024.01.04 ~ 2024.02.16 (7주)
SSAFY 10th 공통 프로젝트<br>
<br>


## :star: 기획 배경
게임이나 애니메이션, 대중교통등을 통해 모두 '성우'분들과 간접적으로 접해보셨을 겁니다.<br>
이런 성우가 되려면 어떻게 해야 할까요?<br>
현재 공식적인 성우가 되기 위해서는 방송사들의 공채가 유일한 방법입니다. 하지만 1~2년에 한번 열리며, 심하면 600:1까지 되는<br>
경쟁률로 인해 지망생들은 오랜기간 지망생 생활을 하며 성우가 되기 위해 노력합니다.<br>

HypeVoice는 이를 통해 성우 지망생, 특히 아마추어 성우들을 위한 서비스의 필요성을 느끼고 기획하게 되었습니다!<br>
직접 네이버카페, 오픈 채팅방을 통해 설문을 받아서 저희의 타겟층이 원하는 서비스를 목표로 했습니다.<br>
HypeVoice를 통해 내 목소리를 소개를 좀 더 쉽고 편하게 할 수 있습니다!!<br>
<br>



## 기술 스택
***
### Frontend

- Visual Studio Code : 1.85.1
- React : 2.27.0
- Typescript : 5.2.2
- Recoil : 0.7.7
- Tanstack-React-Query : 5.17.19

### Backend
- IntelliJ : 2023.3.4
- Java : 17.0.9
- Springboot : 3.2.2
- Spring Data JPA : 3.2.2
- Spring Security : 6.2.1
- QueryDSL : 5.0.0
- JPQL : 6.4.1
- Junit5

### Infra
- AWS EC2
- Nginx
- Docker

## :microphone: 주요 기능
***
### 1. 로그인/회원가입
  - 소셜로그인으로 회원가입 진행 (네이버, 카카오)
  - 꼭 필요한 정보만 받고 타겟층이 주로 사용하는 소셜을 사용한 회원가입으로 접근성을 높였습니다.
<br>

### 2. 나만의 포트폴리오 - 보이스
- 개발자에게는 GitHub, notion이 있는 것처럼, 성우 지망생들에게는 Hype Voice로 포트폴리오를 제출할 수 있습니다!
- 메인 화면에서 사람들의 목소리에 대한 작업물이 담긴 포트폴리오(보이스)를 한 곳에 모아볼 수 있습니다.
- 미디어, 목소리톤, 목소리스타일, 성별, 나이대 등을 선택해 관심있는 보이스만 검색할 수도 있습니다.
- hypevoice를 찾아온 유저는 새로운 성우를 발굴할 수도 있고, 작업물을 맡길 성우를 구할 수도 있습니다.
- 좋아요 기능을 통해 관심있는 목소리를 한 번에 모아 볼 수도 있습니다.
<br>

### 3. 작업물
- 하나의 작업물 당 사진, 녹음본, 대본, 유튜브링크, 설명을 묶어서 등록함으로써, 유저에게 친화적인 등록 과정을 제공합니다.
- 유저는 대표 작업물 설정을 통해 보여주고 싶은 작업물을 우선순위로 보여줄 수 있습니다.
- 작업물 하나에 관련 자료를 묶어 보여주면서 보는 사람에게 친화적인 UI 구현
<br>

### 4. 피드백 게시판
- 영상이나 올리거나 글을 작성해서 피드백을 받거나 자유롭게 소통할 수 있습니다.
- 피드백 글은 제목, 내용, 작성자, 제목+내용 등으로 검색할 수 있습니다.
- 각 글에는 댓글을 작성할 수 있습니다.
- 글 목록은 offset 방식으로 구현되어, 필요한 페이지만 조회해서 넘겨주도록 구현했습니다.
<br>

### 5. 스튜디오
- webrtc를 사용했으며, openvidu를 활용해 서버를 구축했습니다.
- 각 방 생성 시 최대 6명까지 들어갈 수 있습니다.
- 방을 들어가면 화상통화, 녹음, 화상채팅, 화면공유가 가능합니다.
- 녹음의 경우 녹음을 시작하기 전 자신의 목소리만 녹음할지, 다른 사람의 목소리를 모두 포함해서 녹음할 지 지정할 수 있습니다.
<br>


## 협업 툴
- Git
- Jira
- Notion
- Figma
- ERDcloud
- Discord


## :pushpin: 협업 환경
- Gitlab
    - 코드 버전 관리
    - 기능별, 이슈별 브랜치를 생성해서 진행
- JIRA
    - 매주 월요일 오전 목표량을 정해 스프린트 진행
    - 이슈 별 해야하는 업무를 자세히 작성하고, Story Point와 Epic을 설정하여 작업
- 데일리 스크럼
    - 매주 오전 데일리 스크럼을 진행하여 서로 간의 진행 상황 공유 및 그날 진행할 업무 브리핑
    - 매일 소통하면서 새로 생기는 이슈에 빠른 대응 가능해짐
- Notion
    - 회고록 및 회의록이 있을 때마다 매번 기록하여 보관
    - 참고할 기술, 자료 확보 시 자료 공유 페이지에 올려서 모두가 볼 수 있도록 공유
    - 컨벤션 정리 (git, gerrit 등)
    - 문서 정리 (요구사항 명세서, ERD 명세서, API 명세서)
    - develop에 merge되는 브랜치 정리해서 공유
    - 백엔드, 프론트엔드 각각 페이지를 구성해서 실행 과정 정리, secret.yml 파일 등 공유
- Gerrit
    - 각 브랜치에 리뷰오픈으로 push하여 리뷰에서 통과못하면 merge 못하도록 방지
    - dto에 필요한 추가 변수, 테스트코드 오류, css 깨짐, typescript 오류 부분 등 확인하며 코드 리뷰 진행
    - 코드 리뷰가 모두 완료된 브랜치가 생기면 프론트엔드,백엔드 각 지정한 1명이 develop 브랜치에 merge 진행
<br>


## 프로젝트 산출물
***
- [요구사항 명세서](https://www.notion.so/d01ee2ea90fc4556bd6a9e908a9d73e4?v=fccc247768d74dadbfd692fbc215b6a5)
- [디자인(피그마)](https://www.figma.com/file/jchmfM8lJfwxfb2Mhng0fB/%EB%B0%9C%ED%91%9C%EC%9A%A9?type=design&mode=design&t=U2i59v4Hq9zUyUhP-0)
- [API 명세서](https://www.notion.so/API-ff34150cba8045c5a7b079c00e840ce1)
- [컨벤션](https://www.notion.so/31bc1ec7f94545ad8ecb0b7d44fd07d0)
<br>


## 👨‍💻 팀 구성
***
<!-- 가운데 정렬 -->
|이름|역할|
|:---:|:---|
|[윤선경(팀장)](https://github.com/Sunkyoung-Yoon)|- BackEnd<br> - 전역예외 처리 및 테스트 코드 틀 구성 및 작성<br> - Spring security, JWT, JPA를 이용한 소셜로그인, 회원가입 구현 (인증, 인가)<br> - 회원,게시판,댓글, 좋아요 관련 CRUD 구현<br> - AWS S3를 활용한 파일 업로드 기능 구현 및 보이스, 작업물 파일 관련 코드 작성<br> - 게시글 검색 시 querydsl 사용으로 가독성 높임 및 offset 방식으로 구현<br> - 오픈비두 서버 배포 및 react, springboot 배포<br> - Nginx 프론트, 백엔드 분기 처리 (/, /api)|
|[최재식(부팀장)](https://github.com/jsc1014)|- BackEnd<br>- 테스트코드 작성<br>- Spring JPA를 이용한 보이스(포트폴리오), 작업물, 카테고리 관련 CRUD 구현<br>- JPQL, native query를 사용한 db 관련 로직 처리 및 페이징 처리<br>- 녹음물, 스크립트과 같은 파일 관련 api 연동<br>|
|[김가빈]()|- BackEnd<br> - openvidu를 통한 WebRTC 기능 구현<br> - openvidu 기능 테스트<br> - 백엔드 스튜디오 관리 API 구현(방 만들기/방 찾기/방 나가기/방장 부여)<br> - 백엔드 스튜디오 녹음 파일 저장 및 삭제 기능 구현 <br> - 백엔드 스튜디오 참가 인원 관리 API 구현<br> - JPQL, native query를 사용한 db 관련 로직 처리 및 페이징 처리, 관련 테스트 코드 작성|
|[김금환](https://github.com/imkhkim)|- 내 보이스 구현<br> - 좋아요 누른 보이스 구현<br> - 스크롤 네비게이션 구현<br> - 마이페이지 구현<br> - 내 보이스, 마이 페이지 CRUD 구현<br> - 여러 컴포넌트들 구현<br> - 화면 그리드 나누기<br> - 전체적인 CSS 다듬기|
|[안상준](https://github.com/yonggari0821?tab=repositories)|- OpenVidu 화면 구현<br> - StudioList 관련 기능 구현(방 생성, 방 참가, 방 검색)<br> - 전반적인 프로젝트 설계 (SPA - Routes 구조, 폴더 구조)<br> - Recoil 및 TanStack-Query 활용 (서버 및 클라이언트 사이트 전역 상태 관리, 타입스크립트 관련 설계)<br> - 작업물 생성, 수정, 삭제 관련 기능 및 화면 구현<br>|
|[김상민]()|- FrontEnd<br>- Community FeedBack 게시판 기능 구현<br>- (React-Quill을 이용한 글 작성 및 삭제, 댓글 작성 및 삭제, 음성 녹음 후 업로드 기능 등)<br>- WebRTC Openvidu를 이용한 Studio 기능의 기초적인 구현<br>- 웹페이지 내의 CSS 디자인 구현<br>|
<br>


## 아키텍처
<img src="/uploads/c06f11332422434ffd0bfdfb35c48b3c/image.png" width="800px" height="550px">
<br>


## ERD
<img src="/uploads/17a36e2f5a33d531b3a9dec1a6b73b70/image.png" width="800px" height="550px">
<br>


## :microphone: HypeVoice 실행 화면
***
- 회원가입 및 로그인 / 로그아웃
    - 소셜로그인 (네이버, 카카오)

        ![kakao](/uploads/0c44947dea3618bd4dfae0d6a469b300/kakao.gif)
        ![naver](/uploads/00e28ded3aa575b12ca962d67d884e8e/naver.gif)
- 메인 화면
    - 보이스 목록
    - 보이스 카테고리 검색
    - 보이스 좋아요 기능
    - 보이스 좋아요를 누르면 보이스 좋아요 리스트에 추가
    - 다른 사람의 보이스 들어가서 보는 장면
- 내 보이스
    - 내 보이스 작업물 등록

        ![작업물_등록](/uploads/2230d19b106bbe608da47a9bfc1bc8d2/작업물_등록.gif)
    - 대표 작업물 등록, 작업물 상세 화면

        ![대표_작업물_등록__조회](/uploads/275974d23d3aa453c39c114bcbd08b3e/대표_작업물_등록__조회.gif)
- 피드백 게시판
    - 게시글 목록, 페이지

        ![게시글_목록](/uploads/ab6a3c5577784d31c61df5f6f24f692f/게시글_목록.gif)
    - 게시글 검색

        ![게시글_검색](/uploads/f807be6def9fac7be590ca703567994d/게시글_검색.gif)
    - 게시글 등록 (글 및 유투브 링크 적용)

        ![게시물_등록](/uploads/216f27538429d053ca336a3c7b36ad5d/게시물_등록.gif)
    - 보이스 리플

        ![보이스_리플](/uploads/4f979cb9142d8711e479beb84f2970d0/보이스_리플.gif)
- 스튜디오
    - 방 생성
    - 화면 공유, 채팅 기능
    - 녹음(자신것만, 모두) 둘 중에 하나 선택해서 녹음해서 다운 받는 장면
- 마이페이지
    - 마이페이지 프로필 이미지, 닉네임 수정

        ![마이_페이지_수정](/uploads/ad83c698710807d60da0fa6c86d1295d/마이_페이지_수정.gif)
    - 회원 탈퇴
    
        ![회원_탈퇴](/uploads/30291f4e03198484bf1ff79962128cb4/회원_탈퇴.gif)
    

