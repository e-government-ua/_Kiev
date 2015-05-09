'use strict';

angular.module('dashboardJsApp')
  .controller('LoginCtrl', function(Auth, Idle, Modal, $scope, $location, $window) {
    $scope.user = {};
    $scope.errors = {};

    $scope.login = function(form) {
      $scope.submitted = true;

      if (form.$valid) {
        Auth.login({
            login: $scope.user.login,
            password: $scope.user.password
          })
          .then(function() {
            // Logged in, redirect to home          
            $location.path('/tasks');
          })
          .catch(function(err) {
            $scope.errors.other = err.message;
          });
      }
    };
  });