'use strict';

angular.module('dashboardJsApp')
  .controller('ReportsCtrl', function ($scope, $timeout, Modal, reports, processes) {
    $scope.export = {};
    $scope.export.from = '2015-06-01';
    $scope.export.to = '2015-08-01';

    $scope.exportLink = function () {
      return reports.exportLink({from: $scope.export.from, to: $scope.export.to});
    };

    $scope.statistic = {};
    $scope.statistic.from = '2015-06-01';
    $scope.statistic.to = '2015-08-01';
    $scope.statistic.sBP = 'dnepr_mvd-1';

    processes.getUserProcesses().then(function (data) {
      $scope.processesList = data;
      if ($scope.processesList.length > 0) {
        $scope.statistic.sBP = $scope.processesList[0].sID;
      }
    });

    $scope.statisticLink = function () {
      return reports.statisticLink({from: $scope.statistic.from, to: $scope.statistic.to, sBP: $scope.statistic.sBP});
    };

  });
