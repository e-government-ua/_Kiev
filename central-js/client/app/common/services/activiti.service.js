angular.module('app').service('ActivitiService', function ($http, $location, ErrorsFactory) {

  var prepareFormData = function (oService, oServiceData, formData, url) {
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

    data = angular.extend(data, formData.getRequestObject());
    data = angular.extend(data, params);

    return data;
  };

  this.getForm = function (oServiceData, processDefinitionId) {
    var url = oServiceData.sURL + oServiceData.oData.sPath + '?processDefinitionId=' + processDefinitionId.sProcessDefinitionKeyWithVersion;
    var data = {
      'url': url
    };
    return $http.get('./api/process-form', {
      params: data,
      data: data
    }).then(function (response) {
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

  this.loadForm = function (oServiceData, formID) {
    var data = {sURL: oServiceData.sURL, formID: formID};

    return $http.get('./api/process-form/load', {params: data}).then(function (response) {
      if (/err/i.test(response.data.code)) {
        ErrorsFactory.push({
          type: "danger",
          text: [response.data.code, response.data.message].join(" ")
        });
      }
      return response.data;
    });
  };

  this.saveForm = function (oService, oServiceData, businessKey, processName, activitiForm, formData) {
    var url = oServiceData.sURL + oServiceData.oData.sPath;
    var data = {
      formData : prepareFormData(oService, oServiceData, formData, url),
      activitiForm: activitiForm,
      processName : processName,
      businessKey : businessKey
    };

    var restoreFormUrl = $location.absUrl();

    var params = {
      sURL : oServiceData.sURL
    };
    data = angular.extend(data, {
      restoreFormUrl: restoreFormUrl
    });

    return $http.post('./api/process-form/save', data, {params : params}).then(function (response) {
      if (/err/i.test(response.data.code)) {
        ErrorsFactory.push({
          type: "danger",
          text: [response.data.code, response.data.message].join(" ")
        });
      }
      return response.data;
    });
  };

  this.getSignFormPath = function (oServiceData, formID) {
    return '/api/process-form/sign?formID=' + formID + '&sURL=' + oServiceData.sURL;
  };

  this.getUploadFileURL = function (oServiceData) {
    return './api/uploadfile?url=' + oServiceData.sURL + 'service/rest/file/upload_file_to_redis';
  };

  this.updateFileField = function (oServiceData, formData, propertyID, fileUUID) {
    formData.params[propertyID].value = fileUUID;
  };

  this.autoUploadScans = function (oServiceData, scans) {
    var data = {
      url: oServiceData.sURL + 'service/rest/file/upload_file_to_redis',
      scanFields: scans
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
