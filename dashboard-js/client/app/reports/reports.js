'use strict';

angular.module('dashboardJsApp')
  .config(function ($routeProvider) {
    $routeProvider
      .when('/reports', {
        templateUrl: 'app/reports/reports.html',
        controller: 'ReportsCtrl'
      });
  });
