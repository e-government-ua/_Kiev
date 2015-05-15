define('state/documents/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('DocumentsController', ['$rootScope','$scope', function ($rootScope, $scope) {
		console.log('$rootScope');
		var documents = [
			{
				nID : "1",
				nID_Subject : "1",
				sID_Content : "content",
				sName : "Громадянський паспорт",
				sFile : "doc.jpg",
				//undescribed in task fields, but exists in the mock layout
				sDDate : "27 травня 2001",
				sDOwner : "Ощадбанк"
			},
			{
				nID : "2",
				nID_Subject : "2",
				sID_Content : "content",
				sName : "Водійське посвідчення",
				sFile : "doc.jpg",
				//undescribed in task fields, but exists in the mock layout
				sDDate : "04 квітня 2015",
				sDOwner : "ГУМВС м. Львів"
			},
			{
				nID : "3",
				nID_Subject : "3",
				sID_Content : "content",
				sName : "Довідка про несудимість",
				sFile : "doc.jpg",
				//undescribed in task fields, but exists in the mock layout
				sDDate : "11 лютого 2014",
				sDOwner : "ГУМВС м. Львів"
			},
			{
				nID : "4",
				nID_Subject : "4",
				sID_Content : "content",
				sName : "Довідка про несудимість",
				sFile : "doc.jpg",
				//undescribed in task fields, but exists in the mock layout
				sDDate : "13 лютого 2013",
				sDOwner : "ГУМВС м. Львів"
			},
			{
				nID : "5",
				nID_Subject : "5",
				sID_Content : "content",
				sName : "Ідентифікаційний номер платника податків",
				sFile : "doc.jpg",
				//undescribed in task fields, but exists in the mock layout
				sDDate : "02 червня 2012",
				sDOwner : "Податкова інспекція м. Львів"
			}

		];

		$scope.documents = documents;
    }]);
});