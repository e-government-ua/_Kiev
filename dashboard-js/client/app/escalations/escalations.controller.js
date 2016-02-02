(function () {
  'use strict';

  angular
    .module('dashboardJsApp')
    .controller('EscalationsCtrl', escalationsCtrl);

  escalationsCtrl.$inject = ['$scope', '$modal', 'escalationsService', 'iGovNavbarHelper'];
  function escalationsCtrl($scope, $modal, escalationsService, iGovNavbarHelper) {

    iGovNavbarHelper.isTest = false;

    $scope.dataFunctions = {
      //getFunc: escalationsService.getRule,
      setFunc: escalationsService.setRule,
      getAllFunc: escalationsService.getAllRules,
      deleteFunc: escalationsService.deleteRule,
      getAllFunctionsFunc:  escalationsService.getAllEscalationFunctions,
      setRuleFunctionFunc:  escalationsService.setEscalationFunctionFunc,
      deleteRuleFunctionFunc:  escalationsService.deleteEscalationFunctionFunc,
    };
  }
})();
