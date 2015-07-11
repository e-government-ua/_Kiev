angular.module('documents').controller('DocumentsSearchController',
    function($scope, $state, types, operators, FileFactory, ServiceService, $modal) {

    $scope.typeId = 0;
    $scope.code = '';
    $scope.operatorId = 0;
    $scope.typeOptions = types;
    $scope.operatorOptions = operators;
    $scope.document = {};

    $scope.searchDocument = function(typeId, operatorId, code) {
        ServiceService.searchDocument(typeId, operatorId, code, '').then(function(data) {
            return $scope.document = data;
        });
    };
});
