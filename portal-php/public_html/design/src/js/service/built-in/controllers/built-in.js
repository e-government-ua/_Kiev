define('service/built-in/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceBuiltInController', ['$rootScope', '$scope', function ($rootScope, $scope) {
		$scope.data = {
			region: null,
			city: null
		};
    }]);
});