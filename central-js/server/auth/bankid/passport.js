var passport = require('passport');
var OAuth2Strategy = require('passport-oauth2');
var crypto = require('crypto');

exports.setup = function (config, url, accountService) {
    var authorizationURL = url.format({
        protocol: config.bankid.sProtocol_AccessService_BankID,
        hostname: config.bankid.sHost_AccessService_BankID,
        pathname: '/DataAccessService/das/authorize'
    });

    var tokenURL = url.format({
        protocol: config.bankid.sProtocol_AccessService_BankID,
        hostname: config.bankid.sHost_AccessService_BankID,
        pathname: '/DataAccessService/oauth/token'
    });

    function BankIDAuth() {

    }

    BankIDAuth.prototype = new OAuth2Strategy({
            authorizationURL: authorizationURL,
            tokenURL: tokenURL,
            clientID: config.bankid.client_id,
            clientSecret: config.bankid.client_secret
        },
        function (accessToken, refreshToken, subject, done) {
            done(null, subject, {
                accessToken: accessToken,
                refreshToken: refreshToken
            });
        });

    BankIDAuth.prototype.authorizationParams = function (options) {
        return options.eds ? {eds : true} : {};
    };

    BankIDAuth.prototype.tokenParams = function (options) {
        var unhashed = config.bankid.client_id +
            config.bankid.client_secret + options.code;

        var clientSecretHashed = crypto.createHash('sha1').update(unhashed).digest('hex');
        var params = {};
        Object.defineProperty(params, 'client_secret', {
            value: clientSecretHashed,
            writable : false,
            enumerable : true,
            configurable : false
        });

        return params;
    };

    BankIDAuth.prototype.userProfile = function(accessToken, done){
        var options = {
            access: {accessToken : accessToken},
            bankid: config.bankid,
            activiti: config.activiti
        };

        return accountService.syncWithSubject(options, function (err, profile) {
            done(err, profile);
        });
    }

    passport.use(new BankIDAuth());
};