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
				req.cookies.hasOwnProperty('access_token') && 
				req.cookies.hasOwnProperty('refresh_token')) {
				req.headers.authorization = 'Bearer ' + req.query.access_token;
			}
			validateJwt(req, res, next);
			next();
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

/**
 * Set token cookie directly for oAuth strategies
 */
function setTokenCookie(req, res) {
	if (!req.user) return res.json(404, {
		message: 'Something went wrong, please try again.'
	});
	var token = signToken(req.user._id, req.user.role);
	res.cookie('token', JSON.stringify(token));
	res.redirect('/');
}

exports.isAuthenticated = isAuthenticated;
exports.signToken = signToken;
exports.setTokenCookie = setTokenCookie;