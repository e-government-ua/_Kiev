angular.module('app').controller('PlaceFixController', function(
  $state, AdminService, $rootScope, $scope, $location, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService) {

  // Each controller which uses Places Control should tell it:

  // FIXME: preload regions and service and provide them as part of the locations service

  PlacesService.setController(this);

  var self = this;

  var statesMap = {
    'index.service.general.city.built-in': {
      startupFunction: function(iPlaceController, $location, $state, $rootScope, $scope, placeCtrl) {
        $scope.$location = $location;
        iPlaceController.isStep2 = true;
      },
      viewClass: 'state-disabled'
    },
    'index.service.general.city.built-in.bankid.submitted': {
      startupFunction: function(iPlaceController, $location, $state, $rootScope, $scope, placeCtrl) {
        $scope.collapse();
        $scope.state = $state; //.get('index.service.general.placefix.built-in.bankid.submitted');
      },
      viewClass: 'state-collapsed'
    }
  };

  self.getServiceAvailability = function(service) {
    // FIXME move to use ServiceService.oService everywhere instead of $scope.service
    var oService = service || ServiceService.oService;
    var result = {
      isCity: false,
      isRegion: false
    };
    angular.forEach(oService.aServiceData, function(oServiceData) {
      if (oServiceData.nID_City && oServiceData.nID_City.nID !== null) {
        result.isCity = true;
      }
      if (oServiceData.nID_Region && oServiceData.nID_Region.nID !== null) {
        result.isRegion = true;
      }
    });
    console.log('getServiceAvailability, iD:', oService.nID, result);
    return result;
  };

  // oParams = { placeData: placeData, serviceData: serviceData } }
  self.processPlaceChange = function(oParams) {

    var oService = ServiceService.oService;

    // due to availiablity in city / region, do next steps
    // var oAvailable = self.getServiceAvailability( oService );
    // if ( !oAvailable.isRegion && !oAvailable.isCity ) { }

    var stateByServiceType = {
      // Сервіс за посиланням
      1: 'index.service.general.placefix.link',
      // Вбудований сервіс
      4: 'index.service.general.placefix.built-in',
      // Помилка - сервіс відсутній
      0: 'index.service.general.placefix.error'
    };

    var state = stateByServiceType[oParams.serviceData.nID_ServiceType.nID];

    console.log('PlaceFix Controller. On Place сhange, state = ', state, 'oParams =', oParams );
    // FROM COUNTRY CTRL

    // var aServiceData = ServiceService.oService.aServiceData;

    // obtain service data and it's notes
    // angular.forEach(aServiceData, function(value, key) {
    //   $scope.serviceData = value;
    //   $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
    // });

    // FROM COUNTRY

    // STOPPEDHERE

    // FIXME FIXME generalize it: if cur state is step 1 and it's complete, go to step 2 and change state
    // if (state && oParams.placeData.city) {
    if (state && oParams.placeData) {
      self.isStep2 = true;
      // console.log('go state:', state);
      $state.go(state, {
        id: oService.nID
      }, {
        location: false
      }).then(function() {
        self.isStep2 = true;
      });
    }
  };

  // FIXME create sequencer 
  // moved back from places service
  self.isStep2 = self.isStep2 || false;

  $scope.bAdmin = AdminService.isAdmin();
  $scope.$state = $state;

  $scope.getStateName = function() {
    return $state.current.name;
  };

  $scope.getRegionId = function() {
    var place = PlacesService.getPlaceData();
    var region = place ? place.region || null : null;
    return region ? region.nID : 0;
  };

  $scope.getCityId = function() {
    var place = PlacesService.getPlaceData();
    var city = place ? place.city || null : null;
    return city ? city.nID : 0;
  };

  // FIXME remove state dependency
  var curState = $scope.getStateName();

  console.log('PlaceFix Controller. $state name =', $state.current.name);

  if (statesMap[curState] && statesMap[curState].startupFunction) {
    statesMap[curState].startupFunction.call(self, $location, $state, $rootScope, $scope, self.placeCtrl);
  } else {
    // default startup
    $scope.$location = $location;
    $scope.$state = $state;
  }

  $scope.getHtml = function(html) {
    return $sce.trustAsHtml(html);
  };

  $scope.step1 = function() {
    self.isStep2 = false;
    // FIXME
    // if (byState('index.service.general.placefix')) {
    //   return $state.go('index.service.general.placefix', {
    //     id: ServiceService.oService.nID
    //   });
    // }
  };

  $scope.step2 = function() {
    var aServiceData = ServiceService.oService.aServiceData;

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

  /**
   * params: placeData, serviceData
   */
  $scope.$on('onPlaceChange', function(evt, oParams) {
    self.processPlaceChange(oParams);
  });

  $scope.ngIfStep2 = function() {
    return self.isStep2;
  };

  // FIXME
  self.processPlaceChange({
    serviceData: PlacesService.getServiceDataForSelectedPlace(),
    placeData: PlacesService.getPlaceData()
  });

  /*
  angular.module('app').controller('ServiceBuiltInController', function($location, $state, $rootScope, $scope) {
    $scope.$location = $location;
    $scope.$state = $state;
  });
  */

});

// FIXME this is from country/script.js
angular.module('app').config(function($stateProvider) {
  // FIXME REMOVE IT ALL
  $stateProvider
    .state('index.service.general.placefix', {
      url: '/country',
      // resolve: {
      //   service: function($stateParams, ServiceService) {
      //     return ServiceService.get($stateParams.id);
      //   }
      // },
      views: {
        'main@': {
          templateUrl: 'app/service/index.html',
          controller: 'PlaceFixController'
            // controller: 'ServiceC ountryController'
        }
      }
    }).state('index.service.general.placefix.error', {
      url: '/absent',
      views: {
        'content@index.service.general.placefix': {
          templateUrl: 'app/service/placefix/absent.html',
          controller: 'PlaceFixController'
            // controller: 'ServiceC ountryAbsentController'
        }
      }
    });
});


/*
angular.module('app').controller('ServiceRegionController', function($state, $rootScope, $scope, $sce, RegionListFactory, PlacesService, ServiceService, service, AdminService) {
  
  // FIXME - code moved to place.js
  $scope.onSelectRegionList = function($item, $model, $label) {
    $scope.data.region = $item;
    $scope.regionList.select($item, $model, $label);
  };
});
*/