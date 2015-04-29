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
										'places': {
											'regions': [],
											'cities': []
										}
									},
									{
										'id': 2,
										'name': 'Service2',
										'places': {
											'regions': [1],
											'cities': []
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
										'places': {
											'regions': [1],
											'cities': [1]
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
										'cities': null,
										'regions': null,
										
									},
									{
										'id': 6,
										'name': 'Service6',
										'cities': null,
										'regions': [1],
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
										'cities': null,
										'regions': null,
									},
									{
										'id': 8,
										'name': 'Service8',
										'cities': null,
										'regions': null,
									}
								]
							}
						]
					}
				]
			}, {}];
		});
		
		$httpBackend.whenGET(/\/api\/service\?/).respond(function(method, url, rawData) {
			var data = angular.fromJson(rawData);
			
			if(data.id == 1) {
				return [200, {
					'id': 1,
					'name': 'Service1',
					'serviceType': {
						'id': 1,
						'name': 'link'
					},
					'places': {
						'regions': [],
						'cities': []
					}
				}, {}];
			}
			
			if(data.id == 2) {
				return [200, {
					'id': 2,
					'name': 'Service2',
					'serviceType': {
						'id': 4,
						'name': 'built-in'
					},
					'places': {
						'regions': [],
						'cities': []
					}
				}, {}];
			}
			
			if(data.id == 3) {
				return [200, {
					'id': 3,
					'name': 'Service3',
					'serviceType': {
						'id': 1,
						'name': 'link'
					},
					'places': {
						'regions': [1],
						'cities': []
					}
				}, {}];
			}
			
			if(data.id == 4) {
				return [200, {
					'id': 4,
					'name': 'Service4',
					'serviceType': {
						'id': 4,
						'name': 'built-in'
					},
					'places': {
						'regions': [1],
						'cities': []
					}
				}, {}];
			}
			
			if(data.id == 5) {
				return [200, {
					'id': 5,
					'name': 'Service5',
					'serviceType': {
						'id': 1,
						'name': 'link'
					},
					'places': {
						'regions': [1],
						'cities': [1]
					}
				}, {}];
			}
			
			if(data.id == 6) {
				return [200, {
					'id': 6,
					'name': 'Service6',
					'serviceType': {
						'id': 4,
						'name': 'built-in'
					},
					'places': {
						'regions': [1],
						'cities': [1]
					}
				}, {}];
			}
			
			return [200, {
				'id': 6,
				'name': 'Service6',
				'serviceType': {
					'id': 4,
					'name': 'built-in'
				},
				'places': {
					'regions': [1],
					'cities': [1]
				}
			}, {}];
		});
		
		$httpBackend.whenGET(/\/api\/places/).respond(function(method, url, rawData) {
			return [200, {
				'regions': [
					{
						'id': 1,
						'name': 'Львівська область',
						'cities': [
							{
								'id': 1,
								'name': 'Львів'
							},
							{
								'id': 2,
								'name': 'Червонооград'
							}
						]
					},
					{
						'id': 2,
						'name': 'Київська область',
						'cities': [
							{
								'id': 3,
								'name': 'Київ'
							},
						]
					}
				]
			}, {}];
		});
	
		$httpBackend.whenGET('./data.json').passThrough();
	}]);
	
    return app;
});