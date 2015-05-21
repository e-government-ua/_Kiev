#!/bin/sh

echo starting central-js 
cd /project/central-js
echo "npm install ..." 
npm install

cd client
echo "bower install (client) ..." 
bower install --config.interactive=false
echo "nmp install (client) ..." 
npm install
echo "run grunt (client) ..." 
grunt --debug

cd ..
echo "run grunt in screen, to restore type 'screen -r central-js' " 
screen -S central-js -d -m grunt -w
