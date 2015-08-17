'use strict';

angular.module('dashboardJsApp')
  .controller('ServicesCtrl', function ($scope, $modal, schedule, bpForSchedule) {

    $scope.bp = bpForSchedule.bp;
    $scope.bp.onChangeCallback = function () {
      $scope.$broadcast('bpChangedEvent');
    };

    $scope.workHours = {
      getFunc: schedule.getSchedule,
      setFunc: schedule.setSchedule,
      deleteFunc: schedule.deleteSchedule
    };

    $scope.exemptions = {
      getFunc: schedule.getExemptions,
      setFunc: schedule.setExemption,
      deleteFunc: schedule.deleteExemption
    };

  });
