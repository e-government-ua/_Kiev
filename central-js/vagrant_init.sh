#!/usr/bin/env bash

curl -sL https://deb.nodesource.com/setup | sudo bash -
sudo su
apt-get install -y nodejs git
npm install -g grunt-cli bower