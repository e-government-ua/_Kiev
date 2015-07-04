'use strict';
angular.module('app', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ui.router',
  'ui.bootstrap',
  'ngMessages',
  //'ui.event',
  'ngClipboard'
]).config(function($stateProvider, $urlRouterProvider, $locationProvider) {
  $urlRouterProvider.when('', function($match, $state) {
    $state.transitionTo('index', $match, false);
  });
  $urlRouterProvider.otherwise('/404');
  $locationProvider.html5Mode(true);
});
