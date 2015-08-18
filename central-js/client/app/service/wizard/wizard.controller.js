/*angular.module('app').controller('ServiceBuiltInController', function($location, $state, $rootScope, $scope) {
  $scope.$location = $location;
  $scope.$state = $state;
});
*/

angular.module('app').controller('WizardController', function($state, AdminService, $rootScope, $scope, $location, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService, service, regions) {

  var self = this;

  self.isStep2 = self.isStep2 || false;

  $scope.service = service;
  $scope.regions = regions;
  $scope.bAdmin = AdminService.isAdmin();

  $scope.getStateName = function() {
    return $state.current.name;
  };

  var curState = $scope.getStateName();

  console.log('Wizard, state name = ', curState);

  var stateStartupFunction = {
    'index.service.general.city.built-in': function($location, $state, $rootScope, $scope) {
      $scope.$location = $location;
      $scope.$state = $state;
      self.isStep2 = true;
    }
  };

  if (stateStartupFunction[curState]) {
    stateStartupFunction[curState].call(self, $location, $state, $rootScope, $scope);
  } else {
    // default startup
    $scope.$location = $location;
    $scope.$state = $state;
  }

  $scope.step1 = function() {
    self.isStep2 = false;
    // FIXME
    // if (byState('index.service.general.city')) {
    //   return $state.go('index.service.general.city', {
    //     id: $scope.service.nID
    //   });
    // }
  };

  $scope.step2 = function() {
    var aServiceData = $scope.service.aServiceData;

    // console.log('step 2:');
    self.isStep2 = true;
  };

  $scope.makeStep = function(stepId) {
    if (stepId) {
      if (stepId === 'editStep') {
        self.isStep2 = false;
      }
    }
  };

  $scope.onPlaceChange = function(serviceType, placeData) {
    var map = {
      // Сервіс за посиланням
      1: 'index.service.general.city.link',
      // Вбудований сервіс
      4: 'index.service.general.city.built-in',
      // Помилка - сервіс відсутній
      0: 'index.service.general.city.error'
    };

    var state = map[serviceType.nID];
    console.log('onPlaceChange:', state);

    if (state && placeData.city) {
      self.isStep2 = true;
      // console.log('go state:', state);
      $state.go(state, {
        id: $scope.service.nID
      }, {
        location: false
      }).then(function() {
        self.isStep2 = true;
      });
    }
  };

  $scope.ngIfStep2 = function() {
    return self.isStep2;
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