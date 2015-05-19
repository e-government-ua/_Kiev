'use strict';

// use local_config.js if you need to locally change some of the settings
// as described in README
module.exports = {
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

