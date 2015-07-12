angular.module('documents').controller('DocumentsSearchController',
    function($scope, $state, types, operators, FileFactory, ServiceService, $modal) {

    $scope.typeId = 0;
    $scope.code = '';
    $scope.operatorId = 0;
    $scope.smsPass = '';
    $scope.showSmsPass = false;
    $scope.typeOptions = types;
    $scope.operatorOptions = operators;
    $scope.documents = {};

    $scope.searchDocument = function(typeId, operatorId, code, smsPass) {
        ServiceService.searchDocument(typeId, operatorId, code, smsPass)
            .then(function(data) {
                return $scope.documents = data;
            })
            .then(function(data) {
                if (data.hasOwnProperty('message') && data.message == 'Document Access wrong password') {
                    $scope.showSmsPass = true;
                }
            });
    };
});
