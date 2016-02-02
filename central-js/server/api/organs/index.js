'use strict';

var express = require('express');
var router = express.Router();
var organs = require('./index.controller');

router.get('/:nID_SubjectOrgan', organs.getSubjectOrganJoin);
router.post('/attributes/:nID_SubjectOrgan/:nID', organs.getOrganAttributes);

module.exports = router;
