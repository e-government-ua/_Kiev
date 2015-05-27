rem cd /sybase && pm2 stop central-js && pm2 delete central-js

pm2 stop central-js
pm2 delete central-js

rem cd central-js
rem  npm install
rem  cd client
rem  bower install
rem  npm install
rem  npm install grunt-contrib-imagemin
rem  grunt debug

pm2 start process.json --name central-js
pm2 info central-js
