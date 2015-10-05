angular.module('app').controller('PlaceController', function($state, AdminService, $rootScope, $scope, $location, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService, BankIDService, serviceLocationParser, regions, service) {

  var self = this;
  var oService = ServiceService.oService;

  $scope.service = service;
  $scope.regions = regions;
  $scope.$state = $state;
  $scope.$location = $location;
  $scope.bAdmin = AdminService.isAdmin();
  $scope.state = $state.get($state.current.name);
  $scope.stepNumber = 1;

  self.processPlaceChange = function() {

    // діємо в залежності від доступності сервісу
    var serviceAvailableIn = PlacesService.serviceAvailableIn();
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

    $scope.setStepNumber(2);

    // console.info('PROCESSED Place сhange, state to go:', stateToGo);
  };

  $scope.setStepNumber = function(nStep) {
    $scope.stepNumber = nStep;
  };

  $scope.getStepNumber = function() {
    return $scope.stepNumber;
  };

  /**
   * Редагування місця
   */
  $scope.$on('onEditPlace', function(evt) {
    $scope.setStepNumber(1);
    // FIXME переходити на попередній стан, а не на фіксований
    // TODO ще раз перевірити, як це працює у різних контекстах
    return $state.go('index.service.general.place', {
      id: oService.nID
    }).then(function() {
      //
    });
  });

  /**
   * Обробляти зміну місця
   */
  $scope.$on('onPlaceChange', function(evt) {
    self.processPlaceChange();
  });
});