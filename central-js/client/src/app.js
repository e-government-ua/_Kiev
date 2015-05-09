var express = require('express');
var ejs = require('ejs');

var app = express();

app.engine('html', ejs.renderFile);

app.use('/', express.static(__dirname + '/'));

app.use('/', function (req, res, next) {
  console.log('Time:', Date.now());
  next();
});

app.get('/', function (req, res, next) {
  res.render('../index.html');
  next();
});

var server = app.listen(3000, function () {

  var host = server.address().address;
  var port = server.address().port;

  console.log('Example app listening at http://%s:%s', host, port);

});