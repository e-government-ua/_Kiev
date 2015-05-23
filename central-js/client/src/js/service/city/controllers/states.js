define('state/service/city/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceCityController', ['$state', '$rootScope', '$scope', 'service', 'regions', 'ServiceService',
		function ($state, $rootScope, $scope, service, regions, ServiceService) {
			$scope.service = service;
			$scope.regions = regions;
			$scope.onSelectCity = function(item) {
				$scope.data.city = item;
			}
			$scope.data = {
				region: null,
				city: null
			};
			$scope.getCities = function(val) {
				return ServiceService.getRegionCities($scope.data.region['nID'], val);
			}
			
			$scope.step1 = function() {
				$scope.data = {
					region: null,
					city: null
				};
				return $state.go('service.general.city', {id: $scope.service.nID});
			};
			
			$scope.step2 = function() {
				var aServiceData = $scope.service.aServiceData;
				var serviceType = { nID: 0 };
				angular.forEach(aServiceData, function(value, key) {
					if(value.nID_City.nID == $scope.data.city.nID) {
						serviceType = value.nID_ServiceType;
					}
				});
				
				switch(serviceType.nID) {
					case 1:
						return $state.go('service.general.city.link', {id: $scope.service.nID}, { location: false });
					case 4:
						return $state.go('service.general.city.built-in', {id: $scope.service.nID}, { location: false });
					default:
						return $state.go('service.general.city.error', {id: $scope.service.nID}, { location: false });
				}
			};
			
			if($state.current.name == 'service.general.city.built-in.bankid') {
				return true;
			}
			
			$scope.$watchCollection('data.city', function(newValue, oldValue) {
				return (newValue == null) ? null: $scope.step2();
			});
		}
	]);
});