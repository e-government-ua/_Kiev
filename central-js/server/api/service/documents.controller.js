var request = require('request');

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

function buildGetRequest (req, res, apiURL, params) {
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

function getOptions () {
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

function getUrl (apiURL) {
  var options = getOptions();
  return options.protocol + '://' + options.hostname + options.path + apiURL;
}

function getAuth () {
  var options = getOptions();
  return {
    'username': options.username,
    'password': options.password
  }
}
