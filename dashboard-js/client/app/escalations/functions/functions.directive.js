angular.module('dashboardJsApp')
  .directive('functions', function () {

    var controller = function ($scope, $modal, processes) {

      var getAllFunctionsFunc = $scope.funcs.getAllFunctionsFunc;
      var setRuleFunctionFunc = $scope.funcs.setRuleFunctionFunc;
      var deleteRuleFunctionFunc = $scope.funcs.deleteRuleFunctionFunc;

      var openModal = function (ruleFunction) {
        var modalInstance = $modal.open({
          animation: true,
          templateUrl: 'app/escalations/modal/functionmodal.html',
          controller: 'FunctionEditorModalCtrl',
          resolve: {
            ruleFunctionToEdit: function () {
              return angular.copy(ruleFunction);
            },
          }
        });

        modalInstance.result.then(function (editedRuleFunction) {
          setRuleFunctionFunc(editedRuleFunction)
            .then(function (editedRuleFunction) {
              console.log('fine');
              var i = 0;
              var functionNotExistedBefore = $scope.ruleFunctions.every(
                function (element) {
                  if (element.nID == editedRuleFunction.nID) {
                    $scope.ruleFunctions[i] = editedRuleFunction;                   
                    return false;
                  }
                  i++;
                  return true;
                }
                );
              if (functionNotExistedBefore) $scope.ruleFunctions.push(editedRuleFunction);

            });

        });
      };


      $scope.ruleFunctions = [];
      $scope.get = function () {
        return $scope.ruleFunctions;
      };

      $scope.areRuleFunctionsPresent = false;

      $scope.add = function () {
        openModal();
      };

      $scope.edit = function (ruleFunction) {
        openModal(ruleFunction);
      };



      $scope.delete = function (rule) {
         deleteRuleFunctionFunc(rule)
           .then($scope.fillFunctionData);
      };


      $scope.fillFunctionData = function () {

        $scope.inProgress = true;
        $scope.areRulesPresent = false;


        getAllFunctionsFunc()
          .then(function (data) {
            $scope.ruleFunctions = data;

            $scope.areRuleFunctionsPresent = true;
          });


      };

      $scope.functionsLoaded = function () {
        if ($scope.ruleFunctions)
          return true;
        return false;
      }

      $scope.translate = function (text) {
        if (text == 'Отсылка уведомления на электронную почту') return 'відправити повідомлення на e-mail';
        return text;
      };

    };

    return {
      restrict: 'E',
      scope: {
        funcs: '='
      },
      controller: controller,
      templateUrl: 'app/escalations/functions/functions.html',
    }
  }
    );
