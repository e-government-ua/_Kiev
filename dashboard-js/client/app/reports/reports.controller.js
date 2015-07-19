'use strict';

angular.module('dashboardJsApp')
  .controller('ReportsCtrl', function ($scope, $timeout, Modal, reports) {
    $scope.export = {};
    $scope.export.from = '2015-06-01';
    $scope.export.to = '2015-08-01';

    $scope.exportLink = function(){
      return reports.exportLink({from: $scope.export.from, to: $scope.export.to});
    }
  });
