define('state/service/country/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceCountryController', ['$state', '$rootScope', '$scope', 'service', 'places',
		function ($state, $rootScope, $scope, service, places) {
			$scope.service = service;
			$scope.places = places;
			
			$scope.data = {
				region: null,
				city: null
			};
			
			if($state.current.name == 'service.country.built-in.bankid') {
				return true;
			}
			
			switch(service.serviceType.id) {
				case 1:
					return $state.go('service.country.link', {id: service.id}, { location: true });
				case 4:
					return $state.go('service.country.built-in', {id: service.id}, { location: true });
			}
		}
	]);
});