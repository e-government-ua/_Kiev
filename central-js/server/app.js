/**
 * Main application file
 */

'use strict';

// Set default node environment to development
process.env.NODE_ENV = process.env.NODE_ENV || 'local';

var express = require('express');
var config = require('./config/environment');
//var nock = require('nock');

//nock.recorder.rec();
// Setup server
var app = express();

require('./config/server')(app);
require('./config/express')(app);
require('./routes')(app);


// Expose app
module.exports = app;
