define(['angularAMD', 'templates'], function(angularAMD) {
  var app = angular.module("main",
    ['ct.ui.router.extras', 'templates-main', 'ngMessages', 'ui.bootstrap',
      'ui.uploader', 'ui.event', 'ngClipboard', 'ngResource'
      ]);

  app.config(function($locationProvider) {
    $locationProvider.html5Mode(true);
  });

  app.config(function($futureStateProvider) {
    var ngloadStateFactory = function($q, futureState) {
      var ngloadDeferred = $q.defer();
      require(["ngload!" + futureState.src, 'ngload', 'angularAMD'], function ngloadCallback (result, ngload, angularAMD) {
        angularAMD.processQueue();
        ngloadDeferred.resolve(undefined);
      });
      return ngloadDeferred.promise;
    };
    var loadAndRegisterFutureStates = function($http) {
      return $http.get('./data.json').then(function(response) {
        angular.forEach(response.data, function(futureState) {
          $futureStateProvider.futureState(futureState);
        });
      });
    };
    $futureStateProvider.stateFactory('ngload', ngloadStateFactory);
    $futureStateProvider.addResolve(loadAndRegisterFutureStates);
  });

  app.config(function($urlRouterProvider) {
    $urlRouterProvider.when('', function($match, $state) {
      $state.transitionTo('index', $match, false);
    });
    $urlRouterProvider.otherwise('/404');
  });

  angularAMD.bootstrap(app);
  return app;
});