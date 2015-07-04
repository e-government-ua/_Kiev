angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('service.general.country.link', {
      url: '/link',
      views: {
        'content@service.general.country': angularAMD.route({
          templateProvider: ['$templateCache', function($templateCache) {
            return $templateCache.get('html/service/country/link/index.html');
          }],
          controller: 'ServiceLinkController',
          controllerUrl: 'service/link/controller'
        })
      }
    });
});

