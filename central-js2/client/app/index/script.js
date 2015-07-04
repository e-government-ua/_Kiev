var app = angular.module('index', []).config(function($stateProvider) {
  $stateProvider
    .state('index', {
      url: '/',
      resolve: {
        catalog: function(CatalogService) {
          return CatalogService.getServices();
        }
      },
      views: {
        '': {
          templateUrl: 'html/catalog/services.html',
          controller: 'IndexController'
        }
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
        '': {
          templateUrl: 'html/catalog/subcategory.html',
          controller: 'SubcategoryController'
        }
      }
    });
}).run(function($rootScope, $state) {
  $rootScope.state = $state;
});


