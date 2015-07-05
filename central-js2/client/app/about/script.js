var app = angular.module('about', []);

app.config(function($stateProvider) {
  $stateProvider
    .state('index.about', {
      url: '/about',
      views: {
        '': {
          templateUrl: {templateUrl: 'html/about/index.html'}
        }
      }
    });
});
