var app = angular.module('about', []).config(function($stateProvider) {
  $stateProvider
    .state('index.about', {
      url: 'about',
      views: {
        'main@': {
          templateUrl: 'app/about/about.html'
        }
      }
    });
});
