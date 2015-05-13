define('documents/service', ['angularAMD'], function (angularAMD) {
    angularAMD.service('DocumentsService', ['$http', function($http) {

        this.getDocuments = function (){
            return [
                {
                    nID : "1",
                    sID_Content : "doc content",
                    sName : "doc name",
                    sFile : "doc.jpg"
                },
                {
                    nID : "2",
                    sID_Content : "doc content2",
                    sName : "doc name",
                    sFile : "doc.jpg"
                },
                {
                    nID : "3",
                    sID_Content : "doc content3",
                    sName : "doc name",
                    sFile : "doc.jpg"
                }

            ];
        }

    }]);
});