var request = require('request');
var FormData = require('form-data');
var config = require('../../config/environment');
var accountService = require('../bankid/account.service.js');

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

//aAddress: undefined
//aDocument: undefined
//oValue: Array[1]
//0: Object
//extension: "pdf"
//link: "https://bankid.privatbank.ua/ResourceService/checked/scan/c0cc81f968a2e109609d89a46b8e238703e51d2d/passport"
//number: 1
//type: "passport"
//__proto__: Object
//length: 1
//__proto__: Array[0]
//sFieldName: undefined
//sKey: "scans"
module.exports.scanUpload = function (req, res) {
  var data = req.body;
  var uploadURL = data.url;
  var documentScan = data.scan;

  var scanContentRequest = accountService.prepareScanContentRequest(
    _.merge(options, {
      url: documentScan.link
    })
  );

  var form = new FormData();
  form.append('file', scanContentRequest);

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
      if (result.body) {
        result.body += decoder.write(chunk);
      } else {
        result.body = decoder.write(chunk);
      }
    }).on('end', function () {
      res.send(result);
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
