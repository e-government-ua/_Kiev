angular.module('dashboardJsApp')
  .directive('rules', function () {

    var controller = function ($scope, $modal, bpForSchedule) {

      //var getFunc = $scope.funcs.getFunc;
      var getAllFunc = $scope.funcs.getAllFunc;
      var setFunc = $scope.funcs.setFunc;
      var deleteFunc = $scope.funcs.deleteFunc;      
      
      // $scope.exampleRule = {
      //   id: 1,
      //   sID_BP: 'dnepr_spravka_o_doxodax',
      //   sID_UserTask: '*',
      //   sCondition: 'nElapsedDays==nDaysLimit',
      //   soData: 'nDaysLimit:3,asRecipientMail:[test@email.com]',
      //   sPatternFile: 'escalation/escalation_template.html',
      //   nID_EscalationRuleFunction: 'EscalationHandler_SendMailAlert',
      // };

      var openModal = function (rule) {
        var modalInstance = $modal.open({
          animation: true,
          templateUrl: 'app/escalations/modal/modal.html',
          controller: 'RuleEditorModalCtrl',
          resolve: {
            ruleToEdit: function () {
              return angular.copy(rule);
            }
          }
        });

        modalInstance.result.then(function (editedRule) {
            setFunc(editedRule)
            .then(function (editedRule) {
             console.log('fine');
             var i = 0;
             var ruleNotExistedBefore = $scope.rules.every(
               function(element){
                 if (element.nID == editedRule.nID){
                   $scope.rules[i] = editedRule;                   
                   return false;
                 }
                 i++;
                 return true;
               }
             );
             if (ruleNotExistedBefore) $scope.rules.push(editedRule);
             
            });
          
        });
      };

      
      $scope.rules = [];
      $scope.get = function () {
        return $scope.rules;
      };

      $scope.areRulesPresent = false;
      $scope.inProgress = false;


      $scope.isInProgress = function () {
        return $scope.inProgress;
      };

      $scope.isShowData = function () {
        return !$scope.inProgress && $scope.areRulesPresent;
      };

      $scope.getRules = function () {

      }

      $scope.isShowWarning = function () {
        return !$scope.inProgress && !$scope.isSlotsPresent;
      };

      $scope.add = function () {
        openModal();
      };

      $scope.edit = function (rule) {
        openModal(rule);
      };

      $scope.copy = function (rule) {

      };

      $scope.delete = function (rule) {
        deleteFunc(rule)
          .then($scope.fillData);
      };

      $scope.fillData = function () {

        $scope.inProgress = true;
        $scope.areRulesPresent = false;

        getAllFunc()
          .then(function (data) {
            $scope.rules = data;

            $scope.areRulesPresent = true;
          });
                          
      };
    };

    return {
      restrict: 'E',
      scope: {
        funcs: '='
      },
      controller: controller,
      templateUrl: 'app/escalations/rules/rules.html',
    }
  }
    );
