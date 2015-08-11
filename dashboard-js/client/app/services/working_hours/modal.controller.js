'use strict';

angular.module('dashboardJsApp')
  .controller('WorkingHoursModalController', function($scope, $modalInstance, inputData) {

    if (inputData){
      $scope.data = inputData;
    }else{
      $scope.data = {
        sDateTimeAt: '2000-01-01 08:00:00',
        sDateTimeTo: '2000-01-01 17:00:00'
      }
    }

    $scope.save = function () {
      $modalInstance.close($scope.data);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  });
