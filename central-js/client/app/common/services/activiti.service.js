angular.module('app').service('ActivitiService', function($http, ErrorsFactory) {
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
    var data = {
      'url': url
    };

    var nID_Region;
    var sID_UA;
    var sID_UA_Common;

    if(oServiceData.nID_Region){
      nID_Region = oServiceData.nID_Region.nID;
      sID_UA = oServiceData.nID_Region.sID_UA;
      sID_UA_Common = oServiceData.nID_Region.sID_UA;
    } else if (oServiceData.nID_City){
      nID_Region = oServiceData.nID_City.nID_Region.nID;
      sID_UA = oServiceData.nID_City.nID_Region.sID_UA;
      sID_UA_Common = oServiceData.nID_City.sID_UA;
    }

    var params = {
      nID_Service : oService.nID,
      nID_Region : nID_Region,
      sID_UA : sID_UA,
      sID_UA_Common : sID_UA_Common
    };

    var data = angular.extend(data, formData.getRequestObject());
    var data = angular.extend(data, params);

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

  this.getUploadFileURL = function(oServiceData) {
    return './api/uploadfile?url=' + oServiceData.sURL + 'service/rest/file/upload_file_to_redis';
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
