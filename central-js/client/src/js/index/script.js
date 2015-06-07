define('index', ['angularAMD', 'catalog/service'], function(angularAMD) {
  var app = angular.module('index', []);

  app.config(function($stateProvider) {
    $stateProvider
      .state('index', {
        url: '/index',
        resolve: {
          catalog: function(CatalogService) {
            return CatalogService.getServices();
          }
        },
        views: {
          '': angularAMD.route({
            templateProvider: function($templateCache) {
              return $templateCache.get('html/catalog/services.html');
            },
            controller: 'IndexController',
            controllerUrl: 'state/index/controller'
          })
        }
      })
      .state('subcategory', {
        url: '/subcategory/:catID/:scatID',
        resolve: {
          catalog: function(CatalogService) {
            return CatalogService.getServices();
          }
        },
        views: {
          '': angularAMD.route({
            templateProvider: function($templateCache) {
              return $templateCache.get('html/catalog/subcategory.html');
            },
            controller: 'SubcategoryController',
            controllerUrl: 'state/subcategory/controller'
          })
        }
      });

  }).run(function($rootScope, $state) {
    $rootScope.state = $state;
  });

  return app;
});

