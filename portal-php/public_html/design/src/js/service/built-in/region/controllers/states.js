define('state/service/built-in/region/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceBuiltInRegionController', ['$rootScope', 'regions', function ($rootScope, regions) {
		$scope.regions = regions;
    }]);
});