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
  session: {
    maxAge: 1800000 // 3 * 60 * 1000 = 3 min
  }
};
