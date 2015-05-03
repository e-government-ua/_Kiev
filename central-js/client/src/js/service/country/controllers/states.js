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
			
			var aServiceData = $scope.service.aServiceData;
			var serviceType = null;
			angular.forEach(aServiceData, function(value, key) {
				serviceType = value.nID_ServiceType;
			});
				
			switch(serviceType) {
				case 1:
					return $state.go('service.country.link', {id: $scope.service.nID});
				case 4:
					return $state.go('service.country.built-in', {id: $scope.service.nID});
				default:
					return $state.go('service.country.error', {id: $scope.service.nID});
			}
		}
	]);
});