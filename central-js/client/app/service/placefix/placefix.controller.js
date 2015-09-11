angular.module('app').controller('PlaceFixController', function(
  $state, AdminService, $rootScope, $scope, $location, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService) {

  // FIXME: preload regions and service and provide them as part of the locations service

  // Each controller which uses Places Control should tell it:
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

    var stateToGo = stateByServiceType[oParams.serviceData.nID_ServiceType.nID];

    console.log('PlaceFix Controller. On Place сhange, state to go =', stateToGo, 'oParams =', oParams);
    // FROM COUNTRY CTRL

    // obtain service data and it's notes
    angular.forEach(oService.aServiceData, function(service, key) {
      $scope.serviceData = service;
      if (oService.bNoteTrusted === false) {
        $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
        oService.sNoteTrusted = true;
      }
    });

    // END FROM COUNTRY
      
    // moved from place.js processPlaceSelection
    // $scope.serviceData = oService;
    // if (oService.bNoteTrusted === false) {
    //   oService.sNote = $sce.trustAsHtml(oService.sNote);
    //   oService.sNoteTrusted = true;
    // }

    // FIXME FIXME generalize it: if cur state is step 1 and it's complete, go to step 2 and change state
    // if (stateToGo && oParams.placeData.city) {
    console.log('step 2:', self.isStep2);

    if (stateToGo && oParams.placeData) {
      self.isStep2 = true;
      console.log('go state:', stateToGo);
      $state.go(stateToGo, {
        id: oService.nID
      }, {
        location: false
      }).then(function() {
        self.isStep2 = true;
      });
    }
  };

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

  $scope.ngIfStep2 = function() {
    return self.isStep2;
  };

  // FIXME create sequencer 
  // moved back from places service
  self.isStep2 = self.isStep2 || false;

  $scope.bAdmin = AdminService.isAdmin();
  $scope.$state = $state;

  // FIXME remove state dependency
  var curState = $scope.getStateName();

  console.log('PlaceFix Controller. $state name =', $state.current.name);

  if (statesMap[curState] && statesMap[curState].startupFunction) {
    console.log('PlaceFix Controller. call startp function for $state name =', $state.current.name);
    statesMap[curState].startupFunction.call(self, $location, $state, $rootScope, $scope, self.placeCtrl);
  } else {
    // default startup
    $scope.$location = $location;
    $scope.$state = $state;
  }

  /**
   * params: placeData, serviceData
   */
  $scope.$on('onPlaceChange', function(evt, oParams) {
    self.processPlaceChange(oParams);
  });

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


// angular.module('app').controller('ServiceRegionController', function($state, $rootScope, $scope, $sce, RegionListFactory, PlacesService, ServiceService, service, AdminService) {
//   // FIXME - code moved to place.js
//   $scope.onSelectRegionList = function($item, $model, $label) {
//     $scope.data.region = $item;
//     $scope.regionList.select($item, $model, $label);
//   };
// });

// regions - moved from the service\region\controllers\serviceRegion.controller.js

// angular.module('app').controller('ServiceRegionController', function($state, $rootScope, $scope, $sce, RegionListFactory, PlacesService, ServiceService, AdminService, serviceLocationParser) {
//   // FIXME
//   // $scope.regions = regions;

//   $scope.bAdmin = AdminService.isAdmin();

//   // $scope.regionList = new RegionListFactory();
//   // $scope.regionList.initialize(regions);

//   // Each controller which uses Places Control should tell it:
//   // FIXME: preload regions and service and provide them as part of the locations service
//   console.log('RegionController. Reg-s: ', regions);

//   PlacesService.setController(this);

//   // $scope.loadRegionList = function(search) {
//   //   return $scope.regionList.load(service, search);
//   // };

//   // FIXME - code moved to place.js
//   // $scope.onSelectRegionList = function($item) {
//   //   $scope.data.region = $item;
//   //   $scope.regionList.select($item);
//   // };

//   // $scope.data = {
//   //   region: null,
//   //   city: null
//   // };

//   // $scope.step1 = function() {
//   //   $scope.data = {
//   //     region: null,
//   //     city: null
//   //   };
//   //   return $state.go('index.service.general.region', {id: ServiceService.oService.nID});
//   // };

//   // $scope.step2 = function() {
//   //   var aServiceData = ServiceService.oService.aServiceData;
//   //   var serviceType = {nID: 0};
//   //   angular.forEach(aServiceData, function(value, key) {
//   //     if (value.nID_Region.nID == $scope.data.region.nID) {
//   //       serviceType = value.nID_ServiceType;
//   //       $scope.serviceData = value;
//   //       $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
//   //     }
//   //   });

//   //   switch (serviceType.nID) {
//   //     case 1:
//   //       return $state.go('index.service.general.region.link', {id: ServiceService.oService.nID}, {location: false});
//   //     case 4:
//   //       return $state.go('index.service.general.region.built-in', {id: ServiceService.oService.nID}, {location: false});
//   //     default:
//   //       return $state.go('index.service.general.region.error', {id: ServiceService.oService.nID}, {location: false});
//   //   }
//   // };

//   // if ($state.current.name == 'service.general.region.built-in.bankid') {
//   //   return true;
//   // }

//   // $scope.$watchCollection('data.region', function(newValue, oldValue) {
//   //   return (newValue == null) ? null : $scope.step2();
//   // });

//   var initialRegion = serviceLocationParser.getSelectedRegion(regions);
//   if (initialRegion) {
//     $scope.onSelectRegionList(initialRegion);
//   }
// });