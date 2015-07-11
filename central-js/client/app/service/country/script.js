angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('index.service.general.country', {
      url: '/country',
      views: {
        'main@': {
          templateUrl: 'app/service/country/index.html',
          controller: 'ServiceCountryController'
        }
      }
    }).state('index.service.general.country.error', {
      url: '/absent',
      views: {
        'content@index.service.general.country': {
          templateUrl: 'app/service/country/absent.html',
          controller: 'ServiceCountryAbsentController'
        }
      }
    });
});