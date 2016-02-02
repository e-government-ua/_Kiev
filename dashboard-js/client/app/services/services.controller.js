'use strict';

angular.module('dashboardJsApp')
  .controller('ServicesCtrl', function ($scope, $modal, schedule, bpForSchedule) {

    $scope.bp = bpForSchedule.bp;
    $scope.departments = [];

    $scope.$watch("bp.chosenBp", function (newValue, oldValue) {
      if (!newValue) { return; }

      schedule.getFlowSlotDepartments(newValue.sID)
        .then(function (data) {
          $scope.departments = data;
        });
    });

    $scope.bp.onBpChangeCallback = function () {
      schedule.getFlowSlotDepartments($scope.bp.chosenBp.sID)
        .then(function (data) {
          $scope.departments = data;
        });
    };

    $scope.bp.onDepartmentChangeCallback = function () {
      if (!$scope.bp.chosenDepartment) { return; }

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
