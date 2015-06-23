define('config', ['angularAMD'], function(angularAMD) {
	angularAMD.value('config', {
		client_id: '9b0e5c63-9fcb-4b11-84ff-31fc2cea8801'
		//, sProtocol_AccessService_BankID: 'https' //Test
		//, sHost_AccessService_BankID: 'bankid.privatbank.ua' //Test
		//, sProtocol_ResourceService_BankID: 'https' //Test
                //, sHost_ResourceService_BankID: 'bankid.privatbank.ua' //Test
		, sProtocol_AccessService_BankID: 'https' //Prod
		, sHost_AccessService_BankID: 'bankid.org.ua' //Prod
		, sProtocol_ResourceService_BankID: 'https' //Prod
                , sHost_ResourceService_BankID: 'biprocessing.org.ua' //Prod
	});
	return angularAMD;
});
