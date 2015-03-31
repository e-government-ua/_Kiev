'use strict';

var passport = require('passport');
var config = require('../config/environment');
var jwt = require('jsonwebtoken');
var expressJwt = require('express-jwt');
var compose = require('composable-middleware');
var validateJwt = expressJwt({
	secret: config.secrets.session
});

/**
 * Attaches the user object to the request if authenticated
 * Otherwise returns 403
 */
function isAuthenticated() {
	return compose()
		// Validate jwt
		.use(function(req, res, next) {
			// allow access_token to be passed through cookies
			if (req.cookies &&
				req.cookies.hasOwnProperty('token') &&
				req.cookies.hasOwnProperty('user')) {

				var signedToken = JSON.parse(req.cookies.token);
				//don't use it
				jwt.verify(signedToken.accessToken, config.secrets.session, function(err, decoded) {
					req.headers.authorization = 'Bearer ' + decoded._id;
					validateJwt(req, res, next);
					next();
				});
			} else {
				validateJwt(req, res, next);
				next();
			}
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
		res.cookie('token', JSON.stringify(token), {
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
exports.signToken = signToken;
exports.setTokenCookie = setTokenCookie;
exports.setUserCookie = setUserCookie;