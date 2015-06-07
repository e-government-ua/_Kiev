define('state/index/controller', ['angularAMD'], function(angularAMD) {
  angularAMD.controller('IndexController', function($scope, CatalogService, catalog) {
    $scope.catalog = catalog;
    $scope.limit = 10;
    $scope.sSearch = null;

    $scope.search = function() {
      return CatalogService.getServices($scope.sSearch).then(function(result) {
        $scope.catalog = result;
      });
    }
  });
});

define('state/subcategory/controller', ['angularAMD'], function(angularAMD) {
  angularAMD.controller('SubcategoryController', function($scope, $stateParams, catalog, $filter) {
      $scope.catalog = catalog;

      $scope.category = $filter('filter')(catalog, {nID: $stateParams.catID})[0];
      $scope.subcategory = $filter('filter')($scope.category.aSubcategory, {nID: $stateParams.scatID})[0];
    }
  );
});