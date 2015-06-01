define('service/service', ['angularAMD'], function (angularAMD) {
	angularAMD.service('ServiceService', ['$http', function($http) {
		this.get = function(id) {
			var data = {
				'nID': id
			};
			return $http.get('./api/service', {
				params: data,
				data: data,
				transformResponse: [function (rawData, headersGetter) {
					var data = angular.fromJson(rawData);
					angular.forEach(data.aServiceData, function(oServiceData) {
						try {
							oServiceData.oData = angular.fromJson(oServiceData.oData);
						} catch(e) {
							oServiceData.oData = {};
						}
					});
					return data;
				}]
			}).then(function(response) {
				return response.data;
			});
		};

		this.syncSubject = function (sInn) {
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

		this.getDocuments = function(sID_Subject) {
			var data = {
				'sID_Subject': sID_Subject
			};
			return $http.get('./api/service/documents', {
				params: data,
				data: data
			}).then(function(response) {
				return response.data;
			});
		};

		this.getDocument = function(sID_Subject, nID) {
			console.log('nID', nID);
			console.log('sID_Subject', nID);
			var data = {
				'sID_Subject': sID_Subject
			};
			return $http.get('./api/service/documents/' + nID, {
				params: data,
				data: data
			}).then(function(response) {
				return response.data;
			});
		};

		this.shareLink = function(nID_Subject, nID_Document, sFIO, sTarget, sTelephone, nDays) {
			var data = {
				'nID_Subject': nID_Subject,
				'nID_Document': nID_Document,
				'sFIO': sFIO,
				'sTarget': sTarget,
				'sTelephone': sTelephone,
				'nDays': nDays.day
			};
			console.log(data);
			return $http.get('./api/service/documents/share', {
				params: data,
				data: data
			}).then(function(response) {
				return response.data;
			});
		};
	}]);
});