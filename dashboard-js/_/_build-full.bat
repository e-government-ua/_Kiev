cd ..

call npm install
call npm list grunt
call npm list grunt-google-cdn
call bower install
call npm install grunt-contrib-imagemin
call grunt build
cd dist
	call npm install
cd ..

cd _
