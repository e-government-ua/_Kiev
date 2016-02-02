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

  soccard : {
    socCardAPIProtocol: 'https',
    socCardAPIHostname: 'test.kyivcard.com.ua',
    socCardAPIVersion : '1.0'
  },

  server: {
    session: {
      maxAge: 14400000, // 4h*60m*60s*1000ms*/
      secure: true
    }
  }
};
