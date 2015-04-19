'use strict';

angular.module('dashboardJsApp')
  .factory('tasks', function tasks($http, $q) {

    return {
      /**
       * Get list of tasks
       *
       * @param  {Function} callback - optional
       * @return {Promise}
       */
      list: function(callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        var req = {
          method: 'GET',
          url: '/api/tasks',
          data: {
          }
        };

        $http(req).
          success(function(data) {
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
       * Get list of task events
       * @param taskId
       * @param  {Function} callback - optional
       * @return {Promise}
       */
      listTaskEvents: function(taskId, callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        var req = {
          method: 'GET',
          url: '/api/tasks/' + taskId + '/events',
          data: {
          }
        };

        $http(req).
          success(function(data) {
            deferred.resolve(data);
            return cb();
          }).
          error(function(err) {
            deferred.reject(err);
            return cb(err);
          }.bind(this));

        return deferred.promise;
      }

    };
  });
