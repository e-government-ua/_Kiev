angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('index.service.general.city.link', {
      url: '/link',
      views: {
        'status@service.general.city': {
          templateUrl: 'app/service/city/link/index.html',
          controller: 'ServiceLinkController'
        }
      }
    });
});

