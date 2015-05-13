define('activiti/service', ['angularAMD'], function (angularAMD) {
	angularAMD.service('ActivitiService', ['$http', function($http) {
		this.getForm = function(oServiceData) {
			var url = oServiceData.sUrl + oServiceData.oData.sPath + '?processDefinitionId=' + oServiceData.oData.oParams.processDefinitionId;
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
		
		this.submitForm = function(oServiceData, formData) {
			var url = oServiceData.sUrl + oServiceData.oData.sPath;
			var data = {
				'url': url
			};
			return $http.post('./api/process-form', angular.extend(data, formData.getRequestObject()), {
			}).then(function(response) {
				return response.data;
			});
		};
	}]);
});