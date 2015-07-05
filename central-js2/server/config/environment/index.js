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


  debug: process.env.DEBUG,

  server: {
    protocol: process.env.SERVER_PROTOCOL,
    port: process.env.SERVER_PORT,
    key: process.env.SERVER_KEY,
    cert: process.env.SERVER_CERT,

    session: {
      secret: process.env.SESSION_SECRET,
      key: [process.env.SESSION_KEY_ONE, process.env.SESSION_KEY_TWO],
      secure: process.env.SESSION_SECURE,
      maxAge: process.env.SESSION_MAX_AGE // 3 * 60 * 1000 = 3 min
    }
  },

  activiti: {
    protocol: process.env.ACTIVITI_PROTOCOL,
    hostname: process.env.ACTIVITI_HOSTNAME,
    port: process.env.ACTIVITI_PORT,
    path: process.env.ACTIVITI_PATH,
    username: process.env.ACTIVITI_USER,
    password: process.env.ACTIVITI_PASSWORD
  },

  bankid: {
    sProtocol_AccessService_BankID: process.env.BANKID_SPROTOCOL_ACCESS_SERVICE,
    sHost_AccessService_BankID: process.env.BANKID_SHOST_ACCESS_SERVICE,
    sProtocol_ResourceService_BankID: process.env.BANKID_SPROTOCOL_RESOURC_SERVICE,
    sHost_ResourceService_BankID: process.env.BANKID_SHOST_RESOURCE_SERVICE,
    client_id: process.env.BANKID_CLIENTID,
    client_secret: process.env.BANKID_CLIENT_SECRET
  }

};

// Export the config object based on the NODE_ENV
// ==============================================
module.exports = _.merge(
  all,
  require('./' + process.env.NODE_ENV + '.js') || {});
