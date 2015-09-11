angular.module('app').config(function($stateProvider, statesRepositoryProvider) {
  statesRepositoryProvider.init(window.location.host);
  $stateProvider
    .state('index', statesRepositoryProvider.index())
    .state('index.service', {
      abstract: true,
      url: 'service/{id:int}',
      resolve: {
        // FIXME: Copy-pasting is bad, bad, bad
        service: function($stateParams, ServiceService) {
          console.log('App.states: calling get service, $stateParams.id =', $stateParams.id);
          return ServiceService.get($stateParams.id);
        },
        regions: function(PlacesService, service) {
          return PlacesService.getRegionsForService(service);
        }
      },
      views: {
        'main@': {
          templateUrl: 'app/service/index.html',
          controller: 'ServiceFormController'
        }
      }
    })
    .state('index.subcategory', {
      url: 'subcategory/:catID/:scatID',
      resolve: {
        catalog: function(CatalogService) {
          return CatalogService.getServices();
        }
      },
      views: {
        'main@': {
          templateUrl: 'app/service/subcategory/subcategory.html',
          controller: 'SubcategoryController'
        }
      }
    })
    .state('index.service.general', {
      url: '/general',
      views: {
        'content': {
          controller: 'ServiceGeneralController'
        }
      }
    })
    .state('index.service.instruction', {
      url: '/instruction',
      views: {
        'content': {
          templateUrl: 'app/service/instruction.html',
          controller: 'ServiceInstructionController'
        }
      }
    })
    .state('index.service.legislation', {
      url: '/legislation',
      views: {
        'content': {
          templateUrl: 'app/service/legislation.html',
          controller: 'ServiceLegislationController'
        }
      }
    })
    .state('index.service.questions', {
      url: '/questions',
      views: {
        'content': {
          templateUrl: 'app/service/questions.html',
          controller: 'ServiceQuestionsController'
        }
      }
    })
    .state('index.service.discussion', {
      url: '/discussion',
      views: {
        'content': {
          templateUrl: 'app/service/discussion.html',
          controller: 'ServiceDiscussionController'
        }
      }
    })
    // .state('index.service.general.placefix.built-in', { // FIXME move to placefix // city
    //   url: '/built-in',
    //   views: {
    //     'city-content': {
    //       templateUrl: 'app/service/placefix/built-in/index.html',
    //       // templateUrl: 'app/service/placefix/placefix.content.html',
    //       controller: 'ServiceBuiltInController'
    //         // controller: 'PlaceFixController'
    //     }
    //   }
    // })
    .state('index.service.general.placefix.built-in', { // FIXME-5 // region
      url: '/built-in',
      views: {
        // 'content@index.service.general.placefix': { // country
        //   templateUrl: 'app/service/placefix/built-in/index.html',
        //   controller: 'ServiceBuiltInController'
        // },
        'content@index.service.general.placefix': { // region
          // FIXME: REPLACE with Place Fix Controller
          // templateUrl: 'app/service/placefix/built-in/index.html',
          templateUrl: 'app/service/placefix/built-in/index.html',
          // controller: 'ServiceBuiltInController'
          controller: 'PlaceFixController'
        },
        'city-content': {
          templateUrl: 'app/service/placefix/built-in/index.html',
          // templateUrl: 'app/service/placefix/placefix.content.html',
          controller: 'ServiceBuiltInController'
            // controller: 'PlaceFixController'
        }
      }
    })
    .state('index.service.statistics', {
      url: '/statistics',
      views: {
        'content': {
          templateUrl: 'app/service/statistics.html',
          controller: 'ServiceStatisticsController'
        }
      }
    });
});