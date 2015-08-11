'use strict';

angular.module('dashboardJsApp')
  .controller('ServicesCtrl', function ($scope, $modal, services, processes) {
    $scope.statistic = {};
    $scope.statistic.sBP = 'kiev_mreo_1';

    $scope.list = {};

    processes.getUserProcesses().then(function (data) {
      $scope.processesList = data;
      if ($scope.processesList.length > 0) {
        $scope.statistic.sBP = $scope.processesList[0].sID;
        $scope.workingHours.change();
      }
    });

    //   Список рабочего времени недели
      $scope.workingHours = (function(){

        var schedule = [];

        var fillData = function(){
          services.getSchedule($scope.statistic.sBP)
            .then(function(slots){
              schedule = slots;
            })
        };

        var openModal = function (slot) {

          var modalInstance = $modal.open({
            animation: true,
            templateUrl: 'app/services/working_hours/modal.html',
            controller: 'WorkingHoursModalController',
            resolve: {
              inputData: function () {
                return angular.copy(slot);
              }
            }
          });

          modalInstance.result.then(function (editedSlot) {
            services.setSchedule($scope.statistic.sBP, editedSlot)
              .then(function (createdSlot) {
                for (var i = 0; i < schedule.length; i ++){
                  if (schedule[i].nID === createdSlot.nID){
                    schedule[i] = createdSlot;
                    return;
                  }
                }

                schedule.push(createdSlot);
            });
          });
        };

      return{
        change: function(){
          fillData();
        },
        get: function(){
          return schedule;
        },
        add: function(){
          openModal();

          // flow/setSheduleFlowInclude
        },
        edit: function(scheduleItem){
          openModal(scheduleItem);
          // flow/setSheduleFlowInclude
        },
        delete: function(slot){
          services.deleteSchedule($scope.statistic.sBP, slot)
            .then(fillData);
        }
      }
    })();


    $scope.getSlots = function(){
      var id = $scope.list.aDay[0].aSlot[0].nID

      services.getIncludes(id).then(function(data){

        $scope.includes = JSON.parse(data);
      })
    }

    $scope.createSlot = function(){

    }

    //$scope.list = services.getServiceSlots($scope.statistic.sBP)
    //services.getServiceSlots(1).then(function(data){
      //$scope.list = JSON.parse(data);
    //})



    //  Список исключений рабочего времени дат
    $scope.exclusions = (function(){

      var openModal = function () {

        $modal.open({
          animation: true,
          templateUrl: 'app/services/exclusions/modal.html'
        });
      };

      var fillData = function(){
        // /flow/getSheduleFlowExcludes
      }

      return{
        add: function(){
          openModal();

          // flow/setSheduleFlowExclude
        },
        edit: function(row){
          // flow/setSheduleFlowExclude
        },
        delete: function(row){
          // flow/removeSheduleFlowExclude
        }
      }
    })();

    $scope.workingHours.change();
  });
