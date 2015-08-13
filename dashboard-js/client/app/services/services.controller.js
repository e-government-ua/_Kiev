'use strict';

angular.module('dashboardJsApp')
  .controller('ServicesCtrl', function ($scope, $modal, schedule, processes) {
    var kievMreo1 = 'kiev_mreo_1';
    $scope.bp = {
      processes: [],
      chosenBp: {},
      change: function(){
        $scope.workingHours.refresh();
        $scope.exemptions.refresh();
      }
    };

    $scope.list = {};

    processes.getUserProcesses().then(function (data) {
      $scope.processesList = data;
      if ($scope.processesList.length > 0) {

        for (var i = 0; i < $scope.processesList.length; i++ ){
          if ($scope.processesList[i].sID === kievMreo1){
            $scope.bp.chosenBp = $scope.processesList[i];
            $scope.bp.change();
          }
        }
      }
    });

    var initTime = function(getFunc, setFunc, deleteFunc){
      var isSlotsPresent = false;
      var inProgress = false;

      var slots = [];

      var fillData = function(){
        inProgress = true;
        isSlotsPresent = false;
        getFunc($scope.bp.chosenBp.sID)
          .then(function(data){
            slots = data;
            isSlotsPresent = true;
          })
          .catch(function(){
            isSlotsPresent = false;
          })
          .finally(function() {
            inProgress = false;
          });
      };

      var openModal = function (slot) {
        var modalInstance = $modal.open({
          animation: true,
          templateUrl: 'app/services/working_hours/modal.html',
          controller: 'WorkingHoursModalController',
          resolve: {
            slotToEdit: function () {
              return angular.copy(slot);
            }
          }
        });

        modalInstance.result.then(function (editedSlot) {
          setFunc($scope.bp.chosenBp.sID, editedSlot)
            .then(function (createdSlot) {
              for (var i = 0; i < slots.length; i ++){
                if (slots[i].nID === createdSlot.nID){
                  slots[i] = createdSlot;
                  return;
                }
              }

              slots.push(createdSlot);
          });
        });
      };

      return{
        isInProgress: function(){
          return inProgress;
        },
        isShowData: function(){
          return !inProgress && isSlotsPresent;
        },
        isShowWarning: function(){
          return !inProgress && !isSlotsPresent;
        },
        refresh: function(){
          fillData();
        },
        get: function(){
          return slots;
        },
        add: function(){
          openModal();
        },
        edit: function(slot){
          openModal(slot);
        },
        delete: function(slot){
          deleteFunc($scope.bp.chosenBp.sID, slot.nID)
            .then(fillData);
        }
      }
    };

    $scope.workingHours = initTime(schedule.getSchedule, schedule.setSchedule, schedule.deleteSchedule)

    $scope.exemptions = initTime(schedule.getExemptions, schedule.setExemption, schedule.deleteExemption)

    $scope.flowSlots = (function(){
      var inProgress = false;

      var date = new Date();
      var today = date.toLocaleString();
      date.setDate(date.getDate() + 5);
      var plusFiveDays = date.toLocaleString();

      var data = {
        sDateStart: today,
        sDateStop: plusFiveDays,
      }

      return{
        data: data,
        isInProgress: function(){
          return inProgress;
        },
        isShowData: function(){
          return !inProgress;
        },
        generate: function(){
          schedule.buildFlowSlots($scope.bp.chosenBp.sID, data.sDateStart, data.sDateStop)
            .then(function(data){
              var d = data;
            })
            .catch(function(error){
              var d = error;
            })
        },
        delete: function(){
          schedule.deleteFlowSlots($scope.bp.chosenBp.sID, data.sDateStart, data.DateStop, false)
            .then(function(data){
                var d = data;
              })
            .catch(function(error){
              var d = error;
            })
        }
      }
    })();

    $scope.resultSchedule = (function(){
      var inProgress = false;

      var data = {
        bAll: false,
        nDays: 0,
        sDate: new Date().toLocaleString()
      }

      return{
        data: data,
        isInProgress: function(){
          return inProgress;
        },
        isShowData: function(){
          return !inProgress;
        },
        get: function(){
          schedule.getFlowSlots($scope.bp.chosenBp.sID, data.bAll, data.nDays, data.sDate)
            .then(function(data){
              var d = data;
            })
            .catch(function(error){
              var a = data;
            })
        }
      }
    })();

  });
