'use strict';

angular.module('dashboardJsApp')
  .controller('RuleEditorModalCtrl', function($scope, $modalInstance, ruleToEdit, processes) {
        
    var exampleRule = {       
        sID_BP: 'dnepr_spravka_o_doxodax',
        sID_UserTask: '*',
        sCondition: 'nElapsedDays==nDaysLimit',
        soData: 'nDaysLimit:3,asRecipientMail:[test@email.com]',
        sPatternFile: 'escalation/escalation_template.html',
        nID_EscalationRuleFunction: 'EscalationHandler_SendMailAlert',
      };
    var getTheRule = function(a){
    if (a != null && a != undefined)
    return angular.copy(a);
    return exampleRule;
    }
    $scope.rule = getTheRule(ruleToEdit);
    
    $scope.rule.bp =  {sID:$scope.rule.sID_BP, sName:$scope.rule.sID_BP};  
    $scope.processesList = [$scope.rule.bp];
    $scope.ruleBpIsIncorrect = false;

    processes.getUserProcesses().then(function (data) {
      $scope.processesList = data;
      if ($scope.processesList != '' && $scope.processesList.length > 0) {
        $scope.ruleBpIsIncorrect = $scope.processesList.every(function (process) {
          if ($scope.rule.bp.sID == process.sID){
            $scope.rule.bp = process;
            return false;
          }
          return true;
        });
        
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
 
    $scope.save = function () {
      // var ruleToSave = converter.convert($scope.slot);
      $modalInstance.close($scope.rule);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };    
  });
