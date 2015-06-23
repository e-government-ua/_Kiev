define('state/index/controller', ['angularAMD', 'service'], function(angularAMD) {
  angularAMD.controller('IndexController', function($scope, $rootScope, $timeout, CatalogService, catalog, AdminService, $filter) {
    $scope.catalog = catalog;
      $scope.catalogCounts = {0:0,1:0,2:0};
    $scope.limit = 4;//7//10//limit of services
    $scope.sSearch = null;
    $scope.bAdmin = AdminService.isAdmin();
    $scope.bShowExtSearch = false;
    $scope.operators = [];
    $scope.showOnlyOnline = false;
    $scope.selectedStatus = -1;//select all services
    console.log($scope.catalog);

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

    $scope.filterByServiceStatus = function (status){
        console.log(status);
        if (!!catalog && !!status){
            $scope.catalog = catalog;
            if (status == -1) {
                return;
            }

            var ctlg = jQuery.extend(true, {}, $scope.catalog);
            angular.forEach(ctlg, function(item)
            {
                angular.forEach(item.aSubcategory, function(subItem)
                {
                    subItem.aService = $filter('filter')(subItem.aService, {nStatus: status});
                });
            });
            $scope.catalog = ctlg;
        }
    };


     $scope.$watch('catalog', function(newValue)
     {
         $timeout(function()
         {
             if ($scope.bShowExtSearch == false) {
                 $scope.catalogCounts = {0:0,1:0,2:0};
             }
             $scope.operators = [];
             angular.forEach(newValue, function(item)
             {
                 angular.forEach(item.aSubcategory, function(subItem)
                 {
                     angular.forEach(subItem.aService, function(aServiceItem)
                     {
                         if ($scope.bShowExtSearch == false){
                             $scope.catalogCounts[aServiceItem.nStatus] ++ ;
                         }
                         $scope.operators.push(aServiceItem);
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