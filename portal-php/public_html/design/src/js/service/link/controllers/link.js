define('service/link/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceLinkController', ['$location', '$state', '$rootScope', '$scope', function ($location, $state, $rootScope, $scope) {
		$scope.$location = $location;
		$scope.$state = $state;
    }]);
});