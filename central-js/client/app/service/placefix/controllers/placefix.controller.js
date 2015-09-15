angular.module('app').controller('PlaceFixController', function(
  $state, AdminService, $rootScope, $scope, $location, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService, BankIDService, serviceLocationParser, regions, service) {

  var self = this;

  // Each controller which uses Places Control should tell it:
  PlacesService.setController(this);

  var statesMap = {
    'index.service.general.placefix.built-in': { // city
      startupFunction: function(iPlaceController, $location, $state, $rootScope, placeCtrl) {
        iPlaceController.isStep2 = true;
      },
      viewClass: 'state-disabled'
    },
    'index.service.general.placefix.built-in.bankid.submitted': { // city
      startupFunction: function(iPlaceController, $location, $state, $rootScope, placeCtrl) {
        $scope.bankIDAccount = BankIDService.account();
      },
      viewClass: 'state-collapsed'
    }
  };

  // oParams = { placeData: placeData, serviceData: serviceData } }
  self.processPlaceChange = function(oParams) {

    var oService = ServiceService.oService;

    // due to availiablity, do next steps
    // var bAvail = PlacesService.serviceIsAvailableInPlace();

    var stateByServiceType = {
      // Сервіс за посиланням
      1: 'index.service.general.placefix.link',
      // Вбудований сервіс
      4: 'index.service.general.placefix.built-in',
      // Помилка - сервіс відсутній
      0: 'index.service.general.placefix.error'
    };

    var stateToGo = stateByServiceType[oParams.serviceData.nID_ServiceType.nID];

    // obtain service data and it's notes
    angular.forEach(oService.aServiceData, function(service, key) {
      $scope.serviceData = service;
      if (oService.bNoteTrusted === false) {
        $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
        oService.sNoteTrusted = true;
      }
    });

    // FIXME generalize it: if cur state is step 1 and it's complete, go to step 2 and change state
    // if (stateToGo && oParams.placeData.city) {
    // if (stateToGo && oParams.placeData) {
    // console.log('step 2:', self.isStep2);

    $scope.service = service;
    $scope.regions = regions;
    $scope.$location = $location;
    $scope.$state = $state;
    $scope.state = $state.get($state.current.name);

    if (!stateToGo || ($state.current.name === stateToGo)) {
      return;
    }
    // не переходити до іншого стану, якщо даний стан є кінцевим
    if ($state.current.name === 'index.service.general.placefix.built-in.bankid' || $state.current.name === 'index.service.general.placefix.built-in.bankid.submitted') {
      // STOPPEDHERE
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

    console.info('PROCESS Place сhange, $state:', $state.current.name, ', to go:', stateToGo /*, 'oParams =', oParams*/ );
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

    // region - from ServiceRegionController
    //   $scope.data = {
    //     region: null,
    //     city: null
    //   };
    //   return $state.go('index.service.general.placefix', {id: ServiceService.oService.nID});

    // city
    //    $scope.data = {
    //      region: null,
    //      city: null
    //    };

    //    $scope.regionList.reset();
    //    $scope.regionList.initialize(regions);

    //    $scope.localityList.reset();
    //    return $state.go('index.service.general.placefix', {id: ServiceService.oService.nID}).then(function() {
    //      isStep2 = false;
    //    });
  };


  $scope.step2 = function() {
    var aServiceData = ServiceService.oService.aServiceData;

    console.log('step 2:');
    self.isStep2 = true;

    // region - from ServiceRegionController
    //   var aServiceData = ServiceService.oService.aServiceData;
    //   var serviceType = {nID: 0};
    //   angular.forEach(aServiceData, function(value, key) {
    //     if (value.nID_Region.nID == $scope.data.region.nID) {
    //       serviceType = value.nID_ServiceType;
    //       $scope.serviceData = value;
    //       $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
    //     }
    //   });
    //   switch (serviceType.nID) {
    //     case 1:
    //       return $state.go('index.service.general.placefix.link', {id: ServiceService.oService.nID}, {location: false});
    //     case 4:
    //       return $state.go('index.service.general.placefix.built-in', {id: ServiceService.oService.nID}, {location: false});
    //     default:
    //       return $state.go('index.service.general.placefix.error', {id: ServiceService.oService.nID}, {location: false});
    //   }

    // city 
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
  };

  // region
  // $scope.$watchCollection('data.region', function(newValue, oldValue) {
  //   return (newValue == null) ? null : $scope.step2();
  // });

  $scope.ngIfStep2 = function() {
    // console.log('ngIfStep2 =', self.isStep2);
    // return $scope.place ControlIsComplete();
    return self.isStep2;
  };

  // moved back from places service
  self.isStep2 = self.isStep2 || false;

  $scope.bAdmin = AdminService.isAdmin();
  $scope.$state = $state;

  // FIXME remove state dependency
  var curState = $scope.getStateName();

  // console.log('PlaceFix Controller. $state name =', $state.current.name);

  if (statesMap[curState] && statesMap[curState].startupFunction) {
    console.log('PlaceFix Controller. call startup function for $state name =', $state.current.name);
    statesMap[curState].startupFunction.call(self, $location, $state, $rootScope, self.placeCtrl);
  } else {
    // default startup
  }

  $scope.$on('onEditPlace', function(evt, oParams) {
    self.isStep2 = false;
  });

  /**
   * params: placeData, serviceData
   */
  $scope.$on('onPlaceChange', function(evt, oParams) {
    self.processPlaceChange(oParams);
  });

  self.processPlaceChange({
    serviceData: PlacesService.getServiceDataForSelectedPlace(),
    placeData: PlacesService.getPlaceData()
  });

});

// city, region - merged