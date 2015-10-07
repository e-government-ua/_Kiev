angular.module('app').service('ActivitiService', function($http, ErrorsFactory) {

  var prepareFormData = function(oService, oServiceData, formData, url){
    var data = {
      'url': url
    };

    var nID_Region;
    var sID_UA;

    if(oServiceData.nID_Region){
      nID_Region = oServiceData.nID_Region.nID;
      sID_UA = oServiceData.nID_Region.sID_UA
    } else if (oServiceData.nID_City){
      nID_Region = oServiceData.nID_City.nID_Region.nID;
      sID_UA = oServiceData.nID_City.nID_Region.sID_UA
    }

    var params = {
      nID_Service : oService.nID,
      nID_Region : nID_Region,
      sID_UA : sID_UA
    };

    data = angular.extend(data, formData.getRequestObject());
    data = angular.extend(data, params);

    return data;
  };

  this.getForm = function(oServiceData, processDefinitionId) {
    var url = oServiceData.sURL + oServiceData.oData.sPath + '?processDefinitionId=' + processDefinitionId.sProcessDefinitionKeyWithVersion;
    var data = {
      'url': url
    };
    return $http.get('./api/process-form', {
      params: data,
      data: data
    }).then(function(response) {
      return response.data;
    });
  };

  this.submitForm = function (oService, oServiceData, formData) {
    var url = oServiceData.sURL + oServiceData.oData.sPath;
    var data = prepareFormData(oService, oServiceData, formData, url);

    return $http.post('./api/process-form', data).then(function (response) {
        if (/err/i.test(response.data.code)) {
          ErrorsFactory.push({
            type: "danger",
            text: [response.data.code, response.data.message].join(" ")
          });
        }
        return response.data;
      });
  };

  this.saveForm = function(oService, oServiceData, formData) {
    var url = oServiceData.sURL + oServiceData.oData.sPath;
    var data = prepareFormData(oService, oServiceData, formData, url);
    var uploadFormUrl = oServiceData.sURL + 'service/rest/file/upload_file_to_redis';
    var loadFormUrl = 'http://localhost:9000/service/720/general/place/built-in/region/1/city/1/';

    data = angular.extend(data, {uploadFormUrl : uploadFormUrl, loadFormUrl : loadFormUrl});

    return $http.post('./api/process-form/save', data).then(function (response) {
      if (/err/i.test(response.data.code)) {
        ErrorsFactory.push({
          type: "danger",
          text: [response.data.code, response.data.message].join(" ")
        });
      }
      return response.data;
    });
  };

  this.signForm = function(oService, oServiceData, formData){
    var data = prepareFormData(oService, oServiceData, formData);

    return $http.post('./api/process-form/sign', data).then(function (response) {
      if (/err/i.test(response.data.code)) {
        ErrorsFactory.push({
          type: "danger",
          text: [response.data.code, response.data.message].join(" ")
        });
      }
      return response.data;
    });
  };

  this.getUploadFileURL = function(oServiceData) {
    return './api/uploadfile?url=' + oServiceData.sURL + 'service/rest/file/upload_file_to_redis';
  };

  this.getDownloadFileUrlPart = function(oServiceData) {
    return oServiceData.sURL + 'service/rest/file/upload_file_to_redis';
  };

  this.updateFileField = function(oServiceData, formData, propertyID, fileUUID) {
    formData.params[propertyID].value = fileUUID;
  };

  this.autoUploadScans = function(oServiceData, scans){
    var data = {
      url: oServiceData.sURL + 'service/rest/file/upload_file_to_redis',
      scanFields : scans
    };

    return $http.post('./api/process-form/scansUpload', data).then(function (response) {
      if (/err/i.test(response.data.code)) {
        ErrorsFactory.push({
          type: "danger",
          text: [response.data.code, response.data.message].join(" ")
        });
      }
      return response.data;
    });
  }
});
