define('state/service/built-in/region/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceBuiltInRegionController', ['$rootScope', '$scope', 'regions', function ($rootScope, $scope, regions) {
		$scope.region = null;
		$scope.regions = regions;
    }]);
});