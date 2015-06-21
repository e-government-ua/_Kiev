var express = require('express');
var fs = require('fs');
var config = require('./config');

var app = express();

process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
require('./config/express')(app);
require('./routes')(app);
require('./config/server')(app);

exports = module.exports = app;