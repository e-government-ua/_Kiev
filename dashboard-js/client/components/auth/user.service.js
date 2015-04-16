'use strict';

angular.module('dashboardJsApp')
  .factory('User', function($resource) {
    var data = $resource('/service/identity/users/:user', {
      user: "@user",
      size: 100
    });
    return data;
  });