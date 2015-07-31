'use strict';

angular.module('dashboardJsApp')
  .factory('processes', function processes($http, $q) {
    var idToProcessMap = function (data) {
      var map = {};
      for (var i = 0; i < data.length; i++) {
        map[data[i].id] = data[i];
      }
      return map;
    };

    var processesDefinitions;

    return {
      getUserProcesses: function () {

        return $http.get('/api/processes/getLoginBPs')
          .then(function (response) {
            return JSON.parse(response.data);
          });
      },

      list: function (callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        if (processesDefinitions) {
          deferred.resolve(processesDefinitions);
        } else {
          var req = {
            method: 'GET',
            url: '/api/processes',
            cache: true,
            data: {}
          };

          $http(req).
            success(function (result) {
              processesDefinitions = idToProcessMap(JSON.parse(result).data);
              deferred.resolve(processesDefinitions);
              return cb();
            }).
            error(function (err) {
              deferred.reject(err);
              return cb(err);
            }.bind(this));

        }

        return deferred.promise;
      },

      getProcessName: function (processDefinitionId) {
        if (processesDefinitions && processesDefinitions[processDefinitionId]) {
          return processesDefinitions[processDefinitionId].name;
        } else {
          return processDefinitionId;
        }
      },

      getProcessDescription: function (processDefinitionId) {
        if (processesDefinitions && processesDefinitions[processDefinitionId]) {
          return processesDefinitions[processDefinitionId].description;
        } else {
          return processDefinitionId;
        }
      }
    };
  });
