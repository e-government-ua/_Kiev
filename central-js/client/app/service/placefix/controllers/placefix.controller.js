angular.module('app').controller('PlaceFixController', function(
  $state, AdminService, $rootScope, $scope, $location, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService, serviceLocationParser, regions) {

  // FIXME: preload regions and service and provide them as part of the locations service

  // Each controller which uses Places Control should tell it:
  PlacesService.setController(this);

  var self = this;

  var statesMap = {
    'index.service.general.placefix.built-in': { // city
      startupFunction: function(iPlaceController, $location, $state, $rootScope, placeCtrl) {
        $scope.$location = $location;
        iPlaceController.isStep2 = true;
      },
      viewClass: 'state-disabled'
    },
    'index.service.general.placefix.built-in.bankid.submitted': { // city
      startupFunction: function(iPlaceController, $location, $state, $rootScope, placeCtrl) {
        $scope.collapse();
        $scope.state = $state; //.get('index.service.general.placefix.built-in.bankid.submitted');
      },
      viewClass: 'state-collapsed'
    }
  };

  var initCityService = function() {

    $scope.regions = regions;
    $scope.regionList = new RegionListFactory();
    $scope.regionList.initialize(regions);

    $scope.localityList = new LocalityListFactory();

    // Each controller which uses Places Control should tell it:
    // FIXME: preload regions and service and provide them as part of the locations service
    console.log('initCityService. Reg-s: ', regions.length);

    ///
    ///
    ///

    var initialRegion = serviceLocationParser.getSelectedRegion(regions);
    if (initialRegion) {
      $scope.onSelectRegionList(initialRegion);
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

    console.log('PlaceFixController on Place сhange, $state:', $state.current.name, ', to go:', stateToGo /*, 'oParams =', oParams*/ );
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

    // FIXME FIXME generalize it: if cur state is step 1 and it's complete, go to step 2 and change state
    // if (stateToGo && oParams.placeData.city) {
    // if (stateToGo && oParams.placeData) {
    // console.log('step 2:', self.isStep2);

    if (stateToGo) {
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

    initCityService();
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

  $scope.ngIfStep2 = function() {
    // console.log('ngIfStep2 =', self.isStep2);
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
    $scope.$location = $location;
    $scope.$state = $state;
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

// angular.module('app').controller('ServiceBuiltInController', function($location, $state, $rootScope, $scope) {
//   $scope.$location = $location;
//   $scope.$state = $state;
// });

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

//   // if ($state.current.name == 'service.general.placefix.built-in.bankid') {
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


// country

angular.module('app').controller('ServiceCountryAbsentController', function($state,
  $rootScope,
  $scope,
  service,
  MessagesService,
  ValidationService) {
  $scope.service = service;
  $scope.hiddenCtrls = true;
  (function() {
    if (window.pluso && typeof window.pluso.start === 'function') {
      return;
    }
    if (window.ifpluso === undefined) {
      window.ifpluso = 1;
      var d = document,
        s = d.createElement('script'),
        g = 'getElementsByTagName';
      s.type = 'text/javascript';
      s.charset = 'UTF-8';
      s.async = true;
      s.src = ('https:' === window.location.protocol ? 'https' : 'http') + '://share.pluso.ru/pluso-like.js';
      var h = d[g]('body')[0];
      h.appendChild(s);
    }
  })();

  if (!!window.pluso) {
    window.pluso.build(document.getElementsByClassName('pluso')[0], false);
  }

  $scope.absentMessage = {
    email: '',
    showErrors: false
  };

  $scope.emailKeydown = function(e, absentMessageForm, absentMessage) {
    $scope.absentMessage.showErrors = false;
    // If key is Enter (has 13 keyCode), try to submit the form:
    if (e.keyCode === 13) {
      $scope.sendAbsentMessage(absentMessageForm, absentMessage);
    }
  };

  $scope.sendAbsentMessage = function(absentMessageForm, absentMessage) {

    // TODO Test it here
    // ValidationService.validateByMarkers( absentMessageForm );

    if (false === absentMessageForm.$valid) {
      console.log('states absentMessageForm', absentMessageForm);
      $scope.absentMessage.showErrors = true;
      return false;
    }

    // @todo Fix hardcoded city name, we should pass it into state
    var data = {
      sMail: absentMessage.email,
      sHead: 'Закликаю владу перевести цю послугу в електронну форму!',
      sBody: 'Україна - ' + service.sName
    };
    MessagesService.setMessage(data, 'Дякуємо! Ви будете поінформовані, коли ця послуга буде доступна через Інтернет');
  };
});

// end country



// FIXME
// city - from serviceCity.controller.js

angular.module('app').controller('ServiceCityController', function($state, AdminService, $rootScope, $scope, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService, regions, service, serviceLocationParser) {

  $scope.regions = regions;
  $scope.regionList = new RegionListFactory();
  $scope.regionList.initialize(regions);

  $scope.localityList = new LocalityListFactory();

  // Each controller which uses Places Control should tell it:
  // FIXME: preload regions and service and provide them as part of the locations service
  console.log('ServiceCityController. Reg-s: ', regions);

  PlacesService.setController(this);

  // Each controller which uses Places Control should tell it:

  // $scope.loadRegionList = function(search) {
  //   return $scope.regionList.load(ServiceService.oService, search);
  // };

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

  // if ($state.current.name == 'service.general.placefix.built-in.bankid') {
  //  return true;
  // }

  var initialRegion = serviceLocationParser.getSelectedRegion(regions);
  if (initialRegion) {
    $scope.onSelectRegionList(initialRegion);
  }
});

angular.module('app').controller('BuiltinCityController', function($scope) {});

// end city