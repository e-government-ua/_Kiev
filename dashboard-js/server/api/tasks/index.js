'use strict';

var express = require('express');
var controller = require('./tasks.controller');

var router = express.Router();

router.get('/', controller.index);
router.get('/getPatternFile', controller.getPatternFile);
router.get('/:taskId/events', controller.getAllTaskEvents);
router.get('/:taskId/form', controller.getForm);
router.get('/:taskId/form-from-history', controller.getFormFromHistory);
router.get('/:taskId/attachments', controller.getAttachments);
router.get('/:taskId/attachments/:attachmentId/content/:nFile', controller.getAttachmentContent);
router.post('/:taskId/attachments',controller.uploadFile);
router.post('/:taskId/form', controller.submitForm);
router.put('/:taskId', controller.updateTask);
router.get('/:taskId', controller.getTask);
router.get('/search/byOrder/:orderId', controller.getTasksByOrder);
router.get('/search/byText/:text/type/:sType', controller.getTasksByText);
router.post('/:taskId/upload_content_as_attachment', controller.upload_content_as_attachment);


module.exports = router;
