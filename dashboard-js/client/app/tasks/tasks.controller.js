'use strict';

angular.module('dashboardJsApp')
  .controller('TasksCtrl', function ($scope, tasks) {
    tasks
      .list()
      .then(function (result) {
        result = JSON.parse(result);
        $scope.tasks = result.data;
      })
      .catch(function (err) {
        $scope.errors.other = err.message;
      });
  });
