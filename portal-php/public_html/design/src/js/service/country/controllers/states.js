define('state/service/country/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceCountryController', ['$state', '$rootScope', '$scope', 'service', function ($state, $rootScope, $scope, service) {
		$scope.service = service;
		
		switch(service.serviceType.id) {
			case 1:
				return $state.go('service.country.link', {id: service.id}, { location: true });
			case 4:
				return $state.go('service.country.built-in', {id: service.id}, { location: true });
		}
    }]);
});