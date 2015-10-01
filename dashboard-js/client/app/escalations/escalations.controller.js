'use strict';

angular.module('dashboardJsApp')
  .controller('EscalationsCtrl', function ($scope, $modal, escalationsService) {
    
     $scope.rulesList = {
      //getFunc: escalationsService.getRule,
      setFunc: escalationsService.setRule,
      getAllFunc: escalationsService.getAllRules,
      deleteFunc: escalationsService.deleteRule
    };
  });
