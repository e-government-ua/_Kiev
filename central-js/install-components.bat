rem cd /sybase && pm2 stop central-js && pm2 delete central-js

rem cd central-js
 npm install -g bower
 npm install -g grunt
 npm install grunt-cli -g
 npm install grunt --save-dev

pm2 start process.json --name central-js
pm2 info central-js
