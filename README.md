> 토비의 스프링 책을 읽고 배우고 느낀점 등을 정리합니다.

# 서문 

- [사전모임 #1 (라이브러리 vs 프레임워크)](https://velog.io/@joosing/%ED%86%A0%EB%B9%84%EC%9D%98-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%82%AC%EC%A0%84%EB%AA%A8%EC%9E%84-1-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC-vs-%ED%94%84%EB%A0%88%EC%9E%84%EC%9B%8C%ED%81%AC)
- [저자의 말 #2 (누군가를 고민하며)](https://velog.io/@joosing/%ED%86%A0%EB%B9%84%EC%9D%98-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8A%A4%ED%84%B0%EB%94%94)
- [들어가며 #3 (몸소 실천하는 스프링)](https://velog.io/@joosing/follow-toby-spring-intro)
- [Vol. 1 시작하며 #4 (객체지향의 기본으로)](https://velog.io/@joosing/follow-toby-spring-vol1-start)

# 1장 오브젝트와 의존관계

### 글로 정리 
- [토비의 스프링 | 1장 오브젝트와 의존 관계 (책읽기 모임 나눔)](https://velog.io/@joosing/toby-spring-object-and-dependency-1-share)
- [토비의 스프링 | 1장 오브젝트와 의존 관계 (독서메모)](https://velog.io/@joosing/toby-spring-object-and-dependency-1-memo)
- [IoC(Inversion of Control) 현실 예제로 이해하기](https://velog.io/@joosing/understand-ioc-with-a-real-example)

### 예제 코딩 (Pull Request)
- [#1 초난감 DAO 구현의 시작](https://github.com/Jsing/follow-toby-spring/pull/2)
- [#2 DB 연결 기능 메서드로 추출](https://github.com/Jsing/follow-toby-spring/pull/3)
- [#3 상속 구조(템플릿 메서드 패턴, 팩토리 메서드 패턴)로 DB 연결에 대한 관심사 분리](https://github.com/Jsing/follow-toby-spring/pull/4)
- [#4 DB 연결 기능 별도의 클래스로 분리](https://github.com/Jsing/follow-toby-spring/pull/5)
- [#5 인터페이스 분리 (UserDao 내부에서 인터페이스 참조하도록 수정)](https://github.com/Jsing/follow-toby-spring/pull/6)
- [#6 오브젝트의 관계를 맺는 클라이언트](https://github.com/Jsing/follow-toby-spring/pull/8)
- [#7 UserDao 생성 책임 DaoFactory로 분리](https://github.com/Jsing/follow-toby-spring/pull/9)
- [#8 DaoFactory 내의 ConnectionMaker 생성 중복 코드 분리](https://github.com/Jsing/follow-toby-spring/pull/10)
- [#9 스프링 ApplicationContext에서 DaoFactory 설정 정보로 사용하기](https://github.com/Jsing/follow-toby-spring/pull/11)
- [#10 IoC/DI 사용하여 부가기능(참조 카운팅) 분리하기](https://github.com/Jsing/follow-spring-through-toby/pull/12)
- [#11 XML을 이용한 설정](https://github.com/Jsing/follow-spring-through-toby/pull/13)
- [#12 스프링에서 제공하는 DataSource 인터페이스로 전환](https://github.com/Jsing/follow-spring-through-toby/pull/14)
