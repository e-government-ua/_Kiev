angular.module('app').controller('ServiceController', function($scope, $rootScope, $timeout, CatalogService, AdminService, $filter) {
  $scope.catalog = CatalogService.getServices().finally(function(){
      $scope.spinner = false; // Hide a loading spinner
  });

  $scope.catalogCounts = {0: 0, 1: 0, 2: 0};
  $scope.limit = 4;
  $scope.sSearch = null;
  $scope.bAdmin = AdminService.isAdmin();
  $scope.recalcCounts = true;
  $scope.bShowExtSearch = false;
  $scope.operators = [];
  $scope.selectedStatus = -1;
  $scope.operator = -1;
  $scope.sourceCatalog = $scope.catalog;
  $scope.spinner = true;

  $scope.search = function() {
    return CatalogService.getServices($scope.sSearch).then(function(result) {
      $scope.catalog = result;
      $scope.sourceCatalog = result;
      if ($scope.bShowExtSearch) {
        $scope.filterByExtSearch();
      }
    });
  };

  $scope.onExtSearchClick = function() {
    $scope.bShowExtSearch = !$scope.bShowExtSearch;
    if (!$scope.bShowExtSearch) {
      clearExtSearchDialog();
    }
  };

  $scope.filterByStatus = function(status) {
    //close extended search dialog if opened
    $scope.bShowExtSearch = false;
    clearExtSearchDialog();
    var ctlg = jQuery.extend(true, {}, $scope.catalog);
    angular.forEach(ctlg, function(item) {
      angular.forEach(item.aSubcategory, function(subItem) {
        subItem.aService = $filter('filter')(subItem.aService, {nStatus: status});
      });
    });

    $scope.recalcCounts = false;
    $scope.catalog = ctlg;
  };

  $scope.filterByExtSearch = function() {
    if (!!$scope.sourceCatalog && $scope.bShowExtSearch) {
      $scope.catalog = $scope.sourceCatalog;
      var filterCriteria = {};
      if ($scope.selectedStatus != -1) {
        filterCriteria.nStatus = $scope.selectedStatus;
      }
      if ($scope.operator != -1) {
        filterCriteria.sSubjectOperatorName = $scope.operator;
      }
      var ctlg = jQuery.extend(true, {}, $scope.catalog);
      angular.forEach(ctlg, function(item) {
        angular.forEach(item.aSubcategory, function(subItem) {
          subItem.aService = $filter('filter')(subItem.aService, filterCriteria);
        });
      });
      $scope.catalog = ctlg;
    }
  };

  $scope.$watch('catalog', function(newValue) {
    $timeout(function() {
      if ($scope.bShowExtSearch == false) {
        if ($scope.recalcCounts == true) {
          $scope.catalogCounts = {0: 0, 1: 0, 2: 0};
        }

        $scope.operators = [];
      }

      angular.forEach(newValue, function(item) {
        angular.forEach(item.aSubcategory, function(subItem) {
          angular.forEach(subItem.aService, function(aServiceItem) {
            if ($scope.bShowExtSearch == false) {
              if ($scope.recalcCounts == true) {
                $scope.catalogCounts[aServiceItem.nStatus]++;
              }

              var found = false;
              for (var i = 0; i < $scope.operators.length; i++) {
                if ($scope.operators[i].sSubjectOperatorName === aServiceItem.sSubjectOperatorName) {
                  found = true;
                  break;
                }
              }
              if (!found) {
                $scope.operators.push(aServiceItem);
              }
            }
          });
        });
        if (item.aSubcategory) {
          item.aSubcategory = jQuery.grep(item.aSubcategory, function(sc) {
            return sc.aService.length > 0;
          });

          if (item.aSubcategory.length == 0) {
            item.sName = "";
          }
        }
      });
      $scope.recalcCounts = true;
    });
  });

  function clearExtSearchDialog () {
    $scope.selectedStatus = -1;
    $scope.operator = -1;
    $scope.catalog = $scope.sourceCatalog;
  }

  $scope.search();
});