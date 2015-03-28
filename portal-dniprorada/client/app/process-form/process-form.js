'use strict';

angular.module('portalDniproradaApp')
	.config(function($routeProvider) {
		$routeProvider
			.when('/process-form/:processDefinitionId', {
				templateUrl: 'app/process-form/process-form.html',
				controller: 'ProcessFormCtrl'
			})
			.when('/process-form', {
				templateUrl: 'app/process-form/process-form.html',
				controller: 'ProcessFormCtrl'
			});
	});