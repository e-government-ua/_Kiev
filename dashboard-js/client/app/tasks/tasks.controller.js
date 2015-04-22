'use strict';

angular.module('dashboardJsApp')
  .controller('TasksCtrl', function($scope, tasks) {

    tasks
      .list()
      .then(function(result) {
        result = JSON.parse(result);
        $scope.tasks = result.data;
        $scope.selectTask($scope.tasks[0].id);
      })
      .catch(function(err) {
        $scope.errors.other = err.message;
      });

    $scope.selectTask = function(taskId) {
      $scope.taskForm = null;
      
      tasks
        .taskForm(taskId)
        .then(function(result) {
          result = JSON.parse(result);
          $scope.taskForm = result.formProperties;
        })
        .catch(function(err) {
          $scope.errors.other = err.message;
        });

      tasks
        .listTaskEvents(taskId)
        .then(function(result) {
          result = JSON.parse(result);
          $scope.events = result;
        })
        .catch(function(err) {
          $scope.errors.other = err.message;
        });
    };
  });
