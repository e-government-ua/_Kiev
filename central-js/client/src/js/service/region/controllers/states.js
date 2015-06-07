define('state/service/region/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceRegionController', [
		'$state', '$rootScope', '$scope', 'RegionListFactory', 'PlacesService', 'ServiceService', 'service', 'regions',
		function ($state, $rootScope, $scope, RegionListFactory, PlacesService, ServiceService, service, regions) {
			$scope.service = service;
			$scope.regions = regions;
			
			$scope.regionList = new RegionListFactory();
			$scope.regionList.initialize(regions);
			
			$scope.loadRegionList = function(search) {
				return $scope.regionList.load(service, search);
			};
			
			$scope.onSelectRegionList = function($item, $model, $label) {
				$scope.data.region = $item;
				$scope.regionList.select($item, $model, $label);
			};
			
			$scope.data = {
				region: null,
				city: null
			};
			
			$scope.step1 = function() {
				$scope.data = {
					region: null,
					city: null
				};
				return $state.go('service.general.region', {id: $scope.service.nID});
			};
			
			$scope.step2 = function() {
				var aServiceData = $scope.service.aServiceData;
				var serviceType = { nID: 0 };
				angular.forEach(aServiceData, function(value, key) {
					if(value.nID_Region.nID == $scope.data.region.nID) {
						serviceType = value.nID_ServiceType;
						$scope.serviceData = value;
					}
				});
				
				switch(serviceType.nID) {
					case 1:
						return $state.go('service.general.region.link', {id: $scope.service.nID}, { location: false });
					case 4:
						return $state.go('service.general.region.built-in', {id: $scope.service.nID}, { location: false });
					default:
						return $state.go('service.general.region.error', {id: $scope.service.nID}, { location: false });
				}
			};
			
			if($state.current.name == 'service.general.region.built-in.bankid') {
				return true;
			}
			
			$scope.$watchCollection('data.region', function(newValue, oldValue) {
				return (newValue == null) ? null: $scope.step2();
			});
		}
	]);
});