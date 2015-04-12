'use strict';

var express = require('express');
var passport = require('passport');
var url = require('url')
var config = require('../config/environment');
var request = require('../components/request');
var user = require('../components/user');

// Passport Configuration
require('./bankID/passport').setup(config, request, url, user);

var router = express.Router();

router.use('/bankID', require('./bankID'));

module.exports = router;