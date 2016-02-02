'use strict';

angular.module('appBoilerPlate', ['ngCookies',
  'ngResource',
  'ngSanitize',
  'ui.router',
  'ui.bootstrap',
  'ngMessages',
  'ui.uploader',
  'ui.event',
  'angularMoment',
  'ngClipboard',
  'ngJsonEditor',
  'dialogs.main',
  'pascalprecht.translate',
  'dialogs.default-translations',
  'textAngular']);

angular.module('documents', ['appBoilerPlate']);
angular.module('auth', ['appBoilerPlate']);
angular.module('journal', ['appBoilerPlate']);
angular.module('order', ['appBoilerPlate']);
angular.module('about', ['appBoilerPlate']);
angular.module('feedback', ['appBoilerPlate']);

angular.module('app', [
  'documents',
  'auth',
  'journal',
  'order',
  'about',
  'feedback'
]).config(function ($urlRouterProvider, $locationProvider) {
  $urlRouterProvider.otherwise('/');
  $locationProvider.html5Mode(true);
}).run(function ($rootScope, $state) {
  $rootScope.state = $state;
  $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error) {
     console.error('stateChangeError', error);
     //TODO: Заменить на нормальный див-диалог из ErrorFactory
     alert("Невідома помилка: " + error);
  });
});


