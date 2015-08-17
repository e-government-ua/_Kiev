#!/bin/sh

echo "starting central-js"
cd /project/central-js
echo "npm install ..." 
npm install
echo "bower install ..." 
bower install --config.interactive=false

echo "run grunt serve in screen, to restore type 'screen -r central-js'" 
screen -S central-js -d -m grunt serve
