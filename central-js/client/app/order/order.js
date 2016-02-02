angular.module('order').config(function ($stateProvider, statesRepositoryProvider) {
  statesRepositoryProvider.init(window.location.host);
//  if (statesRepositoryProvider.isCentral()) {
    $stateProvider
      .state('index.order', {
        url: 'order',
        views: {
          'main@': {
            templateUrl: 'app/order/order.html',
            controller: 'OrderController'
          }
        }
      })
      .state('index.order.search', {
        url: '/search?sID_Order&sToken&nID',
        parent: 'index.order',
        resolve: {
          order: function($q, $stateParams, ServiceService) {
            if($stateParams.sID_Order) {
              return ServiceService.searchOrder($stateParams.sID_Order, $stateParams.sToken);
            }else if($stateParams.nID) {
              return ServiceService.searchOrder($stateParams.nID, $stateParams.sToken);
            };

            var deffered = $q.defer();
            deffered.resolve(null);
            return deffered.promise;
          }
        },
        views: {
          'content': {
            templateUrl: 'app/order/search.html',
            controller: 'OrderSearchController'
          }
        }
      });
//  }
});

