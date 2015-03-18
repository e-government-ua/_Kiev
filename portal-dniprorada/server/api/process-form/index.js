'use strict';

var express = require('express');
var controller = require('./process-form.controller');

var router = express.Router();

router.get('/:processDefinitionId', controller.getFormByProcessDefinitionId);
router.post('/:processDefinitionId', controller.submitForm);

module.exports = router;