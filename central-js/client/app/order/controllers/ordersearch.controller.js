angular.module('order').controller('OrderSearchController', function($rootScope, $scope, $state, ServiceService) {
    $scope.sID = '';

    $scope.searchOrder = function(sID) {
        ServiceService.searchOrder(sID)
            .then(function(data) {
                //$scope.documents = {};
                $scope.messages = {};

                return data;
            });
    };
});
