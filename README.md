> 토비의 스프링 책을 읽고 배우고 느낀점 등을 정리합니다.
  
토비의 스프링 읽기 모임을 통해 1권을 다 읽을 수 있었다. 스프링이란 프레임워크가 그냥 코드를 좀더 쉽게 작성할 수 있게 도와주는 도구 정도로 생각하고 있었는데 스프링이 지향하는 생각들을 이해할 수 있어서 너무 좋았다. 몇 가지 인상깊었던 것들을 정리해 본다.

### 기술이 드러나지 않게
스프링은 기술의 복잡함과 비지니스 로직의 복잡함을 분리하려 했고, 기술적인 서비스를 제공하는 스프링 자신은 드러나지 않으면서 사용자가 비지니스 로직 개발에 집중할 수 있게 함을 지향했다고 한다. 멋있었다. 그리고 IoC/DI, AOP, PSA 같은 기술들의 존재 이유가 조금 이해 되었다. 예를 들면 IoC 컨테이너 없이 작성한 예전 클래스 코드들을 보면 의존성을 주입받고, 주입해 주기 위한 코드가 일정량 존재한다. 이런건 비지니스 로직을 구현하기 위한 기술적인 준비단계라고 할 수 있는데 비지니스 로직을 구현한 코드와 함께 섞여서 존재함을 이해할 수 있었다.

### 라이브코딩
예제 코드를 꼼꼼히 읽고 코드를 같이 작성해 보았는데 너무 좋았다. 점진적인 문제 해결 과정, 핵심적인 디자인 패턴, 테스트 방법, IDE 활용, 변수 네이밍 조차 배울 것이 되었다. 책을 읽으며 토비님 옆에 앉아서 라이브코딩을 지켜보는 것 같은 생동감을 느껴졌다. 사실 훌륭한 동료가 회사 곁에 있더라도 이 정도로 자세히 뭔가 보여주고 설명해주기는 힘들텐데 책이라는 매개체가 참 좋다는 생각이 들었다. 그럼에도 불구하고 책을 읽고 나니 좋은 개발자들과 발꿈치는 맞대고 함께 일하는 것에 대한 열망이 더 커진 것 같기도 하다.

### 함께
이런 모임을 처음 가져 보았는데, 책읽기를 완주하는데 도움이 되었다. 혼자 했다면 중간에 분명 포기했을 것 같다. 일정한 책임감과 누군가 함께 하고 있다는 생각이 끝까지 지속할 수 있게 해 준 것 같다. 앞으로 무언가를 함께 하려 더 노력하게 될 것 같다.


# 서문 

- [사전모임 #1 (라이브러리 vs 프레임워크)](https://velog.io/@joosing/%ED%86%A0%EB%B9%84%EC%9D%98-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%82%AC%EC%A0%84%EB%AA%A8%EC%9E%84-1-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC-vs-%ED%94%84%EB%A0%88%EC%9E%84%EC%9B%8C%ED%81%AC)
- [저자의 말 #2 (누군가를 고민하며)](https://velog.io/@joosing/%ED%86%A0%EB%B9%84%EC%9D%98-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8A%A4%ED%84%B0%EB%94%94)
- [들어가며 #3 (몸소 실천하는 스프링)](https://velog.io/@joosing/follow-toby-spring-intro)
- [Vol. 1 시작하며 #4 (객체지향의 기본으로)](https://velog.io/@joosing/follow-toby-spring-vol1-start)

# 1장 오브젝트와 의존관계

- [토비의 스프링 | 1장 오브젝트와 의존 관계 (책읽기 모임 나눔)](https://velog.io/@joosing/toby-spring-object-and-dependency-1-share)
- [토비의 스프링 | 1장 오브젝트와 의존 관계 (독서메모)](https://velog.io/@joosing/toby-spring-object-and-dependency-1-memo)
- [IoC(Inversion of Control) 현실 예제로 이해하기](https://velog.io/@joosing/understand-ioc-with-a-real-example)

# 2장 테스트

- [토비의 스프링 | 2장 테스트 (핵심 요약)](https://velog.io/@joosing/toby-spring-2-test-summary)  
- [토비의 스프링 | 2장 테스트 (느낌점, Q&A)](https://velog.io/@joosing/toby-spring-2-test-impressive-qna-pr)  
- [토비의 스프링 | 2장 테스트 (모임 중 든 생각, 기억에 남는 말말말)](https://velog.io/@joosing/toby-spring-2-test-words-thinking)  

# 3장 템플릿

- [토비의 스프링 | 3장 템플릿 (생각나눔)](https://velog.io/@joosing/toby-spring-3-template-share)
- [토비의 스프링 | 변하는 것과 변하지 않는 것을 분리하는 과정](https://velog.io/@joosing/toby-spring-3-process-of-separating-changing-from-unchanging)
- [콜백을 위해 항상 람다식을 전달하는게 좋나요?](https://velog.io/@joosing/pass-lambda-expression-for-callbacks)
- [토비의 스프링 | 3장 템플릿 (모임 중 든 생각, 기억에 남는 말말말](https://velog.io/@joosing/toby-spring-3-template-thinking-words)
- [토비의 스프링 | 3장 템플릿 (독서메모)](https://velog.io/@joosing/toby-spring-3-template-read-memo)

# 4장 예외

- [토비의 스프링 | 4장 예외 (배우고 느낀점)](https://velog.io/@joosing/toby-spring-4-exception-learn-and-thoughts)
- [토비의 스프링 | 4장 예외 (독서메모)](https://velog.io/@joosing/toby-spring-4-exception-summary)
- [토비의 스프링 | 4장 예외 (생각거리, 기억에 남는 말)](https://velog.io/@joosing/toby-spring-4-exception-after-meeting)

# 5장 서비스 추상화

- [토비의 스프링 | 5장 서비스 추상화 (학습)](https://velog.io/@joosing/toby-spring-5-abstraction-learn)
- [토비의 스프링 | 5장 서비스 추상화 (읽기모임)](https://velog.io/@joosing/toby-spring-5-service-abstraction-reading-club)
- [테스트 대역(Double)](https://velog.io/@joosing/test-double)

# 6장 AOP

- [토비의 스프링 | 6장 AOP 발전과정 (학습)](https://velog.io/@joosing/toby-spring-6-aop-history)
- [토비의 스프링 | 6장 AOP - Transaction (학습)](https://velog.io/@joosing/toby-spring-6-aop-transaction)
- [토비의 스프링 | 6장 AOP - 더 나은 설계, 테스트](https://velog.io/@joosing/toby-spring-6-aop-better-design-and-test)
- [토비의 스프링 | 6장 AOP (읽기모임)](https://velog.io/@joosing/toby-spring-6-aop-reading-club)

# 7장 스프링 핵심 기술의 응용

- [토비의 스프링 | 7장 스프링 핵심 기술의 응용 (SQL 쿼리문을 코드에서 분리해 보자)](https://velog.io/@joosing/toby-spring-7-separate-sql-from-dao)  
- [토비의 스프링 | 7장 스프링 핵심 기술의 응용 (SQL 쿼리문을 동적으로 변경해 보자)](https://velog.io/@joosing/toby-spring-7-update-sql-query)  
- [토비의 스프링 | 7장 스프링 핵심 기술의 응용 (읽기 모임)](https://velog.io/@joosing/toby-spring-7-reading-club)  
- [토비의 스프링 | 7장 스프링 핵심 기술의 응용 (여러가지 배운것)](https://velog.io/@joosing/toby-spring-7-etc)  

# 8장 
- [토비의 스프링 | 8장 스프링이란 무엇인가? (학습)](https://velog.io/@joosing/toby-spring-8-what-is-spring)  
- [토비의 스프링 | 8장 스프링이란 무엇인가? (읽기모임)](https://velog.io/@joosing/toby-spring-8-reading-club)  
