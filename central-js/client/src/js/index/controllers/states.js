define('state/index/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('IndexController', ['$scope', 'CatalogService', 'catalog', function ($scope, CatalogService, catalog) {
		$scope.catalog = catalog;
		
		$scope.sSearch = null;
		
		$scope.search = function() {
			return CatalogService.getServices($scope.sSearch).then(function(result) {
				$scope.catalog = result;
			});
		}
    }]);
});