angular.module('app').controller('IndexController', function ($scope, BankIDService) {
  // See why it's needed for navbar:
  // http://stackoverflow.com/questions/14741988/twitter-bootstrap-navbar-with-angular-js-collapse-not-functioning
  $scope.navBarIsCollapsed = true;
  $scope.navBarStatusVisible = false;
  $scope.logout = function () {
    BankIDService.logout();
    $scope.navBarStatusVisible = false;
  };

  BankIDService.isLoggedIn().then(function (result) {
    $scope.navBarStatusVisible = result;
    BankIDService.fio().then(function (res) {
      $scope.userName = capitalize(res.firstName)
        + " " +
        capitalize(res.middleName)
        + " " +
        capitalize(res.lastName);
    });
  }, function () {
    $scope.navBarStatusVisible = false;
  });

  function capitalize(string) {
    return string !== null && string !== undefined ? string.charAt(0).toUpperCase() + string.slice(1).toLowerCase() : '';
  }

});
