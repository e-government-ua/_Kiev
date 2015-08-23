'use strict';

angular.module('dashboardJsApp')
  .controller('ScheduleModalController', function($scope, $modalInstance, slotToEdit) {

    var dateRangePickerLocale = {
      format: 'DD.MM.YYYY',
      separator: " - "
    };

    $scope.singleDateRangePickerOptions = {
      singleDatePicker: true,
      locale: dateRangePickerLocale
    };

    $scope.dateRangePickerOptions = {
      singleDatePicker: false,
      locale: dateRangePickerLocale
    };

    $scope.durationTypes = {
      endless: { label: 'Безстроково'},
      noEnd: { label: 'Без терміну закінчення'},
      noBeginning:{ label: 'Без терміну початку'},
      beginningAndEnd: { label: 'З терміном початку і терміном кінця'}
    };

    var createArrayStartingWithOne = function(numberOfElements){
      var arr = [];
      for (var i = 0; i <= numberOfElements; i++ ){
        arr[i] = i;
      }
      return arr;
    };

    $scope.daysAvailableValues = createArrayStartingWithOne(31);
    $scope.hoursAvailableValues = createArrayStartingWithOne(24);
    $scope.minutesAvailableValues = createArrayStartingWithOne(60);

    var parser = function(){
      var createTimeObject = function(timeString){
        var arr = timeString.split(':');
        var date = new Date();
        date.setHours(arr[0], arr[1]);
        return date;
      };

      var getTimeRange = function(sRegionTime){
        if (sRegionTime) {
          var arr = sRegionTime.split('-');
          return {
            from: createTimeObject(arr[0]),
            to: createTimeObject(arr[1])
          }
        }
      };

      var getDateRange = function(slot){
        if (slot.sDateTimeAt && slot.sDateTimeTo){
          return {
            type: $scope.durationTypes.beginningAndEnd,
            range: {
              startDate: new Date(slot.sDateTimeAt),
              endDate: new Date(slot.sDateTimeTo)
            }
          };
        }
        if (slot.sDateTimeAt){
          return {
            type: $scope.durationTypes.noEnd,
            range: {
              startDate: new Date(slot.sDateTimeAt),
              endDate: null
            }
          };
        }
        if (slot.sDateTimeTo){
          return {
            type: $scope.durationTypes.noBeginning,
            range: {
              startDate: new Date(slot.sDateTimeTo),
              endDate: null
            }
          };
        }
        return {
          type: $scope.durationTypes.endless,
          range:{
            startDate: null,
            endDate: null
          }
        };
      };

      function getPartition(lengthOfPartition) {
        if (lengthOfPartition) {
          var duration = moment.duration(lengthOfPartition, "minutes");
          return {
            isDividedIntoSlots: true,
            minutes: duration.minutes(),
            hours: duration.hours(),
            days: duration.days()
          };
        }
        else{
          return {
            isDividedIntoSlots: false
          }
        }
      }

      return {
        parse: function (slotToEdit) {
          if (slotToEdit){
            return {
              id: slotToEdit.nID,
              name: slotToEdit.sName,
              timeRange: getTimeRange(slotToEdit.sRegionTime),
              dateRange: getDateRange(slotToEdit),
              weekDays: slotToEdit.saRegionWeekDay,
              partition: getPartition(slotToEdit.nLen),
              scheduleFormula: slotToEdit.sData
            };
          }else{
            return {
              timeRange:{
                from: createTimeObject('09:00'),
                to: createTimeObject('17:00')
              },
              dateRange: {
                type: $scope.durationTypes.endless,
                range: {
                  startDate: null,
                  endDate: null
                }
              },
              weekDays: {
                mo: true,
                tu: true,
                we: true,
                th: true,
                fr: true,
                sa: false,
                su: false
              },
              partition: {
                isDividedIntoSlots: true,
                minutes: 15,
                hours: 0,
                days: 0
              },
              scheduleFormula: ''
            }
          }
        }
      }
    }();

    $scope.atLeastOneWeekDayChosen = function () {
      if (!$scope.slot.weekDays) {
        return false;
      }

      return Object.keys($scope.slot.weekDays)
        .some(function (key) {
          return $scope.slot.weekDays[key];
        }
      );
    };

    $scope.timeOrderIsCorrect = function(){
      var at = new Date( $scope.slot.timeRange.from);
      var to = new Date( $scope.slot.timeRange.to);
      return at < to;
    };

    $scope.showSave = function(){
      return $scope.atLeastOneWeekDayChosen() && $scope.timeOrderIsCorrect();
    };

    var converter = function(){
      var getRegionTime = function(slot){
        var from = moment(slot.timeRange.from).format('HH:mm');
        var to = moment(slot.timeRange.to).format('HH:mm');
        return from + '-' + to;
      };

      var getDateForSaving = function(date){
        if(date){
          return moment(date).format('YYYY-MM-DD');
        }
        return undefined;
      };

      var saveDatesFromDuration = function(slot, slotToSave){
        if (slot.dateRange.type === $scope.durationTypes.beginningAndEnd){
          slotToSave.sDateTimeAt = getDateForSaving(slot.dateRange.range.startDate);
          slotToSave.sDateTimeTo = getDateForSaving(slot.dateRange.range.endDate);
          return;
        }
        if (slot.dateRange.type === $scope.durationTypes.noEnd){
          slotToSave.sDateTimeAt = getDateForSaving(slot.dateRange.range.startDate);
          slotToSave.sDateTimeTo = undefined;
          return;
        }
        if (slot.dateRange.type === $scope.durationTypes.noBeginning){
          slotToSave.sDateTimeAt = undefined;
          slotToSave.sDateTimeTo = getDateForSaving(slot.dateRange.range.startDate);
          return;
        }
        if (slot.dateRange.type === $scope.durationTypes.endless){
          slotToSave.sDateTimeAt = undefined;
          slotToSave.sDateTimeTo = undefined;
        }
      };

      var saveSlotDuration = function(slot, slotToSave){
        if (slot.partition.isDividedIntoSlots){
          var duration = moment.duration({
            minutes: slot.partition.minutes,
            hours: slot.partition.hours,
            days: slot.partition.days
          });
          slotToSave.nLen = duration.asMinutes();
          slotToSave.sLenType = 'Minute';
        }
        else{
          slotToSave.nLen = 0;
          slotToSave.sLenType = 'Minute';
        }
      };

      return {
        convert: function(slot){
          var slotToSave = {
            nID: slot.id,
            sName: slot.name,
            sRegionTime: getRegionTime(slot),
            saRegionWeekDay: slot.weekDays,
            sData: slot.scheduleFormula
          };
          saveDatesFromDuration(slot, slotToSave);
          saveSlotDuration(slot, slotToSave);
          return slotToSave;
        }
      }
    }();

    $scope.save = function () {
      var slotToSave = converter.convert($scope.slot);
      $modalInstance.close(slotToSave);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    //Init
    $scope.slot = parser.parse(slotToEdit);
  });
