var request = require('request');
var FormData = require('form-data');
var config = require('../../config/environment');
var accountService = require('../bankid/account.service.js');
var _ = require('lodash');
var StringDecoder = require('string_decoder').StringDecoder;
var async = require('async');

module.exports.index = function (req, res) {

  var activiti = config.activiti;

  var options = {
    protocol: activiti.protocol,
    hostname: activiti.hostname,
    port: activiti.port,
    path: activiti.path,
    username: activiti.username,
    password: activiti.password,
    params: {
      url: req.query.url || null
    }
  };

  var callback = function (error, response, body) {
    res.send(body);
    res.end();
  };

  return request.get({
    url: options.params.url,
    auth: {
      username: options.username,
      password: options.password
    }
  }, callback);
};

module.exports.submit = function (req, res) {
  var activiti = config.activiti;

  var options = {
    protocol: activiti.protocol,
    hostname: activiti.hostname,
    port: activiti.port,
    path: activiti.path,
    username: activiti.username,
    password: activiti.password,
    formData: req.body
  };

  var callback = function (error, response, body) {
    res.send(body);
    res.end();
  };

  var nID_Subject = req.session.subject.nID;
  var properties = [];
  for (var id in options.formData.params) {
    var value = options.formData.params[id];
    if (id === 'nID_Subject') {
      value = nID_Subject;
    }
    if (id === 'sID_UA' && options.formData.sID_UA_Common !== null) {
    //if (id === 'sID_UA_Common') {
      value = options.formData.sID_UA_Common;
    }else if (id === 'sID_UA') {
      value = options.formData.sID_UA;
    }
    
    properties.push({
      id: id,
      value: value
    });
  }

  return request.post({
    url: options.formData.url || null,
    auth: {
      username: options.username,
      password: options.password
    },
    body: {
      processDefinitionId: options.formData.processDefinitionId,
      businessKey: "key",
      properties: properties,
      nID_Subject: nID_Subject
    },
    qs: {
      nID_Service: options.formData.nID_Service,
      nID_Region: options.formData.nID_Region,
      sID_UA: options.formData.sID_UA
    },
    json: true
  }, callback);
};

module.exports.scanUpload = function (req, res) {
  var accessToken = req.session.access.accessToken;
  var data = req.body;
  var uploadURL = data.url;
  var documentScans = data.scanFields;

  var uploadResults = [];
  var uploadScan = function (documentScan, callback) {
    var scanContentRequest = accountService.prepareScanContentRequest(
      _.merge(getBankIDOptions(accessToken), {
        url: documentScan.scan.link
      })
    );

    var form = new FormData();
    form.append('file', scanContentRequest, {
        filename: documentScan.scan.type + '.' + documentScan.scan.extension
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
        result.scanField= documentScan;
        uploadResults.push(result);
        callback();
      });
  };

  async.forEach(documentScans, function (documentScan, callback) {
    uploadScan(documentScan, callback);
  }, function (error) {
    res.send(uploadResults);
    res.end();
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
