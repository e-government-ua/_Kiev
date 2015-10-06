var url = require('url')
var express = require('express');
var router = express.Router();
var accountService = require('./account.service.js');

module.exports.fio = function (req, res) {
  var account = req.session.account;
  res.send({firstName: account.firstName, middleName: account.middleName, lastName: account.lastName});
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

var originalURL = function (req, options) {
  options = options || {};
  var app = req.app;
  if (app && app.get && app.get('trust proxy')) {
    options.proxy = true;
  }
  var trustProxy = options.proxy;

  var proto = (req.headers['x-forwarded-proto'] || '').toLowerCase()
    , tls = req.connection.encrypted || (trustProxy && 'https' == proto.split(/\s*,\s*/)[0])
    , host = (trustProxy && req.headers['x-forwarded-host']) || req.headers.host
    , protocol = tls ? 'https' : 'http'
    , path = req.url || '';
  return protocol + '://' + host + path;
};

module.exports.signHtmlForm = function (req, res) {
  var callbackURL = url.resolve(originalURL(req, {}),
    '/api/bankid/signHtmlForm/callback' +
    '?uploadLink=' + req.query.uploadLink +
    '&formLink=' + req.query.formLink +
    '&formID=' + req.query.formID);
  var options = {
    acceptKeyUrl: callbackURL
  };
  accountService.signHtmlForm(options, function (error, result) {
    if (error) {
      res.status(401).send(error);
    } else {
      res.redirect(result.desc);
    }
  });
};

module.exports.signHtmlFormCallback = function (req, res) {
  //После отработки п.3 (подписание), BankID делает редирект на https://{PI:port}/URL_callback?code=code_value с передачей
  //параметра авторизационного ключа code, тем самым заканчивая фазу п.4.
  var bankIDOptions = getBankIDOptions(req.session.access.accessToken);
  var codeValue = req.query.code;
  var uploadURL = req.query.uploadLink;
  var formLink = req.query.formLink;
  var formID = req.query.formID;

  var url = bankIDOptions.protocol + '://' + bankIDOptions.hostname
    + bankIDOptions.path + '/checked/claim/' + codeValue + '/clientPdfClaim';
  var options = _.merge(bankIDOptions, {
    url: url
  });

  async.waterfall([
    function (callback) {
      var signedFormForUpload = accountService.prepareScanContentRequest(options);
      var form = new FormData();
      form.append('file', signedFormForUpload, {
        filename: 'signedForm.pdf'
      });

      var requestOptionsForUploadContent = {
        url: uploadURL,
        auth: getAuth(),
        headers: form.getHeaders()
      };

      var decoder = new StringDecoder('utf8');
      var result = {};
      form.pipe(request.post(requestOptionsForUploadContent))
        .on('response', function (response) {
          result.statusCode = response.statusCode;
        }).on('data', function (chunk) {
          if (result.fileID) {
            result.fileID += decoder.write(chunk);
          } else {
            result.fileID = decoder.write(chunk);
          }
        }).on('end', function () {
          callback(null, {signedUpload: result});
        });
    }
  ], function (err, result) {
    if (err) {
      res.redirect(formLink
        + '?formID=' + result.signedUpload.fileID
        + '&error=' + JSON.stringify(err));
    } else {
      res.redirect(formLink
        + '?formID=' + result.signedUpload.fileID
        + '?signedFileID=' + result.signedUpload.fileID);
    }
  });

};

function getOptions() {
  var config = require('../../config/environment');
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

function getAuth() {
  var options = getOptions();
  return {
    'username': options.username,
    'password': options.password
  };
}


function getBankIDOptions(accessToken) {
  var config = require('../../config/environment');
  var bankid = config.bankid;

  return {
    protocol: bankid.sProtocol_AccessService_BankID,
    hostname: bankid.sHost_ResourceService_BankID,
    params: {
      client_id: bankid.client_id,
      client_secret: bankid.client_secret,
      access_token: accessToken
    }
  };
}
