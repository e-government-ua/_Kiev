define('state/index/controller', ['angularAMD'], function(angularAMD) {
  angularAMD.controller('IndexController', function($scope, $rootScope, $timeout, CatalogService, catalog) {
    $scope.catalog = catalog;
      $scope.catalogCounts = {0:0,1:0,2:0};
    $scope.limit = 4;//7//10//limit of services
    $scope.sSearch = null;
    $scope.bShowExtSearch = false;

    $scope.search = function() {
      return CatalogService.getServices($scope.sSearch).then(function(result) {
        $scope.catalog = result;
      });
    };

    $scope.onExtSearchClick = function(){
        $scope.bShowExtSearch = !$scope.bShowExtSearch;
    };

    $scope.hideExtSearchPanel = function(){
        $scope.bShowExtSearch = false;
    };
    
    //Admin buttons visibility handling
    $rootScope.hiddenCtrlsGlobal = true;
    $scope.toggle = function() {
        $rootScope.hiddenCtrlsGlobal = !$rootScope.hiddenCtrlsGlobal;
        $cookies.put('badmin', 'true');
    };
    $scope.hiddenCtrls = $rootScope.hiddenCtrlsGlobal;

    

     $scope.$watch('catalog', function(newValue)
     {
         $timeout(function()
         {
             $scope.catalogCounts = {0:0,1:0,2:0};
             angular.forEach(newValue, function(item)
             {
                 angular.forEach(item.aSubcategory, function(subItem)
                 {
                     angular.forEach(subItem.aService, function(aServiceItem)
                     {
                         $scope.catalogCounts[aServiceItem.nStatus] ++ ;
                     })
                 });
             });
         });
     });
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