angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('index.service.general.city.link', {
      url: '/link',
      views: {
        'status@service.general.city': {
          templateUrl: 'html/service/city/link/index.html',
          controller: 'ServiceLinkController'
        }
      }
    });
});

