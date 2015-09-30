'use strict';

var express = require('express');
var controller = require('./escalations.controller');

var router = express.Router();

router.get('/escalationRules', controller.getAllRules);
router.post('/escalationRules', controller.setRule);
router.delete('/escalationRules', controller.deleteRule);

module.exports = router;
