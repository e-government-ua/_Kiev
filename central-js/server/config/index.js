var _ = require('lodash');

var config = {
	'server': {
		'protocol': 'https',
		'key': '/sybase/cert/server.key',
		'cert': '/sybase/cert/server.crt',
		'port': '8443',
		'debug': 'false'
	},
	'activiti': {
		'protocol': 'https',
		'hostname': 'e-gov.org.ua',
		'port': '8443',
		'path': '/wf-central/service',
		'username': 'activiti-master',
		'password': 'UjhtJnEvf!'
	},
	'bankid': {
		'protocol': 'https',
		'hostname': 'bankid.privatbank.ua',
		'port': null,
		'client_id': 'dniprorada',
		'client_secret': 'NzVmYTI5NGJjMDg3OThlYjljNDY5YjYxYjJiMjJhNA=='
	}
};


try {
	var local_config = require('../local_config');
	_.extend(config, local_config);
} catch (e) {
	if (e.code === 'MODULE_NOT_FOUND') {
		// do nothing
	}
}

// use local_config.js if you need to locally change some of the settings
// as described in README
module.exports = config;   