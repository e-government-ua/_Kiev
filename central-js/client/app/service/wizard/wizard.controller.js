/*angular.module('app').controller('ServiceBuiltInController', function($location, $state, $rootScope, $scope) {
  $scope.$location = $location;
  $scope.$state = $state;
});
*/

angular.module('app').controller('WizardController', function($state, AdminService, $rootScope, $scope, $location, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService, service, regions) {

  console.log('Wizard: state name = ', $scope.state.current.name);

  $scope.service = service;
  $scope.regions = regions;
  $scope.bAdmin = AdminService.isAdmin();

  $scope.getStateName = function() {
    return $state.current.name;
  };

  var isStep2 = false;

  $scope.step1 = function() {

    var isStep2 = false;

    // FIXME
    // if (byState('index.service.general.city')) {
    //   return $state.go('index.service.general.city', {
    //     id: $scope.service.nID
    //   });
    // }
  };


  $scope.step2 = function() {
    var aServiceData = $scope.service.aServiceData;
    var serviceType = $scope.findServiceDataByCity();

    switch (serviceType.nID) {
      case 1:
        return $state.go('index.service.general.city.link', {
          id: $scope.service.nID
        }, {
          location: false
        });
      case 4:
        return $state.go('index.service.general.city.built-in', {
          id: $scope.service.nID
        }, {
          location: false
        });
      default:
        return $state.go('index.service.general.city.error', {
          id: $scope.service.nID
        }, {
          location: false
        });
    }
  };

  $scope.makeStep = function(stepId) {
    if (stepId) {
      if (stepId === 'editStep') {
        console.log('edit step > 2 > 1');
        isStep2 = false;
      }
    }
  };

  $scope.onPlaceChange = function(serviceType, placeData) {
    // FIXME:

    function goState(serviceType) {
      var map = {
        // Сервіс за посиланням
        1: 'index.service.general.city.link',
        // Вбудований сервіс
        4: 'index.service.general.city.built-in',
        // Помилка - сервіс відсутній
        0: 'index.service.general.city.error'
      };

      var state = map[serviceType.nID];
      console.log('onPlaceChange:', serviceType, state, placeData);

      if ( state && placeData.city) {
        console.log('go state:', serviceType);
        $state.go(state, {
          id: $scope.service.nID
        }, {
          location: false
        }).then(function() {
          isStep2 = true;
        });
      }
    }

    goState(serviceType);

  };

  $scope.ngIfStep2 = function() {
    // console.log('isStep2: ', isStep2);
    return isStep2;
  };

  /* Moved to place.controller:
    $scope.loadRegionList = function(search) { };
    $scope.onSelectRegionList = function($item, $model, $label) {};
    $scope.loadLocalityList = function(search) {};
    $scope.onSelectLocalityList = function($item, $model, $label) { };
    $scope.ngIfCity = function() {};
    $scope.getRegionId = function() {};
    $scope.getCityId = function() {};
    $scope.findServiceDataByRegion = function() {};
    $scope.findServiceDataByCity = function() {};
  */

});


// FIXME
angular.module('app').controller('BuiltinCityController', function($scope, service) {
  $scope.service = service;
});

/*
angular.module('app').controller('ServiceRegionController', function($state, $rootScope, $scope, $sce, RegionListFactory, PlacesService, ServiceService, service, regions, AdminService) {
  !!!
  $scope.onSelectRegionList = function($item, $model, $label) {
    $scope.data.region = $item;
    $scope.regionList.select($item, $model, $label);
  };
});
*/