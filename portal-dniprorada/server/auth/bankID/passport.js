var passport = require('passport')
var OAuth2Strategy = require('passport-oauth').OAuth2Strategy;

exports.setup = function(config, request, url, user) {
	var authorizationURL = url.format({
		protocol: config.bankid.prot,
		hostname: config.bankid.host,
		pathname: config.bankid.path
	});

	var tokenURL = url.format({
		protocol: config.bankid.prot,
		hostname: config.bankid.host,
		pathname: config.bankid.token.path
	});

	passport.use(new OAuth2Strategy({
			authorizationURL: authorizationURL,
			tokenURL: tokenURL,
			clientID: config.bankid.appid,
			clientSecret: config.bankid.token.secret,
			callbackURL: request.getBackRedirectURL('/auth/bankID/callback')
		},
		function(accessToken, refreshToken, profile, done) {
			user.findUser(accessToken, function(err, user) {
				if (err) return done(err);

				return done(null, user, {
					accessToken: accessToken,
					refreshToken: refreshToken
				});
			});
		}));
};