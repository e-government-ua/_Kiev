angular.module('about').config(function ($stateProvider, statesRepositoryProvider) {
  statesRepositoryProvider.init(window.location.host);
//  if (statesRepositoryProvider.isCentral()) {
    $stateProvider
      .state('index.about', {
        url: 'about',
        views: {
          'main@': {
            templateUrl: 'app/about/about.html'
          }
        }
      })
      .state('index.test', {
        url: 'test',
        views: {
          'main@': {
            templateUrl: 'app/about/test.html',
            controller: 'TestController'
          }
        }
      });
//  }
});
