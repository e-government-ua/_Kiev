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
        url: '/search',
        parent: 'index.order',
        views: {
          'content': {
            templateUrl: 'app/order/search.html',
            controller: 'OrderSearchController'
          }
        }
      });
//  }
});

