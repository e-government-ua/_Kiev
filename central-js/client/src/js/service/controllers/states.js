define('state/service/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceController', ['$state', '$rootScope', '$scope', 'service', function ($state, $rootScope, $scope, service) {
		$scope.service = service;
	}]);
});

define('state/service/general/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceGeneralController', ['$state', '$rootScope', '$scope', 'service', function ($state, $rootScope, $scope, service) {
		$scope.service = service;
		
		var aServiceData = service.aServiceData;
		
		var isCity = false;
		angular.forEach(aServiceData, function(value, key) {
			if(value.hasOwnProperty('nID_City')) {
				isCity = true;
			}
		});
		if(isCity) {
			return $state.go('service.general.city', {id: service.nID}, { location: false });
		}
		
		var isRegion = false;
		angular.forEach(aServiceData, function(value, key) {
			if(value.hasOwnProperty('nID_Region')) {
				isRegion = true;
			}
		});
		if(isRegion) {
			return $state.go('service.general.region', {id: service.nID}, { location: false });
		}
		
		return $state.go('service.general.country', {id: service.nID}, { location: false });
    }]);
});



define('state/service/instruction/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceInstructionController', ['$state', '$rootScope', '$scope', 'service', function ($state, $rootScope, $scope, service) {
		$scope.service = service;
		return $state.go('service.instruction', {id: service.nID}, { location: false });
    }]);
});

define('state/service/legislation/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceLegislationController', ['$state', '$rootScope', '$scope', 'service', function ($state, $rootScope, $scope, service) {
		$scope.service = service;
		return $state.go('service.legislation', {id: service.nID}, { location: false });
    }]);
});

define('state/service/questions/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceQuestionsController', ['$state', '$rootScope', '$scope', 'service', function ($state, $rootScope, $scope, service) {
		$scope.service = service;
		return $state.go('service.questions', {id: service.nID}, { location: false });
    }]);
});

define('state/service/discussion/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceDiscussionController', ['$state', '$rootScope', '$scope', 'service', function ($state, $rootScope, $scope, service) {
		$scope.service = service;
		return $state.go('service.discussion', {id: service.nID}, { location: false });
    }]);
});
