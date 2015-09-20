'use strict';

angular.module('dashboardJsApp')
  .controller('EscalationsCtrl', function ($scope, $modal, rules) {

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
