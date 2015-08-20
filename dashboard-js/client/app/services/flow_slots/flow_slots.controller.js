'use strict';

angular.module('dashboardJsApp')
  .controller('FlowSlotsCtrl', function ($scope, $modal, $timeout, schedule, bpForSchedule) {

    $scope.inProgress = false;

    var plusFiveDays = new Date();
    plusFiveDays.setDate(plusFiveDays.getDate() + 5);

    $scope.data = {
      sDateStart: moment().format('YYYY-MM-DD'),
      sDateStop: moment(plusFiveDays).format('YYYY-MM-DD')
    };

    $scope.opened = false;

    $scope.open = function($event) {
      $event.preventDefault();
      $event.stopPropagation();
      console.log('..');
      return $scope.opened = true;
    };

    $scope.result = [];

    var getFormattedToTime = function(slot){
      var time = moment(slot.sTime, 'HH:mm');
      time.add(slot.nMinutes, 'minutes');
      return time.format('HH:mm');
    };

    $scope.generate= function(){
      var start = moment($scope.data.sDateStart).format('YYYY-MM-DD HH:mm:ss.SSS');
      var stop = moment($scope.data.sDateStop).format('YYYY-MM-DD HH:mm:ss.SSS');

      schedule.buildFlowSlots(bpForSchedule.bp.chosenBp.sID, start, stop)
      .then(function(data){
          angular.forEach(data, function(slot){
            slot.sToTime = getFormattedToTime(slot);
          })
          $scope.result = data;
      })
      .catch(function(error){
        var result = error;
      })
    };

    $scope.delete = function(){
      var start = moment($scope.data.sDateStart).format('YYYY-MM-DD HH:mm:ss.SSS');
      var stop = moment($scope.data.sDateStop).format('YYYY-MM-DD HH:mm:ss.SSS');

      schedule.deleteFlowSlots(bpForSchedule.bp.chosenBp.sID, start, stop, false)
        .then(function(data){
          var result = data;
        })
        .catch(function(error){
          var result = error;
        })
    };
  });
