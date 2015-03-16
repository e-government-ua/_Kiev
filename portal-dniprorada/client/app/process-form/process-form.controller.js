'use strict';

angular.module('portalDniproradaApp')
  .controller('ProcessFormCtrl', function ($scope, $routeParams) {
    $scope.processDefinitionId = $routeParams.processDefinitionId;
  });
