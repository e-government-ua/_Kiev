'use strict';

angular.module('dashboardJsApp')
  .controller('RuleEditorModalCtrl', function($scope, $modalInstance, ruleToEdit, processesList) {
        
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
    
        var getTheProcesses = function(a){
    return a;
    }
    
    $scope.rule = getTheRule(ruleToEdit);
    $scope.processes = getTheProcesses(processesList);
    
    $scope.rule.bp =  {sID:$scope.rule.sID_BP, sName:$scope.rule.sID_BP};  
    
    $scope.ruleBpIsIncorrect = false;    

 $scope.resolveBP = function () {
            if ($scope.processes != '' && $scope.processes.length > 0) {
        $scope.ruleBpIsIncorrect = $scope.processes.every(function (process) {
          if ($scope.rule.bp.sID == process.sID){
            $scope.rule.bp = process;
            return false;
          }
          return true;
        });
        
      }
    };
    
    $scope.save = function () {
      // var ruleToSave = converter.convert($scope.slot);
      $modalInstance.close($scope.rule);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };    
  });
