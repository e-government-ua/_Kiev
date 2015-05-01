var express = require('express');
var router = express.Router();

router.use(function(req, res, next) {
	process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
	var login = require('./login.controller');
	
	var options = {
		protocol: 'https',
		hostname: 'bankid.privatbank.ua',
		path: '/DataAccessService',
		method: 'GET',
		params: {
			client_id: 'dniprorada',
			client_secret: 'NzVmYTI5NGJjMDg3OThlYjljNDY5YjYxYjJiMjJhNA==',
			code: req.query.code || null,
			redirect_uri: req.query.redirect_uri || null
		}
	};
	
	var callback = function(error, response, body) {
		res.send(body);
		res.end();
	}
	
	login.index(options, callback);
});

module.exports = router;