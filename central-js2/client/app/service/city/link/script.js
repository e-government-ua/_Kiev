angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('service.general.city.link', {
      url: '/link',
      views: {
        'status@service.general.city': angularAMD.route({
          templateProvider: ['$templateCache', function($templateCache) {
            return $templateCache.get('html/service/city/link/index.html');
          }],
          controller: 'ServiceLinkController',
          controllerUrl: 'service/link/controller'
        })
      }
    })
});

