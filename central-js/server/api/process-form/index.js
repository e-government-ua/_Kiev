var express = require('express');
var router = express.Router();
var form = require('./form.controller');
var auth = require('../auth/auth.service');

router.get('/', auth.isAuthenticated(), form.index);
router.post('/', auth.isAuthenticated(), form.submit);

module.exports = router;