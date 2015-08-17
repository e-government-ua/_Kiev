'use strict';

var express = require('express');
var router = express.Router();
var countries = require('./index.controller');

router.get('/', countries.getCountryList);
router.get('/getCountry', countries.getCountry);

module.exports = router;
