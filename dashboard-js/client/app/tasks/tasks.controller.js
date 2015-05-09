'use strict';
angular.module('dashboardJsApp').controller('TasksCtrl', function($scope, tasks, processes, Modal, Auth) {
  $scope.tasks = [];
  $scope.selectedTasks = {};
  $scope.menus = [{
    title: 'В роботі',
    type: tasks.filterTypes.selfAssigned,
    count: 0
  }, {
    title: 'Необроблені',
    type: tasks.filterTypes.unassigned,
    count: 0
  }, {
    title: 'Оброблені',
    type: tasks.filterTypes.finished,
    count: 0
  }];

  $scope.isFormPropertyDisabled = function(formProperty) {
    if ($scope.selectedTask && $scope.selectedTask.assignee == null) {
      return true;
    } else if ($scope.selectedTask
      && $scope.selectedTask.assignee != null
      && !formProperty.writable) {
      return true;
    }
    return false;
  };

  $scope.isTaskFilterActive = function(taskType) {
    return $scope.tasksFilter === taskType;
  };

  $scope.isTaskSelected = function(task) {
    return $scope.selectedTask && $scope.selectedTask.id === task.id;
  };

  $scope.hasAttachment = function() {
    return $scope.taskAttachments !== undefined && $scope.taskAttachments !== null && $scope.taskAttachments.length !== 0;
  };

  $scope.downloadAttachment = function() {
    tasks.downloadDocument($scope.selectedTask.id);
  };

  $scope.applyTaskFilter = function(taskFilter) {
    $scope.selectedTask = $scope.selectedTasks[taskFilter];
    $scope.taskForm = null;
    $scope.tasksFilter = taskFilter;
    $scope.tasks = [];

    tasks
      .list($scope.tasksFilter)
      .then(function(result) {
        result = JSON.parse(result);
        $scope.tasks = result.data;
        updateTaskSelection();
      })
      .catch(function(err) {
        $scope.errors.other = err.message;
      });
  };

  $scope.selectTask = function(task) {
    $scope.selectedTask = task;
    $scope.selectedTasks[$scope.tasksFilter] = task;
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
        $scope.taskForm = addIndexForFileItems($scope.taskForm);
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
        console.log(err);
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

  $scope.sDateShort = function(sDateLong) {
    if (sDateLong !== null) {
      var o = new Date('2015-04-27T13:19:44.098+03:00');
      return o.getFullYear() + '-' + o.getMonth() + '-' + o.getDate() + ' ' + o.getHours() + ':' + o.getMinutes();
    }
  };

  $scope.getProcessName = function(processDefinitionId) {
    return processes.getProcessName(processDefinitionId);
  };

  $scope.init = function() {
    loadTaskCounters();
    loadSelfAssignedTasks();
  };

  function loadTaskCounters () {
    _.forEach($scope.menus, function(menu) {
      tasks.list(menu.type)
        .then(function(result) {
          result = JSON.parse(result);
          menu.count = result.data.length;
        });
    })
  }

  function loadSelfAssignedTasks () {
    processes.list().then(function(processesDefinitions) {
      $scope.applyTaskFilter(tasks.filterTypes.selfAssigned);
    }).catch(function(err) {
      err = JSON.parse(err);
      $scope.error = err;
    });
  }

  function addIndexForFileItems (val) {
    var idx = 0;
    return (val || []).map(function(item) {
      if (item.type == 'file') {
        item.nFileIdx = idx;
        idx++;
      }
      return item;
    });
  }

  function updateTaskSelection () {
    if ($scope.selectedTask) {
      $scope.selectTask($scope.selectedTask);
    } else if ($scope.tasks[0]) {
      $scope.selectTask($scope.tasks[0]);
    }
  }
});
