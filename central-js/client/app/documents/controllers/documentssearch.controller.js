angular.module('documents').controller('DocumentsSearchController',
    function($scope, $state, types, operators, FileFactory, ServiceService, $modal, ErrorsFactory) {

    $scope.typeId = 0;
    $scope.code = '';
    $scope.operatorId = 0;
    $scope.smsPass = '';
    $scope.showSmsPass = false;

    $scope.typeOptions = types;
    $scope.operatorOptions = operators;
    $scope.documents = [];
    $scope.messages = [];
    $scope.spinner = false;

    $scope.getDocumentLink = ServiceService.getSearchDocumentLink;
    
    $scope.searchDocument = function(nID_DocumentType, nID_DocumentOperator, sID_Document, sVerifyCode) {
        $scope.spinner = true;
        var oFuncNote = {sHead:"Пошук документу", sFunc:"searchDocument"};
        ErrorsFactory.init(oFuncNote, {asParam:['nID_DocumentType: '+nID_DocumentType, 'nID_DocumentOperator: '+nID_DocumentOperator, 'sID_Document: '+sID_Document, 'sVerifyCode: '+sVerifyCode]});
        ServiceService.searchDocument(nID_DocumentType, nID_DocumentOperator, sID_Document, sVerifyCode)
            .then(function(oResponse) {
                $scope.documents = [];
                //$scope.messages = {};
                if(ErrorsFactory.bSuccessResponse(oResponse,function(oThis,doMerge, sMessage, aCode, sResponse){
                    if (!sMessage) {
                    } else if (sMessage.indexOf(['Document Access password wrong']) > -1) {
                        doMerge(oThis,{sType: "warning", sBody: $scope.smsPass ? 'Невірний код' : 'Треба ввести код', asParam:['$scope.smsPass: '+$scope.smsPass]});
                    } else if (sMessage.indexOf(['Document Access password need - sent SMS']) > -1) {
                        var sPhone = sMessage.match(/\([^\)]+/)[0].substring(1);
                        $scope.blurredPhone  = sPhone.slice(0, -7) + '*****' + sPhone.slice(-2);
                        $scope.showSmsPass = true;
                        doMerge(oThis,{sType: "info", sBody: 'Відсилка SMS-паролю для підтверження',asParam:['sPhone: '+sPhone]});
                    } else if (sMessage.indexOf(["Can't get document"]) > -1) {
                        doMerge(oThis,{sType: "warning", sBody: 'Неможливо отримати документ!'});
                    } else if (sMessage.indexOf(["Entity with nID_SubjectOrgan"]) > -1) {
                        doMerge(oThis,{sType: "warning", sBody: 'Цей оператор ще немає підтримки своїх докумениів на порталі!'});
                    } else if (sMessage.indexOf(['Document Access not found']) > -1) {
                        doMerge(oThis,{sType: "warning", sBody: 'Документи не знайдено'});
                    }                    
                })){                
                    oResponse = [oResponse];
                    $scope.documents = oResponse;
                    return oResponse;
                /*if (data.hasOwnProperty('message')) {
                  var startsWith = function (str) { return data.message.indexOf(str) > -1; };
                  if (startsWith('Document Access password wrong')) {
                    //ErrorsFactory.logWarn({sBody:'Ви використовуєте старий формат номеру заявки!<br>У майбутньому необхідно перед номером доповнити префікс "0-". (тобто "0-'+sID_Order+'", замість "'+sID_Order+'")'},{asParam:['sID_Order: '+sID_Order,'sToken: '+sToken]});
                    if ($scope.smsPass)
                      $scope.messages = ['Неправильний код'];
                  } else if (startsWith('Document Access password need - sent SMS')) {
                    var phone = data.message.match(/\([^\)]+/)[0].substring(1);
                    $scope.blurredPhone  = phone.slice(0, -7) + '*****' + phone.slice(-2);
                    $scope.showSmsPass = true;
                  } else if (startsWith('Document Access not found')) {
                      $scope.messages = ['Документи не знайдено'];
                  } else {
                      $scope.messages = [data.message];
                  }
                } else {
                    if (typeof oData === 'object') {
                        oData = [oData];
                        $scope.documents = oData;
                    }else{
                        ErrorsFactory.addFail({sBody:'Помилка - повернено не об`єкт!', asParam:['nID_DocumentType: '+nID_DocumentType, 'nID_DocumentOperator: '+nID_DocumentOperator, 'sID_Document: '+sID_Document, 'sVerifyCode: '+sVerifyCode]});
                    }*/
                }
                $scope.spinner = false;
            });
    };
});
