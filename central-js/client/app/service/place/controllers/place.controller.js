angular.module('app').controller('PlaceController', function($state, AdminService, $rootScope, $scope, $location, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService, service, regions) {

  /*angular.module('app').controller('ServiceBuiltInController', function($location, $state, $rootScope, $scope) {
    $scope.$location = $location;
    $scope.$state = $state;
  });
  */

  console.log('Place: state name = ', $scope.state.current.name);

  var byState = function(stateName) {
    return $scope.state.current.name === stateName;
  };

  var stateIsCityBuiltIn = function() {
    return byState('index.service.general.city.built-in');
  };

  if (byState('index.service.general.city.built-in')) {
    $scope.$location = $location;
    $scope.$state = $state;

    $scope.$watchCollection('data.region', function(newValue, oldValue) {
      return (newValue === null) ? null : $scope.step2();
    });

  } else if (byState('index.service.general.city')) {
    $scope.service = service;
    $scope.regions = regions;

    $scope.bAdmin = AdminService.isAdmin();

    $scope.regionList = new RegionListFactory();
    $scope.regionList.initialize(regions);

    $scope.localityList = new LocalityListFactory();

    $scope.data = {
      region: null,
      city: null
    };

    //isStep2 = false;
  }

  $scope.initLists = function() {
    return this;
  };

  $scope.loadRegionList = function(search) {
    return $scope.regionList.load(service, search);
  };

  $scope.onSelectRegionList = function($item, $model, $label) {
    $scope.data.region = $item;
    $scope.regionList.select($item, $model, $label);

    var serviceType = $scope.findServiceDataByRegion();
    switch (serviceType.nID) {
      case 1:
        $state.go('index.service.general.city.link', {
          id: $scope.service.nID
        }, {
          location: false
        }).then(function() {
          isStep2 = true;
        });
        break;
      case 4:
        $state.go('index.service.general.city.built-in', {
          id: $scope.service.nID
        }, {
          location: false
        }).then(function() {
          isStep2 = true;
        });
        break;
      default:
        $scope.localityList.load(service, $item.nID, null).then(function(cities) {
          $scope.localityList.typeahead.defaultList = cities;
        });
    }
  };

  $scope.loadLocalityList = function(search) {
    return $scope.localityList.load(service, $scope.data.region.nID, search);
  };

  $scope.onSelectLocalityList = function($item, $model, $label) {
    $scope.data.city = $item;
    $scope.localityList.select($item, $model, $label);

    var serviceType = $scope.findServiceDataByCity();
    switch (serviceType.nID) {
      case 1:
        $state.go('index.service.general.city.link', {
          id: $scope.service.nID
        }, {
          location: false
        }).then(function() {
          isStep2 = true;
        });
        break;
      case 4:
        $state.go('index.service.general.city.built-in', {
          id: $scope.service.nID
        }, {
          location: false
        }).then(function() {
          isStep2 = true;
        });
        break;
      default:
        $state.go('index.service.general.city.error', {
          id: $scope.service.nID
        }, {
          location: false
        });
    }
  };

  $scope.ngIfCity = function() {
    if (byState('index.service.general.city.built-in')) {
      if ($scope.data.city) {
        return true;
      } else {
        return false;
      }
    }
    if (byState('index.service.general.city.built-in.bankid')) {
      if ($scope.data.city) {
        return true;
      } else {
        return false;
      }
    }
    return $scope.data.region ? true : false;
  };

  $scope.getRegionId = function() {
    var region = $scope.data.region;
    return region ? region.nID : 0;
  };

  $scope.getCityId = function() {
    var city = $scope.data.city;
    return city ? city.nID : 0;
  };

  $scope.ngIfStep2 = function() {
    console.log('isStep2: ', isStep2);
    return isStep2;
  };

  $scope.findServiceDataByRegion = function() {
    var aServiceData = $scope.service.aServiceData;
    var serviceType = {
      nID: 0
    };
    angular.forEach(aServiceData, function(value, key) {
      if (value.nID_Region && value.nID_Region.nID === $scope.data.region.nID) {
        serviceType = value.nID_ServiceType;
        $scope.serviceData = value;
        if ($scope.serviceData.bNoteTrusted === false) {
          $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
          $scope.serviceData.sNoteTrusted = true;
        }
      }
    });
    return serviceType;
  };

  $scope.findServiceDataByCity = function() {
    var aServiceData = $scope.service.aServiceData;
    var serviceType = {
      nID: 0
    };
    angular.forEach(aServiceData, function(value, key) {
      if (value.nID_City && value.nID_City.nID === ( $scope.data.city && $scope.data.city.nID ) ) {
        serviceType = value.nID_ServiceType;
        $scope.serviceData = value;
        if ($scope.serviceData.bNoteTrusted === false) {
          $scope.serviceData.sNote = $sce.trustAsHtml($scope.serviceData.sNote);
          $scope.serviceData.sNoteTrusted = true;
        }
      }
    });
    return serviceType;
  };

  $scope.step1 = function() {

    $scope.data = {
      region: null,
      city: null
    };

    $scope.regionList.reset();
    $scope.regionList.initialize(regions);

    $scope.localityList.reset();

    isStep2 = false;

    if (byState('index.service.general.city')) {
      return $state.go('index.service.general.city', {
        id: $scope.service.nID
      });
    }
  };

  $scope.step2 = function() {
    var aServiceData = $scope.service.aServiceData;
    var serviceType = $scope.findServiceDataByCity();

    switch (serviceType.nID) {
      case 1:
        return $state.go('index.service.general.city.link', {
          id: $scope.service.nID
        }, {
          location: false
        });
      case 4:
        return $state.go('index.service.general.city.built-in', {
          id: $scope.service.nID
        }, {
          location: false
        });
      default:
        return $state.go('index.service.general.city.error', {
          id: $scope.service.nID
        }, {
          location: false
        });
    }
  };

  if (byState('service.general.city.built-in.bankid')) {
    return true;
  }

});

angular.module('app').controller('BuiltinCityController', function($scope, service) {
  $scope.service = service;
});


/*
angular.module('app').controller('ServiceRegionController', function($state, $rootScope, $scope, $sce, RegionListFactory, PlacesService, ServiceService, service, regions, AdminService) {
  
  !!!
  $scope.onSelectRegionList = function($item, $model, $label) {
    $scope.data.region = $item;
    $scope.regionList.select($item, $model, $label);
  };

});

*/