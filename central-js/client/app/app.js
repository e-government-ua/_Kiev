'use strict';

angular.module('appBoilerPlate', ['ngCookies',
  'ngResource',
  'ngSanitize',
  'ui.router',
  'ui.bootstrap',
  'ngMessages',
  'ui.uploader',
  'ui.event',
  'ngClipboard'])

angular.module('documents', ['appBoilerPlate']);
angular.module('journal', ['appBoilerPlate']);
angular.module('order', ['appBoilerPlate']);
angular.module('about', ['appBoilerPlate']);

angular.module('app', [
  'documents',
  'journal',
  'order',
  'about'
]).config(function ($urlRouterProvider, $locationProvider) {
  $urlRouterProvider.otherwise('/');
  $locationProvider.html5Mode(true);
}).run(function ($rootScope, $state) {
  $rootScope.state = $state;
});


