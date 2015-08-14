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

    $scope.getDocumentLink = ServiceService.getSearchDocumentLink;
    $scope.searchDocument = function(typeId, operatorId, code, smsPass) {
        ServiceService.searchDocument(typeId, operatorId, code, smsPass)
            .then(function(data) {
                $scope.documents = {};
                $scope.messages = {};
                if (data.hasOwnProperty('message')) {
                  var startsWith = function (str) { return data.message.indexOf(str) > -1; };
                  if (startsWith('Document Access password wrong')) {
                    if ($scope.smsPass)
                      $scope.messages = ['Неправильний код'];
                  } else if (startsWith('Document Access password need - sent SMS')) {
                    var phone = data.message.match(/\([^\)]+/)[0].substring(1);
                    $scope.blurredPhone  = phone.slice(0, -7) + '*****' + phone.slice(-2);
                    $scope.showSmsPass = true;
                  } else if (startsWith('Document Access not found')) {
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
