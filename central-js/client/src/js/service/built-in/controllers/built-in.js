define('service/built-in/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceBuiltInController', ['$location', '$state', '$rootScope', '$scope', function ($location, $state, $rootScope, $scope) {
		$scope.$location = $location;
		$scope.$state = $state;
    }]);
});

define('service/built-in/bankid/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceBuiltInBankIDController', ['$state', '$stateParams', '$scope', 'BankIDAccount', 'ActivitiForm',
		function($state, $stateParams, $scope, BankIDAccount, ActivitiForm) {
			angular.forEach($scope.places.aRegion, function(value, key) {
				if($stateParams.region == value.nID) {
					$scope.data.region = value;
				}
			});
			if($scope.data.region) {
				angular.forEach($scope.data.region.aCity, function(value, key) {
					if($stateParams.city == value.nID) {
						$scope.data.city = value;
					}
				});
			}
			
			$scope.account = BankIDAccount;
			$scope.ActivitiForm = ActivitiForm;
			
			$scope.data = {
				bankid: {}
			};
			
			angular.forEach(BankIDAccount.customer, function(value, key) {
				$scope.data.bankid['bankId'+key] = value;
			});
			
			console.log($scope.data);
		}
	]);
});