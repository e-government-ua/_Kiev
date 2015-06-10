define('state/service/country/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceCountryController', ['$state', '$rootScope', '$scope', '$sce', 'service',
		function ($state, $rootScope, $scope, $sce, service) {
			$scope.service = service;
			
			$scope.data = {
				region: null,
				city: null
			};
			
			$scope.step1 = function() {
				var aServiceData = $scope.service.aServiceData;
				var serviceType = { nID: 0 };
				angular.forEach(aServiceData, function(value, key) {
					serviceType = value.nID_ServiceType;
					$scope.serviceData = value;
					$scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
				});
					
				switch(serviceType.nID) {
					case 1:
						return $state.go('service.general.country.link', {id: $scope.service.nID}, { location: false });
					case 4:
						return $state.go('service.general.country.built-in', {id: $scope.service.nID}, { location: false });
					default:
						return $state.go('service.general.country.error', {id: $scope.service.nID}, { location: false });
				}
			}
			
			if($state.current.name == 'service.general.country.built-in.bankid') {
				return true;
			}
			
			$scope.step1();
		}
	]);
});