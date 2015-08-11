'use strict';

angular.module('dashboardJsApp')
  .config(function ($routeProvider) {
    $routeProvider
      .when('/services', {
        templateUrl: 'app/services/services.html',
        controller: 'ServicesCtrl'
      });
  });
