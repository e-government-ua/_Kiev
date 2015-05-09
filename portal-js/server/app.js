/**
 * Main application file
 */

'use strict';

// Set default node environment to development
process.env.NODE_ENV = process.env.NODE_ENV || 'development';

var express = require('express');
var fs = require('fs');
var config = require('./config/environment');
// Setup server
var app = express();

var port = config.port;
var server;

if (config.ssl.port) {
	var privateKey = fs.readFileSync(config.ssl.private_key).toString();
	var certificate = fs.readFileSync(config.ssl.certificate).toString();

	var credentials = {
		key: privateKey,
		cert: certificate
	};

	server = require('https').createServer(credentials, app)
} else {
	server = require('http').createServer(app);
}

require('./config/express')(app);
require('./routes')(app);

// Start server
server.listen(port, config.ip, function() {
	console.log('Express server listening on %d, in %s mode', port, app.get('env'));
});

// Expose app
exports = module.exports = app;