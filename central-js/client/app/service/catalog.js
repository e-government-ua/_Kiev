angular.module('app').service('CatalogService', ['$http', function ($http) {
  var servicesCache = {};
  this.getModeSpecificServices = function (asIDPlacesUA, sFind) {
    var asIDPlaceUA = asIDPlacesUA && asIDPlacesUA.length > 0 ? asIDPlacesUA.reduce(function (ids, current, index) {
      return ids + ',' + current;
    }) : null;

    var data = {
      asIDPlaceUA: asIDPlaceUA,
      sFind: sFind || null
    };
    return $http.get('./api/services', {
      params: data,
      data: data
    }).then(function (response) {
      servicesCache = response.data;
      return response.data;
    });
  };

  this.getServices = function (sFind) {
    var data = {
      sFind: sFind || null
    };
    return $http.get('./api/services', {
      params: data,
      data: data
    }).then(function (response) {
      servicesCache = response.data;
      return response.data;
    });
  };

  this.getCatalogCounts = function(catalog) {
    var catalogCounts = {'0': 0, '1': 0, '2': 0};
    if (catalog === undefined) {
      catalog = servicesCache;
    }

    angular.forEach(catalog, function(category) {
      angular.forEach(category.aSubcategory, function(subItem) {
        angular.forEach(subItem.aService, function(aServiceItem) {
          if (typeof (catalogCounts[aServiceItem.nStatus]) == 'undefined') {
            catalogCounts[aServiceItem.nStatus] = 0;
          }
          ++catalogCounts[aServiceItem.nStatus];
        });
      });
    });
    return catalogCounts;
  };
  this.getOperators = function(catalog) {
    var operators = [];
    if (catalog === undefined) {
      catalog = servicesCache;
    }
    angular.forEach(catalog, function(category) {
      angular.forEach(category.aSubcategory, function(subCategory) {
        angular.forEach(subCategory.aService, function(aServiceItem) {
          var found = false;
          for (var i = 0; i < operators.length; ++i) {
            if (operators[i].sSubjectOperatorName === aServiceItem.sSubjectOperatorName) {
              found = true;
              break;
            }
          }
          if (!found && aServiceItem.sSubjectOperatorName != "") {
            operators.push(aServiceItem);
          }
        });
      });
    });
    return operators;
  }
}]);
