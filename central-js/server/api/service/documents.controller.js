var request = require('request');
var account = require('../bankid/account.controller');
var _ = require('lodash');
request.debug = true;

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

module.exports.getDocumentFile = function(req, res) {
    var options = getOptions(req);
    var url = options.protocol + '://' + options.hostname + options.path + '/services/getDocumentFile';
    var r = request({
        'url': url,
        'auth': {
            'username': options.username,
            'password': options.password
        },
        'qs': {
            'nID': req.params.nID
        }
    });
    req.pipe(r).on('response', function(response) {
        response.headers['content-type'] = 'application/octet-stream';
    }).pipe(res);
};


module.exports.getDocument = function(req, res) {
    var options = getOptions(req);
    var url = options.protocol + '://' + options.hostname + options.path + '/services/getDocument';

    var callback = function(error, response, body) {
        res.send(body);
        res.end();
    };

    return request.get({
        'url': url,
        'auth': {
            'username': options.username,
            'password': options.password
        },
        'qs': {
            'nID': req.params.nID
        }
    }, callback);
};

module.exports.index = function(req, res) {
    //options, callback
    var options = getOptions(req);
    var url = options.protocol + '://' + options.hostname + options.path + '/services/getDocuments';

    var callback = function(error, response, body) {
        res.send(body);
        res.end();
    };

    return request.get({
        'url': url,
        'auth': {
            'username': options.username,
            'password': options.password
        },
        'qs': {
            'sID_Subject': req.query.sID_Subject
        }
    }, callback);
};

module.exports.initialUpload = function(req, res) {
    process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

    var accessToken = req.query.access_token;
    var sID_Subject = req.query.sID_Subject;

    if (!accessToken || !sID_Subject) {
        res.status(400);
        res.send('both accessToken and sID_Subject are needed');
        res.end();
        return;
    }

    var config = require('../../config');
    var bankid = config.bankid;
    var activiti = config.activiti;

    var options = {
        protocol: bankid.protocol,
        hostname: bankid.hostname,
        params: {
            client_id: bankid.client_id,
            client_secret: bankid.client_secret,
            access_token: req.query.access_token
        }
    };

    var optionsForScans = _.merge(options, {
        path: '/ResourceService'
    });

    var url = activiti.protocol + '://' + activiti.hostname + activiti.path + '/services/setDocumentFile';
    var optionsForUploadContent = {
        'url': url,
        'auth': {
            'username': options.username,
            'password': options.password
        },
        'qs': {
            'nID_Subject_Upload': 1,
            'sID_Subject_Upload': 1,
            'sSubjectName_Upload': 'Приватбанк',
            'sName': 'Паспорт',
            'nID_DocumentType': 1
        }
    };

    var scansRequest = account.prepareScansRequest(optionsForScans);
    
    // result: Object
    // customer: Object
    // clId: "93f71a3963ecc87f5fd63ee1bce067dc0df62d7f"
    // clIdText: "Передана інформація є достовірною і підтверджена BankID 02.06.2015 00:59"
    // scans: Array[1]
    // 0: Object
    // dateCreate: "10.05.2015"
    // link: "https://bankid.privatbank.ua/ResourceService/checked/scan/passport"
    // type: "passport"

    request
        .post(scansRequest, function(error, response, body) {
            if (!error && body) {
                var result = body;
                if (!result.error) {
                    var customer = result.customer;
                    var passport = customer.scans[0];

                    var scanContentRequest = account.prepareScanContentRequest(
                        _.merge(options, {
                            url: passport.link
                        })
                    );

                    request
                        .get(scanContentRequest)
                        .pipe(optionsForUploadContent)
                        .pipe(res);
                }
            }
            res.status(response.statusCode);
            res.end();
        });
};