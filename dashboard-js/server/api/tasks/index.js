'use strict';

var express = require('express');
var controller = require('./tasks.controller');

var router = express.Router();

router.get('/', controller.index);
router.get('/:taskId/events', controller.getAllTaskEvents);

module.exports = router;
