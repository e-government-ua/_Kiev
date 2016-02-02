'use strict';

angular.module('dashboardJsApp')
  .controller('GenerationCtrl', function ($scope, $modal, schedule, bpForSchedule) {

    $scope.isBpChosen = false;

    $scope.generator = function(){

      var data = {
        range: moment().format('YYYY-MM-DD HH:mm:ss') + ' - ' + moment().add(7, 'days').format('YYYY-MM-DD HH:mm:ss'),
        isDeleteWithTickets: false,
        isGenerated: false,
        isDeleted: false,
        isGeneratedNothing: false,
        isGenerationError: false
      };

      var formatDateTime = function(dateTime){
        return moment(dateTime).format('YYYY-MM-DD HH:mm:ss.SSS');
      };

      var deleteExemptions = function(){
        schedule.getExemptions(bpForSchedule.bp.chosenBp.sID)
          .then(function (exemptions) {
            angular.forEach(exemptions, function(exemption){
              var start = formatDateTime(exemption.sDateTimeAt);
              var stop = formatDateTime(exemption.sDateTimeTo);

              schedule.deleteFlowSlots(bpForSchedule.bp.chosenBp.sID, start, stop, true)
                .then(function(){}, function(){
                  data.isGenerationError = true;
                })
            })
          });
      };

      var generate= function(){
        var values = data.range.split(' - ');
        var start = formatDateTime(values[0]);
        var stop = formatDateTime(values[1]);

        schedule.buildFlowSlots(bpForSchedule.bp.chosenBp.sID, start, stop)
          .then(
            function(generatedSlots){
              if (generatedSlots.length <= 0){
                data.isGeneratedNothing = true;
              }
              else{
                deleteExemptions();
                data.isGenerated = true;
              }
            },
            function(){
              data.isGenerationError = true;
            })
      };

      var del = function(){
        var values = data.range.split(' - ');
        var start = formatDateTime(values[0]);
        var stop = formatDateTime(values[1]);

        schedule.deleteFlowSlots(bpForSchedule.bp.chosenBp.sID, start, stop, data.isDeleteWithTickets)
          .then(function(){
            data.isDeleted = true;
          });
      };

      return{
        data: data,
        onDateChange: function() {
          data.isGenerated = false;
          data.isDeleted = false;
          data.isGeneratedNothing = false;
          data.isGenerationError = false;
        },
        generate: generate,
        del: del
      }
    }();

    $scope.calendar = function(){
      var request = {
        bAll: true,
        nDays: 1,
        sDateStart: new Date()
      };

      var data = {
        days: [],
        inProgress: false
      };

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

      var formatDays = function(days){
        angular.forEach(days, function(day){
          formatDate(day);
          angular.forEach( day.aSlot, function(slot){
            slot.sToTime = getFormattedToTime(day.sDate, slot);
          });
        });
      };

      var get = function(){
        data.inProgress = true;
        var date = moment(request.sDateStart);
        schedule.getFlowSlots(bpForSchedule.bp.chosenBp.sID, bpForSchedule.bp.chosenDepartment.nID, request.bAll, request.nDays, date.format('YYYY-MM-DD'))
          .then(function(obj){
            var days = obj.aDay;
            formatDays(days);
            data.days = days;
            data.inProgress = false;
          })
      };

      $scope.$on('bpChangedEvent', function () {
        get();
      });

      return {
        data: data,
        request: request,
        onChangeDate: get
      }
    }();

    // Init:

    if (bpForSchedule.bp.chosenBp){
      $scope.isBpChosen = true;
    }

    $scope.$on('bpChangedEvent', function () {
      $scope.isBpChosen = true;
    })
  });
