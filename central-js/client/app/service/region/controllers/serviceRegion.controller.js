angular.module('app').controller('ServiceRegionController', function($state, $rootScope, $scope, $sce, RegionListFactory, PlacesService, ServiceService, service, regions, AdminService, serviceLocationParser) {
  // FIXME
  // $scope.service = service;
  // $scope.regions = regions;

  $scope.bAdmin = AdminService.isAdmin();

  // $scope.regionList = new RegionListFactory();
  // $scope.regionList.initialize(regions);

  // Each controller which uses Places Control should tell it:
  // FIXME: preload regions and service and provide them as part of the locations service
  console.log('RegionController. Regions: ', regions );

  PlacesService.setController({
    controller: this,
    regions: regions,
    service: service
  });

  // $scope.loadRegionList = function(search) {
  //   return $scope.regionList.load(service, search);
  // };

  // FIXME - code moved to place.js
  // $scope.onSelectRegionList = function($item) {
  //   $scope.data.region = $item;
  //   $scope.regionList.select($item);
  // };

  // $scope.data = {
  //   region: null,
  //   city: null
  // };

  // $scope.step1 = function() {
  //   $scope.data = {
  //     region: null,
  //     city: null
  //   };
  //   return $state.go('index.service.general.region', {id: $scope.service.nID});
  // };

  // $scope.step2 = function() {
  //   var aServiceData = $scope.service.aServiceData;
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
  //       return $state.go('index.service.general.region.link', {id: $scope.service.nID}, {location: false});
  //     case 4:
  //       return $state.go('index.service.general.region.built-in', {id: $scope.service.nID}, {location: false});
  //     default:
  //       return $state.go('index.service.general.region.error', {id: $scope.service.nID}, {location: false});
  //   }
  // };

  // if ($state.current.name == 'service.general.region.built-in.bankid') {
  //   return true;
  // }

  // $scope.$watchCollection('data.region', function(newValue, oldValue) {
  //   return (newValue == null) ? null : $scope.step2();
  // });

  var initialRegion = serviceLocationParser.getSelectedRegion(regions);
  if (initialRegion)
    $scope.onSelectRegionList(initialRegion);
});
