define('activiti/service', ['angularAMD'], function (angularAMD) {
	angularAMD.service('ActivitiService', ['$http', function($http) {
		this.getForm = function(oServiceData) {
			var url = oServiceData.sURL + oServiceData.oData.sPath + '?processDefinitionId=' + oServiceData.oData.oParams.processDefinitionId;
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
		
		this.submitForm = function(form) {
			console.log('submitForm', form);
		};
	}]);
});