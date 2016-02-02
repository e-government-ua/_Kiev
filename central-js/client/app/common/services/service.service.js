angular.module('app').service('ServiceService', function($http, $q) {

  var self = this;

  // FIXME хардкод треба замінити динамікою:
  var docTypes = {
    other: {nID: 0, sName: 'Другое'},
    reference: {nID: 1, sName: 'Справка'},
    passport: {nID: 2, sName: 'Паспорт'},
    zpassport: {nID: 3, sName: 'Загранпаспорт'},
    photo: {nID: 4, sName: 'Персональное фото'},
    inn: {nID: 5, sName: 'Справка о предоставлении ИНН'}
  };

  var passportFilter = function(docTypeID) {
    return docTypeID === docTypes.passport.nID;
  };

  var zpassportFilter = function(docTypeID) {
    return docTypeID === docTypes.zpassport.nID;
  };

  var rejectIfError = function(data) {
    if (data.hasOwnProperty('error')) {
      return $q.reject(data);
    }
    return data;
  };

  this.get = function(id) {
    var data = {
      'nID': id
    };
    return $http.get('./api/service', {
      params: data,
      data: data,
      transformResponse: [function(rawData, headersGetter) {
        var data = angular.fromJson(rawData);
        angular.forEach(data.aServiceData, function(oServiceData) {
          try {
            oServiceData.oData = angular.fromJson(oServiceData.oData);
          } catch (e) {
            oServiceData.oData = {};
          }
        });
        return data;
      }]
    }).then(function(response) {
      self.oService = response.data;
      return self.oService;
    });
  };

  this.set = function(data){

    angular.forEach(data.aServiceData, function(oServiceData){
      if (oServiceData.oData){
        oServiceData.oData = angular.toJson(oServiceData.oData);
      } else{
        oServiceData.oData = null;
      }
    });

    return $http.post('./api/service', data, {
      transformResponse: [function(rawData, headersGetter) {
        var data = angular.fromJson(rawData);
        angular.forEach(data.aServiceData, function(oServiceData) {
          try {
            oServiceData.oData = angular.fromJson(oServiceData.oData);
          } catch (e) {
            oServiceData.oData = {};
          }
        });
        return data;
      }]
    }).then(function(response) {
      self.oService = response.data;
      return self.oService;
    });
  };

  this.remove = function(nID, bRecursive){
    return $http.delete('/api/service', {
      params: {
        nID: nID,
        bRecursive: bRecursive
      }
    });
  };

  this.getProcessDefinitions = function(oServiceData, latest) {
    var data = {
      //--//'url': oServiceData.sURL,
      'nID_Server': oServiceData.nID_Server,
      'latest': latest || null
    };
    return $http.get('./api/process-definitions', {
      'params': data,
      'data': data
    }).then(function(response) {
      return response.data;
    });
  };

  this.getDocuments = function() {
    var data = {};
    return $http.get('./api/documents', {
      params: data,
      data: data
    }).then(function(response) {
      return response.data;
    });
  };

  this.getDocumentLink = function(docnID) {
    return '/api/documents/download/' + docnID;
  };

  this.getSearchDocumentLink = function(docnID, typeId, operatorId, code, smsPass) {
      if(smsPass==null||smsPass==''){
          smsPass=0;
      }
    return '/api/documents/search/download/' + docnID  + '/' +  code + '/'
        + operatorId + '/' + typeId + '/' + smsPass;
  };

  this.shareLink = function(nID_Subject, nID_Document, sFIO, sTelephone, sMail, nMS) {
    var data = {
      'nID_Subject': nID_Subject,
      'nID_Document': nID_Document,
      'sFIO': sFIO,
      'sTarget': '',
      'sTelephone': sTelephone,
      'sMail': sMail,
      'nMS': nMS
    };
    return $http.get('./api/documents/' + nID_Document + '/share', {
      params: data,
      data: data
    }).then(function(response) {
      return response.data;
    });
  };

  this.getDocumentTypes = function() {
    return $http.get('./api/documents/search/getDocumentTypes').then(function(response) {
      return response.data;
    });
  };

  this.getDocumentOperators = function() {
    return $http.get('./api/documents/search/getDocumentOperators').then(function(response) {
      return response.data;
    });
  };

  this.searchDocument = function(typeId, operatorId, code, pass) {
    var data = {
      'sCode_DocumentAccess': code,
      'nID_DocumentOperator_SubjectOrgan': operatorId,
      'nID_DocumentType': typeId,
      'sPass': pass
    };
    return $http.post('./api/documents/search/searchDocument', {
      params: data,
      data: data
    }).then(function(response) {
      return response.data;
    });
  };

  this.searchOrder = function(sID, sToken) {
    return $http.get('./api/order/search/' + sID, {params: sToken!==null ? {sToken: sToken} : {}}).then(function (response) {
      return response.data;
    });
  };

  this.getCountOrders = function(nID_Service, sID_UA, nLimit, bExcludeClosed) {
    return $http.get('./api/order/count', {params: {
      nID_Service: nID_Service,
      sID_UA: sID_UA,
      nLimit: nLimit,
      bExcludeClosed: bExcludeClosed
    }}).then(function (response) {
      return response.data;
    });
  };

  this.getJournalEvents = function() {
    var data = {};
    return $http.get('./api/journal', {
      params: data,
      data: data
    }).then(function(response) {
      return response.data;
    });
  };

  this.initialUpload = function(typesToUpload) {
    var data = {};
    return $http.post('./api/documents/initialUpload', typesToUpload, {
      params: data
    }).then(function(response) {
      return response.data;
    });
  };

  this.getOrUploadDocuments = function() {
    var initialUpload = this.initialUpload;
    var getDocuments = this.getDocuments;

    return this.getDocuments().then(function(data) {
      return rejectIfError(data);
    }).then(function(documents) {
      var typesToUpload = [];

      var alreadyUploadedTypes = documents.map(function(doc) {
        return doc.oDocumentType.nID;
      });

      if (alreadyUploadedTypes.filter(passportFilter).length === 0) {
        typesToUpload.push(docTypes.passport);
      }

      if (alreadyUploadedTypes.filter(zpassportFilter).length === 0) {
        typesToUpload.push(docTypes.zpassport);
      }

      return $q.when(typesToUpload.length === 0 ? documents :
        initialUpload(typesToUpload).then(function(uploadingResult) {
          return rejectIfError(uploadingResult);
        }).then(function() {
          return getDocuments();
        }).then(function(updatedDocuments) {
          return rejectIfError(updatedDocuments);
        }));
    });
  };

  this.getStatisticsForService = function(serviceId) {
    return $http.get('./api/service/' + serviceId + '/statistics');
  };

  this.verifyContactEmail = function(authEmailData) {
    var data = {
      sQuestion: authEmailData.email
    };
    return $http.get('./auth/email/verifyContactEmail', {
      params: data,
      data: data
    }).then(function(response) {
      return response.data;
    });
  };

  this.verifyContactEmailAndCode = function(authEmailData) {
    var data = {
      sQuestion: authEmailData.email,
      sAnswer: authEmailData.code
    };
    return $http.get('./auth/email/verifyContactEmailAndCode', {
      params: data,
      data: data
    }).then(function(response) {
      return response.data;
    });
  };
});
