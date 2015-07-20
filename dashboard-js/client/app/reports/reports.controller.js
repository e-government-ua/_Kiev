'use strict';

angular.module('dashboardJsApp')
  .controller('ReportsCtrl', function ($scope, $timeout, Modal, reports) {
    $scope.export = {};
    $scope.export.from = '2015-06-01';
    $scope.export.to = '2015-08-01';

    $scope.exportLink = function(){
      return reports.exportLink({from: $scope.export.from, to: $scope.export.to});
    }
    
    $scope.statistic = {};
    $scope.statistic.from = '2015-06-01';
    $scope.statistic.to = '2015-08-01';
    $scope.statistic.sBP = 'dnepr_mvd-1';

    $scope.statisticLink = function(){
      return reports.statisticLink({from: $scope.statistic.from, to: $scope.statistic.to, sBP: $scope.statistic.sBP});
    }
    
  });
