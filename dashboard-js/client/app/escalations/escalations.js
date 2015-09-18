'use strict';

angular.module('dashboardJsApp')
  .config(function ($routeProvider) {
    $routeProvider
      .when('/escalations', {
        templateUrl: 'app/escalations/escalations.html',
        controller: 'EscalationsCtrl',
        access: {
          requiresLogin: true
        }
      });
  });