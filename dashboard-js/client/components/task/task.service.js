'use strict';

angular.module('dashboardJsApp')
  .factory('tasks', function tasks($http, $q) {

    return {
      filterTypes: {
        selfAssigned: 'selfAssigned',
        unassigned: 'unassigned',
        finished: 'finished'
      },
      /**
       * Get list of tasks
       *
       * @param  {Function} callback - optional
       * @return {Promise}
       */
      list: function(filterType, callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        var req = {
          method: 'GET',
          url: '/api/tasks',
          data: {},
          params: {
            filterType: filterType
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
          data: {}
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

      getEventMap: function() {
        var deferred = $q.defer();
        var eventMap = {
          'AddAttachment': {},
          'AddComment': {
            'messageTemplate': '${ user.name } відповів(ла): ${ message }',
            'getMessageOptions': function(messageObject) {
              return !_.isEmpty(messageObject) ? messageObject[0] : '';
            },
            'getFullMessage': function(user, messageObject) {
              return _.template(
                eventMap.AddComment.messageTemplate, {
                  'user': {
                    'name': user.name
                  },
                  'message': eventMap.AddComment.getMessageOptions(messageObject)
                }
              );
            }
          },
          'AddGroupLink': {},
          'AddUserLink': {
            'messageTemplate': '${ user.name } призначив(ла) : ${ message }',
            'getMessageOptions': function(messageObject) {
              return !_.isEmpty(messageObject) ? messageObject[0] : '';
            },
            'getFullMessage': function(user, messageObject) {
              return _.template(
                eventMap.AddUserLink.messageTemplate, {
                  'user': {
                    'name': user.name
                  },
                  'message': eventMap.AddUserLink.getMessageOptions(messageObject)
                }
              );
            }
          },
          'DeleteAttachment': {},
          'DeleteGroupLink': {},
          'DeleteUserLink': {}
        };

        deferred.resolve(eventMap);

        return deferred.promise;
      },

      assignTask: function(taskId, userId, callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        var req = {
          method: 'PUT',
          url: '/api/tasks/' + taskId,
          data: {
            assignee: userId
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

      downloadDocument: function(taskId, callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        var req = {
          method: 'GET',
          url: '/api/tasks/' + taskId + '/document',
          data: {}
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

      getTaskAttachments: function(taskId, callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        var req = {
          method: 'GET',
          url: '/api/tasks/' + taskId + '/attachments',
          data: {}
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

      taskForm: function(taskId, callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        var req = {
          method: 'GET',
          url: '/api/tasks/' + taskId + '/form',
          data: {}
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

      submitTaskForm: function(taskId, formProperties, callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        var createProperties = function(formProperties) {
          var properties = new Array();
          for (var i = 0; i < formProperties.length; i++) {
            var formProperty = formProperties[i];
            if (formProperty && formProperty.writable) {
              properties.push({
                id: formProperty.id,
                value: formProperty.value
              });
            }
          }
          return properties;
        };

        var submitTaskFormData = {
          'taskId': taskId,
          'properties': createProperties(formProperties)
        };

        var req = {
          method: 'POST',
          url: '/api/tasks/' + taskId + '/form',
          data: submitTaskFormData
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