'use strict';

angular.module('dashboardJsApp')
  .factory('escalationsService', function services($http, $q) {


    var getRule = function(url, sID_BP, callback){
      var cb = callback || angular.noop;
      var deferred = $q.defer();

      var request = {
        method: 'GET',
        url: url,
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
    };

    var setRule = function(url, ruleToSet, callback){
      var cb = callback || angular.noop;
      var deferred = $q.defer();
      
      //var id = 0;
      //if (ruleToSet.nID != null && ruleToSet.nID != undefined) id = ruleToSet.nID;
      
      var request = {
        method: 'POST',
        url: url,
        params: {
          nID: ruleToSet.nID,
          sID_BP: ruleToSet.sID_BP,
          sID_UserTask: ruleToSet.sID_UserTask,
          sCondition: ruleToSet.sCondition,
          soData: ruleToSet.soData,
          sPatternFile: ruleToSet.sPatternFile,
          nID_EscalationRuleFunction: ruleToSet.nID_EscalationRuleFunction.nID,
        }
      };

      $http(request).
        success(function (data) {
          var slot = angular.fromJson(data);
          deferred.resolve(slot);
          return cb();
        }).
        error(function (err) {
          deferred.reject(err);
          return cb(err);
        }.bind(this));

      return deferred.promise;
    };


    var setEscalationFunctionFunc = function(url, ruleFunctionToSet, callback){
      var cb = callback || angular.noop;
      var deferred = $q.defer();
      
      //var id = 0;
      //if (ruleToSet.nID != null && ruleToSet.nID != undefined) id = ruleToSet.nID;
      
      var request = {
        method: 'POST',
        url: url,
        params: {
          nID: ruleFunctionToSet.nID,
          sName: ruleFunctionToSet.sName,
          sBeanHandler: ruleFunctionToSet.sBeanHandler         
        }
      };

      $http(request).
        success(function (data) {
          var slot = angular.fromJson(data);
          deferred.resolve(slot);
          return cb();
        }).
        error(function (err) {
          deferred.reject(err);
          return cb(err);
        }.bind(this));

      return deferred.promise;
    };
    
 var getAll = function(url, callback){
      var cb = callback || angular.noop;
      var deferred = $q.defer();

      var request = {
        method: 'GET',
        url: url,
        data: {},
        params: {
        }
      };

      $http(request).
        success(function(data) {
          var rules = angular.fromJson(data);
          
          deferred.resolve(rules);
          return cb();
        }).
        error(function(err) {
          deferred.reject(err);
          return cb(err);
        }.bind(this));

      return deferred.promise;
    };       
    
        var deleteRule = function(url, ruleToDelete, callback){
      var cb = callback || angular.noop;
      var deferred = $q.defer();

      var request = {
        method: 'DELETE',
        url: url,
        params: {
          nID: ruleToDelete.nID         
        }
      };

      $http(request).
        success(function (data) {         
          deferred.resolve(data);
          return cb();
        }).
        error(function (err) {
          deferred.reject(err);
          return cb(err);
        }.bind(this));

      return deferred.promise;
    };
    
    return {
      getRule: function(sID_BP, callback) {
        //return getRule('/api/escalations/getEscalationRule', sID_BP, callback);
      },
      getAllRules: function(callback){
        return getAll('/api/escalations/escalationRules', callback);
        
      },
      setRule: function(ruleToSet, callback) {
        return setRule('/api/escalations/escalationRules', ruleToSet, callback);
      },
      deleteRule: function(ruleToDelete, callback) {
        return deleteRule('/api/escalations/escalationRules', ruleToDelete, callback);
      },
     getAllEscalationFunctions: function(callback) {
        return getAll('/api/escalations/escalationFunctions', callback);
      },
      setEscalationFunctionFunc: function(ruleFunctionToSet, callback) {
        return setEscalationFunctionFunc('/api/escalations/escalationFunctions', ruleFunctionToSet, callback);
      },
      deleteEscalationFunctionFunc: function(ruleFunctionToDelete, callback) {
        return deleteRule('/api/escalations/escalationFunctions', ruleFunctionToDelete, callback);
      }
    };
  });

