angular.module('app').controller('SubcategoryController', function($scope, $stateParams, catalog, $filter, $location, $anchorScroll) {
  $scope.catalog = catalog;

  $scope.category = $filter('filter')(catalog, {nID: $stateParams.catID})[0];
  $scope.subcategory = $filter('filter')($scope.category.aSubcategory, {nID: $stateParams.scatID})[0];

  // Scroll to the top of the section - issues/589
  // After Angular upgrade (current version which is used is 1.3.15),
  // it can be used with parameter: $anchorScroll('top') (which will also keep location hash intact)
  // https://github.com/angular/angular.js/pull/9596/files
  $anchorScroll();

});