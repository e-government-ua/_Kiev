rem cd /sybase && pm2 stop central-js && pm2 delete central-js

rem cd central-js
 npm install
 cd client
 bower install
 npm install
 npm install grunt-contrib-imagemin
 grunt debug

pm2 start process.json --name central-js
pm2 info central-js
