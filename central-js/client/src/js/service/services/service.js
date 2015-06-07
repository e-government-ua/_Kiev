define('service/service', ['angularAMD'], function(angularAMD) {
  angularAMD.service('ServiceService', function($http) {

  	var docTypes = {
		other : { nID: 0, sName: 'Другое'},
		reference : { nID: 1, sName: 'Справка'},
		passport : { nID: 2, sName: 'Паспорт'},
		zpassport : { nID: 3, sName: 'Загранпаспорт'},
		photo : { nID: 4, sName: 'Персональное фото'},
		inn: { nID: 5, sName: 'Справка о предоставлении ИНН'}
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

    this.syncSubject = function(sInn) {
      var data = {
        'sINN': sInn
      };
      return $http.get('./api/service/syncSubject', {
        params: data,
        data: data
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

    this.getDocuments = function(nID_Subject) {
      var data = {
        'nID_Subject': nID_Subject
      };
      return $http.get('./api/service/documents', {
        params: data,
        data: data
      }).then(function(response) {
        return response.data;
      });
    };

    this.shareLink = function(nID_Subject, nID_Document, sFIO, sTelephone, sMail) {
      var fiveDays = 5 * 86400000;
      var data = {
        'nID_Subject': nID_Subject,
        'nID_Document': nID_Document,
        'sFIO': sFIO,
        'sTarget': '',
        'sTelephone': sTelephone,
        'sMail': sMail,
        'nMS': fiveDays
      };
      return $http.get('./api/service/documents/' + nID_Document + '/share', {
        params: data,
        data: data
      }).then(function(response) {
        return response.data;
      });
    };

    this.getJournalEvents = function(nID_Subject) {
      var data = {
        'nID_Subject': nID_Subject
      };
      return $http.get('./api/service/journal', {
        params: data,
        data: data
      }).then(function(response) {
        return response.data;
      });
    };
    
   this.initialUpload = function(accessToken, nID_Subject, sID_Subject, typesToUpload) {
		var data = {
			'access_token': accessToken,
			'nID_Subject': nID_Subject,
			'sID_Subject': sID_Subject
		};
		return $http.post('./api/service/documents/initialupload', typesToUpload, {
			params: data
		}).then(function(response) {
			return response.data;
		});
	};

	this.getOrUploadDocuments = function(accessToken, nID_Subject, sID_Subject) {
		var initialUpload = this.initialUpload;
		var getDocuments = this.getDocuments;
		return this.getDocuments(nID_Subject).then(function(data) {
			if (data.hasOwnProperty('error')) {
				return $q.reject(null);
			}

			var passportFilter = function(docTypeID){ return docTypeID === docTypes.passport.nID;};
			var zpassportFilter = function(docTypeID){ return docTypeID === docTypes.zpassport.nID;};
			var typesToUpload = [];

			var alreadyUploadedTypes = data.map(function(doc) {
				return doc.oDocumentType.nID;
			});

			var passportResult = alreadyUploadedTypes.filter(passportFilter);

			if (alreadyUploadedTypes.filter(passportFilter).length === 0) {
				typesToUpload.push(docTypes.passport);					
			}
			
			// don't use for now. Back should have service for recieving multiple files
			// if(alreadyUploadedTypes.filter(zpassportFilter).length === 0){
			// 	typesToUpload.push(docTypes.zpassport);
			// }

			if (typesToUpload.length > 0) {
				return initialUpload(accessToken,
						nID_Subject,
						sID_Subject,
						typesToUpload)
					.then(function(uploadingResult) {
						if (!uploadingResult.hasOwnProperty('error')) {
							return getDocuments($state.nID_Subject).then(function(updatedData) {
								return updatedData.hasOwnProperty('error') ? $q.reject(null) : updatedData;
							});
						} else {
							return data;
						}
					});
			} else {
				return data;
			}
		});
	};
  });
});