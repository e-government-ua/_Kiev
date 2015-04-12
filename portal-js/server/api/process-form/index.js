'use strict';

var express = require('express');
var controller = require('./process-form.controller');
var auth = require('../../auth/auth.service');
var router = express.Router();

router.get('/:processDefinitionId', auth.isAuthenticated(), controller.getFormByProcessDefinitionId);
router.get('/', auth.isAuthenticated(), controller.getLastFormProcess);
router.post('/:processDefinitionId', auth.isAuthenticated(), controller.submitForm);

module.exports = router;