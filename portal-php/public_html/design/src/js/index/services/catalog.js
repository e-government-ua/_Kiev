define('catalog/service', ['angularAMD'], function (angularAMD) {
	angularAMD.service('CatalogService', ['$http', function($http) {
		this.getServices = function() {
			return $http.get('./api/services').then(function(response) {
				return response.data;
			});
		};
	}]);
});