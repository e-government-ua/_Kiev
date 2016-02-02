angular.module('app')
  .service('CatalogService', ['$http', '$q', function ($http, $q) {

  var servicesCache = {};
  this.getModeSpecificServices = function (asIDPlacesUA, sFind, bShowEmptyFolders) {
    var asIDPlaceUA = asIDPlacesUA && asIDPlacesUA.length > 0 ? asIDPlacesUA.reduce(function (ids, current, index) {
      return ids + ',' + current;
    }) : null;

    var data = {
      asIDPlaceUA: asIDPlaceUA,
      sFind: sFind || null,
      bShowEmptyFolders: bShowEmptyFolders
    };
    return $http.get('./api/catalog', {
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
    return $http.get('./api/catalog', {
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
  };

  function simpleHttpPromise(req, callback) {
    var cb = callback || angular.noop;
    var deferred = $q.defer();

    $http(req).then(
      function(response) {
        deferred.resolve(response.data);
        return cb();
      },
      function(response) {
        deferred.reject(response);
        return cb(response);
      }.bind(this));
    return deferred.promise;
  }

  this.setServicesTree = function(data, callback){
    var request = {
      method: 'POST',
      url: '/api/catalog',
      data: data
    };

    return simpleHttpPromise(request, callback);
  };

  var del = function(path, nID, bRecursive, callback){
    var request = {
      method: 'DELETE',
      url: path,
      params: {
        nID: nID,
        bRecursive: bRecursive
      }
    };

    return simpleHttpPromise(request, callback);
  };

  this.removeCategory = function(nID, bRecursive, callback){
    return del('/api/catalog/category', nID, bRecursive, callback);
  };

  this.removeSubcategory = function(nID, bRecursive, callback){
    return del('/api/catalog/subcategory', nID, bRecursive, callback);
  };

  this.removeService = function(nID, bRecursive, callback){
    return del('/api/catalog/service', nID, bRecursive, callback);
  };

  this.removeServicesTree = function(nID_Subject, callback){
    return del('/api/catalog/servicesTree', undefined, undefined, nID_Subject, callback);
  };
}]);
