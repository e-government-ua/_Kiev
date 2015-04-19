'use strict';

angular.module('dashboardJsApp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'ngIdle',
    'ui.bootstrap',
    'angularMoment'
  ])
  .config(function($routeProvider, $locationProvider) {
    $routeProvider
      .otherwise({
        redirectTo: '/'
      });

    $locationProvider.html5Mode(true);
  })
  .run(function(amMoment) {
    amMoment.changeLocale('uk');
  })
;
