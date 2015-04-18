'use strict';

var express = require('express');
var basic = require('./basic');
var router = express.Router();

var router = express.Router();

router.post('/', basic.authenticate);
router.post('/ping', basic.ping);

module.exports = router;