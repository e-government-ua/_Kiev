'use strict';

var express = require('express');
var passport = require('passport');
var auth = require('../auth.service');

var router = express.Router();

router.get('/', passport.authenticate('oauth2'));

router.get('/callback', function(req, res, next) {
  passport.authenticate('oauth2', {
    session: false
  }, function(err, user, info) {
    //TODO user = xml format string. Have todo something with it
    if (err) {
      return res.json(err, {
        message: 'Error' + err
      });
    }
    if (!info.accessToken) {
      return res.json(404, {
        message: 'Cant find acess token. Something went wrong, please try again.'
      });
    }
    if (info.accessToken.oauthError) {
      return res.json(404, {
        message: info.accessToken.message + ' ' + info.accessToken.oauthError.message
      });
    }
    if (!info.refreshToken) {
      return res.json(404, {
        message: 'Cant find refresh token. Something went wrong, please try again.'
      });
    }
    if (!user) {
      return res.json(404, {
        message: 'Cant find user.'
      });
    }

    var token = {
      accessToken: auth.signToken(info.accessToken),
      refreshToken: auth.signToken(info.refreshToken)
    }

    res.cookie('user', JSON.stringify(user), {
      expires: new Date(Date.now() + 1000 * 60 * 10)
    });
    res.cookie('token', JSON.stringify(token), {
      expires: new Date(Date.now() + 1000 * 60 * 3)
    });

    res.redirect('/process-form');
  })(req, res, next)
});

module.exports = router;