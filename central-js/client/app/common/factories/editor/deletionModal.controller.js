'use strict';

angular.module('app')
  .controller('DeletionModalController', function($scope, $modalInstance, title, message, level) {

    $scope.title = title;
    $scope.message = message;
    $scope.showSimpleConfirmation = level === 1;
    $scope.showComplexConfirmation = level === 2;

    $scope.delete = function () {
      $modalInstance.close();
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  });
