'use strict';
angular.module('app', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ui.router',
  'ui.bootstrap',
  'ngMessages',
  'ngClipboard',
  'bankid',
  'index',
  'journal',
  'documents'
]).config(function($stateProvider, $urlRouterProvider, $locationProvider) {

  $stateProvider
    .state('index', {
      url: '/',
      abstract: true
    })
    .state('index.main', {
      url: 'index',
      views: {
        header: {
          templateUrl: 'app/header/header.html'
        },
        footer: {
          templateUrl: 'app/footer/footer.html'
        },
        main: {
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
  //});
  $urlRouterProvider.otherwise('/index');
  //$locationProvider.html5Mode(true);
}).run(function($rootScope, $state) {
  $rootScope.state = $state;
});


