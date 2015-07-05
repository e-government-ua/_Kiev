angular.module('app').config(function($stateProvider) {
  $stateProvider
    .state('index.service', {
      url: '/service/{id:int}',
      resolve: {
        service: function($stateParams, ServiceService) {
          return ServiceService.get($stateParams.id);
        }
      },
      views: {
        '': {
          templateUrl: 'app/service/index.html',
          controller: 'ServiceController'
        }
      }
    })
    .state('index.service.general', {
      url: '/general',
      views: {
        '': {
          templateUrl: 'app/service/general.html',
          controller: 'ServiceGeneralController'
        }
      }
    })
    .state('index.service.instruction', {
      url: '/instruction',
      views: {
        '': {
          templateUrl: 'app/service/instruction.html',
          controller: 'ServiceInstructionController'
        }
      }
    })
    .state('index.service.legislation', {
      url: '/legislation',
      views: {
        '': {
          templateUrl: 'app/service/legislation.html',
          controller: 'ServiceLegislationController'
        }
      }
    })
    .state('index.service.questions', {
      url: '/questions',
      views: {
        '': {
          templateUrl: 'app/service/questions.html',
          controller: 'ServiceQuestionsController'
        }
      }
    })
    .state('index.service.discussion', {
      url: '/discussion',
      views: {
        '': {
          templateUrl: 'app/service/discussion.html',
          controller: 'ServiceDiscussionController'
        }
      }
    })
});


