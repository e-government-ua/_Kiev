angular.module('app').controller('IndexController', function($scope) {
  // See why it's needed for navbar:
  // http://stackoverflow.com/questions/14741988/twitter-bootstrap-navbar-with-angular-js-collapse-not-functioning
  $scope.navBarIsCollapsed = true;
});
