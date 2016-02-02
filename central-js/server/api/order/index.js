var express = require('express');
var router = express.Router();
var order = require('./order.controller');
var auth = require('../../auth/auth.service.js');

router.get('/search/:sID_Order', order.searchOrderBySID);
router.post('/setTaskAnswer', order.setTaskAnswer);
router.get('/count', order.getCountOrders);
router.get('/getStartFormByTask', order.getStartFormByTask);

router.post('/setEventSystem/:sFunction', order.setEventSystem);



module.exports = router;
