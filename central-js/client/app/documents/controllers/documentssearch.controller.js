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
    $scope.messages = [];

    $scope.getDocumentLink = ServiceService.getDocumentLink;
    $scope.searchDocument = function(typeId, operatorId, code, smsPass) {
        ServiceService.searchDocument(typeId, operatorId, code, smsPass)
            .then(function(data) {
                $scope.documents = {};
                $scope.messages = {};
                if (data.hasOwnProperty('message')) {
                    if (data.message == 'Document Access wrong password') {
                        if ($scope.smsPass) {
                            $scope.messages = ['Неправильний код'];
                        }
                        $scope.showSmsPass = true;
                    } else if (data.message == 'Document Access not found') {
                        $scope.messages = ['Документи не знайдені'];
                    } else {
                        $scope.messages = [data.message];
                    }
                } else {
                    if (typeof data === 'object') {
                        data = [data];
                    }
                    $scope.documents = data;
                }
                return data;
            });
    };
});
