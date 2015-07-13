cd ./..

pm2 start process.json --name central-js
pm2 info central-js

cd ./_
