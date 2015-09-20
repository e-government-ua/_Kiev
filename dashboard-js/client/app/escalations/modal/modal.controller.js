'use strict';

angular.module('dashboardJsApp')
  .controller('RuleEditorModalCtrl', function($scope, $modalInstance, ruleToEdit) {
    //Init
    $scope.rule = ruleToEdit;
    
   
 $scope.conditions = [{     
       id: 1,
       name: 'чиновник не відповідає ',
       parametersName:  function() {return this.parametersValue+ ' днів';},
       parametersValue: 10, //should be set by user, not read from source
       //paramsSelector: $scope.daysSelector()
     }];
      
      $scope.rule.condition = $scope.conditions[0];
      $scope.rule.condition.parametersValue = 10; //should be set by user, not read from source
      
      $scope.onConditionChanges = function(){
        //load condition and read available parameters for the condition
        
      }
 
  $scope.escalationActions = [{
        'nID': 1,
        name: 'відправити e-mail на ',
        //'fAction': $scope.sendEmail(),
        parameters: {default: 'katebutenko@gmail.com'},
        //'fParamsSelector': $scope.emailSelector()
      }, {
          'nID': 2,
          name: 'відправити sms на ',
          //'sAction': $scope.sendSms(),
          parameters: {default: '+380919008788'},
          //'fParamsSelector': $scope.smsSelector()
        }];
    $scope.rule.action = $scope.escalationActions[0];
          
    $scope.constants = function(){

      var createArray = function(numberOfElements){
        var arr = [];
        for (var i = 0; i <= numberOfElements; i++ ){
          arr[i] = i;
        }
        return arr;
      };

      return{
        separator: ' - ',
        durationTypes: {
          endless: { label: 'Безстроково'},
          noEnd: { label: 'Без терміну закінчення'},
          noBeginning:{ label: 'Без терміну початку'},
          beginningAndEnd: { label: 'З терміном початку і терміном кінця'}
        },
        daysAvailableValues: createArray(31),
        hoursAvailableValues: createArray(24),
        minutesAvailableValues: createArray(60)
      }
    }();

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
            type: $scope.constants.durationTypes.beginningAndEnd,
            range: slot.sDateTimeAt + $scope.constants.separator + slot.sDateTimeTo
          };
        }
        if (slot.sDateTimeAt){
          return {
            type: $scope.constants.durationTypes.noEnd,
            range: slot.sDateTimeAt
          };
        }
        if (slot.sDateTimeTo){
          return {
            type: $scope.constants.durationTypes.noBeginning,
            range: slot.sDateTimeTo
          };
        }
        return {
          type: $scope.constants.durationTypes.endless,
          range: ''
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
                type: $scope.constants.durationTypes.endless,
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

    var converter = function(){
      var getRegionTime = function(slot){
        var from = moment(slot.timeRange.from).format('HH:mm');
        var to = moment(slot.timeRange.to).format('HH:mm');
        return from + '-' + to;
      };

      var getDateForSaving = function(date){
        if(date){
          return moment(date).format('YYYY-MM-DD HH:mm:ss');
        }
        return undefined;
      };

      var saveDatesFromDuration = function(slot, slotToSave){
        if (slot.dateRange.type === $scope.constants.durationTypes.beginningAndEnd){
          var values = slot.dateRange.range.split($scope.constants.separator);
          slotToSave.sDateTimeAt = getDateForSaving(values[0]);
          slotToSave.sDateTimeTo = getDateForSaving(values[1]);
          return;
        }
        if (slot.dateRange.type === $scope.constants.durationTypes.noEnd){
          slotToSave.sDateTimeAt = getDateForSaving(slot.dateRange.range);
          slotToSave.sDateTimeTo = undefined;
          return;
        }
        if (slot.dateRange.type === $scope.constants.durationTypes.noBeginning){
          slotToSave.sDateTimeAt = undefined;
          slotToSave.sDateTimeTo = getDateForSaving(slot.dateRange.range);
          return;
        }
        if (slot.dateRange.type === $scope.constants.durationTypes.endless){
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

    $scope.onDurationTypeChange = function(dateRange){
      dateRange.range = '';
    };

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
     // return $scope.atLeastOneWeekDayChosen() && $scope.timeOrderIsCorrect();
     return true;
    };

    $scope.save = function () {
      var slotToSave = converter.convert($scope.slot);
      $modalInstance.close(slotToSave);
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };


   
    
  });
