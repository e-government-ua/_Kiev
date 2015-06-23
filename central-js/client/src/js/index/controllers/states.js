define('state/index/controller', ['angularAMD', 'service'], function(angularAMD) {
  angularAMD.controller('IndexController', function($scope, $rootScope, $timeout, CatalogService, catalog, AdminService, $filter) {
    $scope.catalog = catalog;
      $scope.catalogCounts = {0:0,1:0,2:0};
    $scope.limit = 4;//7//10//limit of services
    $scope.sSearch = null;
    $scope.bAdmin = AdminService.isAdmin();
    $scope.bShowExtSearch = false;
    $scope.operators = [];
    $scope.selectedStatus = -1;//select all services
    $scope.operator = -1;
    $scope.sourceCatalog = catalog;

    $scope.search = function() {
      return CatalogService.getServices($scope.sSearch).then(function(result) {
        $scope.catalog = result;
        $scope.sourceCatalog = result;
          if ($scope.bShowExtSearch){
              $scope.filterByExtSearch();
          }
      });
    };

    $scope.onExtSearchClick = function(){
        $scope.bShowExtSearch = !$scope.bShowExtSearch;
        if (!$scope.bShowExtSearch){
            $scope.selectedStatus = -1;
            $scope.operator = -1;
            $scope.catalog = $scope.sourceCatalog;
        }
    };

    $scope.filterByExtSearch = function(){
      if (!!$scope.sourceCatalog && $scope.bShowExtSearch){
          $scope.catalog = $scope.sourceCatalog;
          var filterCriteria = {};
          if ($scope.selectedStatus !=-1){
              filterCriteria.nStatus = $scope.selectedStatus;
          }
          if ($scope.operator != -1){
              filterCriteria.sSubjectOperatorName = $scope.operator;
          }
          var ctlg = jQuery.extend(true, {}, $scope.catalog);
          angular.forEach(ctlg, function(item)
          {
              angular.forEach(item.aSubcategory, function(subItem)
              {
                  subItem.aService = $filter('filter')(subItem.aService, filterCriteria);
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
                 $scope.operators = [];
             }

             angular.forEach(newValue, function(item)
             {
                 angular.forEach(item.aSubcategory, function(subItem)
                 {
                     angular.forEach(subItem.aService, function(aServiceItem)
                     {
                         if ($scope.bShowExtSearch == false){
                             $scope.catalogCounts[aServiceItem.nStatus] ++ ;
                             var found = false;
                             for (var i = 0 ; i < $scope.operators.length ; i++){
                                 if ($scope.operators[i].sSubjectOperatorName === aServiceItem.sSubjectOperatorName){
                                     found = true;
                                     break;
                                 }
                             }
                             if (!found){
                                 $scope.operators.push(aServiceItem);
                             }
                         }
                     });
                 });

                 item.aSubcategory = jQuery.grep(item.aSubcategory, function( sc ) {
                    return sc.aService.length > 0;
                 });

                 if (item.aSubcategory.length == 0){
                    item.sName = "";
                 }
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