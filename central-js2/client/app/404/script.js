var app = angular.module('404', []).config(function($stateProvider, $urlRouterProvider) {
  $urlRouterProvider.otherwise('/404');
  $stateProvider
    .state('404', {
      url: '/404',
      views: {
        '': angularAMD.route({
          templateProvider: ['$templateCache', function($templateCache) {
            return $templateCache.get('html/404/404.html');
          }]
        })
      }
    })
});
