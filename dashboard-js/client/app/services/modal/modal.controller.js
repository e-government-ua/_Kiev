'use strict';

angular.module('dashboardJsApp')
  .controller('ScheduleModalController', function($scope, $modalInstance, slotToEdit) {

    if (slotToEdit){
      $scope.slot = slotToEdit;
    }else{
      $scope.slot = {
        sDateTimeAt: '2015-01-01 08:00:00',
        sDateTimeTo: '2015-01-01 17:00:00',
        saRegionWeekDay: {
          mo: true,
          tu: true,
          we: true,
          th: true,
          fr: true,
          sa: false,
          su: false
        }
      }
    }

    $scope.showAlert = false;

    $scope.atLeastOneWeekDayChosen = function () {
      if (!$scope.slot.saRegionWeekDay) {
        return false;
      }

      return Object.keys($scope.slot.saRegionWeekDay)
        .some(function (key) {
          return $scope.slot.saRegionWeekDay[key];
        }
      );
    };

    $scope.timeOrderIsCorrect = function(){
      var at = new Date( $scope.slot.sDateTimeAt);
      var to = new Date( $scope.slot.sDateTimeTo);
      return at < to;
    };

    $scope.showSave = function(){
      return $scope.atLeastOneWeekDayChosen() && $scope.timeOrderIsCorrect();
    };

    $scope.save = function () {
      $modalInstance.close($scope.slot);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  });
