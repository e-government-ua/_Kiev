'use strict';

var path = require('path');
var _ = require('lodash');

function requiredProcessEnv(name) {
  if (!process.env[name]) {
    throw new Error('You must set the ' + name + ' environment variable');
  }
  return process.env[name];
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
    host: process.env.ACTIVITI_HOST || 'localhost',
    port: process.env.ACTIVITI_PORT || 8080,
    rest: process.env.ACTIVITI_REST || 'activiti-rest/service/repository',
    auth: {
      basic: process.env.ACTIVITI_AUTH_BASIC || 'Basic a2VybWl0Omtlcm1pdA=='
    }
  },

  bankid : {
    host: process.env.BANK_ID_HOST || 'localhost',
    path: process.env.BANK_ID_PATH || 'service/bankid',
    appid: process.env.BANK_ID_APP_ID || 'myApp',
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