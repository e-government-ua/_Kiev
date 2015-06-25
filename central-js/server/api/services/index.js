var express = require('express');
var router = express.Router();
var services = require('./servicesTree.controller.js');

router.get('/', services.index);

module.exports = router;