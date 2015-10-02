angular.module('app').controller('PlaceController', function($state, AdminService, $rootScope, $scope, $location, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService, BankIDService, serviceLocationParser, regions, service) {

  var self = this;
  var oService = ServiceService.oService;

  $scope.service = service;
  $scope.regions = regions;
  $scope.$state = $state;
  $scope.$location = $location;
  $scope.bAdmin = AdminService.isAdmin();
  // FIXME перевірити, чи це потрібно:
  $scope.state = $state.get($state.current.name);
  $scope.stepNumber = 1;

  // oParams = { placeData: placeData } }
  self.processPlaceChange = function(oParams) {

    // діємо в залежності від доступності сервісу
    var oAvail = PlacesService.getServiceAvailability();
    var stateToGo = PlacesService.getServiceStateForPlace();

    // отримати дані сервісу та його опис
    angular.forEach(oService.aServiceData, function(service, key) {
      $scope.serviceData = service;
      if (oService.bNoteTrusted === false) {
        $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
        oService.sNoteTrusted = true;
      }
    });

    // console.info('PROCESS Place сhange, $state:', $state.current.name, ', to go:', stateToGo);

    if (!stateToGo || ($state.current.name === stateToGo)) {
      return;
    }

    // не переходити до іншого стану, якщо даний стан є кінцевим
    if ($state.current.name === 'index.service.general.place.built-in.bankid' || $state.current.name === 'index.service.general.place.built-in.bankid.submitted') {
      return;
    }

    $state.go(stateToGo, {
      id: oService.nID
    }, {
      location: false
    }).then(function() {
      // якщо треба, робимо додаткові дії тут
    });

    $scope.setStepNumber(2);

    // console.info('PROCESSED Place сhange, state to go:', stateToGo);
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

  $scope.$on('onEditPlace', function(evt, oParams) {
    $scope.setStepNumber(1);
    // TODO треба ще раз перевірити, як це працює у різних контекстах
    return $state.go('index.service.general.place', {
      id: ServiceService.oService.nID
    }).then(function() {
      //
    });
  });

  $scope.setStepNumber = function(nStep) {
    $scope.stepNumber = nStep;
  };

  $scope.getStepNumber = function() {
    return $scope.stepNumber;
  };

  /**
   * oParams: { placeData: placeData };
   */
  $scope.$on('onPlaceChange', function(evt, oParams) {
    self.processPlaceChange(oParams);
  });
});