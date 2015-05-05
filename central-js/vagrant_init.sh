#!/usr/bin/env bash

curl -sL https://deb.nodesource.com/setup | sudo bash -
sudo su
apt-get install -y nodejs git
npm install -g grunt-cli bower
su vagrant
mkdir /tmp/node1
mkdir /tmp/node2
mkdir /tmp/bower
ln -s /tmp/node1 /vagrant/node_modules
ln -s /tmp/node2 /vagrant/client/node_modules
ln -s /tmp/bower /vagrant/client/bower_components