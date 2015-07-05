angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('index.service.general.region.link', {
      url: '/link',
      views: {
        'content@service.general.region': {
          templateUrl: 'app/service/region/link/index.html',
          controller: 'ServiceLinkController'
        }
      }
    });
});

