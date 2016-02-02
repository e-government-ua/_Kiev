/*
  This factory is for displaying error messages
  to add new message to display you should use a push method
  the error object has two fields:
    type - defines a message style
    text - message displaying to user
*/
//angular.module('app').directive('dropdownOrgan', function (OrganListFactory, $http, $timeout) {
//angular.module("app").factory("ErrorsFactory", function() {
angular.module("app").factory("ErrorsFactory", function(SimpleErrorsFactory,$http) {
  /* existing error types */
    
  var oDataInfos = {}; //console only
  var oDataWarns = {};
  var oDataErrors = {};
  var oDataDefaultCommon = {};
    
  return {

    merge: function(oDataOld, oDataNew){
        if(!oDataNew){
            oDataNew={};
        }
        if(!oDataOld){
            oDataOld={};
        }
        console.log("[merge]:(START)oDataOld="+JSON.stringify(oDataOld)+",oDataNew="+JSON.stringify(oDataNew));
        if(oDataNew.asParam && oDataOld.asParam){
            angular.forEach(oDataOld.asParam, function (sParamOld) {
                //oDataOld.asParam
                //oMessage.sType = errorTypes.indexOf(oMessage.sType) >= 0 ? oMessage.sType : "danger";
                console.log("[merge]:sParamOld="+sParamOld);
                if(oDataNew.asParam.indexOf(sParamOld)<0){
                    console.log("[merge]:(ADDED)sParamNew="+sParamOld);
                    oDataNew.asParam = oDataNew.asParam.concat([sParamOld]);
                }
            });
            //oDataNew.asParam = $.merge(oDataOld.asParam, oDataNew.asParam);
        }
        if(oDataNew.oResponse && oDataOld.oResponse){
            oDataNew.oResponse = $.extend(oDataOld.oResponse, oDataNew.oResponse);
        }
        oDataNew=$.extend(oDataOld,oDataNew);
        console.log("[merge]:(FINAL)oDataOld="+JSON.stringify(oDataOld)+",oDataNew="+JSON.stringify(oDataNew));
        return oDataNew;
    },
    init: function(oDataDefault,oDataNew){
        oDataDefaultCommon = oDataDefault ? oDataDefault : {};
        oDataDefaultCommon = this.merge(oDataDefaultCommon, oDataNew);
        //oDataDefaultCommon = $.extend(oDataDefaultCommon,oDataNew);
        oDataErrors = {};
        oDataWarns = {};
        oDataInfos = {};        
    },
    reset: function(){
        this.init(oDataDefaultCommon);
    },
    addInfo: function(oDataNew, oDataDefault){
        oDataDefaultCommon = oDataDefault ? oDataDefault : oDataDefaultCommon;
        oDataInfos = this.merge(oDataInfos, oDataNew);
        //oDataInfos=$.extend(oDataInfos,oDataNew);
        //oDataInfos = $.merge(oDataInfos || {}, oDataNew);
    },
    addWarn: function(oDataNew, oDataDefault){
        oDataDefaultCommon = oDataDefault ? oDataDefault : oDataDefaultCommon;
        oDataWarns = this.merge(oDataWarns, oDataNew);
        //oDataWarns=$.extend(oDataNew,oDataNew);
        //oDataWarns = $.merge(oDataWarns || {}, oDataNew);
    },
    addFail: function(oDataNew, oDataDefault){
        oDataDefaultCommon = oDataDefault ? oDataDefault : oDataDefaultCommon;
        oDataErrors = this.merge(oDataErrors, oDataNew);
        //oDataErrors=$.extend(oDataErrors,oDataNew);
        //oDataErrors = $.merge(oDataErrors || {}, oDataNew);
    },
    add: function(oDataNew){
        //var errorTypes = ["warning", "danger", "success", "info"],
//        console.log("[add]:soDataNew="+JSON.stringify(oDataNew));
        if(oDataNew.sType==="warning"){
            this.addWarn(oDataNew);
        }else if(oDataNew.sType==="info" || oDataNew.sType==="debug" || oDataNew.sType==="success"){
            this.addInfo(oDataNew);
        }else{
            this.addFail(oDataNew);
        }
    },

    bSuccess: function(oDataDefault,bSend,bHide){
        var bSuccess = true;
        if(!oDataDefault){
            oDataDefault=oDataDefaultCommon;
        }
        if(oDataErrors.sBody){
            this.addFail(oDataDefault);
            console.error("[bSuccess]:oDataErrors="+JSON.stringify(oDataErrors));
            var oData=oDataErrors;
            this.push({type: "danger", oData: oData});
            bSuccess  = false;
        }
        if(oDataWarns.sBody){
            this.addWarn(oDataDefault);
            console.warn("[bSuccess]:oDataWarns="+JSON.stringify(oDataWarns));
            var oData=oDataWarns;
            this.push({type: "warning", oData: oData});
            bSuccess  = false;
        }
        if(oDataInfos.sBody){
            this.addInfo(oDataDefault);
            console.info("[bSuccess]:oDataInfos="+JSON.stringify(oDataInfos));
            var oData=oDataInfos;
            //var errorTypes = ["warning", "danger", "success", "info"],
            if(bSend){
                this.push({type: "success", oData: oData, bSend: true, bHide: bHide});
            }
            bSuccess  = false;
        }
        this.init(oDataDefaultCommon);
        return bSuccess;
    },        
    log: function(oDataDefault, bSend, bHide){
        this.bSuccess(oDataDefault,bSend, bHide);
    },
    
    logInfoSend: function(oDataNew, oDataDefault){
        this.addInfo(oDataNew, oDataDefault);
        this.log(null, true);
    },

    logInfoSendHide: function(oDataNew, oDataDefault){
        this.addInfo(oDataNew, oDataDefault);
        this.log(null, true, true);
    },

    logInfo: function(oDataNew, oDataDefault){
        this.addInfo(oDataNew, oDataDefault);
        this.log();
    },
    logWarn: function(oDataNew, oDataDefault){
        this.addWarn(oDataNew, oDataDefault);
        this.log();
    },
    logFail: function(oDataNew, oDataDefault){
        this.addFail(oDataNew, oDataDefault);
        this.log();
    },

    bSuccessResponse: function(oResponse, onCheckMessage, oDataDefault){
        if(!oDataDefault){
            oDataDefault=oDataDefaultCommon;
        }
        this.init(oDataDefaultCommon);
        try{
//            var fMerge=this.merge;
//            var fAdd=this.add;
            if (!oResponse) {
                var oMergeDefault={sType: 'danger',sBody: 'Пуста відповідь на запит!'};
                if(onCheckMessage){
                    onCheckMessage(this,function(oThis, oMerge){
//                        console.log("[bSuccessResponse](!oResponse)oMerge="+oMerge)
                        //oMergeDefault=$.extend(oMergeDefault,oMerge);
                        oMergeDefault = oThis.merge(oMergeDefault, oMerge) ;
                    });
                }
//                console.log("[bSuccessResponse](add(oMergeDefault))oMergeDefault="+oMergeDefault)
                this.add(oMergeDefault);
            }else if (typeof oResponse !== 'object') {
                var oMergeDefault={sType: 'danger',sBody: 'Повернено не об`єкт!'};
                if(onCheckMessage){
                    onCheckMessage(this,function(oThis, oMerge){
//                        console.log("[bSuccessResponse.oResponse !== 'object'](!oResponse)oMerge="+oMerge)
                        //oMergeDefault=$.extend(oMergeDefault,oMerge);
                        oMergeDefault = oThis.merge(oMergeDefault, oMerge) ;
                    }, null, null, oResponse);
                }
                oMergeDefault=$.extend(oMergeDefault,{oResponse:{sData: oResponse}});
//                console.log("[bSuccessResponse.oResponse !== 'object'](add(oMergeDefault))oMergeDefault="+oMergeDefault)
                this.add(oMergeDefault);
            }else {
                var nError=0;
                if (oResponse.hasOwnProperty('message')) {
//                    console.log("[bSuccessResponse.message](onCheckMessage)oResponse.message="+oResponse.message)
                    if(onCheckMessage){
                        onCheckMessage(this,function(oThis, oMerge){
//                            console.log("[bSuccessResponse.message](onCheckMessage)soMerge="+JSON.stringify(oMerge))
                            oThis.add(oMerge);
                        }, oResponse.message);
                    }
                    this.add({oResponse:{sMessage: oResponse.message}});
                    oResponse.message=null;
                    nError++;
                }
                if (oResponse.hasOwnProperty('code')) {
//                    console.log("[bSuccessResponse.code](onCheckMessage)oResponse.code="+oResponse.code)
                    if(onCheckMessage){
                        onCheckMessage(this,function(oThis, oMerge){
//                            console.log("[bSuccessResponse.code](onCheckMessage)soMerge="+JSON.stringify(oMerge))
                            oThis.add(oMerge);
                        }, null, [oResponse.code]);
                    }
                    this.add({oResponse:{sCode: oResponse.code}});
                    oResponse.code=null;
                    nError++;
                }
//                console.log("[bSuccessResponse](onCheckMessage)nError="+nError+",soResponse="+JSON.stringify(oResponse));
                if(nError>0){
                    if(!oDataErrors.sBody&&!oDataWarns.sBody&&!oDataInfos.sBody){
                        if(nError!==2){
                            this.add({sBody: 'Помилка при запиті та повернено не стандартній об`єкт!'});
                        }else{
                            this.add({sBody: 'Помилка при запиті!'});
                        }
                    }
                    this.add({oResponse:{soData: JSON.stringify(oResponse)}});
                }
            }
        }catch(sError){
            this.addFail({sBody: 'Невідома помилка у обробці відповіді сервера!', sError: sError, oResponse: {soData: typeof oResponse !== 'object' ? oResponse : JSON.stringify(oResponse)}});
        }
        if(oDataErrors.sBody){
            this.logFail(oDataDefault);
            return false;
        }else if(oDataWarns.sBody){
            this.logWarn(oDataDefault);
            return false;
        }
        return true;
    },     

    send: function(oMessage){//oDataMessage
/*
 /api/order/setEventSystem/:sFunction
    @RequestMapping(value = "/action/event/setEventSystem", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    Long setEventSystem(
            @ApiParam(value = "", required = false) @RequestParam(value = "sType", required = false) String sType,
            @ApiParam(value = "Номер-ИД субьекта", required = false) @RequestParam(value = "nID_Subject", required = false) Long nID_Subject,
            @ApiParam(value = "Номер-ИД сервера", required = false) @RequestParam(value = "nID_Server", required = false) Long nID_Server,
            @ApiParam(value = "", required = false) @RequestParam(value = "sFunction", required = false) String sFunction,
            @ApiParam(value = "", required = false) @RequestParam(value = "sHead", required = false) String sHead,
            @ApiParam(value = "", required = false) @RequestParam(value = "sBody", required = false) String sBody,
            @ApiParam(value = "", required = false) @RequestParam(value = "sError", required = false) String sError,
            @ApiParam(value = "Карта других параметров", required = false) @RequestBody String smData
*/

    /*
      <p ng-bind-html="error.sBody" class="text-{{error.sType}}">
        <!--{{error.sBody}}-->
      </p>
      <p ng-if="error.sDate" class="text-{{error.sType}}">
        Час: {{error.sDate}}
      </p>
      <p ng-if="error.oData.sFunc" class="text-{{error.sType}}">
        Функція: {{error.oData.sFunc}}
      </p>
      <p ng-if="error.oData.sError" class="text-{{error.sType}}">
        Війняток: {{error.oData.sError}}
      </p>
      <div ng-if="error.oData.oResponse" class="text-{{error.sType}}">
        <b>Відповідь сервера:</b>
        <label ng-if="error.oData.oResponse.sMessage" class="text-{{error.sType}}">
            Повідомленя: {{error.oData.oResponse.sMessage}}
        </label>
        <label ng-if="error.oData.oResponse.sCode" class="text-{{error.sType}}">
            Код: {{error.oData.oResponse.sCode}}
        </label>
        <label ng-if="error.oData.oResponse.soData" class="text-{{error.sType}}">
            Інші дані (обь'єкт): {{error.oData.oResponse.soData}}
        </label>
        <label ng-if="error.oData.oResponse.sData" class="text-{{error.sType}}">
            Інші дані (строка): {{error.oData.oResponse.sData}}
        </label>
      </div>
      <div ng-if="error.oData.asParam" class="text-{{error.sType}}">
        <b>Значення параметрів:</b>
        <label ng-repeat="sParam in error.oData.asParam" class="text-{{error.sType}}">
          {{sParam}}
        </label><br>
      </div>
    */
        try{
            var bProcessing = false;
            var sendData = function (oMessage) {//oData//oDataMessage
                var sFunction=oMessage.oData.sFunc;
                var oParams={sHead:oMessage.sHead,sBody:oMessage.sBody,sError:oMessage.oData.sError,sType:oMessage.sType};
                var oBody={oResponse:oMessage.oData.oResponse,asParam:oMessage.oData.asParam,sDate:oMessage.sDate};
                var oBodyData={oParams:oParams, oBody:oBody};
                //oMessage.sHead
                /*    
                oParams = _.extend(oParams, req.body.oParams);
                var apiReq = activiti.buildRequest(req, '/action/event/setEventSystem', oParams);
                apiReq.body = req.body.oBody;
                */
                var bSending = true;
                oMessage.bSending=bSending;
                $http.post('./api/order/setEventSystem/' + sFunction, oBodyData).success(function (nID) {
                    bProcessing = true;
                    //angular.forEach(attributes, function(attr){
                      //console.log("attr.sName="+attr.sName+",currentKey="+currentKey);
                    //});
                    console.log("[send]:oMessage.nID="+oMessage.nID);
                    //$timeout(function(){
                      bProcessing = false;
                      bSending=false;
                    //});
                    oMessage.nID=nID;
                    oMessage.bSending=bSending;
                });
            };  
            //oDataMessage
            sendData(oMessage);
            /*angular.forEach(Object.keys(scope.formData.params), function (key) {
              scope.$watch('formData.params.' + key + '.value', function () {
                if (scope.ngModel !== null && scope.ngModel !== '0' && scope.ngModel.length > 0 && !bSent)
                  sendData(key);
              })
            });*/            
        }catch(sError){
            console.log("[send]:sError="+sError);
        }
    },
    
    /*
      returns all existing errors
    */
    getErrors: function() {
      return SimpleErrorsFactory.getErrors();
    },
    /*
      adding a new error message to errors collection
      @example ... ErrorsFactory.push({type:"warning", text: "Critical Error"});
    */
    push: function(oMessage) {
        SimpleErrorsFactory.push(oMessage);
        //ErrorsFactory.push({type: "danger", text: s});
        //this.send(oMessage);
        //var errorTypes = ["warning", "danger", "success", "info"],
        if(oMessage.sType==="warning"||oMessage.sType==="danger"){
            this.send(oMessage);
        }else if(oMessage.sType==="success"&&oMessage.bSend){
            this.send(oMessage);
        }else if(oMessage.sType==="info"&&oMessage.bSend){
            this.send(oMessage);
        }
    }
    
    
  };

});
