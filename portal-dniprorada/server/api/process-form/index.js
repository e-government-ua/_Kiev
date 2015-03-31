'use strict';

var express = require('express');
var controller = require('./process-form.controller');
var auth = require('../../auth/auth.service');
var router = express.Router();

router.get('/:processDefinitionId', controller.getFormByProcessDefinitionId);
router.get('/', controller.getLastFormProcess);
router.post('/:processDefinitionId', controller.submitForm);

module.exports = router;