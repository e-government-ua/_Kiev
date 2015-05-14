var express = require('express');
var router = express.Router();

router.use(function(req, res, next) {
	process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
	var login = require('./login.controller');
	
	var config = require('../../config.js');
	var bankid = config.bankid;
	
	var options = {
		protocol: bankid.protocol,
		hostname: bankid.hostname,
		path: '/DataAccessService',
		params: {
			client_id: bankid.client_id,
			client_secret: bankid.client_secret,
			code: req.query.code || null,
			redirect_uri: req.query.redirect_uri || null
		}
	};
	
	var callback = function(error, response, body) {
		res.send(body);
		res.end();
	}
	
	login.index(res, options, callback);
});

module.exports = router;