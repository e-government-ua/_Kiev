'use strict';
angular.module('app', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ui.router',
  'ui.bootstrap',
  'ngMessages',
  'ui.uploader',
  'ui.event',
  'ngClipboard',
  'journal',
  'documents',
  'order',
  'about'
]).config(function($urlRouterProvider, $locationProvider) {
  $urlRouterProvider.otherwise('/');
  $locationProvider.html5Mode(true);
}).run(function($rootScope, $state) {
  $rootScope.state = $state;
});


