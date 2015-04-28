define('state/service/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceController', ['$state', '$rootScope', '$scope', 'service', function ($state, $rootScope, $scope, service) {
		$scope.service = service;
	}]);
});

define('state/service/general/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceGeneralController', ['$state', '$rootScope', '$scope', 'service', function ($state, $rootScope, $scope, service) {
		$scope.service = service;
		
		var places = service.places;
		if(places.regions.length == 0) {
			return $state.go('service.country', {id: service.id}, { location: true });
		}
		
		if(places.cities.length == 0) {
			return $state.go('service.region', {id: service.id}, { location: true });
		}
		
		return $state.go('service.city', {id: service.id}, { location: true });
    }]);
});