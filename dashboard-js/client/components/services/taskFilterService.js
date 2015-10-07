angular.module('dashboardJsApp').service('taskFilterService', ['$filter', function($filter) {
  var taskDefinitions = [
      {name: 'Всі', id: 'all'},
      {name: 'Старт', id: 'usertask1'},
      {name: 'Обробка', id: 'usertask2'}
    ];
  var service = {
    getFilteredTasks: function(tasks, taskDefinition) {
      if (tasks === null) {
        return null;
      }
      if (tasks.length == 0) {
        return [];
      }
      if (!taskDefinition) {
        return tasks;
      }
      switch (taskDefinition.id) {
        case 'all':
          return tasks;
        break;
        case 'usertask1':
        case 'usertask2':
          var filteredTasks = tasks.filter(function(task, index) {
            if (!task.taskDefinitionKey) {
              return false;
            }
            if (task.taskDefinitionKey.substr(task.taskDefinitionKey.length-9) == taskDefinition.id) {
              return true;
            }
          });
          return filteredTasks;
        break;
        return null;
      }
    },
    getTaskDefinitions: function() {
      return taskDefinitions;
    }
  };
  return service;
}]);