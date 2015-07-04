angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('service.general.region.link', {
      url: '/link',
      views: {
        'content@service.general.region': angularAMD.route({
          templateProvider: ['$templateCache', function($templateCache) {
            return $templateCache.get('html/service/region/link/index.html');
          }],
          controller: 'ServiceLinkController',
          controllerUrl: 'service/link/controller'
        })
      }
    });

