var express = require('express');
var router = express.Router();
var account = require('./account.controller');
var auth = require('../../auth/auth.service.js');

router.use('/account', auth.isAuthenticated(), account.index);
router.use('/fio', auth.isAuthenticated(), account.fio);


module.exports = router;
