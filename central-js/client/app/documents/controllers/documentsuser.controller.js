angular.module('documents').controller('DocumentsUserController', function($scope, $state, $location, $window, BankIDService) {

  $scope.authProcess = false;
  $scope.error = undefined;

  BankIDService.isLoggedIn().then(function() {
    $scope.loading = true;
    return $state.go('index.documents.content').finally(function() {
      $scope.loading = false;
    });
  }).catch(function() {
    return $state.go('index.documents.bankid');
  });

});
