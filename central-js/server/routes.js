'use strict';
var express = require('express');

module.exports = function(app) {
	app.use('/', express.static(__dirname + '../../client/build/'));
	app.use('/api/account', require('./api/account/index'));
	app.get('/api/bankid/login', require('./api/bankid/login'));
	app.use('/api/bankid/account', require('./api/bankid/account'));
	app.use('/api/documents', require('./api/documents/index'));
	app.use('/api/journal', require('./api/journal/index'));
	app.use('/api/login', require('./api/login/index'));
	app.use('/api/logout', require('./api/logout/index'));
	app.use('/api/places', require('./api/places/index'));
	app.use('/api/process-definitions', require('./api/process-definitions/index'));
	app.get('/api/process-form', require('./api/process-form/get'));
	app.post('/api/process-form', require('./api/process-form/post'));
	app.get('/api/service', require('./api/service/index'));
	app.use('/api/service/documents', require('./api/service/documents'));
	app.use('/api/service/journal', require('./api/service/journal'));
	app.use('/api/messages', require('./api/messages/index'));
	app.get('/api/services', require('./api/services/index'));
	app.post('/api/uploadfile', require('./api/uploadfile/post'));

	app.use('/', function(req, res, next) {
		res.render(__dirname + '../../client/build/index.html');
		next();
	});
};