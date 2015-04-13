'use strict';

// Use local.env.js for environment variables that grunt will set when the server starts locally.
// Use for your api keys, secrets, etc. This file should not be tracked by git.
//
// You will need to set these on the server you deploy to.

module.exports = {
	DEBUG: '',
	DOMAIN: 'localhost',
	SESSION_SECRET: 'dashboardjs-secret',

	//activiti settings
	ACTIVITI_PROT: 'protocol for activiti service [http|https]',
	ACTIVITI_HOST: 'activiti rest service host [localhost]',
	ACTIVITI_PORT: 8081, //port for activiti service
	ACTIVITI_REST: 'activiti rest service path [activiti-rest/service]',
	ACTIVITI_AUTH_BASIC: 'activiti basic auth [Basic a2VybWl0Omtlcm1pdA==]'
};