angular.module('documents', []).controller('DocumentsController', function($state, $scope, BankIDService) {
  $scope.loading = false;
  BankIDService.isLoggedIn().then(function() {
    if ($state.is('documents')) {
      $scope.loading = true;
      return $state.go('documents.content').finally(function() {
        $scope.loading = false;
      });
    }
  }).catch(function() {
    if ($state.is('documents')) {
      return $state.go('documents.bankid');
    }
  });
});