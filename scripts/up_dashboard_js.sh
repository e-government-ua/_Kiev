#!/bin/sh

echo starting dashboard-js 
cd /project/dashboard-js
echo "npm install ..." 
npm install
echo "bower install ..." 
bower install

echo "grunt build ..."
grunt build


cd dist
echo "npm install (dist) ..."
npm install


cd ..
echo "grunt serve run in screen, to restore type 'screen -r dashboard-js' "
screen -S dashboard-js -d -m grunt serve

