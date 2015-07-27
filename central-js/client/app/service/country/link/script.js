angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('index.service.general.country.link', {
      url: '/link',
      views: {
        'content@index.service.general.country': {
          templateUrl: 'app/service/country/link/index.html',
          controller: 'ServiceLinkController'
        }
      }
    });
});

