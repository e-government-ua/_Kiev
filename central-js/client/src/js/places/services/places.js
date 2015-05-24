define('places/service', ['angularAMD'], function (angularAMD) {
    angularAMD.service('PlacesService', ['$http', function ($http) {
		
		this.getRegions = function() {
			return $http.get('./api/places/regions');
		};
		
		this.getRegion = function(region) {
			return $http.get('./api/places/region/' + region);
		};
		
		this.getCities = function(region, search) {
			var data = {
				sFind: search
			};
			return $http.get('./api/places/region/' + region + '/cities', {
				params: data,
				data: data
			});
		};
		
		this.getCity = function(region, city) {
			return $http.get('./api/places/region/' + region + '/city/' + city);
		};
    }]);
});