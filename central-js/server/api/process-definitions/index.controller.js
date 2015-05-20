var request = require('request');

module.exports.index = function(options, callback) {
	var url = options.protocol+'://'+options.hostname+':'+options.port+options.path+'/rest/process-definitions';
	return request.get({
		'url': url,
		'auth': {
			'username': options.username,
			'password': options.password
		}
	}, callback);
};