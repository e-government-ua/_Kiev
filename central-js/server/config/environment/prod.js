'use strict';

// Production specific configuration
// =================================
module.exports = {
  bankid: {
    sProtocol_AccessService_BankID: 'https', //Prod
    sHost_AccessService_BankID: 'bankid.org.ua', //Prod
    sProtocol_ResourceService_BankID: 'https', //Prod
    sHost_ResourceService_BankID: 'biprocessing.org.ua' //Prod
  },
  server: {
    session: {
      secret: 'put yor session secret here',
      key: ['solt for session 1', 'solt for session 2'],
      /*secure: false,*/
      maxAge: 14400000 // 4h*60m*60s*1000ms
    }
  }
};
