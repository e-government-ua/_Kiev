angular.module('service').config(function($stateProvider) {
  $stateProvider
    .state('index.service.general.region.link', {
      url: '/link',
      views: {
        'content@index.service.general.region': {
          templateUrl: 'app/service/region/link/index.html',
          controller: 'ServiceLinkController'
        }
      }
    });
});

