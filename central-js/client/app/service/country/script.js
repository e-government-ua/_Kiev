angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('index.service.general.country', {
      url: '/country',
      resolve: {
        service: function($stateParams, ServiceService) {
          return ServiceService.get($stateParams.id);
        }
      },
      views: {
        'main@': {
          templateUrl: 'app/service/index.html',
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