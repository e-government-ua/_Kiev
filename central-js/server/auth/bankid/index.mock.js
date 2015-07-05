var nock = require('nock');
var config = require('../../config');
var url = require('url');
var bankid = config.bankid;

var bankIDHost = url.format({
    protocol: config.bankid.sProtocol_AccessService_BankID,
    hostname: config.bankid.sHost_AccessService_BankID
});

var bankid = nock(bankIDHost)
    .get('/DataAccessService/das/authorize')
    .reply(200)
    .get('/DataAccessService/oauth/token')
    .reply(200, {
        access_token: "095be9eb-01e7-4045-b60b-9d71581fb4d9",
        token_type: "bearer",
        refresh_token: "a3798f16-8039-49c9-9b61-82936977489d",
        expires_in: 179,
        scope: "read trust write"
    });