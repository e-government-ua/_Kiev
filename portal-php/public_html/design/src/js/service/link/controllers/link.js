define('service/link/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceLinkController', ['$rootScope', '$scope', function ($rootScope, $scope) {
		$scope.$location = $location;
		$scope.$state = $state;
    }]);
});