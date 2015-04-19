'use strict';

angular.module('dashboardJsApp')
  .directive('taskEvent', function (Auth, tasks) {
    return {
      templateUrl: 'app/taskEvent/taskEvent.html',
      restrict: 'EA',
      scope: {
        event: '='
      },
      link: function (scope, element, attrs) {
        var currentUser = Auth.getCurrentUser();
        var userName = currentUser.firstName + ' ' + currentUser.lastName;
        tasks.getEventMap().then(function(result) {
          var eventSettings = result[scope.event.action];
          scope.event.fullMessage = eventSettings.getFullMessage({name: userName}, scope.event.message);
        });
      }
    };
  });
