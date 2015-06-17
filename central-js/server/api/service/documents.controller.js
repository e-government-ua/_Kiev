var request = require('request');
var account = require('../bankid/account.controller');
var _ = require('lodash');
var FormData = require('form-data');
var async = require('async');
var StringDecoder = require('string_decoder').StringDecoder;

module.exports.shareDocument = function(req, res) {
    var params = {
        'nID_Document': req.query.nID_Document,
        'sFIO': req.query.sFIO,
        'sTarget': req.query.sTarget,
        'sTelephone': req.query.sTelephone,
        'nMS': req.query.nMS,
        'sMail': req.query.sMail
    };
    return buildGetRequest(req, res, '/setDocumentLink', params);
};

module.exports.getDocumentFile = function(req, res) {
    var r = request({
        'url': getUrl('/services/getDocumentFile'),
        'auth': getAuth(),
        'qs': {
            'nID': req.params.nID
        }
    });
    req.pipe(r).on('response', function(response) {
        response.headers['content-type'] = 'application/octet-stream';
    }).pipe(res);
};

module.exports.getDocument = function(req, res) {
    var params = {
        'nID': req.params.nID
    };
    return buildGetRequest(req, res, '/services/getDocument', params);
};

module.exports.index = function(req, res) {
    var params = {
        'nID_Subject': req.query.nID_Subject
    };
    return buildGetRequest(req, res, '/services/getDocuments', params);
};

/* 
 nID;sName
 0;Другое
 1;Справка
 2;Паспорт
 3;Загранпаспорт
 4;Персональное фото
 5;Справка о предоставлении ИНН */
module.exports.initialUpload = function(req, res) {
    process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

    var accessToken = req.query.access_token;
    var nID_Subject = req.query.nID_Subject;
    var sID_Subject = req.query.sID_Subject;
    var typesToUpload = req.body;

    if (!accessToken || !sID_Subject) {
        res.status(400);
        res.send({
            error: 'both accessToken and sID_Subject are needed'
        });
        res.end();
        return;
    }

    if (!typesToUpload || typesToUpload.length === 0) {
        res.status(400);
        res.send({
            error: 'nothing to upload'
        });
        res.end();
    }

    var options = getBankIDOptions(req.query.access_token);

    var optionsForScans = _.merge(options, {
        path: '/ResourceService'
    });

    var docTypesToBankIDDocTypes = {
        2: 'passport',
        3: 'zpassport'
    };

    var optionsForUploadContentList = typesToUpload.map(function(docType) {
        var o = {
            'url': getUrl('/services/setDocumentFile'),
            'auth': getAuth(),
            'qs': {
                'nID_Subject': nID_Subject,
                'sID_Subject_Upload': sID_Subject,
                'sSubjectName_Upload': 'Приватбанк',
                'sName': docType.sName,
                'nID_DocumentType': docType.nID
            }
        };
        var bankIDType = docTypesToBankIDDocTypes[docType.nID];
        return {
            scanFilter: function(scan) {
                return scan.type === bankIDType;
            },
            option: o
        };
    });

    var uploadScan = function(documentScan, optionsForUploadContent, callback) {
        var scanContentRequest = account.prepareScanContentRequest(
            _.merge(options, {
                url: documentScan.link
            })
        );

        var form = new FormData();
        form.append('oFile', scanContentRequest);

        var requestOptionsForUploadContent =
            _.merge(optionsForUploadContent.option, {
                headers: form.getHeaders()
            });

        var decoder = new StringDecoder('utf8');
        var result = {};
        form.pipe(request.post(requestOptionsForUploadContent))
            .on('response', function(response) {
                result.statusCode = response.statusCode;
            }).on('data', function(chunk) {
                if (result.body) {
                    result.body += decoder.write(chunk);
                } else {
                    result.body = decoder.write(chunk);
                }
            }).on('end', function() {
                callback(result);
            });
    };

    var doAsyncScansUpload = function(scans) {
        async.forEach(optionsForUploadContentList, function(optionsForUploadContent, callback) {
            var results = scans.filter(optionsForUploadContent.scanFilter);
            if (results.length === 1) {
                uploadScan(results[0], optionsForUploadContent, callback);
            } else {
                async.setImmediate(function() {
                    callback(null);
                });
            }
            //don't do end here, it will be executed after all async
        }, function(result) {
            //here
            res.send(result);
            res.end();
        });
    };

    var scansCallback = function(error, response, body) {
        var ifDoUpload = false;
        if (!error && body) {
            var result = body;
            if (!result.error) {
                var customer = result.customer;
                if (customer.scans && customer.scans.length > 0) {
                    ifDoUpload = true;
                    doAsyncScansUpload(customer.scans);
                }
            }
        }
        if (!ifDoUpload) {
            res.status(response.statusCode);
            res.end();
        }
    };

    account.scansRequest(optionsForScans, scansCallback);
};

function buildGetRequest(req, res, apiURL, params) {
    var callback = function(error, response, body) {
        res.send(body);
        res.end();
    };

    return request({
        'url': getUrl(apiURL),
        'auth': getAuth(),
        'qs': params
    }, callback);
}

function getBankIDOptions(accessToken) {
    var config = require('../../config');
    var bankid = config.bankid;

    return {
        protocol: bankid.sProtocol_ResourceService_BankID,
        hostname: bankid.sHost_ResourceService_BankID,
        params: {
            client_id: bankid.client_id,
            client_secret: bankid.client_secret,
            access_token: accessToken
        }
    };
}

function getOptions(req) {
    var config = require('../../config');
    var activiti = config.activiti;

    return {
        protocol: activiti.protocol,
        hostname: activiti.hostname,
        port: activiti.port,
        path: activiti.path,
        username: activiti.username,
        password: activiti.password
    };
}

function getOptions() {
    var config = require('../../config');
    var activiti = config.activiti;

    return {
        protocol: activiti.protocol,
        hostname: activiti.hostname,
        port: activiti.port,
        path: activiti.path,
        username: activiti.username,
        password: activiti.password
    };
}

function getUrl(apiURL) {
    var options = getOptions();
    return options.protocol + '://' + options.hostname + options.path + apiURL;
}

function getAuth() {
    var options = getOptions();
    return {
        'username': options.username,
        'password': options.password
    };
}
