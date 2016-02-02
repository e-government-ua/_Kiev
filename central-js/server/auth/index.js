var express = require('express');
var passport = require('passport');
var request = require('request');
var config = require('../config/environment');
var accountService = require('./bankid/bankid.service.js');
var authService = require('./auth.service');
var authController = require('./auth.controller');
var router = express.Router();

// Registering oauth2 strategies
require('./bankid/bankid.passport').setup(config, accountService);
router.use('/bankID', require('./bankid'));
router.use('/eds', require('./eds'));
router.use('/email', require('./email'));

if(config.hasSoccardAuth()){
  require('./soccard/soccard.passport').setup(config, accountService);
  router.use('/soccard', require('./soccard'));
}

router.get('/isAuthenticated', authService.isAuthenticated(), authController.isAuthenticated);
router.post('/logout', authService.isAuthenticated(), authController.logout);

module.exports = router;
