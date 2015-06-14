'use strict';
angular.module('dashboardJsApp', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngRoute',
  'ngIdle',
  'ngStorage',
  'ui.bootstrap',
  'ui.uploader',
  'ui.event',
  'angularMoment',
  'ngClipboard'
]).config(function($routeProvider, $locationProvider) {
  $routeProvider
    .otherwise({
      redirectTo: '/'
    });

  $locationProvider.html5Mode(true);
}).run(function(amMoment) {
  amMoment.changeLocale('uk');
});