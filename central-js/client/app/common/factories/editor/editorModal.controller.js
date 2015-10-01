'use strict';

angular.module('app')
  .controller('EditorModalController', function($scope, $modalInstance, entityToEdit) {

    $scope.isNew = entityToEdit === undefined;
    $scope.entity = entityToEdit || {};

    $scope.save = function () {
      $modalInstance.close($scope.entity);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  });
