'use strict';

var express = require('express');
var router = express.Router();
var controller = require('./index.controller');

router.get('/', controller.index);
router.get('/:nID([0-9]+)/statistics', controller.getServiceStatistics);
router.post('/', controller.setService);
router.delete('/', controller.removeServiceData);

module.exports = router;
