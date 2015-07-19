'use strict';

// alpha specific configuration
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
      key: ['solt for session 1', 'solt for session 2'],
      secure: false,
      maxAge: 1180000 // 3 * 60 * 1000 = 3 min
    }
  }
  
};
