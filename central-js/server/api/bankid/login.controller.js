var request = require('request');

module.exports.index = function(options, callback) {
	var url = options.protocol+'://'+options.hostname+options.path+'/oauth/token';
	console.log(url);
	return request.get({
		'url': url,
		'qs': {
			'grant_type': 'authorization_code',
			'client_id': options.params.client_id,
			'client_secret': options.params.client_secret,
			'code': options.params.code,
			'redirect_uri': options.params.redirect_uri
		}
	}, callback);
};