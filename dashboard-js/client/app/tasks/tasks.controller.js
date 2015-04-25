'use strict';

angular.module('dashboardJsApp')
  .controller('TasksCtrl', function($scope, tasks, Modal, Auth) {
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

    $scope.isTaskSelected = function(task) {
      return $scope.selectedTask.id === task.id;
    };

    $scope.hasAttachment = function(){
      return $scope.taskAttachments !== undefined && $scope.taskAttachments !== null && $scope.taskAttachments.length !== 0;
    };

    $scope.downloadAttachment = function(){
      tasks.downloadDocument($scope.selectedTask.id);
    };

    $scope.applyTaskFilter = function(taskFilter) {
      $scope.selectedTask = null;
      $scope.taskForm = null;
      $scope.tasksFilter = taskFilter;
      tasks
        .list($scope.tasksFilter)
        .then(function(result) {
          result = JSON.parse(result);
          $scope.tasks = result.data;
          if ($scope.tasks[0]) {
            $scope.selectTask($scope.tasks[0]);
          }
        })
        .catch(function(err) {
          $scope.errors.other = err.message;
        });
    };

    $scope.applyTaskFilter(tasks.filterTypes.selfAssigned);

    $scope.selectTask = function(task) {
      $scope.selectedTask = task;
      $scope.taskForm = null;
      $scope.taskId = task.id;
      $scope.attachments = null;
      $scope.error = null;
      $scope.taskAttachments = null;

      tasks
        .taskForm(task.id)
        .then(function(result) {
          result = JSON.parse(result);
          $scope.taskForm = result.formProperties;
        })
        .catch(function(err) {
          err = JSON.parse(err);
          $scope.error = err;
        });

      tasks
        .taskAttachments(task.id)
        .then(function(result) {
          result = JSON.parse(result);
          $scope.attachments = result;
        })
        .catch(function(err) {
          // err = JSON.parse(err);
          // $scope.error = err;
          console.log(err)
        });

        tasks.getTaskAttachments(task.id)
        .then(function(result) {
          $scope.taskAttachments = result;
        })
        .catch(function(err) {
          $scope.error = err.message;
        });
    };

    $scope.submitTask = function() {
      if ($scope.selectedTask && $scope.taskForm) {
        tasks.submitTaskForm($scope.selectedTask.id, $scope.taskForm)
          .then(function(result) {
            Modal.inform.success(function(event) {
              $scope.applyTaskFilter($scope.tasksFilter);
            })('Форма відправлена : ' + result);

          })
          .catch(function(err) {
            Modal.inform.error()('Помилка. ' + err.code + ' ' + err.message);
          });
      }
    };

    $scope.assignTask = function() {
      tasks.assignTask($scope.selectedTask.id, Auth.getCurrentUser().id).then(function(result) {
          Modal.inform.success(function(event) {
              $scope.applyTaskFilter($scope.tasksFilter);
            })('Задача у вас в роботі');
        })
        .catch(function(err) {
           Modal.inform.error()('Помилка. ' + err.code + ' ' + err.message);
        });
    };
  });
