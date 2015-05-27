var request = require('request');
var crypto = require('crypto');

module.exports.index = function(options, callback) {
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