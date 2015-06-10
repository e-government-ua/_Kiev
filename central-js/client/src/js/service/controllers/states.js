define('state/service/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceController', ['$state', '$rootScope', '$scope', 'service', function ($state, $rootScope, $scope, service) {
		$scope.service = service;
	}]);
});

define('state/service/general/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceGeneralController', ['$state', '$rootScope', '$scope', 'service', function ($state, $rootScope, $scope, service) {
		$scope.service = service;
		
		var aServiceData = service.aServiceData;
		
		var isCity = false;
		angular.forEach(aServiceData, function(value, key) {
			if(value.hasOwnProperty('nID_City')) {
				isCity = true;
			}
		});
		if(isCity) {
			return $state.go('service.general.city', {id: service.nID}, { location: false });
		}
		
		var isRegion = false;
		angular.forEach(aServiceData, function(value, key) {
			if(value.hasOwnProperty('nID_Region')) {
				isRegion = true;
			}
		});
		if(isRegion) {
			return $state.go('service.general.region', {id: service.nID}, { location: false });
		}
		
		return $state.go('service.general.country', {id: service.nID}, { location: false });
    }]);
});



define('state/service/instruction/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceInstructionController', ['$state', '$rootScope', '$scope', 'service', 'hiddenCtrls', function ($state, $rootScope, $scope, service, hiddenCtrls) {
		$scope.service = service;
		return $state.go('service.instruction', {id: service.nID, service: service}, { location: false });
                
                //Admin buttons visibility handling
                //$scope.hiddenCtrls = hiddenCtrls;
                $scope.hiddenCtrls = false;
                $scope.hiddenEdit = false;
                $scope.hiddenSave = !$scope.hiddenEdit;
                $scope.hiddenCancel = !$scope.hiddenEdit;
    }]);
});

define('state/service/legislation/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceLegislationController', ['$state', '$rootScope', '$scope', 'service', function ($state, $rootScope, $scope, service) {
		$scope.service = service;
		return $state.go('service.legislation', {id: service.nID, service: service}, { location: false });
                
                //Admin buttons visibility handling
                $scope.hiddenCtrls = false;
                $scope.hiddenEdit = false;
                $scope.hiddenSave = !$scope.hiddenEdit;
                $scope.hiddenCancel = !$scope.hiddenEdit;
    }]);
});

define('state/service/questions/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceQuestionsController', ['$state', '$rootScope', '$scope', 'service', function ($state, $rootScope, $scope, service) {
		$scope.service = service;
		return $state.go('service.questions', {id: service.nID, service: service}, { location: false });
                
                //Admin buttons visibility handling
                $scope.hiddenCtrls = false;
                $scope.hiddenEdit = false;
                $scope.hiddenSave = !$scope.hiddenEdit;
                $scope.hiddenCancel = !$scope.hiddenEdit;               
    }]);
});

define('state/service/discussion/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceDiscussionController', ['$state', '$rootScope', '$scope', 'service', function ($state, $rootScope, $scope, service) {
		$scope.service = service;

		// TODO:Refactoring Consider way to introduce a wrapper directive to keep HC logic and settings
		var HC_LOAD_INIT = false;
		window._hcwp = window._hcwp || [];
		window._hcwp.push({widget: 'Stream', widget_id: 60115});
		if ('HC_LOAD_INIT' in window) return;
		HC_LOAD_INIT = true;
		var lang = (navigator.language || navigator.systemLanguage || navigator.userLanguage ||  'en').substr(0, 2).toLowerCase();
		var hcc = document.createElement('script');
		hcc.type = 'text/javascript';
		hcc.async = true;
		hcc.src = ('https:' == document.location.protocol ? 'https' : 'http') + '://w.hypercomments.com/widget/hc/60115/' + lang + '/widget.js';
		angular.element(document.querySelector('#hypercomments_widget')).append(hcc);

		return $state.go('service.discussion', {id: service.nID, service: service}, { location: false });
    }]);
});
