var express = require('express');
var router = express.Router();
var accountService = require('./account.service.js');

module.exports.fio = function(req, res) {
  var account = req.session.account;
  res.send({ firstName: account.firstName, middleName : account.middleName, lastName : account.lastName});
};

module.exports.index = function (req, res) {
    var config = require('../../config/environment');

    var options = {
        access: req.session.access,
        bankid: config.bankid,
        activiti: config.activiti
    };

    accountService.syncWithSubject(options, function (err, result) {
        if (err) {
            res.status(err.code);
            res.send(err);
            res.end();
        } else {
            req.session.subject = result.subject;
            res.send({
                customer: result.customer,
                admin: result.admin
            });
            res.end();
        }
    });
};
