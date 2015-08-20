angular.module('app').controller('WizardController', function(
  $state, AdminService, $rootScope, $scope, $location, $sce, RegionListFactory, LocalityListFactory, PlacesService, ServiceService, service, regions ) {

  PlacesService.setController({
    self: this,
    regions: regions,
    service: service
  });

  /*
  angular.module('app').controller('ServiceBuiltInController', function($location, $state, $rootScope, $scope) {
    $scope.$location = $location;
    $scope.$state = $state;
  });
  */

});

/*
angular.module('app').controller('ServiceRegionController', function($state, $rootScope, $scope, $sce, RegionListFactory, PlacesService, ServiceService, service, AdminService) {
  !!!
  $scope.onSelectRegionList = function($item, $model, $label) {
    $scope.data.region = $item;
    $scope.regionList.select($item, $model, $label);
  };
});
*/