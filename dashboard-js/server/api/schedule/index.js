'use strict';

var express = require('express');
var controller = require('./schedule.controller');

var router = express.Router();

router.get('/schedule', controller.getSchedule);
router.post('/schedule', controller.setSchedule);
router.delete('/schedule', controller.deleteSchedule);

router.get('/exemption', controller.getExemptions);
router.post('/exemption', controller.setExemption);
router.delete('/exemption', controller.deleteExemption);

router.get('/flowSlots', controller.getFlowSlots);
router.post('/buildFlowSlots', controller.buildFlowSlots);
router.delete('/flowSlots', controller.deleteFlowSlots);
router.get('/getFlowSlotTickets', controller.getFlowSlotTickets);

router.get('/getFlowSlotDepartments', controller.getFlowSlotDepartments);

module.exports = router;
