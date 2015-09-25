angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('index.service.general.placefix', {
      url: '/placefix',
      views: {
        'content@index.service.general.placefix': {
          templateUrl: 'app/service/placefix/content.html'
        },
        'content@index.service': {
          templateUrl: 'app/service/placefix/content.html',
          controller: 'PlaceFixController'
        },
        'main@': {
          templateUrl: 'app/service/index.html',
          controller: 'PlaceFixController'
        }
      }
    })
    .state('index.service.general.placefix.error', {
      url: '/absent',
      views: {
        'content@index.service.general.placefix': {
          templateUrl: 'app/service/placefix/absent.html',
          controller: 'PlaceFixAbsentController'
        }
      }
    })
    .state('index.service.general.placefix.link', {
      url: '/link',
      views: {
        'status@index.service.general.placefix': {
          templateUrl: 'app/service/placefix/link.html',
          controller: 'PlaceFixController'
        }
      }
    });
});