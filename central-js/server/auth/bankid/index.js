'use strict';

var express = require('express');
var passport = require('passport');
var auth = require('../auth.service');

var router = express.Router();

router.get('/', function(req, res, next) {
    passport.authenticate('oauth2', {
        callbackURL: '/auth/bankID/callback?link=' + req.query.link
    })(req, res, next);
});

router.get('/callback', function(req, res, next) {
    passport.authenticate('oauth2', {
        session: false,
        code: req.query.code,
        callbackURL: '/auth/bankID/callback?link=' + req.query.link
    }, function(err, user, info) {
        if (err) {
            res.status(401).json(err);
        }
        if (!info.accessToken) {
            res.status(404).json({
                message: 'Cant find acess token. Something went wrong, please try again.'
            });
        }
        if (info.accessToken.oauthError) {
            res.status(404).json({
                message: info.accessToken.message + ' ' + info.accessToken.oauthError.message
            });
        }
        if (!info.refreshToken) {
            res.status(404).json({
                message: 'Cant find refresh token. Something went wrong, please try again.'
            });
        }
        if(!user){
            res.status(404).json({
                message: 'Cant find sync user'
            });
        }

        req.session.subject = user.subject;
        req.session.access = info;

        res.redirect(req.query.link);
    })(req, res, next)
});

module.exports = router;