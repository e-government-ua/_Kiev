cd ..

call gem install sass
call npm cache clean
call npm install
call bower install
call npm install grunt-contrib-imagemin
call grunt build
cd dist
	call npm install
cd ..

cd _
