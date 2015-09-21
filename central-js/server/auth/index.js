'use strict';

var express = require('express');
var passport = require('passport');
var request = require('request');
var url = require('url');
var config = require('../config/environment');
var accountService = require('../api/bankid/account.service.js');
var auth = require('./auth.service');

// Passport Configuration
require('./bankid/passport').setup(config, url, accountService);

var router = express.Router();

router.use('/bankID', require('./bankid'));
router.use('/eds', require('./eds'));
router.get('/isAuthenticated', auth.isAuthenticated(), function(req,res){
    res.status(200);
    res.end();
});

router.post('/logout', auth.isAuthenticated(), function(req,res){
  req.session = null;
  res.session = null;
  res.status(200);
  res.end();
});
module.exports = router;
