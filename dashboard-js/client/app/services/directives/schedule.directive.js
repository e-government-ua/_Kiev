angular.module('dashboardJsApp')
  .directive('schedule', function(){

    var controller = function($scope, $modal, bpForSchedule ) {

      var self = this;

      var isSlotsPresent = false;
      var inProgress = false;

      var slots = [];

      var getFunc = $scope.funcs.getFunc;
      var setFunc = $scope.funcs.setFunc;
      var deleteFunc = $scope.funcs.deleteFunc;

      var fillData = function () {
        inProgress = true;
        isSlotsPresent = false;
        getFunc(bpForSchedule.bp.chosenBp.sID)
          .then(function (data) {
            slots = data;
            isSlotsPresent = true;
          })
          .catch(function () {
            isSlotsPresent = false;
          })
          .finally(function () {
            inProgress = false;
          });
      };

      var openModal = function (slot) {
        var modalInstance = $modal.open({
          animation: true,
          templateUrl: 'app/services/modal/modal.html',
          controller: 'ScheduleModalController',
          resolve: {
            slotToEdit: function () {
              return angular.copy(slot);
            }
          }
        });

        modalInstance.result.then(function (editedSlot) {
          setFunc(bpForSchedule.bp.chosenBp.sID, editedSlot)
            .then(function (createdSlot) {
              for (var i = 0; i < slots.length; i++) {
                if (slots[i].nID === createdSlot.nID) {
                  slots[i] = createdSlot;
                  return;
                }
              }

              slots.push(createdSlot);
            });
        });
      };

      self.isInProgress = function () {
        return inProgress;
      };

      self.isShowData = function () {
        return !inProgress && isSlotsPresent;
      };

      self.isShowWarning = function () {
        return !inProgress && !isSlotsPresent;
      };

      self.get = function () {
        return slots;
      };

      self.add = function () {
        openModal();
      };

      self.edit = function (slot) {
        openModal(slot);
      };

      self.delete = function (slot) {
        deleteFunc(bpForSchedule.bp.chosenBp.sID, slot.nID)
          .then(fillData);
      };

      // Init:

      if (bpForSchedule.bp.chosenBp){
        fillData();
      }

      $scope.$on('bpChangedEvent', function () {
        fillData();
      })
    };

    return{
      restrict: 'EA',
      scope: {
        funcs: '='
      },
      controller: controller,
      controllerAs: 'scheduleCtrl',
      templateUrl:'app/services/directives/schedule.html',
    }
  }
);
