angular.module('service').config(function($stateProvider) {
  $stateProvider
    .state('index.service.general.city.link', {
      url: '/link',
      views: {
        'status@index.service.general.city': {
          templateUrl: 'app/service/city/link/index.html',
          controller: 'ServiceLinkController'
        }
      }
    });
});

