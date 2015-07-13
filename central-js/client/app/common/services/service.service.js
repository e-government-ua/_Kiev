angular.module('app').service('ServiceService', function($http, $q) {

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
      return response.data;
    });
  };

  this.getProcessDefinitions = function(oServiceData, latest) {
    var data = {
      'url': oServiceData.sURL,
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
});
