'use strict';

angular.module('dashboardJsApp')
  .factory('tasks', function tasks($http, $q, $rootScope, uiUploader) {
    function simpleHttpPromise(req, callback) {
      var cb = callback || angular.noop;
      var deferred = $q.defer();

      $http(req).then(
        function(response) {
          deferred.resolve(response.data);
          return cb();
        },
        function(response) {
          deferred.reject(response);
          return cb(response);
        }.bind(this));
      return deferred.promise;
    }

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
        return simpleHttpPromise({
          method: 'GET',
          url: '/api/tasks',
          params: {
            filterType: filterType
          }
        }, callback);
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
        return simpleHttpPromise({
          method: 'PUT',
          url: '/api/tasks/' + taskId,
          data: {
            assignee: userId
          }
        }, callback);
      },

      downloadDocument: function(taskId, callback) {
        return simpleHttpPromise({
          method: 'GET',
          url: '/api/tasks/' + taskId + '/document'
        }, callback);
      },

      getTaskAttachments: function(taskId, callback) {
        return simpleHttpPromise({
          method: 'GET',
          url: '/api/tasks/' + taskId + '/attachments'
        }, callback);
      },

      taskForm: function(taskId, callback) {
        return simpleHttpPromise({
          method: 'GET',
            url: '/api/tasks/' + taskId + '/form'
        }, callback);
      },

      taskFormFromHistory: function(taskId, callback) {
        return simpleHttpPromise({
          method: 'GET',
          url: '/api/tasks/' + taskId + '/form-from-history'
        }, callback);
      },

      taskAttachments: function(taskId, callback) {
        return simpleHttpPromise({
          method: 'GET',
          url: '/api/tasks/' + taskId + '/attachments'
        }, callback);
      },

      submitTaskForm: function(taskId, formProperties, callback) {
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
        return simpleHttpPromise(req, callback);
      },
      upload: function(files, taskId) {
        var deferred = $q.defer();

        var self = this;
        var scope = $rootScope.$new(true, $rootScope);
        uiUploader.removeAll();
        uiUploader.addFiles(files);
        uiUploader.startUpload({
          url: '/api/tasks/' + taskId + '/attachments',
          concurrency: 1,
          onProgress: function(file) {
            scope.$apply(function() {

            });
          },
          onCompleted: function(file, response) {
            scope.$apply(function() {
              try {
                deferred.resolve({
                  file: file,
                  response: JSON.parse(response)
                });
              } catch (e) {
                deferred.reject({
                  err: response
                });
              }
            });
          }
        });

        return deferred.promise;
      },
      getTasksByOrder: function(nID_Protected) {
        return simpleHttpPromise({
            method: 'GET',
            url: '/api/tasks/getTasksByOrder',
            params: { nID_Protected: nID_Protected }
          }
        );
      }
    };
  });
