'use strict';

var express = require('express');
var controller = require('./process-form.controller');

var router = express.Router();

router.get('/:processDefinitionId', controller.getFormByProcessDefinitionId);

module.exports = router;