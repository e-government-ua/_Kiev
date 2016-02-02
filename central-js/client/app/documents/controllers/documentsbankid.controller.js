angular.module('documents').controller('DocumentsBankIdController', function ($scope, $state, $location, $window, BankIDService) {
  $scope.authProcess = false;

  $scope.loginWithBankId = function () {
    var stateForRedirect = $state.href('index.documents.user', {error: ''});
    var redirectURI = $location.protocol() +
      '://' + $location.host() + ':'
      + $location.port()
      + stateForRedirect;
    $window.location.href = './auth/bankID?link=' + redirectURI;
  };

  $scope.loginWithEds = function () {
    var stateForRedirect = $state.href('index.documents.user', {error: ''});
    var redirectURI = $location.protocol() +
      '://' + $location.host() + ':'
      + $location.port()
      + stateForRedirect;
    $window.location.href = './auth/eds?link=' + redirectURI;
  };

  $scope.loginWithEmail = function () {
    $state.go('index.auth.email.verify');
  };

  $scope.loginWithSoccard = function () {
    var stateForRedirect = $state.href('index.documents.user', {error: ''});
    var redirectURI = $location.protocol() +
      '://' + $location.host() + ':'
      + $location.port()
      + stateForRedirect;
    $window.location.href = './auth/soccard?link=' + redirectURI;
  };

  if ($state.is('index.documents.bankid')) {
    if (!$state.params.error) {
      BankIDService.isLoggedIn().then(function () {
        $scope.authProcess = true;
        return $state.go('index.documents.content').catch(function (fallback) {
          throw new Error(fallback.error);
        }).finally(function () {
          $scope.authProcess = false;
        });
      });
    }
  }
});
