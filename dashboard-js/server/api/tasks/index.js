'use strict';

var express = require('express');
var controller = require('./tasks.controller');

var router = express.Router();

router.get('/', controller.index);
router.get('/:taskId/events', controller.getAllTaskEvents);
router.get('/:taskId/form', controller.getForm);
router.get('/:taskId/attachments', controller.getAttachments);
router.get('/:taskId/attachments/:attachmentId/content/:nFile', controller.getAttachmentContent);
router.post('/:taskId/attachments',controller.uploadFile);
router.post('/:taskId/form', controller.submitForm);
router.put('/:taskId', controller.updateTask);


module.exports = router;
