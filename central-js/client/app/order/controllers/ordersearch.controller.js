angular.module('order').controller('OrderSearchController', function($rootScope, $scope, $state, ServiceService) {
    $scope.sID = '';
    $scope.orders = {};

    $scope.searchOrder = function(sID) {
        ServiceService.searchOrder(sID)
            .then(function(data) {
                $scope.messages = {};
                $scope.orders = {};
                if (!data) {
                    $scope.messages = ['Неверный номер!'];
                } else if (data.hasOwnProperty('message')) {
                    if (data.message.indexOf('CRC Error') > -1) {
                        $scope.messages = ['Неверный номер!'];
                    } else if (data.message.indexOf('Record not found') > -1) {
                        $scope.messages = ['Заявка не знайдена'];
                    } else {
                        $scope.messages = ['Заявка не знайдена'];
                    }
                } else {
                    if (typeof data === 'object') {
                        data.sDateEdit = new Date();
                        data = [data];
                    }
                    $scope.orders = data;
                }

                return data;
            });
    };
});
