angular.module('app').directive('dropdownOrgan', function (OrganListFactory, $http, $timeout) {
  return {
    restrict: 'EA',
    templateUrl: 'app/common/components/form/directives/dropdownOrgan/dropdownOrgan.html',
    scope: {
      ngModel: "=",
      serviceData: "=",
      ngRequired: "=",
      formDataProperty: "=",
      activitiForm: "=",
      formData: "="
    },
    link: function (scope) {
      // init organ list for organ select
      scope.organList = new OrganListFactory(scope.serviceData);
      scope.loadOrganList = function (search) {
        return scope.organList.load(scope.serviceData, search);
      };
      scope.onSelectOrganList = function (organ) {
        scope.ngModel = organ.sID_Public;
        scope.formDataProperty.nID = organ.nID;
        scope.organList.typeahead.model = organ.sNameUa;
      };
      scope.organList.reset();
      scope.organList.initialize();
      scope.organList.load(scope.serviceData, null).then(function (regions) {
        scope.organList.initialize(regions);
      });

      var getAttributesDataObject = function () {
        var result = {};
        angular.forEach(scope.formData.params, function (param, key) {
          result[key] = param.value;
        });
        return result;
      };

      var getSelectedRegion = function() {
        for (var i=0;i<scope.organList.dropdown.list.length;i++) {
          var region = scope.organList.dropdown.list[i];
          if (region.sID_Public == scope.ngModel)
            return region;
        }
      };

      var attributesApplying = false;

      var loadAttributesData = function (currentKey) {
        $http.post('./api/organs/attributes/' + scope.serviceData.oSubject_Operator.nID + '/' + getSelectedRegion().nID,
          getAttributesDataObject()).success(function (attributes) {
          attributesApplying = true;
          angular.forEach(attributes, function(attr){
            console.log("attr.sName="+attr.sName+",currentKey="+currentKey);
            
          //angular.forEach(attributes, function(attr){
              if(attr.sValue && attr.sValue !== null && attr.sValue.substr(0,1)==="["){
                console.log("attr.sValue="+attr.sValue);
                var n=0;
                console.log("scope.activitiForm="+scope.activitiForm);
                if(scope.activitiForm && scope.activitiForm!==null){
                    console.log("scope.activitiForm.formProperties="+scope.activitiForm.formProperties);
                    if(scope.activitiForm.formProperties && scope.activitiForm.formProperties!==null){
                        angular.forEach(scope.activitiForm.formProperties, function(oProperty){
                            console.log("oProperty.id="+oProperty.id+",oProperty.type="+oProperty.type);
                            if(oProperty.id === attr.sName && oProperty.type === "enum"){
                                console.log('oProperty.id === attr.sValue && oProperty.type === "enum"');
                                //if(scope.formData.params[attr.sName].type === "enum" && attr.sValue.substr(0,1)==="["){
                                //if(scope.activitiForm.formProperties[attr.sName].type === "enum" && attr.sValue.substr(0,1)==="["){
                                var sa=attr.sValue;
                                console.log("sa(before)="+sa);
                                sa=sa.substr(1);
                                sa=sa.substr(0,sa.length-1);
                                console.log("sa(after)="+sa);
                                var as=sa.split(",");
                                console.log("as="+as);
                                var a=[];
                                var nItem=0;
                                angular.forEach(as, function(s){
                                    if(s.substr(0,1)==="\""){
                                        s=s.substr(1);
                                    }
                                    if(s.substr(s.length-1,1)==="\""){
                                        s=s.substr(0,s.length-1);
                                    }
                                    var o={id: nItem+"", name: s};
                                    a=a.concat([o]);
                                    /*enumValues: [{id: "attr1_post", name: "через національного оператора поштового зв'язку"},…]
                                          0: {id: "attr1_post", name: "через національного оператора поштового зв'язку"}
                                          id: "attr1_post"
                                          name: "через національного оператора поштового зв'язку"
                                          1: {id: "attr2_bank", name: "на рахунок у банку"}
                                          id: "attr2_bank"
                                          name: "на рахунок у банку"*/
                                    nItem++;
                                });
                                console.log("a="+a);
                                //console.log("scope.formData.params[attr.sName].enumValues="+scope.formData.params[attr.sName].enumValues);
                                console.log("oProperty.enumValues="+oProperty.enumValues);
//                                console.log("scope.activitiForm.formProperties[n].enumValues="+scope.activitiForm.formProperties[n].enumValues);
                                //if(scope.formData.params[attr.sName].enumValues!==a){
                                if(oProperty.enumValues!==a && oProperty && oProperty !== null && oProperty.enumValues.length === 0 ){
                                    console.log("<>");
                                    //scope.formData.params[attr.sName].enumValues = a;
                                    //if(oProperty.type === "enum" && oProperty.bVariable && oProperty.bVariable !== null && oProperty.bVariable === true){//oProperty.id === attr.sName && 
                                    oProperty.bVariable = true;
                                    oProperty.enumValues = a;
//                                    scope.activitiForm.formProperties[n].bVariable = true;
//                                    scope.activitiForm.formProperties[n].enumValues = a;
                                    //var aEnum = $scope.data.formData.aEnum;
                                    //scope.activitiForm.formProperties[n].sCustomType
//                                    scope.formData.params[attr.sName].sCustomType = "enum";

                                }
                            }
                            n++;
                        });   
                    }
                }
            }else if (angular.isDefined(scope.formData.params[attr.sName]) && currentKey != attr.sName){
                //console.log("isDefined,attr.sValue="+attr.sValue+",scope.formData.params[attr.sName].type="+scope.formData.params[attr.sName].type );
                console.log("isDefined,attr.sValue="+attr.sValue );
                scope.formData.params[attr.sName].value = attr.sValue || "";
            }
          });
          $timeout(function(){
            attributesApplying = false;
          })
        });
      };

      angular.forEach(Object.keys(scope.formData.params), function (key) {
        scope.$watch('formData.params.' + key + '.value', function () {
          if (scope.ngModel !== null && scope.ngModel !== '0' && scope.ngModel.length > 0 && !attributesApplying)
            loadAttributesData(key);
        })
      });
    }
  };
});
