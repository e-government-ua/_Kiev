angular.module('order').controller('OrderSearchController', function($rootScope, $scope, $state, $stateParams, ServiceService, order) {
    $scope.sID = '';
    $scope.orders = {};

    if(order != null) {
      $scope.sID = $stateParams.nID;
    
      $scope.messages = {};
      $scope.orders = {};
      if (!order) {
        $scope.messages = ['Невірний номер!'];
      } else if (order.hasOwnProperty('message')) {
        if (order.message.indexOf('CRC Error') > -1) {
          $scope.messages = ['Невірний номер!'];
        } else if (order.message.indexOf('Record not found') > -1) {
          $scope.messages = ['Заявку не знайдено'];
        } else {
          $scope.messages = ['Заявку не знайдено'];
        }
      } else {
        if (typeof order === 'object') {
          //order.sDateEdit = new Date();
          //order.sDateEdit = order.sDate;
          order = [order];
        }
        $scope.orders = order;
      }
    }
    
    $scope.searchOrder = function(sID) {
        ServiceService.searchOrder(sID)
            .then(function(data) {
                $scope.messages = {};
                $scope.orders = {};
                if (!data) {
                    $scope.messages = ['Невірний номер!'];
                } else if (data.hasOwnProperty('message')) {
                    if (data.message.indexOf('CRC Error') > -1) {
                        $scope.messages = ['Невірний номер!'];
                    } else if (data.message.indexOf('Record not found') > -1) {
                        $scope.messages = ['Заявку не знайдено'];
                    } else {
                        $scope.messages = ['Заявку не знайдено'];
                    }
                } else {
                    if (typeof data === 'object') {
                        //data.sDateEdit = new Date();
                        //data.sDateEdit = data.sDate;
                        data = [data];
                    }
                    $scope.orders = data;
                }

                return data;
            });
    };
});
