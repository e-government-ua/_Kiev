var request = require('request');

module.exports.index = function (req, res) {
    var config = require('../../config');
    var activiti = config.activiti;

    var options = {
        protocol: activiti.protocol,
        hostname: activiti.hostname,
        port: activiti.port,
        path: activiti.path,
        username: activiti.username,
        password: activiti.password,
        params: {
            sFind: req.query.sFind || null
        }
    };

    var callback = function (error, response, body) {
        res.send(body);
        res.end();
    }


    var url = options.protocol + '://' + options.hostname + options.path + '/services/getServicesTree';

    return request.get({
        'url': url,
        'auth': {
            'username': options.username,
            'password': options.password
        },
        'qs': {
            'sFind': options.params.sFind
        }
    }, callback);
};