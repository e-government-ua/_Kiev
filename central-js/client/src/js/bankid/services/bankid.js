define('bankid/service', ['angularAMD'], function (angularAMD) {
	angularAMD.service('BankIDService', ['$http', 'AdminService', function($http, AdminService) {
		this.login = function(code, redirect_uri) {
			var data = {
				'code': code,
				'redirect_uri': redirect_uri
			};
			return $http.get('./api/bankid/login', {
				params: data,
				data: data
			}).then(function(response) {
				return response.data;
			});
		};
		
		this.account = function(access_token) {
			var data = {
				'access_token': access_token
			};
			return $http.get('./api/bankid/account', {
				params: data,
				data: data
			}).then(function(response) {
				AdminService.processAccountResponse(response);
				return response.data;
			});
		}
		
	}]);
});