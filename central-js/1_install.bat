rem cd /sybase && pm2 stop central-js && pm2 delete central-js

rem cd central-js

echo started
npm install -g bower
npm install -g grunt
rem npm install -g bower-update
npm install grunt-cli -g
npm install grunt --save-dev
npm install pm2 --save-dev
npm install pm2 -cli -g
echo finished
