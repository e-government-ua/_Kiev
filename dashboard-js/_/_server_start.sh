cd ./..

pm2 start process.json --name dashboard-js
pm2 info dashboard-js

cd ./_
