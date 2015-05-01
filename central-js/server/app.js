var express = require('express');
var ejs = require('ejs');

var app = express();
app.engine('html', ejs.renderFile);
app.use(require('./routes'));

var server = app.listen(3000, function () {
  var host = server.address().address;
  var port = server.address().port;

  console.log('Example app listening at http://%s:%s', host, port);
});

module.exports = app;