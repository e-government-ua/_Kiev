define('state/service/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceController', ['$state', '$rootScope', '$scope', 'service', function ($state, $rootScope, $scope, service) {
		$scope.service = service;
	}]);
});

define('state/service/general/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceGeneralController', ['$state', '$rootScope', '$scope', 'service', function ($state, $rootScope, $scope, service) {
		$scope.service = service;
		switch(service.serviceType.id) {
			case 1:
				$state.go('service.link', {id: service.id}, { location: true });
				break;
			case 4:
				$state.go('service.built-in', {id: service.id}, { location: true });
				break;
		}
    }]);
});