angular.module('app').controller('IndexController', function($scope, BankIDService) {
  // See why it's needed for navbar:
  // http://stackoverflow.com/questions/14741988/twitter-bootstrap-navbar-with-angular-js-collapse-not-functioning
  $scope.navBarIsCollapsed = true;
  $scope.navBarStatusVisible = false;
  $scope.logout = function () {
    BankIDService.logout();
    $scope.navBarStatusVisible = false;
  };

  BankIDService.isLoggedIn().then(function (result){
      $scope.navBarStatusVisible = result;
      BankIDService.account().then(function(res){
        console.log(res);
        //console.log(res.customer.firstName);
        $scope.userName = capitalize(res.customer.firstName)
          + " " +
        capitalize(res.customer.middleName)
          + " " +
        capitalize(res.customer.lastName);
      });
    }, function (){
      $scope.navBarStatusVisible = false;
    });

  function capitalize(string) {
    return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
  }

});
