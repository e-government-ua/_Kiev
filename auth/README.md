
cd bankid-oauth-provider-mock
mvn jetty:run-exploded

cd bankid-oauth-example
mvn jetty:run-exploded -Djetty.port=8081
