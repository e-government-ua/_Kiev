var express = require('express');
var router = express.Router();
var account = require('./account.controller');
var login = require('./login.controller');

router.get('/login', login.index);
router.use('/account', account.index);

module.exports = router;