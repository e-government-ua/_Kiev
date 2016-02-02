'use strict';

var path = require('path');
var _ = require('lodash');

// All configurations will extend these options
// ============================================
var all = {
  env: process.env.NODE_ENV,

  // Root path of server
  root: path.normalize(__dirname + '/../../..'),

  // Server port
  port: process.env.PORT || 9000,

  // Secret for session, you will want to change this and make it an environment variable
  secrets: {
    session: process.env.SESSION_SECRET
  },

  activiti: {
    prot: process.env.ACTIVITI_PROT,
    host: process.env.ACTIVITI_HOST,
    port: process.env.ACTIVITI_PORT,
    rest: process.env.ACTIVITI_REST,
    username: process.env.ACTIVITI_USER,
    password: process.env.ACTIVITI_PASSWORD,
    session: {
      sessionIdle: process.env.ACTIVITI_SESSION_IDLE || 60 * 80, //sec show warning
      timeOut: process.env.ACTIVITI_SESSION_TIMEOUT || 60 * 20, //sec close session after warning
      interval: process.env.ACTIVITI_SESSION_INTERVAL || 60 * 10 //sec update session
    }
  },

  ssl: {
    private_key: process.env.PRIVATE_KEY,
    certificate: process.env.CERTIFICATE,
    port: process.env.SSL_PORT
  },

  // List of user roles
  userRoles: ['guest', 'user', 'admin'],

  request: {
    debug: process.env.DEBUG
  }

};

// Export the config object based on the NODE_ENV
// ==============================================
var result = _.merge(
  require('./' + process.env.NODE_ENV + '.js') || {},
  all);
module.exports = result;
