// country

// angular.module('app').config(function($stateProvider) {
//   $stateProvider
//     .state('index.service.general.placefix.link', {
//       url: '/link',
//       views: {
//         'content@index.service.general.placefix': {
//           templateUrl: 'app/service/placefix/link/index.html',
//           controller: 'PlaceFixController'
//           // controller: 'ServiceLinkController'
//         }
//       }
//     });
// });

// end country

// region

angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('index.service.general.placefix.link', {
      url: '/link',
      views: {
        'content@index.service.general.placefix': {
          templateUrl: 'app/service/placefix/link/index.html',
          controller: 'ServiceLinkController'
        }
      }
    });
});

// end region