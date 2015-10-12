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
  'dialogs.main',
  'pascalprecht.translate',
  'dialogs.default-translations']);

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

// uirouter debug
    // FIXME debug only, remove before deploy
    // Credits: Adam's answer in http://stackoverflow.com/a/20786262/69362
    // var $rootScope = angular.element(document.querySelectorAll('[ui-view]')[0]).injector().get('$rootScope');
    // console.log('After state handler:');
    // $rootScope = angular.element(document).scope();

    $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
      console.log('>>>>> $stateChangeStart to ' + toState.to + '- fired when the transition begins. toState,toParams : \n', toState, toParams);
    });

    $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams) {
      console.log('>>>>> $stateChangeError - fired when an error occurs during transition.');
      console.log(arguments);
    });

    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
      console.log('>>>>> $stateChangeSuccess to ' + toState.name + '- fired once the state transition is complete.');
    });

    $rootScope.$on('$viewContentLoaded', function(event) {
      console.log('>>>>> $viewContentLoaded - fired after dom rendered', event);
    });

    $rootScope.$on('$stateNotFound', function(event, unfoundState, fromState, fromParams) {
      console.log('>>>>> $stateNotFound ' + unfoundState.to + '  - fired when a state cannot be found by its name.');
      console.log(unfoundState, fromState, fromParams);
    });

});


