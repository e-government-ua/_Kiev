angular.module('dashboardJsApp')
  .directive('rules', function(){

   var controller = function ($scope, $modal, bpForSchedule) {         
      
     $scope.openModal = function (rule) {
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
          // setFunc(bpForSchedule.bp.chosenBp.sID, editedSlot)
          //   .then(function (createdSlot) {
          //     setDuration(createdSlot);

          //     for (var i = 0; i < slots.length; i++) {
          //       if (slots[i].nID === createdSlot.nID) {
          //         slots[i] = createdSlot;
          //         return;
          //       }
          //     }

          //     slots.push(createdSlot);
          //   });
        });
      };
      
      $scope.sendEmail = function(emailsArray){
        //send a request to the server to send the emails
        
      }
      $scope.emailSelector = function(){
        //open a dialog to write emails
      }
      $scope.sendSms = function(mobileNumbersArray){
        
      }
      $scope.smsSelector = function(mobileNumbersArray){
        //open a dialog to enter phone number(s)
      }    
       $scope.daysSelector = function(){
        //open a dialog to enter the number of days
      }    
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
        
      };

      $scope.copy = function (slot) {
       
      };

      $scope.delete = function (slot) {
       
      };

      // Init:

      // if (bpForSchedule.bp.chosenBp){
      //   fillData();
      // }

      $scope.$on('bpChangedEvent', function () {
         $scope.fillData();
       })
      
                 $scope.escalationActions = [{
       'nID': 1,
       name: 'відправити e-mail ',
       'fAction': $scope.sendEmail(),
       parametersName: 'наступним людям',
       'fParamsSelector': $scope.emailSelector()
     }, {
         'nID': 2,
         'sName': 'відправити sms',
         'sAction': $scope.sendSms(),
         'sParametersName': 'наступним людям',
         'fParamsSelector': $scope.smsSelector()
       }];

     $scope.conditions = [{
       id: 1,
       name: 'чиновник не відповідає ',
       parametersName: 'N днів',
       paramsSelector: $scope.daysSelector()
     }];
     
     $scope.getConditions = function(){
       return $scope.conditions;
     }
     $scope.exampleRule = {
       id: 1,
       sID_BP: 'dnepr_dovidka_pro_dohody',
       // 'sID_UserTask': '*',
       // 'sCondition': 'nElapsedDays == nDays',
       condition: $scope.conditions[0],
       action: $scope.escalationActions[0]
       // 'soData': "{nDays:10, asRecipientMail:['bvv4ik@gmail.com', 'askosyr@gmail.com']}",
       // 'sPatternFile': 'escalation/escalation_template.html',
       // 'nID_EscalationRuleFunction': '1'
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
      

     
    return{
      restrict: 'E',
      scope: {
      },
      controller: controller,
      templateUrl:'app/escalations/rules/rules.html',
    }
  }
);
