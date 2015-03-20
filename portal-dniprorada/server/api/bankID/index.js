'use strict';

var express = require('express');
var controller = require('./bankID.controller');

var router = express.Router();

router.get('/:processDefinitionId', controller.auth);

module.exports = router;