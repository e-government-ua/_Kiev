#cd /sybase && pm2 stop central-js && pm2 delete central-js

# cd /sybase/central-js
#  npm install -g bower
#  npm install -g grunt
#  npm install grunt-cli -g
#  npm install grunt --save-dev

 npm install
 cd client
 bower install
 npm install
 npm install grunt-contrib-imagemin
 grunt debug
cd ..

#pm2 stop central-js && pm2 delete central-js

#pm2 start process.json --name central-js
#pm2 info central-js
