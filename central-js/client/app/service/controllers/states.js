angular.module('app').controller('ServiceFormController', function($scope, service, AdminService) {
  console.log('service controller called');
  $scope.service = service;
  $scope.bAdmin = AdminService.isAdmin();
});

angular.module('app').controller('ServiceGeneralController', function($state, $scope) {
  var aServiceData = $scope.service.aServiceData;

  var isCity = false;
  angular.forEach(aServiceData, function(value, key) {
    if (value.hasOwnProperty('nID_City')) {
      isCity = true;
    }
  });
  if (isCity) {
    return $state.go('index.service.general.city', {id: $scope.service.nID}, {location: false});
  }

  var isRegion = false;
  angular.forEach(aServiceData, function(value, key) {
    if (value.hasOwnProperty('nID_Region')) {
      isRegion = true;
    }
  });
  if (isRegion) {
    return $state.go('index.service.general.city', {id: $scope.service.nID}, {location: false});
  }

  return $state.go('index.service.general.country', {id:  $scope.service.nID}, {location: false});
});

angular.module('app').controller('ServiceInstructionController', function($state, $rootScope, $scope) {
});

angular.module('app').controller('ServiceLegislationController', function($state, $rootScope, $scope) {
});

angular.module('app').controller('ServiceQuestionsController', function($state, $rootScope, $scope) {
});

angular.module('app').controller('ServiceDiscussionController', function($state, $rootScope, $scope) {
  var HC_LOAD_INIT = false;
  window._hcwp = window._hcwp || [];
  window._hcwp.push({widget: 'Stream', widget_id: 60115});
  if ('HC_LOAD_INIT' in window) return;
  HC_LOAD_INIT = true;
  var lang = (navigator.language || navigator.systemLanguage || navigator.userLanguage || 'en').substr(0, 2).toLowerCase();
  var hcc = document.createElement('script');
  hcc.type = 'text/javascript';
  hcc.async = true;
  hcc.src = ('https:' == document.location.protocol ? 'https' : 'http') + '://w.hypercomments.com/widget/hc/60115/' + lang + '/widget.js';
  angular.element(document.querySelector('#hypercomments_widget')).append(hcc);
});

angular.module('app').controller('ServiceStatisticsController', function($scope, ServiceService) {
  $scope.loaded = false;
  $scope.arrow = '\u2191';
  $scope.reverse = false;

  $scope.changeSort = function() {
    $scope.reverse = !$scope.reverse;
    $scope.arrow = $scope.reverse ? '\u2191' : '\u2193';
  };

  ServiceService.getStatisticsForService($scope.service.nID).then(function(response) {
    $scope.stats = response.data;
  }, function(response) { alert(response.status + ' ' + response.statusText + '\n' + response.data); })
    .finally(function() {$scope.loaded = true;});
});
