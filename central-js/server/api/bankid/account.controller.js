var express = require('express');
var router = express.Router();
var syncSubject = require('../service/syncSubject.controller');
var accountService = require('./account.service.js');
var async = require('async');

var createError = function (error, error_description, response) {
    return {
        code: response ? response.statusCode : 500,
        err: {
            error: error,
            error_description: error_description
        }
    };
};

module.exports.index = function (req, res, next) {
    var config = require('../../config');
    async.waterfall([
        function (callback) {
            var bankid = config.bankid;

            var options = {
                protocol: bankid.sProtocol_ResourceService_BankID,
                hostname: bankid.sHost_ResourceService_BankID,
                path: '/ResourceService',
                params: {
                    client_id: bankid.client_id,
                    client_secret: bankid.client_secret,
                    access_token: req.query.access_token || null
                }
            };

            accountService.index(options, function (error, response, body) {
                if (error || body.error) {
                    callback(createError(error || body.error, body.error_description, response), null);
                } else {
                    callback(null, {
                        customer: body.customer
                    });
                }
            });
        },

        function (result, callback) {
            var activiti = config.activiti;

            var syncSubjectOptions = {
                protocol: activiti.protocol,
                hostname: activiti.hostname,
                port: activiti.port,
                path: activiti.path,
                username: activiti.username,
                password: activiti.password,
                params: {
                    sINN: result.customer.inn || null
                }
            };

            syncSubject.index(syncSubjectOptions, function (error, response, body) {
                if (error) {
                    callback(createError(error, response), null);
                } else {
                    result.subject = JSON.parse(body);
                    callback(null, result);
                }
            });
        }
    ], function (err, result) {
        if (err) {
            res.status(err.code);
            res.send(err);
            res.end();
        } else {
            req.session.subject = result.subject;
            res.send({
                customer: result.customer
            });
            res.end();
        }
    });

};