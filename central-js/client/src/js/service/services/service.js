define('service/service', ['angularAMD'], function (angularAMD) {
	angularAMD.service('ServiceService', ['$http', function($http) {
		this.get = function(id) {
			var data = {
				'id': id
			};
			return $http.get('./api/service', {
				params: data,
				data: data
			}).then(function(response) {
				return response.data;
			});
		};
		this.getPlaces = function() {
			return $http.get('./api/places').then(function(response) {
				return response.data;
			});
		};
	}]);
});