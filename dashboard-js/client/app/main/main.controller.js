'use strict';

angular.module('dashboardJsApp')
	.controller('MainCtrl', function($scope, $http) {
		$scope.weblinks = [{
			name: 'Дніпровська міська рада',
			link: 'https://dniprorada.egov.dp.ua',
			info: 'Дніпровська міська рада (портал громадян)'
		}];
	});