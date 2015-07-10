angular.module('app').controller('SubcategoryController', function($scope, $stateParams, catalog, $filter) {
  $scope.catalog = catalog;

  $scope.category = $filter('filter')(catalog, {nID: $stateParams.catID})[0];
  $scope.subcategory = $filter('filter')($scope.category.aSubcategory, {nID: $stateParams.scatID})[0];
});