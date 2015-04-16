'use strict';

angular.module('dashboardJsApp')
  .config(function ($routeProvider) {
    $routeProvider
      .when('/tasks', {
        templateUrl: 'app/tasks/tasks.html',
        controller: 'TasksCtrl',
        access: {
            requiresLogin: true
        }
      });
  });
