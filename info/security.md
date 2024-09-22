## HTTP 보안 헤더

HTTP 보안 헤더
브라우저와 함께 동작하는 헤더로써, 브라우저 렌더링 하는 과정에서 보안 취약점을 미연에 방지하고자 하는 목적으로 사용된다.

아래 항목들이 대표적인 HTTP 보안 헤더 중 일부이다.

Strict-Transport-Security
모든 사이트가 HTTPS를 통해 접근해야 하며, HTTP로 접근을 하는 경우 HTTPS로 변경되게 한다.

X-XSS-Protection
페이지에서 XSS 취약점이 발견되면 페이지 로딩을 중단하는 헤더이다.

X-Frame-Options
페이지를 frame, iframe, embed, object 내부에서 렌더링을 허용할지 여부를 나타낼 수 있다.

Permissions-Policy
웹사이트에서 사용할 수 있는 기능과 사용할 수 없는 기능을 명시적으로 선언하는 헤더이다.

X-Content-Type-Options
Content-type 헤더에서 제공하는 MIME 유형이 브라우저에 의해 임의로 변경되지 않게 하는 헤더이다.

Referrer-Policy
Referer 헤더에서 사용할 수 있는 데이터로, 현재 요청을 보낸 페이지의 주소 등의 정보가 담겨있다.

Content-Security-Policy
콘텐츠 보안정책 (이하 CSP), XSS 공격이나 데이터 삽입 공격 등 다양한 보안 위협을 막기 위한 설계 기법