define('server', ['angularAMD'], function (angularAMD) {
    var app = angular.module('server', ['ngMockE2E']);

	app.run(['$http', '$httpBackend', function($http, $httpBackend) {
		$httpBackend.whenGET('./api/services').respond(function(method, url, data) {
			return [200, {
				aCategory : [	
					{
						nID: 1,
						sID: 'Citizen',
						sName: 'Гражданин',
						nOrder: 1,
						aSubcategory: [
							{
								nID: 1,
								sID: 'Family',
								sName: 'Родина',
								nOrder: 2,
								aService: []
							},
							{
								nID: 2,
								sID: 'Transport',
								sName: 'Транспорт',
								nOrder: 6,
								aService: []
							},
							{
								nID: 3,
								sID: 'Nature',
								sName: 'Довкілля',
								nOrder: 5,
								aService: []
							},
							{
								nID: 4,
								sID: 'Study',
								sName: 'Освіта та наука',
								nOrder: 4,
								aService: []
							},
							{
								nID: 5,
								sID: 'Work',
								sName: 'Праця',
								nOrder: 3,
								aService: [
									{
										nID: 3,
										nOrder: 3,
										sName: 'Видача картки обліку об’єкта торговельного призначення, сфери послуг та з виробництва продуктів харчування'
									},
									{
										nID: 4,
										nOrder: 4,
										sName: 'Легалізація об’єднань громадян шляхом повідомлення'
									}
								]
							},
							{
								nID: 6,
								sID: 'Policy',
								sName: 'Міліція',
								nOrder: 1,
								aService: [
									{
										nID: 1,
										nOrder: 1,
										sName: 'Отримати довідку про несудимість'
									},
									{
										nID: 2,
										nOrder: 2,
										sName: 'Подати заяву у міліцію'
									}
								]
							},
							{
								nID: 12,
								sID: 'JusticeCitizen',
								sName: 'Юриспруденция физлиц',
								nOrder: 7,
								aService: [
									{
										nID: 5,
										nOrder: 5,
										sName: 'Звернення громадян стосовно надання копій документів'
									},
									{
										nID: 8,
										nOrder: 8,
										sName: 'Видача копій, витягів з розпоряджень міського голови, рішень, прийнятих міською радою та виконавчим комітетом.'
									},
									{
										nID: 15,
										nOrder: 15,
										sName: 'Видача завіренех копій документів'
									},
									{
										nID: 22,
										nOrder: 22,
										sName: 'Обращения граждан'
									},
									{
										nID: 23,
										nOrder: 23,
										sName: 'Надання відповідей на звернення громадян'
									}
								]
							},
							{
								nID: 13,
								sID: 'Permission',
								sName: 'Разрешения',
								nOrder: 8,
								aService: [
									{
										nID: 6,
										nOrder: 6,
										sName: 'Надання дозволу на використання місцевої символіки (герба міста, назви чи зображення архітектурних, історичних пам’яток)'
									},
									{
										nID: 20,
										nOrder: 20,
										sName: 'Видача дозволу на переоформлення договору найму жилого приміщення державного / комунального житлового фонду'
									}

								]
							},
							{
								nID: 14,
								sID: 'Registration',
								sName: 'Приписка',
								nOrder: 9,
								aService: [
									{
										nID: 9,
										nOrder: 9,
										sName: 'Надання довідки про перебування на квартирному обліку при міськвиконкомі за місцем проживання та в житлово-будівельному кооперативі.'
									},
									{
										nID: 10,
										nOrder: 10,
										sName: 'Надання довідки про перебування на обліку бажаючих отримати земельну ділянку під індивідуальне будівництво'
									},
									{
										nID: 13,
										nOrder: 13,
										sName: 'Присвоєння поштової адреси об’єкту нерухомого майна'
									},
									{
										nID: 14,
										nOrder: 14,
										sName: 'Видача довідок про перебування на квартирному обліку'
									}
								]
							},
							{
								nID: 15,
								sID: 'Earth',
								sName: 'Земля',
								nOrder: 10,
								aService: [
									{
										nID: 7,
										nOrder: 7,
										sName: 'Видача відомостей з документації, що включена до місцевого фонду документації із землеустрою.'
									},
									{
										nID: 11,
										nOrder: 11,
										sName: 'Видача витягу з технічної документації про нормативну грошову оцінку земельної ділянки'
									},
									{
										nID: 12,
										nOrder: 12,
										sName: 'Надання відомостей з Державного земельного кадастру у формі витягу з Державного земельного кадастру про земельну ділянку'
									}
								]
							},
							{
								nID: 17,
								sID: 'Help',
								sName: 'Помощь',
								nOrder: 11,
								aService: [
									{
										nID: 18,
										nOrder: 18,
										sName: 'Взяття на облік громадян, які потребують поліпшення житлових умов'
									},
									{
										nID: 19,
										nOrder: 19,
										sName: 'Взяття на соціальний квартирний облік'
									}
								]
							}
						]
					},
					{
						nID: 2,
						sID: 'Business',
						sName: 'Бизнес',
						nOrder: 2,
						aSubcategory: [
							{
								nID: 7,
								sID: 'Tax',
								sName: 'Податки',
								nOrder: 2,
								aService: [
								]
							},
							{
								nID: 8,
								sID: 'License',
								sName: 'Ліцензиї та реєстрації',
								nOrder: 1,
								aService: [
									{
										nID: 16,
										nOrder: 16,
										sName: 'Дозвіл на розміщення рекоамної конструкції'
									},
									{
										nID: 17,
										nOrder: 17,
										sName: 'Отримання дозволу на МАФ'
									},
									{
										nID: 21,
										nOrder: 21,
										sName: 'Реєстрація громадського об’єднання'
									}
								]
							},
							{
								nID: 9,
								sID: 'Customs',
								sName: 'Митниця',
								nOrder: 5,
								aService: []
							},
							{
								nID: 10,
								sID: 'Transport',
								sName: 'Транспорт',
								nOrder: 4,
								aService: []
							},
							{
								nID: 11,
								sID: 'Ecology',
								sName: 'Земля та екологія',
								nOrder: 3,
								aService: []
							},
							{
								nID: 16,
								sID: 'JusticeBusiness',
								sName: 'Юриспруденция юрлиц',
								nOrder: 6,
								aService: [
									{
										nID: 24,
										nOrder: 24,
										sName: 'Надання відповідей на листи підприємств, установ та організацій в межах компетенції облдержадміністрації'
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
			
			var services = [
				{
					nID: 1,
					nOrder: 1,
					sName: 'Отримати довідку про несудимість',
					sNote: '',
					sFAQ: '',
					sLaw: '',
					aServiceData: [
						{
							nID_City: 2,
							nID_Region: null,
							nID_ServiceType: 3,
							oData:null,
							sURL: 'https://dniprorada.e-gov.org.ua'
						}
					]
				},
				{
					nID:2,
					nOrder:2,
					sName:'Подати заяву у міліцію',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: null,
							nID_Region: 2,
							nID_ServiceType: 3,
							oData: null,
							sURL: 'https://lviv.e-gov.org.ua'
						}
					]
				},
				{
					nID:3,
					nOrder:3,
					sName:'Видача картки обліку об’єкта торговельного призначення, сфери послуг та з виробництва продуктів харчування',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: 1,
							nID_Region: null,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'CivilCardAccountlRequest:6:60061'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:4,
					nOrder:4,
					sName:'Легалізація об’єднань громадян шляхом повідомлення',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: 1,
							nID_Region: null,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'demo1:1:60052'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:5,
					nOrder:5,
					sName:'Звернення громадян стосовно надання копій документів',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: 1,
							nID_Region: null,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'CivilCopyDocRequest:6:60062'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:6,
					nOrder:6,
					sName:'Надання дозволу на використання місцевої символіки (герба міста, назви чи зображення архітектурних, історичних пам’яток)',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: 1,
							nID_Region: null,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'demo14:1:60059'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:7,
					nOrder:7,
					sName:'Видача відомостей з документації, що включена до місцевого фонду документації із землеустрою',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: 4,
							nID_Region: null,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'ivFrank_mvk_2:5:60064'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:8,
					nOrder:8,
					sName:'Видача копій, витягів з розпоряджень міського голови, рішень, прийнятих міською радою та виконавчим комітетом',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: 4,
							nID_Region: null,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'ivFrank_mvk_1:5:60053'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:9,
					nOrder:9,
					sName:'Надання довідки про перебування на квартирному обліку при міськвиконкомі за місцем проживання та в житлово-будівельному кооперативі',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: 4,
							nID_Region: null,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'demo3:1:60045'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:10,
					nOrder:10,
					sName:'Надання довідки про перебування на обліку бажаючих отримати земельну ділянку під індивідуальне будівництво',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: 4,
							nID_Region: null,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'demo4:1:60055'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:11,
					nOrder:11,
					sName:'Видача витягу з технічної документації про нормативну грошову оцінку земельної ділянки',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: 5,
							nID_Region: null,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'monetaryValuationOfLand:5:60066'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:12,
					nOrder:12,
					sName:'Надання відомостей з Державного земельного кадастру у формі витягу з Державного земельного кадастру про земельну ділянку',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: 5,
							nID_Region: null,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'demo13:1:60047'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:13,
					nOrder:13,
					sName:'Присвоєння поштової адреси об’єкту нерухомого майна',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: 6,
							nID_Region: null,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'demo12:1:60058'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:14,
					nOrder:14,
					sName:'Видача довідок про перебування на квартирному обліку',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: 3,
							nID_Region: null,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'demo6:1:60057'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:15,
					nOrder:15,
					sName:'Видача завіренех копій документів',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: 3,
							nID_Region: null,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'demo7:1:60063'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:16,
					nOrder:16,
					sName:'Дозвіл на розміщення рекоамної конструкції',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: 3,
							nID_Region: null,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'demo8:1:60050'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:17,
					nOrder:17,
					sName:'Отримання дозволу на МАФ',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: 3,
							nID_Region: null,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'demo9:1:60048'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:18,
					nOrder:18,
					sName:'Взяття на облік громадян, які потребують поліпшення житлових умов',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: null,
							nID_Region: 4,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'nikolaev_oda_2:5:60065'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:19,
					nOrder:19,
					sName:'Взяття на соціальний квартирний облік',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: null,
							nID_Region: 4,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'nikolaev_oda_3:5:60049'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:20,
					nOrder:20,
					sName:'Видача дозволу на переоформлення договору найму жилого приміщення державного / комунального житлового фонду',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: null,
							nID_Region: 4,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'nikolaev_oda_4:5:60056'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]

				},
				{
					nID:21,
					nOrder:21,
					sName:'Реєстрація громадського об’єднання',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [	
						{
							nID_City: null,
							nID_Region: 4,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'nikolaev_oda_1:5:60046'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:22,
					nOrder:22,
					sName:'Обращения граждан',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: 5,
							nID_Region: null,
							nID_ServiceType: null,
							oData: {
								oParams: {
									'processDefinitionId': 'demo2:1:60054'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:23,
					nOrder:23,
					sName:'Надання відповідей на звернення громадян',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: null,
							nID_Region: 6,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'demo11:1:60051'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				},
				{
					nID:24,
					nOrder:24,
					sName:'Надання відповідей на листи підприємств, установ та організацій в межах компетенції облдержадміністрації',
					sNote:'',
					sFAQ:'',
					sLaw:'',
					aServiceData: [
						{
							nID_City: null,
							nID_Region: 6,
							nID_ServiceType: 4,
							oData: {
								oParams: {
									'processDefinitionId': 'demo10:1:60060'
								}
							},
							sURL: 'https://test.e-gov.org.ua/wf-dniprorada/'
						}
					]
				}
			]

			
			var service = null;
			angular.forEach(services, function(value, key) {
				if(value.nID == data.id) {
					service = value;
				}
			});
			
			return [200, service, {}];
		});
		
		$httpBackend.whenGET(/\/api\/places/).respond(function(method, url, rawData) {
			return [200, {
				aRegion : [
					{
						nID: 1,
						sName:'Дніпропетровська',
						aCity: [
							{
								nID: 1,
								sName:'Дніпропетровськ'
							},
							{
								nID: 2,
								sName:'Кривий Ріг'
							}
						]
					},
					{
						nID: 2,
						sName:'Львівська',
						aCity: [
							{
								nID: 3,
								sName:'Львів'
							}
						]
					},
					{
						nID: 3,
						sName:'Івано-Франківська',
						aCity: [
							{
								nID: 4,
								sName:'Івано-Франківськ'
							}
						]
					},
					{
						nID: 4,
						sName:'Миколаївська',
						aCity: [
							{
								nID: 5,
								sName:'Калуга'
							}
						]
					},
					{
						nID: 5,
						sName:'Київська',
						aCity: [
							{
								nID: 6,
								sName:'Київ'
							}
						]
					},
					{
						nID: 6,
						sName:'Херсонська',
						aCity: [
							{
								nID: 7,
								sName:'Херсон'
							}
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