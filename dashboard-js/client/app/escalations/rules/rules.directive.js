angular.module('dashboardJsApp')
  .directive('rules', function () {

    var controller = function ($scope, $modal, bpForSchedule) {

      $scope.exampleRule = {
        id: 1,
        sID_BP: 'dnepr_spravka_o_doxodax',
        sID_UserTask: '*',
        sCondition: 'nElapsedDays==nDaysLimit',
        soData: 'nDaysLimit:3,asRecipientMail:[test@email.com]',
        sPatternFile: 'escalation/escalation_template.html',
        nID_EscalationRuleFunction: 'EscalationHandler_SendMailAlert',
      };

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

        modalInstance.result.then(function (editedSlot) {
          //update the rule and push
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

      };

      $scope.edit = function (slot) {
        openModal(slot);
      };

      $scope.copy = function (slot) {

      };

      $scope.delete = function (slot) {

      };

      $scope.fillData = function () {

        $scope.inProgress = true;
        $scope.areRulesPresent = false;
        
        //get the rules from the server
        $scope.rules = [
          $scope.exampleRule
        ]
        $scope.areRulesPresent = true;
        
        // getRules()
        //   .then(function (data) {
        //     rules = data;            
        //     areRulesPresent = true;
        //   })
        //   .catch(function () {
        //     areRulesPresent = false;
        //   })
        //   .finally(function () {
        //     inProgress = false;
        //   });
      };
    };

    return {
      restrict: 'E',
      scope: {
      },
      controller: controller,
      templateUrl: 'app/escalations/rules/rules.html',
    }
  }
    );
