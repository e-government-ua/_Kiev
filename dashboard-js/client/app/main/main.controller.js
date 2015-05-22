'use strict';

angular.module('dashboardJsApp')
	.controller('MainCtrl', function($scope) {
		$scope.weblinks = [{
			name: 'Центральний портал громадян',
			link: 'https://igov.org.ua',
			info: 'Портал громадян'
		}];
	});
