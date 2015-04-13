'use strict';

angular.module('dashboardJsApp')
  .config(function($routeProvider) {
    $routeProvider
      .when('/login', {
        templateUrl: 'app/account/login/login.html',
        controller: 'LoginCtrl'
      });
  });