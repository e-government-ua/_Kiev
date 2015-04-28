define('service/link/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceLinkController', ['$rootScope', '$scope', function ($rootScope, $scope) {
		$scope.data = {
			region: null,
			city: null
		};
    }]);
});