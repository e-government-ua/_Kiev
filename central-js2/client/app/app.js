'use strict';
angular.module('app', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ui.router',
  'ui.bootstrap',
  'ngMessages',
  'ngClipboard',
  'index',
  'journal',
  'documents',
  'about'
]).config(function($stateProvider, $urlRouterProvider, $locationProvider) {

  $stateProvider
    .state('index', {
      url: '/',
      abstract: true,
      views:{
        header: {
          templateUrl: 'app/header/header.html'
        },
        footer: {
          templateUrl: 'app/footer/footer.html'
        }
      }
    })
    .state('index.main', {
      url: 'index',
      views: {
          'main@': {
            templateUrl: 'app/catalog/services.html',
            controller: 'IndexController'
          }
      }
    })
    .state('index.subcategory', {
      url: '/subcategory/:catID/:scatID',
      resolve: {
        catalog: function(CatalogService) {
          return CatalogService.getServices();
        }
      },
      views: {
        '': {
          templateUrl: 'app/catalog/subcategory.html',
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


