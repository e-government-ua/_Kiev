angular.module('app').controller('PlaceController', function($state, AdminService, $rootScope, $scope, $location, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService, BankIDService, regions, service) {

  var self = this;
  var oService = ServiceService.oService;

  $scope.service = service;
  $scope.regions = regions;
  $scope.$state = $state;
  $scope.$location = $location;
  $scope.bAdmin = AdminService.isAdmin();
  $scope.state = $state.get($state.current.name);

  /**
   * Обробити зміну місця
   */
  $scope.$on('onPlaceChange', function(evt) {
    // діємо в залежності від доступності сервісу
    var stateToGo = PlacesService.getServiceStateForPlace();

    // отримати дані сервісу та його опис
    var oAvail = PlacesService.serviceAvailableIn();
    var foundInCountry;
    var foundInRegion;
    var foundInCity;

    angular.forEach(oService.aServiceData, function(service, key) {
      foundInCountry = oAvail.thisCountry;
      foundInRegion = oAvail.thisRegion && service.nID_Region && service.nID_Region.nID === $scope.getRegionId();
      foundInCity = oAvail.thisCity && service.nID_City && service.nID_City.nID === $scope.getCityId();
      // if (service.nID_Region && service.nID_Region.nID === $scope.getRegionId() && service.nID_City && service.nID_City.nID === $scope.getCityId()) {
      if (foundInCountry || foundInRegion || foundInCity) {
        $scope.serviceData = service;
        if (oService.bNoteTrusted === false) {
          $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
          oService.sNoteTrusted = true;
        }
      }
    });

    // console.info('PROCESS Place сhange, $state:', $state.current.name, ', to go:', stateToGo);

    // не переходити до іншого стану, якщо даний стан є кінцевим
    if (!stateToGo || ($state.current.name === stateToGo) || $state.current.name === 'index.service.general.place.built-in.bankid' || $state.current.name === 'index.service.general.place.built-in.bankid.submitted') {
      return;
    }

    $state.go(stateToGo, {
      id: oService.nID
    }, {
      location: false
    }).then(function() {
      // якщо треба, робимо додаткові дії тут
    });
  });

  /**
   * Перейти до стану редагування місця
   */
  $scope.$on('onPlaceEdit', function(evt) {
    // TODO ще раз перевірити, як це працює у різних контекстах
    // можливо, треба переходити на попередній стан, а не на фіксований
    return $state.go('index.service.general.place', {
      id: oService.nID
    }, {
      location: false
    }).then(function() {
      //
    });
  });

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
});