'use strict';

angular.module('dashboardJsApp')
  .factory('processes', function processes($http, $q) {
    var idToProcessMap = function (data) {
      var map = {};
      for (var i = 0; i < data.length; i++) {
          //"_test_dependence_form:2:87617"
          //var sID=data[i].id;
          var sKey=data[i].key;
          //var nAt=sID.indexOf("\:")
          //if(nAt)
          //sID=
          map[sKey] = data[i];
//          console.log("i="+i+",sKey="+sKey);
        //map[data[i].id] = data[i];
      }
      return map;
    };

    //var processesDefinitions;
    var processesDefinitions = null;

    return {
      getUserProcesses: function () {

        return $http.get('/api/processes/getLoginBPs')
          .then(function (response) {
            try {
              var result = JSON.parse(response.data);
              return result;
            }
            catch (error) {
              //response.data = "error";
              return result;
            }
          });
      },

      list: function (callback) {
        var cb = callback || angular.noop;
        var deferred = $q.defer();

        //if (processesDefinitions && processesDefinitions!=null) {
        if (processesDefinitions !== null) {
          console.log("processesDefinitions(!== null)="+processesDefinitions);
          deferred.resolve(processesDefinitions);
        } else {
          console.log("processesDefinitions(=== null)="+processesDefinitions);


          var req = {
            method: 'GET',
            url: '/api/processes',
            cache: true,
            data: {}
          };

          $http(req).
            success(function (result) {
              console.log("result="+result);
              //console.log("JSON.parse(result)="+JSON.parse(result));
//              processesDefinitions = idToProcessMap(JSON.parse(result).data);
              processesDefinitions = idToProcessMap(result.data);
              console.log("processesDefinitions(reloaded)="+processesDefinitions);

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

        /*var deferred = $q.defer();
        if (processesDefinitions === null) {
          var req = {
            method: 'GET',
            url: '/api/processes',
            cache: true,
            data: {}
          };
          $http(req).
            success(function (result) {
              console.log("1JSON.parse(result)="+JSON.parse(result));
              processesDefinitions = idToProcessMap(JSON.parse(result).data);
              console.log("processesDefinitions(reloaded)="+processesDefinitions);

              deferred.resolve(processesDefinitions);
              return cb();
            }).
            error(function (err) {
              deferred.reject(err);
              return cb(err);
            }.bind(this));
        }*/


//        processes.list().then(function (processesDefinitions) {
          //$scope.applyTaskFilter($scope.$storage.menuType);
            var sID=processDefinitionId;
//            console.log("[getProcessDescription]sID(before)="+sID);
//            console.log("[getProcessDescription]processesDefinitions="+processesDefinitions);
            if(sID === null || sID === undefined){
                return sID;
            }
            //if(sID!==null){//"_test_dependence_form:2:87617"
              var nAt=sID.indexOf("\:");
              if(nAt>=0){
                sID=sID.substr(0,nAt);
              }
            //}
//            console.log("[getProcessName]sID(after)="+sID);
            /*if (processesDefinitions && processesDefinitions[processDefinitionId]) {
              return processesDefinitions[processDefinitionId].name;
            } else {
              return processDefinitionId;
            }*/
            if (processesDefinitions && processesDefinitions[sID]) {
              return processesDefinitions[sID].name;
            } else {
              return sID;//+"("+processesDefinitions.length+")";
            }

//        }).catch(function (err) {
          //err = JSON.parse(err);
          //$scope.error = err;
//        });


      },

      getProcessDescription: function (processDefinitionId) {

        /*var deferred = $q.defer();
        if (processesDefinitions === null) {
          var req = {
            method: 'GET',
            url: '/api/processes',
            cache: true,
            data: {}
          };
          $http(req).
            success(function (result) {
              console.log("1JSON.parse(result)="+JSON.parse(result));
              processesDefinitions = idToProcessMap(JSON.parse(result).data);
              console.log("processesDefinitions(reloaded)="+processesDefinitions);

              deferred.resolve(processesDefinitions);
              return cb();
            }).
            error(function (err) {
              deferred.reject(err);
              return cb(err);
            }.bind(this));
        }*/

//        processes.list().then(function (processesDefinitions) {
          //$scope.applyTaskFilter($scope.$storage.menuType);

            var sID=processDefinitionId;
//            console.log("[getProcessDescription]sID(before)="+sID);
//            console.log("[getProcessDescription]processesDefinitions="+processesDefinitions);
            if(sID === null || sID === undefined){
                console.log("[getProcessDescription]sID is null!");
                return sID;
            }
            //if(sID!==null){//"_test_dependence_form:2:87617"
              var nAt=sID.indexOf("\:");
              if(nAt>=0){
                sID=sID.substr(0,nAt);
              }
            //}
//            console.log("[getProcessDescription]sID(after)="+sID);
            /*if (processesDefinitions && processesDefinitions[processDefinitionId]) {
              return processesDefinitions[processDefinitionId].description;
            } else {
              return processDefinitionId;ґ
            }*/
            if (processesDefinitions && processesDefinitions[sID]) {
              return processesDefinitions[sID].description;
            } else {
              return sID;//+"("+processesDefinitions.length+")";
            }

//        }).catch(function (err) {
          //err = JSON.parse(err);
          //$scope.error = err;
//        });

      }
    };
  });
