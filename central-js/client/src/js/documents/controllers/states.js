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
				sDate_Upload : "2015-05-25 11:12:35.000",
				sID_Subject_Upload : "OshadBank"
			},
			{
				nID : "2",
				nID_Subject : "2",
				sID_Content : "content",
				sName : "Водійське посвідчення",
				sFile : "doc.jpg",
				//undescribed in task fields, but exists in the mock layout
				sDate_Upload : "2015-04-04 13:10:35.000",
				sID_Subject_Upload : "GYMVS_Lviv"
			},
			{
				nID : "3",
				nID_Subject : "3",
				sID_Content : "content",
				sName : "Довідка про несудимість",
				sFile : "doc.jpg",
				//undescribed in task fields, but exists in the mock layout
				sDate_Upload : "2013-02-11 18:00:12.000",
				sID_Subject_Upload : "GYMVS_Lviv"
			},
			{
				nID : "4",
				nID_Subject : "4",
				sID_Content : "content",
				sName : "Довідка про несудимість",
				sFile : "doc.jpg",
				//undescribed in task fields, but exists in the mock layout
				sDate_Upload : "2015-02-13 17:00:11.000",
				sID_Subject_Upload : "GYMVS_Lviv"
			},
			{
				nID : "5",
				nID_Subject : "5",
				sID_Content : "content",
				sName : "Ідентифікаційний номер платника податків",
				sFile : "doc.jpg",
				//undescribed in task fields, but exists in the mock layout
				sDate_Upload : "2012-06-02 14:31:16.000",
				sID_Subject_Upload : "Podatkova_Inspekcia_Lviv"
			}

		];

		$scope.documents = documents;
    }]);
});