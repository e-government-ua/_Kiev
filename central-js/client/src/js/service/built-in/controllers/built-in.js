define('service/built-in/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceBuiltInController', ['$location', '$state', '$rootScope', '$scope', function ($location, $state, $rootScope, $scope) {
		$scope.$location = $location;
		$scope.$state = $state;
    }]);
});

define('service/built-in/bankid/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceBuiltInBankIDController', ['$state', '$stateParams', '$scope', 'BankIDAccount', function($state, $stateParams, $scope, BankIDAccount) {
		angular.forEach($scope.places.regions, function(value, key) {
			if($stateParams.region == value.id) {
				$scope.data.region = value;
			}
		});
		if($scope.data.region) {
			angular.forEach($scope.data.region.cities, function(value, key) {
				if($stateParams.city == value.id) {
					$scope.data.city = value;
				}
			});
		}
		
		$scope.account = BankIDAccount;
	}]);
});