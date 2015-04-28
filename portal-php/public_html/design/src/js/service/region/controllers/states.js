define('state/service/region/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceRegionController', ['$state', '$rootScope', '$scope', 'service', 'places',
		function ($state, $rootScope, $scope, service, places) {
			$scope.service = service;
			$scope.places = places;
			
			switch(service.serviceType.id) {
				case 1:
					return $state.go('service.region.link', {id: service.id}, { location: true });
				case 4:
					return $state.go('service.region.built-in', {id: service.id}, { location: true });
			}
		}
	]);
});