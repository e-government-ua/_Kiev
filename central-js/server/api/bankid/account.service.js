var request = require('request');
var FormData = require('form-data');
var async = require('async');
var syncSubject = require('../service/syncSubject.controller');
var Admin = require('../../components/admin');
var _ = require('lodash');

var getAuth = function (options) {
  return 'Bearer ' + options.params.access_token + ', Id ' + options.params.client_id;
};

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
      'Authorization': getAuth(options),
      'Accept': 'application/json'
    },
    json: true,
    body: {
      "type": "physical",
      "fields": ["firstName", "middleName", "lastName", "phone", "inn", "clId", "clIdText", "birthDay", "email"],

      "addresses": [
        {
          "type": "factual",
          "fields": ["country", "state", "area", "city", "street", "houseNo", "flatNo", "dateModification"]
        },
        {
          "type": "birth",
          "fields": ["country", "state", "area", "city", "street", "houseNo", "flatNo", "dateModification"]
        }
      ],

      "documents": [{
        "type": "passport",
        "fields": ["series", "number", "issue", "dateIssue", "dateExpiration", "issueCountryIso2"]
      }],

      "scans": [{
        "type": "passport",
        "fields": ["link", "dateCreate", "extension"]
      }, {
        "type": "zpassport",
        "fields": ["link", "dateCreate", "extension"]
      }]
    }
  }, adminCheckCallback);
};

module.exports.scansRequest = function (options, callback) {
  var url = options.protocol + '://' + options.hostname + options.path + '/checked/data';
  return request.post({
    'url': url,
    'headers': {
      'Content-Type': 'application/json',
      'Authorization': getAuth(options),
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
      'Authorization': getAuth(options)
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
          access_token: options.params.accessToken || null
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

/**
 *  Content-Type = "multipart/form-data"
 * Authorization = "Bearer access_token, Id client_id" - (последовательность не важна)
 * Accept = "application/json"
 * acceptKeyUrl = URL_callback - (урл перенаправления браузера и передача ключа для забора подписанного PDF)
 * fileType = "pdf" (так же принимает еще значения html, image, которые будут преобразованы в формат PDF )
 * В результате ответа будет получен JSON вида
 * {"state":"ok","code":"000000","desc":"https://{IP:port}/IdentDigitalSignature/signPdf?sidBi=sidBi_value"}
 *
 * @param options
 * @param callback
 */
module.exports.signHtmlForm = function (options, callback) {
  var uploadURL = options.protocol + '://' + options.hostname + options.path + '/checked/uploadFileForSignature';

  var formToUpload = options.params.formToUpload;
  var form = new FormData();
  form.append('file', formToUpload, {
    contentType: 'text/html'
  });

  var requestOptionsForUploadContent = {
    url: uploadURL,
    headers: _.merge({
      Authorization: getAuth(options),
      acceptKeyUrl: options.params.acceptKeyUrl,
      fileType: 'html'
    }, form.getHeaders()),
    formData: {
      file: formToUpload
    },
    json: true
  };

  request.post(requestOptionsForUploadContent, function (error, response, body) {
    if (error || (error = body.error)) {
      callback(error, null);
    } else {
      callback(null, body);
    }
  });
};

/**
 * После отработки п.3 (подписание), BankID делает редирект на https://{PI:port}/URL_callback?code=code_value с передачей
 * параметра авторизационного ключа code, тем самым заканчивая фазу п.4.
 * https://{PI:port}/ResourceService/checked/claim/code_value/clientPdfClaim
 * @param req
 * @param res
 */
module.exports.prepareSignedContentRequest = function (bankIDOptions, codeValue) {
  var url = bankIDOptions.protocol + '://' + bankIDOptions.hostname +
    '/ResourceService/checked/claim/' + codeValue + '/clientPdfClaim';
  var options = _.merge(bankIDOptions, {
    url: url
  });

  return module.exports.prepareScanContentRequest(options);
};

