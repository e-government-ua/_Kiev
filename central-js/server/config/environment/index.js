'use strict';

var path = require('path');
var _ = require('lodash');

// All configurations will extend these options
// ============================================
var all = {
  env: process.env.NODE_ENV,
  // Root path of server
  root: path.normalize(__dirname + '/../../..'),


  debug: process.env.DEBUG,

  server: {
    sServerRegion: process.env.sServerRegion,
    protocol: process.env.SERVER_PROTOCOL,
    port: process.env.SERVER_PORT,
    key: process.env.SERVER_KEY,
    cert: process.env.SERVER_CERT,

    session: {
      secret: process.env.SESSION_SECRET,
      key: process.env.SESSION_KEY_ONE && process.env.SESSION_KEY_TWO ?
        [process.env.SESSION_KEY_ONE, process.env.SESSION_KEY_TWO] : undefined,
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
    client_secret: process.env.BANKID_CLIENT_SECRET,
    /**
     * Should be used only in connection to privateKey and privateKeyPassphrase,
     * when BANKID enables ciphering of its data. In that case BANKID service has
     * public key on its side, generated from privateKey in config
     * */
    enableCipher: process.env.BANKID_ENABLE_CIPHER,
    /**
     * Will work and Should be specified if enableCipher === true
     */
    privateKey: process.env.BANKID_PRIVATE_KEY,
    /**
     * It's passphrase for privateKey.
     * Will work and Should be specified if enableCipher === true.
     */
    privateKeyPassphrase: process.env.BANKID_PRIVATE_KEY_PASSPHRASE
  },

  soccard: {
    socCardAPIProtocol: process.env.KC_SPROTOCOL_ACCESS_SERVICE,
    socCardAPIHostname: process.env.KC_SHOST_ACCESS_SERVICE,
    socCardAPIVersion: process.env.SOC_CARD_APIVERSION || '1.0',
    socCardAPIClientID: process.env.SOC_CARD_API_CLIENTID || 'here should be test client id',
    socCardAPIClientSecret: process.env.SOC_CARD_API_CLIENT_SECRET || 'here should be test client secret',
    socCardAPIPrivateKey: process.env.SOC_CARD_PRIVATE_KEY || '/sybase/cert/server.key',
    socCardAPIPrivateKeyPassphrase: process.env.SOC_CARD_PRIVATE_KEY_PASSPHRASE || 'some passprhase for the key'
  },

  hasSoccardAuth: function () {
    return this.soccard.socCardAPIProtocol
      && this.soccard.socCardAPIHostname
      && this.soccard.socCardAPIVersion
      && this.soccard.socCardAPIClientID
      && this.soccard.socCardAPIClientSecret
      && this.soccard.socCardAPIPrivateKey
      && this.soccard.socCardAPIPrivateKeyPassphrase;
  }
};

// Export the config object based on the NODE_ENV
// ==============================================
var result = _.merge(
  require('./' + process.env.NODE_ENV) || {},
  all);
module.exports = result;
