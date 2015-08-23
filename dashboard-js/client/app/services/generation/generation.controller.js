'use strict';

angular.module('dashboardJsApp')
  .controller('GenerationCtrl', function ($scope, $modal, $timeout, schedule, bpForSchedule) {

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
      return $scope.opened = true;
    };

    $scope.isGenerated = false;
    $scope.isDeleted = false;

    var formatDateTime = function(dateTime){
      return moment(dateTime).format('YYYY-MM-DD HH:mm:ss.SSS');
    };

    $scope.generate= function(){
      var start = formatDateTime($scope.data.sDateStart);
      var stop = formatDateTime($scope.data.sDateStop);

      schedule.buildFlowSlots(bpForSchedule.bp.chosenBp.sID, start, stop)
      .then(function(){
        schedule.getExemptions(bpForSchedule.bp.chosenBp.sID)
          .then(function (data) {
            var exemptions = data;

            angular.forEach(exemptions, function(exemption){
              var start = formatDateTime(exemption.sDateTimeAt);
              var stop = formatDateTime(exemption.sDateTimeTo);

              schedule.deleteFlowSlots(bpForSchedule.bp.chosenBp.sID, start, stop, false)
                .then(function(){
                  $scope.isGenerated = true;
                })
            })
          });
      })
    };

    $scope.delete = function(){
      var start = formatDateTime($scope.data.sDateStart);
      var stop = formatDateTime($scope.data.sDateStop);

      schedule.deleteFlowSlots(bpForSchedule.bp.chosenBp.sID, start, stop, false)
        .then(function(){
          $scope.isDeleted = true;
        })
    };
  });
