angular.module('index').service('CatalogService', function($http) {
  this.getServices = function(sFind) {
    var data = {
      'sFind': sFind || null
    };
    return $http.get('./api/services', {
      'params': data,
      'data': data
    }).then(function(response) {
      return response.data;
    });
  };
});