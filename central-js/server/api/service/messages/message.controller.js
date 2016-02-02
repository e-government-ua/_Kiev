var request = require('request');

module.exports.setMessage = function (req, res, callback) {
    var params = {
        'sHead': req.query.sHead,
        'sBody': req.query.sBody,
        'nID_Subject': req.query.nID_Subject,
        'sMail': req.query.sMail,
        'sContacts': req.query.sContacts,
        'sData': req.query.sData
    };
    console.log('object request: ' + req);
    console.log('url: ' + getUrl(req, '/subject/message/setMessage') + ' auth: ' + getAuth(req) + ' qs: ' + params);
    return request.post({
        'url': getUrl(req, '/subject/message/setMessage'),
        'auth': getAuth(req),
        'qs': params
    }, callback);
};

module.exports.getMessageById = function (req, callback) {
    var params = {
        'nID': req.params.nID
    };
    return buildGetRequest(req, '/subject/message/getMessage', params, callback);
};

module.exports.getMessages = function (req, callback) {
    var params = {
        'nID': req.params.nID
    };
    return buildGetRequest(req, '/subject/message/getMessages', params, callback);
};

function buildGetRequest(req, apiURL, params, callback) {
    return request({
        'url': getUrl(req, apiURL),
        'auth': getAuth(req),
        'qs': params
    }, callback);
}

function getUrl(options, apiURL) {
    return options.protocol + '://' + options.hostname + options.path + apiURL;
}

function getAuth(options) {
    return {
        'username': options.username,
        'password': options.password
    }
}
