'use strict';

angular.module('dashboardJsApp')
  .factory('services', function services($http, $q, $rootScope, uiUploader) {

    var stringToArray = function(str){
      var weekDays = {
        mo: false,
        tu: false,
        we: false,
        th: false,
        fr: false,
        sa: false,
        su: false
      };

      var keys = str.split(",");
      keys.forEach(function(key){
        weekDays[key] = true;
      });

      return weekDays;
    };


    var clearAndConvert = function (slot){
      slot.sDateTimeAt = slot.sDateTimeAt.replace(/['"]+/g, '');
      slot.sDateTimeTo = slot.sDateTimeTo.replace(/['"]+/g, '');
      slot.saRegionWeekDay = slot.saRegionWeekDay.replace(/['"]+/g, '');

      slot.saRegionWeekDay = stringToArray(slot.saRegionWeekDay);
    }

    var arrayToString = function(weekDays){
      var str = "";
      var keys = Object.keys(weekDays);

      keys.forEach(function(key){
        if (weekDays[key]){
          str += key + ',';
        }
      });

      return str.substring(0, str.length - 1);;
    };


    return {
      filterTypes: {
        selfAssigned: 'selfAssigned',
        unassigned: 'unassigned',
        finished: 'finished'
      },
      /**
       * Get service slots grouped by days
       *
       * @param  {Function} callback - optional
       * @return {Promise}
       */
      getServiceSlots: function(nID_ServiceData, bAll, nDays, sDate, callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        var request = {
          method: 'GET',
          url: '/api/services',
          data: {},
          params: {
            nID_ServiceData: nID_ServiceData,
            bAll: bAll,
            nDays: nDays,
            sDate: sDate,
          }
        };

        $http(request).
          success(function(data) {
            var data = angular.fromJson(data);
            deferred.resolve(data);
            return cb();
          }).
          error(function(err) {
            deferred.reject(err);
            return cb(err);
          }.bind(this));

        return deferred.promise;
      },
      /**
       * Get service slots grouped by days
       *
       * @param  {Function} callback - optional
       * @return {Promise}
       */
      getSchedule: function(sID_BP, callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        var request = {
          method: 'GET',
          url: '/api/services/schedule',
          data: {},
          params: {
            sID_BP : sID_BP
          }
        };

        $http(request).
          success(function(data) {
            var slots = angular.fromJson(data);
            slots.forEach(clearAndConvert);
            deferred.resolve(slots);
            return cb();
          }).
          error(function(err) {
            deferred.reject(err);
            return cb(err);
          }.bind(this));

        return deferred.promise;
      },
      setSchedule: function(sID_BP, slot, callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        var stringWeekDays = arrayToString(slot.saRegionWeekDay);

        var request = {
          method: 'POST',
          url: '/api/services/schedule',
          data: {},
          params: {
            sID_BP: sID_BP,
            sName: slot.sName,
            sRegionTime: slot.sRegionTime,
            saRegionWeekDay: stringWeekDays,
            sDateTimeAt: slot.sDateTimeAt,
            sDateTimeTo: slot.sDateTimeTo
          }
        };

        $http(request).
          success(function (data) {
            var slot = angular.fromJson(data);
            clearAndConvert(slot);
            deferred.resolve(slot);
            return cb();
          }).
          error(function (err) {
            deferred.reject(err);
            return cb(err);
          }.bind(this));

        return deferred.promise;
      }
    };
  });
