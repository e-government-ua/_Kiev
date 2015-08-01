'use strict';

var express = require('express');
var router = express.Router();
var countries = require('./index.controller');

router.get('/', countries.getCountryList);

module.exports = router;
