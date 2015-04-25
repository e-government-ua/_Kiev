define('state/service/built-in/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('ServiceBuiltInController', ['$state', '$rootScope', 'service', function ($state, $rootScope, service) {
		switch(service.range) {
			case 0:
				$state.go('service.built-in.default', {id: service.id}, { location: true });
				break;
			case 1:
				$state.go('service.built-in.region', {id: service.id}, { location: true });
				break;
		}
    }]);
});