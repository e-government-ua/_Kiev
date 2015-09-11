// region - moved from service\region\serviceRegion.states.js

angular.module('app').config(function($stateProvider) {
  $stateProvider
  // .state('index.service.general.placefix', { // country
  //   url: '/placefix',
  //   views: {
  //     'main@': {
  //       templateUrl: 'app/service/index.html',
  //       controller: 'PlaceFixController'
  //         // controller: 'ServiceC ountryController'
  //     }
  //   }
  // })
  // .state('index.service.general.placefix.error', { // country
  //   url: '/absent',
  //   views: {
  //     'content@index.service.general.placefix': {
  //       templateUrl: 'app/service/placefix/absent.html',
  //       controller: 'PlaceFixController'
  //         // controller: 'ServiceC ountryAbsentController'
  //     }
  //   }
  // })
  // .state('index.service.general.placefix', { // region
  //   url: '/placefix',
  //   // FIXME PREVENT Coloring
  //   // FIXME code dup is badbadbad
  //   // resolve: {
  //   //   regions: function($stateParams, PlacesService, service) {
  //   //     return PlacesService.getRegions().then(function(response) {
  //   //       var regions = response.data;
  //   //       var aServiceData = service.aServiceData;

  //   //       angular.forEach(regions, function(region) {
  //   //         var color = 'red';
  //   //         angular.forEach(aServiceData, function(oServiceData) {
  //   //           if (oServiceData.hasOwnProperty('nID_Region') === false) {
  //   //             return;
  //   //           }
  //   //           var oRegion = oServiceData.nID_Region;
  //   //           if (oRegion.nID == region.nID) {
  //   //             color = 'green';
  //   //           }
  //   //         });
  //   //         region.color = color;
  //   //       });
  //   //       return regions;
  //   //     });
  //   //   }
  //   // },
  // })
  // .state('index.service.general.placefix.error', { // region
  //   url: '/absent',
  //   views: {
  //     'content@index.service.general.placefix': {
  //       templateUrl: 'app/service/placefix/absent.html',
  //       controller: 'PlaceFixController'
  //     }
  //   }
  // })
    .state('index.service.general.placefix', { // city - moved from app states
      url: '/placefix',
      // resolve: {
      //   regions: function(PlacesService, service) {
      //     return PlacesService.getRegionsForService(service);
      //   }
      //   ,
      //   // FIXME: Copy-pasting is bad, bad, bad
      //   service: function($stateParams, ServiceService) {
      //     console.log('App.states: calling get service, $stateParams.id =', $stateParams.id);
      //     return ServiceService.get($stateParams.id);
      //   }
      // },

      //   //   regions: function($stateParams, PlacesService, service) {
      //   //     return PlacesService.getRegions().then(function(response) {
      //   //       var regions = response.data;
      //   //       var aServiceData = service.aServiceData;

      //   //       angular.forEach(regions, function(region) {
      //   //         var color = 'red';
      //   //         angular.forEach(aServiceData, function(oServiceData) {
      //   //           if (oServiceData.hasOwnProperty('nID_Region') === false) {
      //   //             return;
      //   //           }
      //   //           var oRegion = oServiceData.nID_Region;
      //   //           if (oRegion.nID == region.nID) {
      //   //             color = 'green';
      //   //           }
      //   //         });
      //   //         region.color = color;
      //   //       });
      //   //       return regions;
      //   //     });
      //   //   }

      views: {
        'content@index.service': {
          // controller: 'ServiceCityController',
          controller: 'PlaceFixController',
          templateUrl: 'app/service/placefix/content.html'
        },
        'main@': {
          templateUrl: 'app/service/placefix/index.html',
          // controller: 'ServiceRegionController'
          controller: 'PlaceFixController'
        }, // merged
        'content@index.service.general.placefix': {
          templateUrl: 'app/service/placefix/content.html'
        }
      }
    })
    .state('index.service.general.placefix.error', {
      url: '/absent',
      views: {
        'status': {
          templateUrl: 'app/service/placefix/absent.html',
          controller: 'ServiceCityAbsentController'
        },
        // regions
        'content@index.service.general.placefix': {
          templateUrl: 'app/service/placefix/absent.html',
          controller: 'PlaceFixController'
        }
      }
    })
    .state('index.service.general.placefix.link', {
      url: '/link',
      views: {
        'status@index.service.general.placefix': {
          templateUrl: 'app/service/placefix/link.html',
          // controller: 'ServiceLinkController'
          controller: 'PlaceFixController'
        }
      }
    });
});