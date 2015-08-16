var request = require('request');
var async = require('async');
var syncSubject = require('../service/syncSubject.controller');
var Admin = require('../../components/admin');

var createError = function (error, error_description, response) {
    return {
        code: response ? response.statusCode : 500,
        err: {
            error: error,
            error_description: error_description
        }
    };
};

module.exports.index = function (options, callback) {
    var url = options.protocol + '://' + options.hostname + options.path + '/checked/data';

    var adminCheckCallback = function (error, response, body) {
        if (body.customer && Admin.isAdminInn(body.customer.inn)) {
            body.admin = {
                inn: body.customer.inn,
                token: Admin.generateAdminToken()
            };
        }
        callback(error, response, body);
    };

    return request.post({
        'url': url,
        'headers': {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + options.params.access_token + ', Id ' + options.params.client_id,
            'Accept': 'application/json'
        },
        json: true,
        body: {
            "type": "physical",
            "fields": ["firstName", "middleName", "lastName", "phone", "inn", "clId", "clIdText", "birthDay"],
            
            "addresses":[
                {"type":"factual","fields":["country","state","area","city","street","houseNo","flatNo","dateModification"]},
                {"type":"birth","fields":["country","state","area","city","street","houseNo","flatNo","dateModification"]}
            ]


        }
    }, adminCheckCallback);
};

module.exports.scansRequest = function (options, callback) {
    var url = options.protocol + '://' + options.hostname + options.path + '/checked/data';
    return request.post({
        'url': url,
        'headers': {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + options.params.access_token + ', Id ' + options.params.client_id,
            'Accept': 'application/json'
        },
        json: true,
        body: {
            "type": "physical",
            "fields": ["firstName", "middleName", "lastName", "phone", "inn", "clId", "clIdText", "birthDay"],
            "scans": [{
                "type": "passport",
                "fields": ["link", "dateCreate", "extension"]
            }, {
                "type": "zpassport",
                "fields": ["link", "dateCreate", "extension"]
            }]
        }
    }, callback);
};

module.exports.prepareScanContentRequest = function (options) {
    var o = {
        'url': options.url,
        'headers': {
            'Authorization': 'Bearer ' + options.params.access_token + ', Id ' + options.params.client_id
        }
    };
    return request.get(o);
};

module.exports.syncWithSubject = function (options, done) {
    async.waterfall([
        function (callback) {
            var bankid = options.bankid;

            var accountOptions = {
                protocol: bankid.sProtocol_ResourceService_BankID,
                hostname: bankid.sHost_ResourceService_BankID,
                path: '/ResourceService',
                params: {
                    client_id: bankid.client_id,
                    client_secret: bankid.client_secret,
                    access_token: options.access.accessToken || null
                }
            };

            module.exports.index(accountOptions, function (error, response, body) {
                if (error || body.error) {
                    callback(createError(error || body.error, body.error_description, response), null);
                } else {
                    callback(null, {
                        customer: body.customer,
                        admin: body.admin
                    });
                }
            });
        },

        function (result, callback) {
            var activiti = options.activiti;

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
        done(err, result);
    });
};