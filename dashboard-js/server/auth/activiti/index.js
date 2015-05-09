'use strict';

var express = require('express');
var basic = require('./basic');
var router = express.Router();

router.post('/', basic.authenticate);
router.post('/ping', basic.ping);
router.post('/logout', basic.logout);

module.exports = router;
