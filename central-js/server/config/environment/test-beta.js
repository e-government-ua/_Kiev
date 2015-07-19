'use strict';

// Beta specific configuration
// =================================
module.exports = {
  bankid: {
    sProtocol_AccessService_BankID: 'https',
    sHost_AccessService_BankID: 'bankid.privatbank.ua',
    sProtocol_ResourceService_BankID: 'https',
    sHost_ResourceService_BankID: 'bankid.privatbank.ua'
  },
  server: {
    session: {
      secret: 'put yor session secret here',
      key: ['solt for session 1', 'solt for session 2']
      /*, secure: false*/
      /*, maxAge: 14400000 // 4h*60m*60s*1000ms*/
    }
  }
};
