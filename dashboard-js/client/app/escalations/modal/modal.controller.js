'use strict';

angular.module('dashboardJsApp')
  .controller('RuleEditorModalCtrl', function($scope, $modalInstance, ruleToEdit, processes) {

    $scope.rule = ruleToEdit;
    $scope.rule.bp =  {sID:$scope.rule.sID_BP, sName:$scope.rule.sID_BP};  
    $scope.processesList = [$scope.rule.bp];


    processes.getUserProcesses().then(function (data) {
      $scope.processesList = data;
      if ($scope.processesList != '' && $scope.processesList.length > 0) {
        $scope.processesList.forEach(function () {
          if ($scope.rule.bp.sID == $scope.processesList[i].sID)
            $scope.rule.bp = $scope.processesList[i];
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
      // $modalInstance.close(ruleToSave);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };    
  });
