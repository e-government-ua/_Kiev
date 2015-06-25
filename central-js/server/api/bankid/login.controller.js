var request = require('request');
var crypto = require('crypto');

module.exports.index = function(req, res) {
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

	var url = options.protocol+'://'+options.hostname+options.path+'/oauth/token';
    var unhashed = options.params.client_id +
                   options.params.client_secret +
                   options.params.code;
    var clientSecretHashed = crypto.createHash('sha1').update(unhashed).digest('hex');

	return request.get({
		'url': url,
		'qs': {
			'grant_type': 'authorization_code',
			'client_id': options.params.client_id,
			'client_secret': clientSecretHashed,
			'code': options.params.code,
			'redirect_uri': options.params.redirect_uri
		}
	}, callback);
};