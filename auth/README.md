BankId OAuth Implementation Library
===================================

this modules aims to provide common usage of bank id security stuff by providing bankid-oauth-weblib jar file.        
importing it to the project allows you achieve security cross-cutting concern.
all you need it is import library and put some secured annotation
(e.g. @Secured("citizen.role.somerole") or @Secured("scope.view.passport"))

Usefull e-gov Links
-------------

* [bankid official site](http://bankid.org.ua/)
* [sequence diagram](https://www.gliffy.com/go/publish/7853145)
* [done in scope of issue #37](https://github.com/e-government-ua/i/issues/37)
* [authorization using bankId](https://github.com/e-government-ua/portal-php/wiki/%D0%90%D0%B2%D1%82%D0%BE%D1%80%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D0%B8-%D0%BF%D0%BE-bankid)

Common Links
------------

* [The OAuth 2.0 Authorization Framework](https://tools.ietf.org/html/rfc6749)
* [The OAuth 2.0 Authorization Framework: Bearer Token Usage](https://tools.ietf.org/html/rfc6750)

Library Usage
-------------

* [OAuth 2 Developers Guide](https://github.com/spring-projects/spring-security-oauth/blob/master/docs/oauth2.md)


maven modules description
-------------------------
- **bankid-oauth-weblib**       -  *oauth weblib provides security capabilities, more documentation see in bankid-oauth-weblib/README.md**
- **bankid-oauth-example**      -  *weblib usage example, more documentation see in bankid-oauth-example/README.md*
- **bankid-oauth-provider-mock**    -  *bank simulation mock, more documentation see in bankid-oauth-provider-mock/README.md*
- **bankid-oauth-systests**         -  *groovy system tests, more documentation see in bankid-oauth-systests/README.md*

systests preparation
--------------------

    cd bankid-oauth-provider-mock
    mvn jetty:run-exploded

    cd bankid-oauth-example
    mvn jetty:run-exploded -Djetty.port=8081

    cd bankid-oauth-systests
    mvn test -DskipTests=false

// TODO provide more description


