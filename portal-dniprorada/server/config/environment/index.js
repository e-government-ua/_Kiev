'use strict';

var path = require('path');
var _ = require('lodash');

(function() {

  var required_for_activiti = [
    'ACTIVITI_PROT',
    'ACTIVITI_HOST',
    'ACTIVITI_REST',
    'ACTIVITI_AUTH_BASIC'
  ];
  requiredProcessEnv(required_for_activiti);

  var required_for_bankid = [
    'BANK_ID_HOST',
    'BANK_ID_APP_ID',
    'BANK_ID_PATH'
  ];
  requiredProcessEnv(required_for_bankid);

  if (process.env.SSL_PORT) {
    var required_for_ssl = [
      'PORT',
      'PRIVATE_KEY',
      'CERTIFICATE'
    ];
    requiredProcessEnv(required_for_ssl);
  }
})();

function requiredProcessEnv(envNames) {
  for (var name in envNames) {
    if (!process.env[envNames[name]]) {
      throw new Error('You must set the ' + envNames[name] + ' environment variable');
    }
  }
}

// All configurations will extend these options
// ============================================
var all = {
  env: process.env.NODE_ENV,

  // Root path of server
  root: path.normalize(__dirname + '/../../..'),

  // Server port
  port: process.env.PORT || 9000,

  // Should we populate the DB with sample data?
  seedDB: false,

  // Secret for session, you will want to change this and make it an environment variable
  secrets: {
    session: process.env.SESSION_SECRET
  },

  activiti: {
    prot: process.env.ACTIVITI_PROT || 'http',
    host: process.env.ACTIVITI_HOST || 'localhost',
    port: process.env.ACTIVITI_PORT || 8080,
    rest: process.env.ACTIVITI_REST || 'activiti-rest/service/repository',
    auth: {
      basic: process.env.ACTIVITI_AUTH_BASIC || 'Basic a2VybWl0Omtlcm1pdA=='
    }
  },

  ssl: {
    private_key: process.env.PRIVATE_KEY,
    certificate: process.env.CERTIFICATE,
    port: process.env.SSL_PORT
  },

  bankid: {
    host: process.env.BANK_ID_HOST || 'localhost',
    path: process.env.BANK_ID_PATH || 'service/bankid',
    appid: process.env.BANK_ID_APP_ID || 'myApp'
  },

  // List of user roles
  userRoles: ['guest', 'user', 'admin'],

  // MongoDB connection options
  mongo: {
    options: {
      db: {
        safe: true
      }
    }
  },

};

// Export the config object based on the NODE_ENV
// ==============================================
module.exports = _.merge(
  all,
  require('./' + process.env.NODE_ENV + '.js') || {});