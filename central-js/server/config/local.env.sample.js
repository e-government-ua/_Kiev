'use strict';

// Use local.env.js for environment variables that grunt will set when the server starts locally.
// Use for your api keys, secrets, etc. This file should not be tracked by git.
//
// You will need to set these on the server you deploy to.

module.exports = {
  DOMAIN: 'http://localhost:9000',
  // Control debug level for modules using visionmedia/debug
  DEBUG: 'true',

  sServerRegion: 'https://test.region.igov.org.ua',
  SERVER_PROTOCOL: 'http',
  SERVER_PORT: '9000',
  SERVER_KEY: '/sybase/cert/server.key',
  SERVER_CERT: '/sybase/cert/server.crt',

  SESSION_SECRET: 'put yor session secret here',
  SESSION_KEY_ONE: 'solt1 for session',
  SESSION_KEY_TWO: 'solt2 for session',
  SESSION_SECURE: true,
  SESSION_MAX_AGE: 10800000, // 3h*60m*60s*1000ms

  ACTIVITI_PROTOCOL: 'https',
  ACTIVITI_HOSTNAME: 'test.igov.org.ua',
  ACTIVITI_PORT: 8080,
  ACTIVITI_PATH: '/wf-central/service',
  ACTIVITI_USER: 'activiti-master',
  ACTIVITI_PASSWORD: 'UjhtJnEvf!',

  BANKID_SPROTOCOL_ACCESS_SERVICE: 'https',
  BANKID_SHOST_ACCESS_SERVICE: 'bankid.privatbank.ua',
  BANKID_SPROTOCOL_RESOURC_SERVICE: 'https',
  BANKID_SHOST_RESOURCE_SERVICE: 'bankid.privatbank.ua',
  BANKID_CLIENTID: 'testIgov',
  BANKID_CLIENT_SECRET: 'testIgovSecret'
};
