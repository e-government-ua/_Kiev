angular.module('app').config(function($stateProvider) {
  // FIXME REMOVE IT ALL
  $stateProvider
    .state('index.service.general.country', {
      url: '/country',
      // resolve: {
      //   service: function($stateParams, ServiceService) {
      //     return ServiceService.get($stateParams.id);
      //   }
      // },
      views: {
        'main@': {
          templateUrl: 'app/service/index.html',
          controller: 'WizardController'
          // controller: 'ServiceCountryController'
        }
      }
    }).state('index.service.general.country.error', {
      url: '/absent',
      views: {
        'content@index.service.general.country': {
          templateUrl: 'app/service/country/absent.html',
          controller: 'WizardController'
          // controller: 'ServiceCountryAbsentController'
        }
      }
    });
});