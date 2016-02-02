'use strict';

angular.module('dashboardJsApp').service('identityUser', function ($q, $http) {
  this.getUserInfo = function (userID) {
    return $http.get('./api/user/' + userID).then(function (response) {
      return response.data;
    });
  };
});
