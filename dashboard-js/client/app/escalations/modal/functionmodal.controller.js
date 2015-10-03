'use strict';

angular.module('dashboardJsApp')
  .controller('FunctionEditorModalCtrl', function ($scope, $modalInstance, ruleFunctionToEdit) {

    var exampleRuleFunction = {

      sName: "відправити e-mail",
      sBeanHandler: "EscalationHandler_SendMailAlert"
    };

    var getTheRuleFunction = function (a) {
      if (a != null && a != undefined)
        return angular.copy(a);
      return exampleRuleFunction;
    }

    $scope.ruleFunction = getTheRuleFunction(ruleFunctionToEdit);

    $scope.saveRuleFunction = function () {      
      $modalInstance.close($scope.ruleFunction);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  });
