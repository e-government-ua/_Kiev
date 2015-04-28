#!/bin/sh
# This script is responsible for setting up environment - install required programs,
# configure them and export correct environment variables
# It should not build and deploy project - this is responsibility of separate scripts
#

echo Provisioning the machine

PROGRAMS_HOME=/opt

echo ******************************************************************
echo ** Setting up tools for backend \(Jave\)                          **
echo ******************************************************************
echo

echo installing JDK
# --no-install-recommends avoids installing X11 stuff.
apt-get -y --no-install-recommends install openjdk-7-jdk

mkdir -p /tmp/egov
mkdir -p /tmp/egov/log
DOWNLOAD_DIR=/tmp/egov/download
mkdir -p $DOWNLOAD_DIR

echo installing Maven
#using direct download because Ubuntu package has too many unnecessary dependencies
MAVEN_DIST=apache-maven-3.3.1-bin.tar.gz
wget -P $DOWNLOAD_DIR http://apache.volia.net/maven/maven-3/3.3.1/binaries/$MAVEN_DIST
tar -xvzf $DOWNLOAD_DIR/$MAVEN_DIST -C $PROGRAMS_HOME
rm $DOWNLOAD_DIR/$MAVEN_DIST

echo installing Tomcat
#Ubuntu repository ATM contains only Tomcat 6 - so using direct download to get something more up-to-date
TOMCAT_DIST=apache-tomcat-8.0.21.tar.gz
wget -P $DOWNLOAD_DIR http://apache.cp.if.ua/tomcat/tomcat-8/v8.0.21/bin/$TOMCAT_DIST
tar -xvzf $DOWNLOAD_DIR/$TOMCAT_DIST -C $PROGRAMS_HOME
rm $DOWNLOAD_DIR/$TOMCAT_DIST
chown -R vagrant $PROGRAMS_HOME/apache-tomcat-8.0.21

echo ******************************************************************
echo ** Setting up tools for portal \(PHP\): TODO                      **
echo ******************************************************************
echo

echo ******************************************************************
echo ** Finalizing setup                                             **
echo ******************************************************************
echo

echo setting up environment

ENV_FILE=/etc/profile.d/egov-env.sh
echo "#!/bin/sh
export PROGRAMS_HOME=$PROGRAMS_HOME
export MAVEN_HOME=\$PROGRAMS_HOME/apache-maven-3.3.1
export TOMCAT_HOME=\$PROGRAMS_HOME/apache-tomcat-8.0.21
export PATH=$PATH:\$MAVEN_HOME/bin:\$TOMCAT_HOME/bin
" > $ENV_FILE

echo ******************************************************************
echo ** Provisioning complete, ready to build and deploy projects    **
echo ******************************************************************


