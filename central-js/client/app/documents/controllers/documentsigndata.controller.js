angular.module('documents').controller('DocumentSignDataController', function($scope, $modalInstance, oSignData) {
  $scope.oSignData = oSignData;
  console.log(oSignData);


  $scope.close = function() {
    $modalInstance.close();
  };
});
