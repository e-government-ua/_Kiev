'use strict';

var express = require('express');
var controller = require('./process-definitions.controller');
var router = express.Router();
var auth = require('../../auth/auth.service');

router.get('/', auth.isDisabledBankID(), controller.index);

module.exports = router;