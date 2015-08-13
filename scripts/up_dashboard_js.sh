#!/bin/sh

echo "starting dashboard-js"
cd /project/dashboard-js
echo "npm install ..." 
npm install
echo "bower install ..." 
bower install --config.interactive=false

echo "grunt serve run in screen, to restore type 'screen -r dashboard-js'"
screen -S dashboard-js -d -m PORT=8443 grunt serve
