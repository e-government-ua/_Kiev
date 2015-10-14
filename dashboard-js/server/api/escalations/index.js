'use strict';

var express = require('express');
var controller = require('./escalations.controller');

var router = express.Router();

router.get('/escalationRules', controller.getAllRules);
router.post('/escalationRules', controller.setRule);
router.delete('/escalationRules', controller.deleteRule);

router.get('/escalationFunctions', controller.getAllEscalationFunctions);
router.post('/escalationFunctions', controller.setRuleFunction);
router.delete('/escalationFunctions', controller.deleteRuleFunction);

module.exports = router;
