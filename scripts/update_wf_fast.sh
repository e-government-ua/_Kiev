#!/bin/sh
echo Set up or update workflow fast
cd /project

echo Building workflow project
mvn install -DskipTests -B

echo Shutdown Tomcat for safer deploy
echo \(may throw java.net.ConnectException: Connection refused if Tomcat isn't yet running - it's ok, just ignore it. Will fix later.\)
$TOMCAT_HOME/bin/shutdown.sh

echo Copy WAR to webapps dir
WAR_SRC=/project/wf-region/target/wf-region.war
TOMCAT_WEBAPPS_DIR=$TOMCAT_HOME/webapps
cp $WAR_SRC $TOMCAT_WEBAPPS_DIR

echo Start Tomcat
$TOMCAT_HOME/bin/startup.sh
