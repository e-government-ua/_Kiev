angular.module('app').service('ActivitiService', function ($q, $http, $location, ErrorsFactory) {

  var aFieldFormData = function (aFormProperties,formData) {//activitiForm
    var aField=[];
    //var aFormProperties = activitiForm.formProperties;
    if(aFormProperties && aFormProperties!==null){
        angular.forEach(aFormProperties, function(oProperty){
            if(oProperty.type==="string" || oProperty.type==="enum" || oProperty.type==="long" || oProperty.type==="date" || oProperty.type==="textArea" || oProperty.type==="queueData" || oProperty.type==="select" || oProperty.type==="file"){
                //var oField = {sID:oProperty.id,sType:oProperty.type,sValue:oProperty.value};
                var oField = {sID:oProperty.id,sType:oProperty.type,sValue:formData.params[oProperty.id].value};//oProperty.value
                //formData.params[propertyID].value
                aField = aField.concat([oField]);
            }
//                console.log("oProperty.id="+oProperty.id+",oProperty.type="+oProperty.type+",oProperty.bVariable="+oProperty.bVariable);
            //oProperty.enumValues = a;
            //if(oProperty.type === "enum" && oProperty.enumValues && oProperty.enumValues != null && oProperty.enumValues.length == 0){//oProperty.id === attr.sName &&
//            if(oProperty.type === "enum" && oProperty.bVariable && oProperty.bVariable !== null && oProperty.bVariable === true){//oProperty.id === attr.sName &&
//                    console.log('oProperty.type === "enum" && oProperty.enumValues && oProperty.enumValues != null && oProperty.enumValues.length == 0');
//                $scope.data.formData.params[oProperty.id].value=null;
//            }
        });
    }
    return aField;
  }

  var prepareFormData = function (oService, oServiceData, formData, nID_Server) {//url
    var data = {
      'nID_Server': nID_Server
    };

    var nID_Region;
    var sID_UA;
    var sID_UA_Common;

    if(oServiceData.nID_Region){
      nID_Region = oServiceData.nID_Region.nID;
      sID_UA = oServiceData.nID_Region.sID_UA;
      sID_UA_Common = oServiceData.nID_Region.sID_UA;
    } else if (oServiceData.nID_City){
      nID_Region = oServiceData.nID_City.nID_Region.nID;
      sID_UA = oServiceData.nID_City.nID_Region.sID_UA;
      sID_UA_Common = oServiceData.nID_City.sID_UA;
    }

    var params = {
      nID_Service : oService.nID,
      nID_Region : nID_Region,
      sID_UA : sID_UA,
      sID_UA_Common : sID_UA_Common
    };

    data = angular.extend(data, formData.getRequestObject());
    data = angular.extend(data, params);

    return data;
  };

  this.getForm = function (oServiceData, processDefinitionId) {
    var oFuncNote = {sHead:"Отримання форми послуги", sFunc:"getForm"};
    var oData = {
      'nID_Server': oServiceData.nID_Server
      , 'sID_BP_Versioned': processDefinitionId.sProcessDefinitionKeyWithVersion
    };
    ErrorsFactory.init(oFuncNote, {asParam:['nID_ServiceData: '+oServiceData.nID, 'sID_BP_Versioned: '+oData.sID_BP_Versioned]});
    return $http.get('./api/process-form', {
      params: oData,
      data: oData
    }).then(function (oResponse) {
        if(ErrorsFactory.bSuccessResponse(oResponse.data)){
            return oResponse.data;
        }
    });
  };

  this.submitForm = function (oService, oServiceData, formData, aFormProperties) {//activitiForm
    var oFuncNote = {sHead:"Сабміт форми послуги", sFunc:"submitForm"};
    var aField = aFieldFormData(aFormProperties,formData);//activitiForm
    ErrorsFactory.init(oFuncNote, {asParam: ['nID_Service: '+oService.nID, 'nID_ServiceData: '+oServiceData.nID, 'processDefinitionId: '+oServiceData.oData.processDefinitionId, "saField: "+JSON.stringify(aField)]});
    var nID_Server = oServiceData.nID_Server;
    var oFormData = prepareFormData(oService, oServiceData, formData, nID_Server);
    return $http.post('./api/process-form', oFormData).then(function (oResponse) {
        if(ErrorsFactory.bSuccessResponse(oResponse.data,function(oThis, doMerge, sMessage, aCode, sResponse){
//            console.log("[submitForm]sMessage="+sMessage+",aCode="+aCode+",sResponse="+sResponse);
            if (!sMessage) {
            } else if (sMessage.indexOf(['happened when sending email']) > -1) {
                doMerge(oThis, {sBody: 'Помилка відсилки єлектронної пошти! (скоріш за все не вірні дані вказані у формі чи електроний адрес)'});
            } else if (sMessage.indexOf(['Exception while invoking TaskListener']) > -1) {
                doMerge(oThis, {sBody: 'Помилка роботи листенера! (скоріш за все не вірні дані вказані у формі)'});
            } else if (sMessage.indexOf(["For input string"]) > -1) {
                doMerge(oThis, {sBody: 'Помилка обробки строкового поля форми! (скоріш за все не вірні дані вказані у формі)'});
            } else if (sMessage.indexOf(["Invalid value for"]) > -1) {
                doMerge(oThis, {sBody: 'Помилка обробки значення поля форми! (скоріш за все не вірні дані вказані у формі)'});
            }
        })){
//            console.log("[submitForm](OK)oResponse.data="+JSON.stringify(oResponse.data));
            return oResponse.data;
        }
      /*if (/err/i.test(response.data.code)) {
          //ErrorsFactory.addFail({""})
        ErrorsFactory.push({
          type: "danger",
          text: [response.data.code, response.data.message].join(" ")
        });
      }*/
    });
  };

  this.loadForm = function (oServiceData, formID) {
    var oFuncNote = {sHead:"Завантаженя форми послуги", sFunc:"loadForm"};
    ErrorsFactory.init(oFuncNote,{asParam: ['nID_ServiceData: '+oServiceData.nID, 'formID: '+formID]});
    var oParams = {nID_Server: oServiceData.nID_Server, formID: formID};
    return $http.get('./api/process-form/load', {params: oParams}).then(function (oResponse) {
        if(ErrorsFactory.bSuccessResponse(oResponse.data)){
            return oResponse.data;
        }
      /*if (/err/i.test(response.data.code)) {
        ErrorsFactory.push({
          type: "danger",
          text: [response.data.code, response.data.message].join(" ")
        });
      }*/
    });
  };

  this.saveForm = function (oService, oServiceData, businessKey, processName, activitiForm, formData) {
    var oFuncNote = {sHead:"Збереження форми послуги", sFunc:"saveForm"};
    var nID_Server = oServiceData.nID_Server;
    var oFormData = prepareFormData(oService, oServiceData, formData, nID_Server);
    var aField = aFieldFormData(activitiForm.formProperties,formData);//activitiForm
    ErrorsFactory.init(oFuncNote, {asParam: ['nID_Service: '+oService.nID, 'nID_ServiceData: '+oServiceData.nID, 'processName: '+processName, 'businessKey: '+businessKey, 'saField: '+JSON.stringify(aField)]});
    var oData = {
      formData : oFormData,
      activitiForm: activitiForm,
      processName : processName,
      businessKey : businessKey
    };
    var restoreFormUrl = $location.absUrl();
    var oParams = {
      nID_Server : nID_Server
    };
    oData = angular.extend(oData, {
      restoreFormUrl: restoreFormUrl
    });
    return $http.post('./api/process-form/save', oData, {params : oParams}).then(function (oResponse) {
        if(ErrorsFactory.bSuccessResponse(oResponse.data)){
            return oResponse.data;
        }
      /*if (/err/i.test(response.data.code)) {
        ErrorsFactory.push({
          type: "danger",
          text: [response.data.code, response.data.message].join(" ")
        });
      }*/
    });
  };

  this.getSignFormPath = function (oServiceData, formID, oService) {
    return '/api/process-form/sign?formID=' + formID + '&nID_Server=' + oServiceData.nID_Server + '&sName=' + oService.sName;

  };

  this.getUploadFileURL = function (oServiceData) {
    return './api/uploadfile?nID_Server=' + oServiceData.nID_Server;
  };

  this.updateFileField = function (oServiceData, formData, propertyID, fileUUID) {
    formData.params[propertyID].value = fileUUID;
  };

  this.checkFileSign = function (oServiceData, fileID){
    var oFuncNote = {sHead:"Перевірка ЕЦП у файлу", sFunc:"checkFileSign"};
    ErrorsFactory.init(oFuncNote, {asParam: ['nID_ServiceData: '+oServiceData.nID, 'fileID: '+fileID]});
    return $http.get('./api/process-form/sign/check', {
      params : {
        fileID : fileID,
        nID_Server : oServiceData.nID_Server
      }
    }).then(function (oResponse) {
        if(ErrorsFactory.bSuccessResponse(oResponse.data)){
            return oResponse.data;
        }
    }).catch(function (error) {
        if(!ErrorsFactory.bSuccessResponse(error.data)){
            return $q.reject(error.data);
        }
      /*ErrorsFactory.push({
        type: "danger",
        text: [error.data.code, error.data.message].join(" ")
      });*/
    });
  };

  this.autoUploadScans = function (oServiceData, scans) {
    var oFuncNote = {sHead:"Завантаженя файлу", sFunc:"autoUploadScans"};
    ErrorsFactory.init(oFuncNote, {asParam: ['nID_ServiceData: '+oServiceData.nID, 'scans: '+scans]});
    var oData = {
      nID_Server: oServiceData.nID_Server,
      scanFields: scans
    };
    return $http.post('./api/process-form/scansUpload', oData).then(function (oResponse) {
        if(ErrorsFactory.bSuccessResponse(oResponse.data)){
            return oResponse.data;
        }
        /*if (/err/i.test(response.data.code)) {
          ErrorsFactory.push({
            type: "danger",
            text: [response.data.code, response.data.message].join(" ")
          });
        }*/
    });
  };
});
