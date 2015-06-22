var express = require('express');
var router = express.Router();

router.use(function(req, res, next) {
	process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
	var login = require('./login.controller');
	
	var config = require('../../config');
	var bankid = config.bankid;
	
	var options = {
		protocol: bankid.sProtocol_AccessService_BankID,
		hostname: bankid.sHost_AccessService_BankID,
		path: '/DataAccessService',
		params: {
			client_id: bankid.client_id,
			client_secret: bankid.client_secret,
			code: req.query.code || null,
			redirect_uri: req.query.redirect_uri || null
		}
	};
	
	var callback = function(error, response, body) {
		if(body){
			var result = JSON.parse(body)
			if(result.hasOwnProperty('access_token')){
				req.session.access = result;
			}
		}
		res.send(body);
		res.end();
	}
	
	login.index(options, callback);
});

module.exports = router;