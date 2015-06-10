define('state/index/controller', ['angularAMD'], function(angularAMD) {
  angularAMD.controller('IndexController', function($scope, CatalogService, catalog) {
    $scope.catalog = catalog;
    $scope.limit = 7;//limit of services
    $scope.sSearch = null;

    $scope.search = function() {
      return CatalogService.getServices($scope.sSearch).then(function(result) {
        $scope.catalog = result;
      });
    };
    
    $scope.hiddenCtrls = true;
    $scope.toggle = function() {
        $scope.hiddenCtrls = !$scope.hiddenCtrls;
    };
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