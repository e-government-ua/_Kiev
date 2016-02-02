angular.module('dashboardJsApp')
  .directive('schedule', function(){

    var controller = function($scope, $modal, bpForSchedule) {

      var isSlotsPresent = false;
      var inProgress = false;

      var slots = [];

      var getFunc = $scope.funcs.getFunc;
      var setFunc = $scope.funcs.setFunc;
      var deleteFunc = $scope.funcs.deleteFunc;

      var setDuration = function(slot){
        if (slot.sDateTimeAt && slot.sDateTimeTo){
          var from = moment(slot.sDateTimeAt).format('YYYY.MM.DD HH:mm');
          var to = moment(slot.sDateTimeTo).format('YYYY.MM.DD HH:mm');
          slot.duration = 'З ' + from + ' по ' + to;
          return;
        }
        if (slot.sDateTimeAt){
          slot.duration = 'З ' + moment(slot.sDateTimeAt).format('YYYY.MM.DD HH:mm') + ' безстроково';
          return;
        }
        if (slot.sDateTimeTo){
          slot.duration = 'Безстроково до ' + moment(slot.sDateTimeTo).format('YYYY.MM.DD HH:mm');
          return;
        }
        slot.duration = 'Безстроково';
      };

      var fillData = function () {
        inProgress = true;
        isSlotsPresent = false;
        getFunc(bpForSchedule.bp.chosenBp.sID, bpForSchedule.bp.chosenDepartment.nID)
          .then(function (data) {
            slots = data;
            angular.forEach(slots, setDuration);
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
          setFunc(bpForSchedule.bp.chosenBp.sID, bpForSchedule.bp.chosenDepartment.nID, editedSlot)
            .then(function (createdSlot) {
              setDuration(createdSlot);

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

      $scope.isInProgress = function () {
        return inProgress;
      };

      $scope.isShowData = function () {
        return !inProgress && isSlotsPresent;
      };

      $scope.isShowWarning = function () {
        return !inProgress && !isSlotsPresent;
      };

      $scope.get = function () {
        return slots;
      };

      $scope.add = function () {
        openModal();
      };

      $scope.edit = function (slot) {
        openModal(slot);
      };

      $scope.copy = function (slot) {
        var slotCopy = angular.copy(slot);
        slotCopy.nID = null;
        openModal(slotCopy);
      };

      $scope.delete = function (slot) {
        deleteFunc(bpForSchedule.bp.chosenBp.sID, bpForSchedule.bp.chosenDepartment.nID, slot.nID)
          .then(fillData);
      };

      // Init:

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
      templateUrl:'app/services/directives/schedule.html',
    }
  }
);
