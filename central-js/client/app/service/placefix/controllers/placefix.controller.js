angular.module('app').controller('PlaceFixController', function(
  $state, AdminService, $rootScope, $scope, $location, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService, BankIDService, serviceLocationParser, regions, service) {

  // FIXME: preload regions and service and provide them as part of the locations service

  // Each controller which uses Places Control should tell it:
  PlacesService.setController(this);

  var self = this;

  var statesMap = {
    'index.service.general.placefix.built-in': { // city
      startupFunction: function(iPlaceController, $location, $state, $rootScope, placeCtrl) {
        iPlaceController.isStep2 = true;
      },
      viewClass: 'state-disabled'
    },
    'index.service.general.placefix.built-in.bankid.submitted': { // city
      startupFunction: function(iPlaceController, $location, $state, $rootScope, placeCtrl) {
        $scope.collapse();
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

    // FIXME generalize it: if cur state is step 1 and it's complete, go to step 2 and change state
    // if (stateToGo && oParams.placeData.city) {
    // if (stateToGo && oParams.placeData) {
    // console.log('step 2:', self.isStep2);

    $scope.service = service;
    $scope.regions = regions;
    $scope.$location = $location;
    $scope.$state = $state;
    $scope.state = $state.get( $state.current.name );
    
    // FIXME $scope.bankIDAccount = BankIDService.account();

    console.log('initCityService. Reg-s: ', regions.length);

    ///
    // FIXME: other ServiceCityController code was here
    ///

    if (!stateToGo || ($state.current.name === stateToGo)) {
      return;
    }

    self.isStep2 = true;
    $state.go(stateToGo, {
      id: oService.nID
    }, {
      location: false
    }).then(function() {
      self.isStep2 = true;
      
    });

    var initialRegion = serviceLocationParser.getSelectedRegion(regions);
    if (initialRegion) {
      $scope.onSelectRegionList(initialRegion);
    }
  

    console.log('PlaceFixController on Place сhange, $state:', $state.current.name, ', to go:', stateToGo /*, 'oParams =', oParams*/ );
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

    console.log('step 2:');
    self.isStep2 = true;
  };

  $scope.ngIfStep2 = function() {
    // console.log('ngIfStep2 =', self.isStep2);
    // return $scope.place ControlIsComplete();
    return self.isStep2;
  };

  // FIXME create sequencer 
  // moved back from places service
  self.isStep2 = self.isStep2 || false;

  $scope.bAdmin = AdminService.isAdmin();
  $scope.$state = $state;

  // FIXME remove state dependency
  var curState = $scope.getStateName();

  // console.log('PlaceFix Controller. $state name =', $state.current.name);

  if (statesMap[curState] && statesMap[curState].startupFunction) {
    console.log('PlaceFix Controller. call startp function for $state name =', $state.current.name);
    statesMap[curState].startupFunction.call(self, $location, $state, $rootScope, self.placeCtrl);
  } else {
    // default startup
  }

  /**
   * params: placeData, serviceData
   */
  $scope.$on('onPlaceChange', function(evt, oParams) {
    self.processPlaceChange(oParams);
  });

  $scope.$on('onEditPlace', function(evt, oParams) {
    //   $scope.makeStep = function(stepId) {
    // if (stepId) {
    // if (stepId === 'editStep') {
    self.isStep2 = false;
    // }
    // }
  });

  // FIXME
  self.processPlaceChange({
    serviceData: PlacesService.getServiceDataForSelectedPlace(),
    placeData: PlacesService.getPlaceData()
  });

});

// angular.module('app').controller('ServiceRegionController', function($state, $rootScope, $scope, $sce, RegionListFactory, PlacesService, ServiceService, service, AdminService) {
//   // FIXME - code moved to place.js
//   $scope.onSelectRegionList = function($item, $model, $label) {
//     $scope.data.region = $item;
//     $scope.regionList.select($item, $model, $label);
//   };
// });



// regions

// moved from the service\region\controllers\serviceRegion.controller.js

// angular.module('app').controller('ServiceRegionController', function($state, $rootScope, $scope, $sce, RegionListFactory, PlacesService, ServiceService, AdminService, serviceLocationParser) {

//   // $scope.step1 = function() {
//   //   $scope.data = {
//   //     region: null,
//   //     city: null
//   //   };
//   //   return $state.go('index.service.general.placefix', {id: ServiceService.oService.nID});
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
//   //       return $state.go('index.service.general.placefix.link', {id: ServiceService.oService.nID}, {location: false});
//   //     case 4:
//   //       return $state.go('index.service.general.placefix.built-in', {id: ServiceService.oService.nID}, {location: false});
//   //     default:
//   //       return $state.go('index.service.general.placefix.error', {id: ServiceService.oService.nID}, {location: false});
//   //   }
//   // };

//   // $scope.$watchCollection('data.region', function(newValue, oldValue) {
//   //   return (newValue == null) ? null : $scope.step2();
//   // });

// });



// city
// from serviceCity.controller.js
// moved to PlaceFixController

// angular.module('app').controller('ServiceCityController', function($state, AdminService, $rootScope, $scope, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService, regions, service, serviceLocationParser) {

// FIXME - code moved to place.js
//  $scope.onSelectRegionList = function($item) {
//    $scope.data.region = $item;
//    $scope.regionList.select($item);

// var serviceType = $scope.findServiceDataByRegion();

//    switch (serviceType.nID) {
//      case 1:
//        $state.go('index.service.general.placefix.link', {id: ServiceService.oService.nID}, {location: false}).then(function() {
//    isStep2 = true;
//  });
//  break;
//      case 4:
//        $state.go('index.service.general.placefix.built-in', {id: ServiceService.oService.nID}, {location: false}).then(function() {
//    isStep2 = true;
//  });
//  break;
//      default:
//     $scope.localityList.load(ServiceService.oService, $item.nID, null).then(function(cities) {
//          $scope.localityList.typeahead.defaultList = cities;
//          var initialCity = serviceLocationParser.getSelectedCity(cities);
//          if (initialCity)
//            $scope.onSelectLocalityList(initialCity);
//        });
//    }
//  };

// $scope.loadLocalityList = function(search) {
//   return $scope.localityList.load(ServiceService.oService, $scope.data.region.nID, search);
// };

//  $scope.onSelectLocalityList = function($item, $model, $label) {
//    $scope.data.city = $item;
//    $scope.localityList.select($item, $model, $label);
// var serviceType = $scope.findServiceDataByCity();
//    switch (serviceType.nID) {
//      case 1:
//        $state.go('index.service.general.placefix.link', {id: ServiceService.oService.nID}, {location: false}).then(function() {
//    isStep2 = true;
//  });
//  break;
//      case 4:
//        $state.go('index.service.general.placefix.built-in', {id: ServiceService.oService.nID}, {location: false}).then(function() {
//    isStep2 = true;
//  });
//  break;
//      default:
//        $state.go('index.service.general.placefix.error', {id: ServiceService.oService.nID}, {location: false}).then(function() {
//    isStep2 = true;
//  });
//    }
//  };

//  $scope.data = {
//    region: null,
//    city: null
//  };

//  $scope.ngIfCity = function() {
// if($state.current.name === 'index.service.general.placefix.built-in') {
//  if($scope.data.city) {
//    return true;
//  } else {
//    return false;
//  }
// }
// if($state.current.name === 'index.service.general.placefix.built-in.bankid') {
//  if($scope.data.city) {
//    return true;
//  } else {
//    return false;
//  }
// }
// return $scope.data.region ? true: false;
//  };

//  $scope.getRegionId = function() {
// var region = $scope.data.region;
// return region ? region.nID: 0;
//  };

//  $scope.getCityId = function() {
// var city = $scope.data.city;
// return city ? city.nID: 0;
//  };

//  var isStep2 = false;
//  $scope.ngIfStep2 = function() {
// return isStep2;
//  };

//  $scope.findServiceDataByRegion = function() {
// var aServiceData = ServiceService.oService.aServiceData;
// var serviceType = {nID: 0};
//    angular.forEach(aServiceData, function(value, key) {
//      // if service is available 
//      if (value.nID_Region && value.nID_Region.nID == $scope.data.region.nID) {
//        serviceType = value.nID_ServiceType;
//        $scope.serviceData = value;
//  if($scope.serviceData.bNoteTrusted == false) {
//    $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
//    $scope.serviceData.sNoteTrusted = true;
//  }
//      }
//    });
// return serviceType;
//  };

//  $scope.findServiceDataByCity = function() {
// var aServiceData = ServiceService.oService.aServiceData;
// var serviceType = {nID: 0};
//    angular.forEach(aServiceData, function(value, key) {
//      if (value.nID_City && value.nID_City.nID == $scope.data.city.nID) {
//        serviceType = value.nID_ServiceType;
//        $scope.serviceData = value;
//  if($scope.serviceData.bNoteTrusted == false) {
//    $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
//    $scope.serviceData.sNoteTrusted = true;
//  }
//      }
//    });
// return serviceType;
//  }

//  $scope.step1 = function() {
//    $scope.data = {
//      region: null,
//      city: null
//    };

//    $scope.regionList.reset();
//    $scope.regionList.initialize(regions);

//    $scope.localityList.reset();
//    return $state.go('index.service.general.placefix', {id: ServiceService.oService.nID}).then(function() {
//  isStep2 = false;
// });
//  };

//  $scope.step2 = function() {
//    var aServiceData = ServiceService.oService.aServiceData;
//    var serviceType = $scope.findServiceDataByCity();

//    switch (serviceType.nID) {
//      case 1:
//        return $state.go('index.service.general.placefix.link', {id: ServiceService.oService.nID}, {location: false});
//      case 4:
//        return $state.go('index.service.general.placefix.built-in', {id: ServiceService.oService.nID}, {location: false});
//      default:
//        return $state.go('index.service.general.placefix.error', {id: ServiceService.oService.nID}, {location: false});
//    }
//  };

// });

// end city