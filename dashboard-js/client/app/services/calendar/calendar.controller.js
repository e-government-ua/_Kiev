'use strict';

angular.module('dashboardJsApp')
  .controller('CalendarCtrl', function ($scope, $modal, schedule, bpForSchedule) {

    $scope.inProgress = false;
    $scope.isDatepickerOpened = false;

    $scope.openDatepicker = function(){
      $scope.isDatepickerOpened = true;
    };

    $scope.request = {
      bAll: false,
      nDays: 0,
      sDate: new Date().toLocaleString()
    };

    $scope.days = [];

    var getFormattedToTime = function(date, slot){
      var time = moment(date + ' ' + slot.sTime);
      time.add(slot.nMinutes, 'minutes');
      return time.format('HH:mm');
    };

    var formatDate = function(day){
      var date = moment(day.sDate);

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
      schedule.getFlowSlots(bpForSchedule.bp.chosenBp.sID,
                            $scope.request.bAll,
                            $scope.request.nDays,
                            $scope.request.sDate)
        .then(function(data){
          var days = data.aDay;
          formatData(days);
          $scope.days = days;
        })
    };
  });
