angular.module('app').controller('PlaceFixController', function(
  $state, AdminService, $rootScope, $scope, $location, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService, BankIDService, serviceLocationParser, regions, service) {

  var self = this;

  var statesMap = {
    'index.service.general.placefix.built-in': {
      viewClass: 'state-disabled'
    },
    'index.service.general.placefix.built-in.bankid.submitted': {
      startupFunction: function($location, $state, $rootScope) {
        $scope.bankIDAccount = BankIDService.account();
      },
      viewClass: 'state-collapsed'
    }
  };

  var oService = ServiceService.oService;

  // FIXME remove state dependency
  var curState = $state.current.name;

  $scope.service = service;
  $scope.regions = regions;
  $scope.$state = $state;
  $scope.$location = $location;
  $scope.bAdmin = AdminService.isAdmin();
  $scope.state = $state.get(curState);

  // console.log('PlaceFix Controller. $state name =', $state.current.name);

  if (statesMap[curState] && statesMap[curState].startupFunction) {
    console.log('PlaceFix Controller. call startup function for $state =', $state.current.name);
    statesMap[curState].startupFunction.call($location, $state, $rootScope);
  } else {
    // default startup
  }

  // oParams = { placeData: placeData, serviceData: serviceData } }
  self.processPlaceChange = function(oParams) {

    // due to availiablity, do next steps
    // var bAvail = PlacesService.serviceIsAvailableInPlace();

    var serviceType = oParams.serviceData.nID_ServiceType.nID;

    var stateByServiceType = {
      // Сервіс за посиланням
      1: 'index.service.general.placefix.link',
      // Вбудований сервіс
      4: 'index.service.general.placefix.built-in',
      // Помилка - сервіс відсутній
      0: 'index.service.general.placefix.error'
    };

    var stateToGo = stateByServiceType[serviceType];

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

    if (!stateToGo || ($state.current.name === stateToGo)) {
      return;
    }
    
    // не переходити до іншого стану, якщо даний стан є кінцевим
    // $state.current.name === 'index.service.general.placefix.built-in' ||
    if ($state.current.name === 'index.service.general.placefix.built-in.bankid' || $state.current.name === 'index.service.general.placefix.built-in.bankid.submitted') {
      // STOPPEDHERE
      return;
    }

    $state.go(stateToGo, {
      id: oService.nID
    }, {
      location: false
    }).then(function() {

    });

    // var initialRegion = serviceLocationParser.getSelectedRegion(regions);
    // if (initialRegion) {
    //   // FIXME debug it
    //   console.log('initialRegion =', initialRegion);
    //   //$scope.onSelectRegionList(initialRegion);
    // }

    console.info('PROCESS Place сhange, $state:', $state.current.name, ', to go:', stateToGo /*, 'oParams =', oParams*/ );
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

  // колись це було step1
  $scope.$on('onEditPlace', function(evt, oParams) {

    // FIXME review this in different contexts
    return $state.go('index.service.general.placefix', {
      id: ServiceService.oService.nID
    }).then(function() {
      //
    });
  });

  /**
   * params: placeData, serviceData
   */
  $scope.$on('onPlaceChange', function(evt, oParams) {
    self.processPlaceChange(oParams);
  });

  // self.processPlaceChange({
  //   serviceData: PlacesService.getServiceDataForSelectedPlace(),
  //   placeData: PlacesService.getPlaceData()
  // });

});

// city, region - merged