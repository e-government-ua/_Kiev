'use strict';
module.exports = {
	'server': {
		'protocol': 'http',
		'key': '/sybase/cert/server.key',
		'cert': '/sybase/cert/server.crt',
		'port': '8443'
	},
	'activiti': {
		'protocol': 'https',
		'hostname': '52.17.126.64',
		'port': '8080',
		'path': '/wf-dniprorada/service',
		'username': 'activiti-master',
		'password': 'UjhtJnEvf!'
	},
	'bankid': {
		'protocol': 'https',
		'hostname': 'bankid.privatbank.ua',
		'port': null,
		'path': '/DataAccessService',
		'client_id': 'dniprorada',
		'client_secret': 'NzVmYTI5NGJjMDg3OThlYjljNDY5YjYxYjJiMjJhNA=='
	}
 }

