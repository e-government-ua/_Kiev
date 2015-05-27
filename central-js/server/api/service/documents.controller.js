var request = require('request');


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
}


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