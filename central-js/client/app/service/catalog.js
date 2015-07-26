angular.module('app').service('CatalogService', function ($http) {
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
      return response.data;
    });
  };
});
