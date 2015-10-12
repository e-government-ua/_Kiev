'use strict';

angular.module('dashboardJsApp')
  .factory('tasks', function tasks($http, $q, $rootScope, uiUploader, $compile, $timeout, processes, $filter, PrintTemplateProcessor) {
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
        finished: 'finished',
        tickets: 'tickets'
      },
      /**
       * Get list of tasks
       *
       * @param  {Function} callback - optional
       * @return {Promise}
       */
      list: function(filterType, callback, params) {
        return simpleHttpPromise({
          method: 'GET',
          url: '/api/tasks',
          params: angular.merge({filterType: filterType}, params)
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

      submitTaskForm: function(taskId, formProperties, task) {
        var self = this;
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

        var deferred = $q.defer();

        // upload files before form submitting
        this.uploadTaskFiles(formProperties, task, taskId).then(function()
        {
          var submitTaskFormData = {
            'taskId': taskId,
            'properties': createProperties(formProperties)
          };

          var req = {
            method: 'POST',
            url: '/api/tasks/' + taskId + '/form',
            data: submitTaskFormData
          };

          simpleHttpPromise(req).then(function(result) {
            deferred.resolve(result);
          });
        });

        return deferred.promise;
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

      /**
       * Ф-ция загрузки файлов из принт-диалога в виде аттачей к форме
       * @param formProperties
       * @param task
       * @param taskId
       * @returns {deferred.promise|{then, always}}
       */
      uploadTaskFiles: function(formProperties, task, taskId) {
        // нужно найти все поля с тимом "file" и id, начинающимся с "PrintForm_"
        var filesFields = $filter('filter')(formProperties, function(prop){
          return prop.type == 'file' && /^PrintForm_/.test(prop.id);
        });
        // удалить после теста. пока что нет БП с таким полем и используем все поля с типом "файл".
        //if (filesFields.length == 0)
        //  filesFields = $filter('filter')(formProperties, {type:'file'});
        //
        var self = this;
        var deferred = $q.defer();
        var filesDefers = [];
        // загрузить все шаблоны
        angular.forEach(filesFields, function (fileField) {
          var defer = $q.defer();
          filesDefers.push(defer.promise);
          var patternFileName = fileField.name.split(';')[2];
          if (patternFileName) {
            patternFileName = patternFileName.replace(/^pattern\//, '');
            self.getPatternFile(patternFileName).then(function (result) {
              defer.resolve({
                fileField: fileField,
                template: result
              });
            });
          } else
            defer.resolve({
              fileField: fileField,
              template: ''
            });
        });
        // компиляция и отправка html
        $q.all(filesDefers).then(function(results){
          var uploadPromises = [];
          angular.forEach(results, function(templateResult){
            var scope = $rootScope.$new();
            scope.selectedTask = task;
            scope.taskForm = formProperties;
            scope.getPrintTemplate = function(){return PrintTemplateProcessor.getPrintTemplate(task, formProperties, templateResult.template);},
            scope.containsPrintTemplate = function(){return templateResult.template!='';}
            scope.getProcessName = processes.getProcessName;
            scope.sDateShort = function(sDateLong){
              if (sDateLong !== null) {
                var o = new Date(sDateLong);
                return o.getFullYear() + '-' + ((o.getMonth() + 1)>9?'':'0')+(o.getMonth() + 1) + '-' + (o.getDate()>9?'':'0')+o.getDate() + ' ' + (o.getHours()>9?'':'0')+o.getHours() + ':' + (o.getMinutes()>9?'':'0')+o.getMinutes();
              }
            };
            scope.sFieldLabel = function (sField) {
              var s = '';
              if (sField !== null) {
                var a = sField.split(';');
                s = a[0].trim();
              }
              return s;
            };
            scope.sEnumValue = function (aItem, sID) {
              var s = sID;
              _.forEach(aItem, function (oItem) {
                if (oItem.id == sID) {
                  s = oItem.name;
                }
              });
              return s;
            };
            var compiled = $compile('<print-dialog></print-dialog>')(scope);
            var defer = $q.defer();
            $timeout(function(){
              var html = '<html><head><meta charset="utf-8"></head><body>'+compiled.find('.print-modal-content').html()+'</body></html>';
              var data = {
                sDescription: 'User form',
                sFileName: 'User form.html',
                sContent: html
              };
              $http.post('/api/tasks/' + taskId + '/upload_content_as_attachment', data).success(function(uploadResult){
                templateResult.fileField.value = JSON.parse(uploadResult).id;
                defer.resolve();
              })
            });
            uploadPromises.push(defer.promise);
          });
          $q.all(uploadPromises).then(function(uploadResults){
            deferred.resolve();
          });
        });
        return deferred.promise;
      },

      getTask: function (taskId) {
        var deferred = $q.defer();

        var req = {
          method: 'GET',
          url: '/api/tasks/' + taskId,
          data: {}
        };

        $http(req).
          success(function (data) {
            deferred.resolve(data);
          }).
          error(function (err) {
            deferred.reject(err);
          }.bind(this));

        return deferred.promise;
      },
      getTasksByOrder: function(nID_Protected) {
        return simpleHttpPromise({
            method: 'GET',
            url: '/api/tasks/search/byOrder/' + nID_Protected
          }
        );
      },
      getTasksByText: function(sFind, sType) {
        return simpleHttpPromise({
            method: 'GET',
            url: '/api/tasks/search/byText/' + sFind + "/type/" + sType
          }
        );
      },
      getPatternFile: function(sPathFile) {
        return simpleHttpPromise({
            method: 'GET',
            url: '/api/tasks/getPatternFile?sPathFile=' + sPathFile
          }
        );
      }
    };
  });
