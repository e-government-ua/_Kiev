angular.module('documents').controller('DocumentsController', function($state, $scope, BankIDService) {
  $scope.loading = false;
  BankIDService.isLoggedIn().then(function() {
    if ($state.is('index.documents')) {
      $scope.loading = true;
      return $state.go('index.documents.content').finally(function() {
        $scope.loading = false;
      });
    }
  }).catch(function() {
    if ($state.is('index.documents')) {
      return $state.go('index.documents.bankid');
    }
  });
});
