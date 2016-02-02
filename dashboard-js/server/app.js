/**
 * Main application file
 */

'use strict';

// Set default node environment to development
process.env.NODE_ENV = process.env.NODE_ENV || 'development';
process.env.NODE_TLS_REJECT_UNAUTHORIZED = 0;
var express = require('express');
var fs = require('fs');
var config = require('./config/environment');
var logger = require('./components/logger').setup();
// Setup server
var app = express();
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
server.listen(config.port, config.ip, function() {
	logger.info('Express server listening on %d, in %s mode', config.port, app.get('env'));
});

// Expose app
exports = module.exports = app;