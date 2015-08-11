'use strict';

var express = require('express');
var controller = require('./services.controller');

var router = express.Router();

router.get('/', controller.index);
router.get('/schedule', controller.getSchedule);
router.post('/schedule', controller.setSchedule);
router.delete('/schedule', controller.deleteSchedule);

module.exports = router;
