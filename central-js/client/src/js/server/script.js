define('server', ['angularAMD'], function (angularAMD) {
    var app = angular.module('server', ['ngMockE2E']);

	app.run(['$http', '$httpBackend', function($http, $httpBackend) {
		$httpBackend.whenGET('./api/services').respond(function(method, url, data) {
			return [200, {
				aCategory : [
					{
						nID: 1,
						sID:'Citizen',
						sName:'Гражданин',
						nOrder:1,
						aSubcategory: [
							{
								nID: 6,
								sID:'Transport',
								sName:'Транспорт',
								nOrder:1,
								aService: [
									{
										nID:1,
										nOrder:1,
										sName:'Отримати довідку про несудимість',
										nID_City:1,
										nID_Region:null,
										nID_ServiceType:1,
										sURL:'https://lviv.e-gov.org.ua'
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
		$httpBackend.whenGET(/\/api\/bankid/).passThrough();
	}]);
	
    return app;
});