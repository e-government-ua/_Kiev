'use strict';

angular.module('dashboardJsApp')
  .controller('TasksCtrl', function($scope, tasks) {
    $scope.menus = [{
      'title': 'В роботі',
      'type': tasks.filterTypes.selfAssigned
    }, {
      'title': 'Необроблені',
      'type': tasks.filterTypes.unassigned
    }, {
      'title': 'Оброблені',
      'type': tasks.filterTypes.finished
    }];

    $scope.isTaskFilterActive = function(taskType) {
      return $scope.tasksFilter === taskType;
    };

    $scope.applyTaskFilter = function(taskType) {
      $scope.taskForm = null;
      $scope.tasksFilter = taskType;
      tasks
        .list($scope.tasksFilter)
        .then(function(result) {
          result = JSON.parse(result);
          $scope.tasks = result.data;
          if($scope.tasks[0]){
             $scope.selectTask($scope.tasks[0]);
           }         
        })
        .catch(function(err) {
          $scope.errors.other = err.message;
        });
    };

    $scope.applyTaskFilter($scope.tasksFilter);

    $scope.selectTask = function(task) {
      $scope.selectedTask = task;
      $scope.taskForm = null;
      $scope.error = null;

      tasks
        .taskForm(task.id)
        .then(function(result) {
          result = JSON.parse(result);
          $scope.taskForm = result.formProperties;
        })
        .catch(function(err) {
          err = JSON.parse(err)
          $scope.error = err;
        });

      tasks
        .listTaskEvents(task.id)
        .then(function(result) {
          result = JSON.parse(result);
          $scope.events = result;
        })
        .catch(function(err) {
          $scope.error = err.message;
        });
    };
  });
