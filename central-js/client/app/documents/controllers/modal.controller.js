angular.module('documents').controller('ModalController', function($scope, $modalInstance, url) {
  $scope.url = url;

  $scope.close = function() {
    $modalInstance.close();
  };
});
