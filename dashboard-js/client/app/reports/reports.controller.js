'use strict';

angular.module('dashboardJsApp')
  .controller('ReportsCtrl', function ($scope, $timeout, Modal, reports, processes) {
    $scope.export = {};
    $scope.export.from = '2015-06-01';
    $scope.export.to = '2015-08-01';
    $scope.export.sBP = 'dnepr_spravka_o_doxodax';
    $scope.exportURL = "/reports";
    
    $scope.statistic = {};
    $scope.statistic.from = '2015-06-01';
    $scope.statistic.to = '2015-08-01';
    $scope.statistic.sBP = 'dnepr_spravka_o_doxodax';
    $scope.statisticUrl = "/reports";
    
     $scope.initExportUrl = function () {
        reports.exportLink({ from: $scope.export.from, to: $scope.export.to, sBP: $scope.export.sBP},
            function (result) {
                $scope.exportURL = result;
            });       
    }
    
     $scope.getExportLink = function () {
          return $scope.exportURL;          
      }
             
      $scope.initStatisticUrl = function () {
        reports.statisticLink({ from: $scope.statistic.from, to: $scope.statistic.to, sBP: $scope.statistic.sBP},
            function (result) {
                $scope.statisticUrl = result;
            });
    }
    
    $scope.getStatisticLink = function () {
          return $scope.statisticUrl;          
      }

    processes.getUserProcesses().then(function (data) {
      $scope.processesList = data;
      if ($scope.processesList != '' && $scope.processesList.length > 0) {
        $scope.statistic.sBP = $scope.processesList[0].sID;
        $scope.export.sBP = $scope.processesList[0].sID;
        $scope.initExportUrl();
        $scope.initStatisticUrl();
      }
    }, function () {
      $scope.processesList = "error";   
    });
    
    $scope.processesLoaded = function() {
      if ($scope.processesList)
      return true;
    return false;
    }
    
     $scope.processesLoadError = function() {
      if ($scope.processesList && $scope.processesList == "error")
      return true;
    return false;
    }
   
  });
