'use strict';

angular.module('dashboardJsApp')
  .directive('tasksbar', function () {
    return {
      templateUrl: 'app/tasksbar/tasksbar.html',
      restrict: 'EA',
      link: function (scope, element, attrs) {
      }
    };
  });