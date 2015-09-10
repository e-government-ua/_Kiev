angular.module('app').config(function($stateProvider) {
  // FIXME REMOVE IT ALL
  $stateProvider
    .state('index.service.general.placefix', {
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
          // controller: 'ServiceC ountryController'
        }
      }
    }).state('index.service.general.placefix.error', {
      url: '/absent',
      views: {
        'content@index.service.general.placefix': {
          templateUrl: 'app/service/placefix/absent.html',
          controller: 'WizardController'
          // controller: 'ServiceC ountryAbsentController'
        }
      }
    });
});