define('state/documents/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('DocumentsController', ['$rootScope', '$state', function ($rootScope, $state) {
		console.log('$rootScope');
		var documents = [
			{
				nID : "1",
				nID_Subject : "1",
				sID_Content : "content",
				sName : "Громадянський паспорт",
				sFile : "doc.jpg",
				//undescribed in task fields, but exists in the mock layout
				sDate_Upload : new Date("2015-05-25 11:12:35.000").getTime(),
				sID_Subject_Upload : "OshadBank",
				sSubjectName_Upload : "Ощадбанк"
			},
			{
				nID : "2",
				nID_Subject : "2",
				sID_Content : "content",
				sName : "Водійське посвідчення",
				sFile : "doc.jpg",
				//undescribed in task fields, but exists in the mock layout
				sDate_Upload : new Date("2015-04-04 13:10:35.000").getTime(),
				sID_Subject_Upload : "GYMVS_Lviv",
				sSubjectName_Upload: "ГУМВС м. Львів"
			},
			{
				nID : "3",
				nID_Subject : "3",
				sID_Content : "content",
				sName : "Довідка про несудимість",
				sFile : "doc.jpg",
				//undescribed in task fields, but exists in the mock layout
				sDate_Upload : new Date("2013-02-11 18:00:12.000").getTime(),
				sID_Subject_Upload : "GYMVS_Lviv",
				sSubjectName_Upload: "ГУМВС м. Львів"
			},
			{
				nID : "4",
				nID_Subject : "4",
				sID_Content : "content",
				sName : "Довідка про несудимість",
				sFile : "doc.jpg",
				//undescribed in task fields, but exists in the mock layout
				sDate_Upload : new Date("2015-02-13 17:00:11.000").getTime(),
				sID_Subject_Upload : "GYMVS_Lviv",
				sSubjectName_Upload: "ГУМВС м. Львів"
			},
			{
				nID : "5",
				nID_Subject : "5",
				sID_Content : "content",
				sName : "Ідентифікаційний номер платника податків",
				sFile : "doc.jpg",
				//undescribed in task fields, but exists in the mock layout
				sDate_Upload : new Date("2012-06-02 14:31:16.000").getTime(),
				sID_Subject_Upload : "Podatkova_Inspekcia_Lviv",
				sSubjectName_Upload: "Податкова інспекція м. Львів"
			}

		];

		$scope.documents = documents;

		console.log('DocumentsController');
        if (!$state.params.code) {
            $state.go('documents.bankid', {}, { location: true });
        }
    }]);
});
define('state/documents/bankid/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('DocumentsBankIdController', ['$rootScope', '$scope', '$location', '$state', '$window', function ($rootScope, $scope, $location, $state, $window) {
		console.log('$rootScope');
        console.log('DocumentsBankIdController');
        var stateForRedirect = $state.href('documents.content', {});
        var redirectURI = $location.protocol() + '://' + $location.host() + ':' + $location.port() + stateForRedirect;
        var bankIdLoginRefUrl = 'https://bankid.privatbank.ua/DataAccessService/das/authorize?response_type=code&client_id=dniprorada&redirect_uri=' + redirectURI;
        $scope.loginWithBankId = function () {
            $window.location.href = bankIdLoginRefUrl;
        }
    }]);
});
define('state/documents/content/controller', ['angularAMD'], function (angularAMD) {
	angularAMD.controller('DocumentsContentController', [
        '$rootScope', '$scope', '$state', '$stateParams', 'BankIDLogin',
        function ($rootScope, $scope, $state, $stateParams, BankIDLogin) {
		console.log('$rootScope');
        console.log('DocumentsContentController');
        console.log($stateParams);
        console.log(BankIDLogin);
    }]);
});