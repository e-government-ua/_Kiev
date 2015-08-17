'use strict';

angular.module('dashboardJsApp')
  .controller('CalendarCtrl', function ($scope, $modal, schedule, bpForSchedule) {

    $scope.inProgress = false;


    $scope.request = {
      bAll: true,
      nDays: 1,
      sDateStart: new Date()
    };

    $scope.days = [];

    var getFormattedToTime = function(date, slot){
      var time = moment(date + ' ' + slot.sTime);
      time.add(slot.nMinutes, 'minutes');
      return time.format('HH:mm');
    };

    var formatDate = function(day){
      var date = moment(new Date(day.sDate));

      day.sWeekDay = date.format('dddd');
      day.sFormattedDate = date.format('D MMMM YYYY');

    };

    var formatData = function(days){
      for (var i = 0; i < days.length; i++){
        var day = days[i]

        formatDate(day);

        for (var j = 0; j < day.aSlot.length; j++){
          var slot = day.aSlot[j];
          slot.sToTime = getFormattedToTime(day.sDate, slot);
        }
      }
    };

    $scope.get= function(){
      var date = moment($scope.request.sDateStart);
      schedule.getFlowSlots(bpForSchedule.bp.chosenBp.sID,
                            $scope.request.bAll,
                            $scope.request.nDays,
                            date.format('YYYY-MM-DD')
                            )
        .then(function(data){
          var days = data.aDay;
          formatData(days);
          $scope.days = days;
        })
    };

    $scope.$on('bpChangedEvent', function () {
      $scope.get();
    })
  });
