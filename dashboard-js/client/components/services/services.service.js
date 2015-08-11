'use strict';

angular.module('dashboardJsApp')
  .factory('services', function services($http, $q, $rootScope, uiUploader) {

    var stringToWeekDays = function(str){
      var weekDays = {
        mo: false,
        tu: false,
        we: false,
        th: false,
        fr: false,
        sa: false,
        su: false
      };

      var keys = str.split(',');
      keys.forEach(function(key){
        weekDays[key] = true;
      });

      return weekDays;
    };

    var clearDateTime = function(dateStr){
      var date = dateStr.replace(/['"]+/g, '');
      var parsed = Date.parse(date);
      var dateObject = new Date(parsed);
      return dateObject.toUTCString();
    };

    var clearAndConvert = function (slot){
      slot.sDateTimeAt = clearDateTime(slot.sDateTimeAt)
      slot.sDateTimeTo = clearDateTime(slot.sDateTimeTo)

      slot.saRegionWeekDay = slot.saRegionWeekDay.replace(/['"]+/g, '');
      slot.saRegionWeekDay = stringToWeekDays(slot.saRegionWeekDay);
    };

    var weekDaysToString = function(weekDays){
      var days = [];
      var keys = Object.keys(weekDays);

      keys.forEach(function(key){
        if (weekDays[key]){
          days.push(key);
        }
      });

      return days.join();
    };

    return {
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

        var stringWeekDays = weekDaysToString(slot.saRegionWeekDay);

        var request = {
          method: 'POST',
          url: '/api/services/schedule',
          params: {
            sID_BP: sID_BP,
            nID: slot.nID,
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
      },
      deleteSchedule: function(sID_BP, slot, callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        var request = {
          method: 'DELETE',
          url: '/api/services/schedule',
          params: {
            sID_BP: sID_BP,
            nID: slot.nID
          }
        };

        $http(request).
          success(function (data) {
            data = angular.fromJson(data);
            deferred.resolve(data);
            return cb();
          }).
          error(function (err) {
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
    };
  });
