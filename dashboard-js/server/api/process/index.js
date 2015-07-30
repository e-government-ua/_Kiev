'use strict';

var express = require('express');
var controller = require('./process.controller');

var router = express.Router();

router.get('/', controller.index);
router.get('/getLoginBPs', controller.getLoginBPs);


module.exports = router;
