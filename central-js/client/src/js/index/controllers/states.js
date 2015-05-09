define('state/index/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('IndexController', ['$scope', 'catalog', function ($scope, catalog) {
		$scope.catalog = catalog;
    }]);
});