'use strict';

angular.module('dashboardJsApp')
  .controller('EscalationsCtrl', function ($scope, $modal, escalationsService) {
    
     $scope.dataFunctions = {
      //getFunc: escalationsService.getRule,
      setFunc: escalationsService.setRule,
      getAllFunc: escalationsService.getAllRules,
      deleteFunc: escalationsService.deleteRule,
      getAllFunctionsFunc:  escalationsService.getAllEscalationFunctions,
      setRuleFunctionFunc:  escalationsService.setEscalationFunctionFunc,
      deleteRuleFunctionFunc:  escalationsService.deleteEscalationFunctionFunc,
      
    };
  });
