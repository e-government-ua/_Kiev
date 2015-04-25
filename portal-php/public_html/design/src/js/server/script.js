define('server', ['angularAMD'], function (angularAMD) {
    var app = angular.module('server', ['ngMockE2E']);

	app.run(['$http', '$httpBackend', function($http, $httpBackend) {
		$httpBackend.whenGET('./api/services').respond(function(method, url, data) {
			return [200, {
				categories: [
					{
						'id': 1,
						'name': 'Category1',
						'subcategories': [
							{
								'id': 1,
								'name': 'SubCategory1',
								'services': [
									{
										'id': 1,
										'name': 'Service1',
										'serviceType': {
											'id': 1,
											'name': 'link',
											'url': 'http://google.com.ua'
										}
									},
									{
										'id': 2,
										'name': 'Service2',
										'serviceType': {
											'id': 1,
											'name': 'link',
											'url': 'http://google.com.ua'
										}
									}
								]
							},
							{
								'id': 2,
								'name': 'SubCategory2',
								'services': [
									{
										'id': 3,
										'name': 'Service3',
										'serviceType': {
											'id': 1,
											'name': 'link',
											'url': 'http://google.com.ua'
										}
									},
									{
										'id': 4,
										'name': 'Service4',
										'serviceType': {
											'id': 1,
											'name': 'link',
											'url': 'http://google.com.ua'
										}
									}
								]
							}
						]
					},
					{
						'id': 2,
						'name': 'Category2',
						'subcategories': [
							{
								'id': 3,
								'name': 'SubCategory3',
								'services': [
									{
										'id': 5,
										'name': 'Service5',
										'serviceType': {
											'id': 4,
											'name': 'built-in',
											'url': null
										}
									},
									{
										'id': 6,
										'name': 'Service6',
										'serviceType': {
											'id': 4,
											'name': 'built-in',
											'url': null
										}
									}
								]
							},
							{
								'id': 4,
								'name': 'SubCategory4',
								'services': [
									{
										'id': 7,
										'name': 'Service7',
										'serviceType': {
											'id': 4,
											'name': 'built-in',
											'url': null
										}
									},
									{
										'id': 8,
										'name': 'Service8',
										'serviceType': {
											'id': 4,
											'name': 'built-in',
											'url': null
										}
									}
								]
							}
						]
					}
				]
			}, {}];
		});
	
		$httpBackend.whenGET('./data.json').passThrough();
	}]);
	
    return app;
});