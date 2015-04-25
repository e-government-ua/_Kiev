define('state/service/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceController', ['$state', '$rootScope', 'service', function ($state, $rootScope, service) {
		switch(service.serviceType.id) {
			case 1:
				$state.go('service.link', {id: service.id}, { location: true });
				break;
			case 4:
				$state.go('service.built-in', {id: service.id}, { location: true });
				break;
		}
    }]);
});