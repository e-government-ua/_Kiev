define('state/service/city/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceCityController', ['$state', '$rootScope', '$scope', 'service', 'places',
		function ($state, $rootScope, $scope, service, places) {
			$scope.service = service;
			$scope.places = places;
			
			$scope.data = {
				region: null,
				city: null
			};
			
			if($state.current.name == 'service.city.built-in.bankid') {
				return true;
			}
			
			$scope.$watchCollection('data.city', function(newValue, oldValue) {
				if(newValue == null) {
					return null;
				}
				
				var aServiceData = $scope.service.aServiceData;
				var serviceType = null;
				angular.forEach(aServiceData, function(value, key) {
					if(value.nID_City == $scope.data.city.nID) {
						serviceType = value.nID_ServiceType;
					}
				});
				
				switch(serviceType) {
					case 1:
						return $state.go('service.city.link', {id: $scope.service.nID});
					case 4:
						return $state.go('service.city.built-in', {id: $scope.service.nID});
					default:
						return $state.go('service.city.error', {id: $scope.service.nID});
				}
			});

		}
	]);
});