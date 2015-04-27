BankId OAuth Implementation Library
===================================

this modules aims to provide common usage of bank id security stuff by providing bankid-oauth-weblib jar file.        
importing it to the project allows you achieve security cross-cutting concern.
all you need it is import library and put some secured annotation
(e.g. @Secured("citizen.role.somerole") or @Secured("scope.view.passport"))

Usefull Links
-------------

* [bankid official site](http://bankid.org.ua/)
* [sequence diagram](https://www.gliffy.com/go/publish/7853145)
* [done in scope of issue #37](https://github.com/e-government-ua/i/issues/37)

maven modules description
-------------------------
- **bankid-oauth-weblib**       *oauth weblib provides security capabilities*
- **bankid-oauth-example**      *weblib usage example*
- **bankid-oauth-provider-mock**    *bank simulation mock*
- **bankid-oauth-systests**         *groovy system tests*

systests preparation
--------------------

    cd bankid-oauth-provider-mock
    mvn jetty:run-exploded

    cd bankid-oauth-example
    mvn jetty:run-exploded -Djetty.port=8081

    run OAuthFlowTest in your IDE

// TODO provide description


