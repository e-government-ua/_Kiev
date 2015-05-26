rem cd /sybase && pm2 stop central-js && pm2 delete central-js

rem cd central-js
rem  npm install -g bower
rem  npm install -g grunt
rem  npm install grunt-cli -g
rem  npm install grunt --save-dev

 npm install
 cd client
 bower install
 npm install
 npm install grunt-contrib-imagemin
 grunt debug

pm2 start process.json --name central-js
pm2 info central-js
