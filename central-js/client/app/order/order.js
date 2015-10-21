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
        url: '/search?nID&sToken',
        parent: 'index.order',
        resolve: {
          order: function($q, $stateParams, ServiceService) {
            if($stateParams.nID) {
              return ServiceService.searchOrder($stateParams.nID);
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

