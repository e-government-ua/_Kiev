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
  'service',
  'journal',
  'documents',
  'about'
]).config(function($stateProvider, $urlRouterProvider, $locationProvider) {

  $stateProvider
    .state('index', {
      url: '/',
      abstract: true,
      views: {
        header: {
          templateUrl: 'app/header/header.html'
        },
        footer: {
          templateUrl: 'app/footer/footer.html'
        }
      }
    });
  $urlRouterProvider.otherwise('/index');
  $locationProvider.html5Mode(true);
}).run(function($rootScope, $state) {
  $rootScope.state = $state;
});


