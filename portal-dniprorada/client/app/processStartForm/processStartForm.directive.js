'use strict';

angular.module('portalDniproradaApp')
  .directive('processStartForm', function () {
    return {
      templateUrl: 'app/processStartForm/processStartForm.html',
      restrict: 'EA',
      link: function (scope, element, attrs) {
      }
    };
  });