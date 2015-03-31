'use strict';

var passport = require('passport');
var config = require('../config/environment');
var jwt = require('jsonwebtoken');
var expressJwt = require('express-jwt');
var compose = require('composable-middleware');
var url = require('url')
var validateJwt = expressJwt({
	secret: config.secrets.session
});

var getBankIDBaseURL = function() {
		return url.format({
			protocol: config.bankid.prot,
			hostname: config.bankid.host
		});
	}
	/**
	 * Attaches the user object to the request if authenticated
	 * Otherwise returns 403
	 */
function isAuthenticated() {
	return compose()
		// Validate jwt
		.use(function(req, res, next) {
			if (config.bankid.disable === 'true') {
				res.cookie('disableBankID', config.bankid.disable, {
					expires: new Date(Date.now() + 1000 * 60 * 100)
				});
				next();
			} else {
				// allow access_token to be passed through cookies
				if (req.cookies &&
					req.cookies.hasOwnProperty('bankdIDToken') &&
					req.cookies.hasOwnProperty('user')) {
					next();
				} else {
					next();
				}
			}
		});
}

function isDisabledBankID() {
	return compose()
		// Validate jwt
		.use(function(req, res, next) {
			if (config.bankid.disable) {
				res.cookie('disableBankID', config.bankid.disable, {
					expires: new Date(Date.now() + 1000 * 60 * 3)
				});
			}
			next();
		});
}

/**
 * Set token cookie directly for oAuth strategies
 */
function setTokenCookie(req, res, accessToken, refreshToken) {
		var token = {
			access_token: accessToken,
			refresh_token: refreshToken
		}
		res.cookie('bankdIDToken', JSON.stringify(token), {
			expires: new Date(Date.now() + 1000 * 60 * 3)
		});
	}
	/**
	 * Set token cookie directly for oAuth strategies
	 */
function setUserCookie(req, res, user) {
	res.cookie('user', JSON.stringify(user), {
		expires: new Date(Date.now() + 1000 * 60 * 10)
	});
}

/**
 * Returns a jwt token signed by the app secret
 */
function signToken(id) {
	return jwt.sign({
		_id: id
	}, config.secrets.session, {
		expiresInMinutes: 60 * 5
	});
}

exports.isAuthenticated = isAuthenticated;
exports.isDisabledBankID = isDisabledBankID;
exports.signToken = signToken;
exports.setTokenCookie = setTokenCookie;
exports.setUserCookie = setUserCookie;