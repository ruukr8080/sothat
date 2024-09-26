~~# 디렉토리 패키지 구조의 선택

디렉토리 구조 또한 디자인패턴 처럼 솔루션이 있다.

이는 사람 취향마다, 조직의 컨벤션마다 다르겠지만 개인 프로젝트의 경우엔
선택을 해야한다.

이 고민의 해결을 도와줄 유명한 디렉토리 구조가 있다.


---

## 계층형 디렉토리 구조

com
ㄴ example
ㄴ sju
ㄴ config
ㄴ controller
ㄴ domain
ㄴ repository
ㄴ service
ㄴ security
ㄴ exception

스프링 *웹 계층의 대표 클래스* 혹은 디렉터리들을 기반으로 패키징됨.

## 도메인형 디렉터리 구조

com
ㄴ example
ㄴ vivid
ㄴ domain
| ㄴ user
| | ㄴ api
| | ㄴ application
| | ㄴ dao
| | ㄴ domain
| | ㄴ dto
| | ㄴ exception
| ㄴ video
| | ㄴ api
| | ㄴ application
| | ㄴ dao
| | ㄴ domain
| | ㄴ dto
| | ㄴ exception
| ...
ㄴ global
ㄴ auth
ㄴ common
ㄴ config
ㄴ error
ㄴ infra
ㄴ util

스프링 웹 계층에 주목하기보다는 *도메인에 주목*함.

각각의 도메인 별로 패키지 분리가 가능하여 관리에 있어서 계층형 방식보다 직관적이며,
각각의 도메인들은 서로를 의존하는 코드가 없도록 설계하기 적합해서 코드의 재활용성이 향상 된다고함.

확실히 기존의 계층형 디렉토리 구조만 써오다가 어쩔 수 없이 각종 클래스들이 늘어나버리는 탓에 머리가 어지러워짐.
프로젝트 규모가 커지면서 여러 기능을 구현하려하니 한데 묶기 애매한 클래스들이 생기고 점점 난잡하다는 느낌이 든다.

## 도메인 기반 패키징 하는 방법

📌 최상위 레벨의 패키지

- 최상위 레벨에서는 domain과 global로 패키징합니다.
- domain 패키지에서는 프로젝트와 DB 구조에서 핵심 역할을 하는 domian entitiy를 기준으로 하위 패키지를 구성합니다.
- global 패키지에서는 프로젝트 전방위적으로 사용할 수 있는 클래스 들로 구성됩니다.

📌 domain 하위 패키지

- 앞서 설명한 것처럼 user, video와 같이 핵심 domain entity 별로 패키지가 구성됩니다.
- 각각의 domian 하위 패키지는 api, application, dao, domain, dto, exception 패키지로 구성됩니다.
- api : controller 클래스가 존재합니다. 해당 프로젝트에서 스프링 부트는 Rest API 서버로서의 역할만을 하기 때문에, 명시적으로 api라는 네이밍으로 패키징 했습니다.
- application : 주로 service 클래스들이 존재합니다. DB 트랜잭션이 일어나며, 주된 비즈니스 로직을 담당합니다. Service 클래스들 뿐만 아니라, handler와 같은 같은 성격의 다른 클래스
  또한 포함하기 때문에 application이라는 네이밍으로 패키징 했습니다.
- dao : dao, repository 클래스들로 구성됩니다.
- domain : entity 클래스들로 구성됩니다.
- dto : dto 클래스들로 구성됩니다.
- exception : exception 클래스들로 구성됩니다.

📌 global 하위 패키지

- 해당 패키지에는 특정 domain에 종속되지 않고, 프로젝트 전방위적으로 사용할 수 있는 클래스들이 모여있습니다.
- global 패키지는 auth, common, config, error, infra, util 패키지로 구성됩니다.

- auth : 인증, 인가와 관련된 클래스들로 구성됩니다.
- common : 공통 클래스 혹은 공통 value 클래스들로 구성됩니다.
- config : 각종 configuration 클래스로 구성됩니다.
- error : 공통 exception, error와 관련된 클래스로 구성됩니다.
- infra : 외부 모듈, api 등을 사용하는 클래스로 구성됩니다.
- util : 공통 util성 클래스들로 구성됩니다.

# 결론

대세는 도메인형이다.~~