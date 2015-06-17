var express = require('express');
var router = express.Router();
var syncSubject = require('../service/syncSubject.controller');
var account = require('./account.controller');
var async = require('async');

router.use(function(req, res, next) {
	var config = require('../../config');

	async.waterfall([
		function(callback) {
			var bankid = config.bankid;
			var options = {
				protocol: bankid.protocol,
				hostname: bankid.hostname,
				path: '/ResourceService',
				params: {
					client_id: bankid.client_id,
					client_secret: bankid.client_secret,
					access_token: req.query.access_token || null
				}
			};

			account.index(options, function(error, response, body) {
				if (error) {
					callback({code: response.statusCode, error: error}, null);
				} else {
					var customer = body.customer;
					callback(null, customer);
				}
			});
		},

		function(customer, callback) {
			var activiti = config.activiti;

			var syncSubjectOptions = {
				protocol: activiti.protocol,
				hostname: activiti.hostname,
				port: activiti.port,
				path: activiti.path,
				username: activiti.username,
				password: activiti.password,
				params: {
					sINN: customer.inn || null
				}
			};

			syncSubject.index(syncSubjectOptions, function(error, response, body) {
				if (error) {
					callback({code: response.statusCode, error: error}, null);
				} else {
					var subject = JSON.parse(body);
					callback(null, subject);
				}
			});
		}
	], function(err, result) {
		if (err) {
			res.status(err.code);
			res.send(err.error);
			res.end();
		} else {
			req.session.subject = result;
			res.send(result);
			res.end();
		}
	});

});

module.exports = router;