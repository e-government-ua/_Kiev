var express = require('express');
var router = express.Router();
var order = require('./order.controller');
var auth = require('../../auth/auth.service.js');

router.get('/search/:nID', order.searchOrderBySID);

module.exports = router;