angular.module('service').controller('ServiceLinkController', function($location, $state, $rootScope, $scope) {
  $scope.$location = $location;
  $scope.$state = $state;
});