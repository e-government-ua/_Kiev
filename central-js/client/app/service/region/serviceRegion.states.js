angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('index.service.general.region', {
      url: '/region',
      resolve: {
        regions: function($stateParams, PlacesService, service) {
          return PlacesService.getRegions().then(function(response) {
            var regions = response.data;
            var aServiceData = service.aServiceData;

            angular.forEach(regions, function(region) {
              var color = 'red';
              angular.forEach(aServiceData, function(oServiceData) {
                if (oServiceData.hasOwnProperty('nID_Region') == false) {
                  return;
                }
                var oRegion = oServiceData.nID_Region;
                if (oRegion.nID == region.nID) {
                  color = 'green';
                }
              });
              region.color = color;
            });

            return regions;
          });
        }
      },
      views: {
        'main@': {
          templateUrl: 'app/service/region/index.html',
          controller: 'ServiceRegionController'
        },
        'content@index.service.general.region': {
          templateUrl: 'app/service/region/content.html'
        }
      }
    })
    .state('index.service.general.region.error', {
      url: '/absent',
      views: {
        'content@index.service.general.region': {
          templateUrl: 'app/service/region/absent.html'
        }
      }
    });
});

