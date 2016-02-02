'use strict';

// Test specific configuration
// ===========================
module.exports = {
  debug: true,

  bankid: {
    sProtocol_AccessService_BankID: 'https', //Test
    sHost_AccessService_BankID: 'bankid.privatbank.ua', //Test
    sProtocol_ResourceService_BankID: 'https', //Test
    sHost_ResourceService_BankID: 'bankid.privatbank.ua', //Test
    client_id: 'testIgov',
    client_secret: 'testIgovSecret',
    /**
     * should be used only as pair for private key in tests
     */
    publicKey: (__dirname + '/local/iGov_sgn_cert.pem'),
    privateKey: (__dirname + '/local/iGov_sgn.pem'),
    privateKeyPassphrase: '1234567899'
  },

  server: {
    protocol: 'http',
    port: '9000',
    session: {
      secret: 'put yor session secret here',
      key: ['solt for session 1', 'solt for session 2'],
      secure: false,
      maxAge: 14400000 // 4h*60m*60s*1000ms*/
    }
  },

  activiti: {
    protocol: 'https',
    hostname: 'test.igov.org.ua',
    port: '8443',
    path: '/wf/service',
    username: 'activiti-master',
    password: 'UjhtJnEvf!'
  }
};
