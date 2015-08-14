'use strict';

angular.module('dashboardJsApp')
  .controller('FlowSlotsCtrl', function ($scope, $modal, schedule, bpForSchedule) {

    $scope.inProgress = false;

    var date = moment();
    var today = date.format('YYYY-MM-DD HH:mm:ss.SSS');
      date.add(5, 'days');
    var plusFiveDays = date.format('YYYY-MM-DD HH:mm:ss.SSS');

    $scope.data = {
      sDateStart: today,
      sDateStop: plusFiveDays,
    };

    $scope.generate= function(){
      schedule.buildFlowSlots(bpForSchedule.bp.chosenBp.sID,
        today,
        plusFiveDays)
      .then(function(data){
        var d = data;
      })
      .catch(function(error){
        var d = error;
      })
    };

    $scope.delete = function(){
      schedule.deleteFlowSlots(bpForSchedule.bp.chosenBp.sID,
                              $scope.data.sDateStart,
                              $scope.data.sDateStop, false)
        .then(function(data){
          var d = data;
        })
        .catch(function(error){
          var d = error;
        })
    };
  });
