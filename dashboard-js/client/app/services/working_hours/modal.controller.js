'use strict';

angular.module('dashboardJsApp')
  .controller('WorkingHoursModalController', function($scope, $modalInstance, slotToEdit) {

    if (slotToEdit){
      $scope.slot = slotToEdit;
    }else{
      $scope.slot = {
        sDateTimeAt: '1970-01-01 08:00:00',
        sDateTimeTo: '1970-01-01 17:00:00'
      }
    }

    $scope.save = function () {
      $modalInstance.close($scope.slot);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  });
