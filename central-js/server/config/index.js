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
		'hostname': 'poligon.igov.org.ua',
		'port': '8443',
		'path': '/wf-central/service',
		'username': 'activiti-master',
		'password': 'UjhtJnEvf!'
	},
	'bankid': {
		//'protocol': 'https',
		//'hostname': 'bankid.privatbank.ua',
		//'port': null,
		'sProtocol_AccessService_BankID': 'https', //Test
		'sHost_AccessService_BankID': 'bankid.privatbank.ua', //Test
                'sHost_ResourceService_BankID': 'bankid.privatbank.ua', //Test
		//'sProtocol_AccessService_BankID': 'https', //Prod
		//'sHost_AccessService_BankID': 'bankid.org.ua', //Prod
                //'sHost_ResourceService_BankID': 'biprocessing.org.ua', //Prod
		'client_id': '9b0e5c63-9fcb-4b11-84ff-31fc2cea8801',
		'client_secret': ''
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