var express = require('express');
var router = express.Router();
var account = require('./account.controller');

router.use('/account', account.index);

module.exports = router;