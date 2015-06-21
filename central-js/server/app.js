var express = require('express');
var fs = require('fs');
var config = require('./config');

var app = express();

require('./config/express')(app);
require('./routes')(app);
require('./config/server')(app);

exports = module.exports = app;