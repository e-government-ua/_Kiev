var request = require('request');
var accountService = require('../../auth/bankid/bankid.service.js');
var proxy = require('../../components/proxy');
var _ = require('lodash');
var FormData = require('form-data');
var async = require('async');
var StringDecoder = require('string_decoder').StringDecoder;
var url = require('url');

module.exports.shareDocument = function (req, res) {
  var params = {
    'nID_Document': req.query.nID_Document,
    'sFIO': req.query.sFIO,
    'sTarget': req.query.sTarget,
    'sTelephone': req.query.sTelephone,
    'nMS': req.query.nMS,
    'sMail': req.query.sMail
  };
  return sendGetRequest(req, res, '/document/access/setDocumentLink', params);
};

module.exports.getDocumentFile = function (req, res) {
  var r = request(buildGetRequest(req, '/document/getDocumentFile', {
    'nID': req.params.nID,
    'sCode_DocumentAccess': req.params.sCode_DocumentAccess,
    'nID_DocumentOperator_SubjectOrgan': req.params.nID_DocumentOperator_SubjectOrgan,
    'nID_DocumentType': req.params.nID_DocumentType,
    'sPass': req.params.sPass
  }));

  req.pipe(r).on('response', function (response) {
    response.headers['content-type'] = 'application/octet-stream';
  }).pipe(res);
};

module.exports.getDocumentAbstract = function (req, res) {
  var r = request(buildGetRequest(req, '/document/getDocumentAbstract', {
    'sID': req.params.sCode_DocumentAccess,
    //'sID': req.params.nID,
    //'sCode_DocumentAccess': req.params.sCode_DocumentAccess,
    'nID_DocumentOperator_SubjectOrgan': req.params.nID_DocumentOperator_SubjectOrgan,
    'nID_DocumentType': req.params.nID_DocumentType,
    'sPass': req.params.sPass
  }));

  req.pipe(r).on('response', function (response) {
    response.headers['content-type'] = 'application/octet-stream';
  }).pipe(res);
};


module.exports.getDocumentTypes = function (req, res) {
  return sendGetRequest(req, res, '/document/getDocumentTypes', {});
};

module.exports.getDocumentOperators = function (req, res) {
  return sendGetRequest(req, res, '/document/getDocumentOperators', {});
};

module.exports.searchDocument = function (req, res) {
  var params = {
    'sCode_DocumentAccess': req.body.params.sCode_DocumentAccess,
    'nID_DocumentOperator_SubjectOrgan': req.body.params.nID_DocumentOperator_SubjectOrgan,
    'nID_DocumentType': req.body.params.nID_DocumentType,
    'sPass': req.body.params.sPass
  };
  return sendGetRequest(req, res, '/document/access/getDocumentAccessByHandler', params);
};

module.exports.getDocument = function (req, res) {
  var params = {
    'nID': req.params.nID
  };
  return sendGetRequest(req, res, '/document/getDocument', params);
};

module.exports.getDocumentInternal = function (req, res, callback) {
  var params = {
    'nID': req.params.nID
  };
  sendGetRequest(req, res, '/document/getDocument', params, callback);
};

module.exports.index = function (req, res) {
  var params = {
    'nID_Subject': req.session.subject.nID
  };
  return sendGetRequest(req, res, '/document/getDocuments', params);
};

module.exports.upload = function (req, res) {
  var nID_Subject = req.session.subject.nID;
  var sID_Subject = req.session.subject.sID;
  var docType_nID = req.query.documentType;
  var docType_sName = req.query.documentName;
  var documentFileName = req.query.documentFileName;

  var qs = {
    'nID_Subject': nID_Subject,
    'sID_Subject_Upload': sID_Subject,
    'sSubjectName_Upload': 'Я',
    'sName': docType_sName,
    'nID_DocumentType': docType_nID
  };

  var fileNameAndExtension = documentFileName.split('.');
  if (fileNameAndExtension.length > 1) {
    _.merge(qs, {sFileExtension: fileNameAndExtension[fileNameAndExtension.length - 1]});
  }

  if(!qs.sFileExtension){
    res.status(400).send({
      error: 'cant find file extension'
    });
    return;
  }

  proxy.upload(req, res, getUrlWithParameters('/document/setDocumentFile', qs));
};

/*
 nID;sName
 0;Другое
 1;Справка
 2;Паспорт
 3;Загранпаспорт
 4;Персональное фото
 5;Справка о предоставлении ИНН */
module.exports.initialUpload = function (req, res) {
  process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

  var accessToken = req.session.access.accessToken;
  var nID_Subject = req.session.subject.nID;
  var sID_Subject = req.session.subject.sID;
  var typesToUpload = req.body;

  if (req.session.documents && req.session.documents.uploaded) {
    res.status(200).end();
    return;
  }

  if (!accessToken || !sID_Subject) {
    res.status(400).send({
      error: 'both accessToken and sID_Subject are needed'
    });
    return;
  }

  if (!typesToUpload || !typesToUpload.length || typesToUpload.length === 0) {
    res.status(400).send({
      error: 'nothing to upload'
    });
    return;
  }

  var options = getBankIDOptions(accessToken);

  var optionsForScans = _.merge(options, {
    path: '/ResourceService'
  });

  var docTypesToBankIDDocTypes = {
    2: 'passport',
    3: 'zpassport'
  };

  var optionsForUploadContentList = typesToUpload.map(function (docType) {
    var o = {
      'url': getUrl('/document/setDocumentFile'),
      'auth': getAuth(),
      'qs': {
        'nID_Subject': nID_Subject,
        'sID_Subject_Upload': sID_Subject,
        'sSubjectName_Upload': 'ПриватБанк',
        'sName': docType.sName,
        'nID_DocumentType': docType.nID
      }
    };
    var bankIDType = docTypesToBankIDDocTypes[docType.nID];
    return {
      scanFilter: function (scan) {
        return scan.type === bankIDType;
      },
      option: o
    };
  });

  var uploadScan = function (documentScan, optionsForUploadContent, callback) {
    var scanContentRequest = accountService.prepareScanContentRequest(
      documentScan.link, accessToken
    );

    var form = new FormData();
    form.append('oFile', scanContentRequest);

    var requestOptionsForUploadContent =
      _.merge(optionsForUploadContent.option, {
        headers: form.getHeaders(),
        'qs': {
          'sFileExtension': documentScan.extension
        }
      });

    var decoder = new StringDecoder('utf8');
    var result = {};
    form.pipe(request.post(requestOptionsForUploadContent))
      .on('response', function (response) {
        result.statusCode = response.statusCode;
      }).on('data', function (chunk) {
        if (result.body) {
          result.body += decoder.write(chunk);
        } else {
          result.body = decoder.write(chunk);
        }
      }).on('end', function () {
        callback(result);
      });
  };

  var doAsyncScansUpload = function (scans) {
    async.forEach(optionsForUploadContentList, function (optionsForUploadContent, callback) {
      var results = scans.filter(optionsForUploadContent.scanFilter);
      if (results.length === 1) {
        uploadScan(results[0], optionsForUploadContent, callback);
      } else {
        async.setImmediate(function () {
          callback(null);
        });
      }
      //don't do end here, it will be executed after all async
    }, function (result) {
      req.session.documents = {uploaded: true};
      res.send(result);
      res.end();
    });
  };

  var scansCallback = function (error, response, body) {
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

  accountService.scansRequest(accessToken, scansCallback);
};

function buildGetRequest(req, apiURL, params) {
  if (req.session.hasOwnProperty('subject') && req.session.subject.hasOwnProperty('nID')) {
    params = _.extend(params, {nID_Subject: req.session.subject.nID});
  }
  return {
    'url': getUrl(apiURL),
    'auth': getAuth(),
    'qs': params
  };
}

function sendGetRequest(req, res, apiURL, params, callback) {
  var _callback = callback ? callback : function (error, response, body) {
    res.send(body);
    res.end();
  };

  return request(buildGetRequest(req, apiURL, params), _callback);
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

function getUrl(apiURL) {
  var options = getOptions();
  return options.protocol + '://' + options.hostname + options.path + apiURL;
}

function getUrlWithParameters(apiURL, parameters) {
  var options = getOptions();

  return url.format({
    protocol: options.protocol,
    hostname: options.hostname,
    pathname: options.path + apiURL,
    query: parameters
  });
}

function getAuth() {
  var options = getOptions();
  return {
    'username': options.username,
    'password': options.password
  };
}
