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
    prot: process.env.ACTIVITI_PROT || 'http',
    host: process.env.ACTIVITI_HOST || 'localhost',
    port: process.env.ACTIVITI_PORT || 8080,
    rest: process.env.ACTIVITI_REST || 'activiti-rest/service',
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

  // MongoDB connection options
  mongo: {
    options: {
      db: {
        safe: true
      }
    }
  },

  request: {
    debug: process.env.DEBUG  || false
  }

};

// Export the config object based on the NODE_ENV
// ==============================================
module.exports = _.merge(
  all,
  require('./' + process.env.NODE_ENV + '.js') || {});
