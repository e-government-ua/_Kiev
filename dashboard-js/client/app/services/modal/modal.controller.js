'use strict';

angular.module('dashboardJsApp')
  .controller('ScheduleModalController', function($scope, $modalInstance, slotToEdit) {

    if (slotToEdit){
      $scope.slot = slotToEdit;
    }else{
      $scope.slot = {
        sDateTimeAt: '2015-01-01 08:00:00',
        sDateTimeTo: '2015-01-01 17:00:00'
      }
    }

    $scope.showAlert = false;

    $scope.atLeastOneWeekDayChosen = function () {
      return Object.keys($scope.slot.saRegionWeekDay).some(function (key) {
        return $scope.slot.saRegionWeekDay[key];
      });
    };

    $scope.save = function () {
      $modalInstance.close($scope.slot);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  });
