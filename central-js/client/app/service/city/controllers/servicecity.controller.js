angular.module('app').controller('ServiceCityController', function($state, $rootScope, $scope, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService, service, regions) {
  $scope.service = service;
  $scope.regions = regions;

  $scope.regionList = new RegionListFactory();
  $scope.regionList.initialize(regions);

  $scope.localityList = new LocalityListFactory();

  $scope.loadRegionList = function(search) {
    return $scope.regionList.load(service, search);
  };

  $scope.onSelectRegionList = function($item, $model, $label) {
    $scope.data.region = $item;
    $scope.regionList.select($item, $model, $label);
    $scope.localityList.load(service, $item.nID, null).then(function(cities) {
      $scope.localityList.typeahead.defaultList = cities;
    });
  };

  $scope.loadLocalityList = function(search) {
    return $scope.localityList.load(service, $scope.data.region.nID, search);
  };

  $scope.onSelectLocalityList = function($item, $model, $label) {
    $scope.data.city = $item;
    $scope.localityList.select($item, $model, $label);
  };

  $scope.data = {
    region: null,
    city: null
  };

  $scope.step1 = function() {
    $scope.data = {
      region: null,
      city: null
    };

    $scope.regionList.reset();
    $scope.regionList.initialize(regions);

    $scope.localityList.reset();
    return $state.go('index.service.general.city', {id: $scope.service.nID});
  };

  $scope.step2 = function() {
    var aServiceData = $scope.service.aServiceData;
    var serviceType = {nID: 0};
    angular.forEach(aServiceData, function(value, key) {
      if (value.nID_City && value.nID_City.nID == $scope.data.city.nID) {
        serviceType = value.nID_ServiceType;
        $scope.serviceData = value;
        $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
      }
    });

    switch (serviceType.nID) {
      case 1:
        return $state.go('index.service.general.city.link', {id: $scope.service.nID}, {location: false});
      case 4:
        return $state.go('index.service.general.city.built-in', {id: $scope.service.nID}, {location: false});
      default:
        return $state.go('index.service.general.city.error', {id: $scope.service.nID}, {location: false});
    }
  };

  if ($state.current.name == 'service.general.city.built-in.bankid') {
    return true;
  }

  $scope.$watchCollection('data.city', function(newValue, oldValue) {
    return (newValue == null) ? null : $scope.step2();
  });
});

angular.module('app').controller('BuiltinCityController', function($scope, service) {
  $scope.service = service;
});
