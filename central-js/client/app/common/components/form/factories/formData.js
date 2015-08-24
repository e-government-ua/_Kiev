angular.module('app').factory('FormDataFactory', function(ParameterFactory, DatepickerFactory, FileFactory, BankIDDocumentsFactory, BankIDAddressesFactory, CountryService, $q) {
  var FormDataFactory = function() {
    this.processDefinitionId = null;

    this.fields = {};
    this.params = {};
  };

  FormDataFactory.prototype.initialize = function(ActivitiForm) {
    this.processDefinitionId = ActivitiForm.processDefinitionId;
    for (var key in ActivitiForm.formProperties) {
      var property = ActivitiForm.formProperties[key];
      switch (property.type) {
        case 'date':
          this.params[property.id] = new DatepickerFactory();
          this.params[property.id].value = property.value;
          break;
        case 'file':
          this.params[property.id] = new FileFactory();
          this.params[property.id].value = property.value;
          break;
        default:
          this.params[property.id] = new ParameterFactory();
          this.params[property.id].value = property.value;
          break;
      }
        //<activiti:formProperty id="bankIdsID_Country" name="Громадянство (Code)" type="invisible" default="UA"></activiti:formProperty>
        //<activiti:formProperty id="sID_Country" name="Country Code (Code)" type="invisible"></activiti:formProperty>
        //<activiti:formProperty id="sCountry" name="Громадянство" type="string"></activiti:formProperty>
      
      var self = this;
      if (property.id === 'resident' || property.id === 'sCountry') {
        // todo: #584 для теста п.2 закомментировать эту строку. после теста - удалить
        //this.params[property.id].value = 'Україна';
        if (this.params[property.id].value) {
          // #584 п.3 автоподстановка зачения sID_Three в поле sID_Country
          angular.forEach(ActivitiForm.formProperties, function (prop) {
            if (prop.id === 'sID_Country') {
              var param = self.params[property.id];
              CountryService.getCountries().then(function(list)
              {
                angular.forEach(list, function(country) {
                  if (country.sNameShort_UA == param.value)
                    self.params[prop.id].value = country.sID_Three;
                });
              })
            }
          });
        } else {
          // #584 п.2 автоподстановка значения в поле "гражданство" из поля bankIdsID_Country если в форме оно не установлено
          angular.forEach(ActivitiForm.formProperties, function (prop) {
            if (prop.id == 'bankIdsID_Country') {
              var param = self.params[property.id];
              //CountryService.getCountryBy_sID_Three(prop.value).then(function (response) {
              CountryService.getCountryBy_sID_Two(prop.value).then(function (response) {
                param.value = response.data.sNameShort_UA;
              });
            }
          });
        }
      }
    }
  };

  FormDataFactory.prototype.hasParam = function(param) {
    return this.params.hasOwnProperty(param);
  };

  FormDataFactory.prototype.setBankIDAccount = function(BankIDAccount) {
    return angular.forEach(BankIDAccount.customer, function(oValue, sKey) {
      switch (sKey) {
        case 'documents':
          var aDocument = new BankIDDocumentsFactory();
          aDocument.initialize(oValue);

          angular.forEach(aDocument.list, function(document) {
            var sFieldName = null;
            switch (document.type) {
              case 'passport':
                sFieldName = 'bankIdPassport';
            }
            if (sFieldName === null) {
              return;
            }
            if (this.hasParam(sFieldName)) {
              this.fields[sFieldName] = true;
              this.params[sFieldName].value = aDocument.getPassport();
            }
          }, this);
          break;

        case 'addresses':
          var aAddress = new BankIDAddressesFactory();
          aAddress.initialize(oValue);

          angular.forEach(aAddress.list, function(document) {
            var sFieldName = null;
            switch (document.type) {
              case 'factual':
                sFieldName = 'bankIdAddressFactual';
                if (this.hasParam(sFieldName)) {
                  this.fields[sFieldName] = true;
                  this.params[sFieldName].value = aAddress.getAddress();
                }
                sFieldName = 'bankIdsID_Country';
                if (this.hasParam(sFieldName)) {
                  this.fields[sFieldName] = true;
                  this.params[sFieldName].value = aAddress.getCountyCode();
                }
              break;
            }
            if (sFieldName === null) {
              return;
            }
            
          }, this);
          break;
          
        default:
          var sFieldName = 'bankId' + sKey;

          if (this.hasParam(sFieldName)) {
            this.fields[sFieldName] = true;
            this.params[sFieldName].value = oValue;
          }
          break;
      }
    }, this);
  };

  FormDataFactory.prototype.setFile = function(name, file) {
    var parameter = this.params[name];
    parameter.removeAll();
    parameter.addFiles([file]);
  };

  FormDataFactory.prototype.setFiles = function(name, files) {
    var parameter = this.params[name];
    parameter.removeAll();
    parameter.addFiles(files);
  };

  FormDataFactory.prototype.addFile = function(name, file) {
    var parameter = this.params[name];
    parameter.addFiles([file]);
  };

  FormDataFactory.prototype.addFiles = function(name, files) {
    var parameter = this.params[name];
    parameter.addFiles(files);
  };

  FormDataFactory.prototype.getRequestObject = function() {
    var data = {
      processDefinitionId: this.processDefinitionId,
      params: {}
    };
    for (var key in this.params) {
      var param = this.params[key];
      data.params[key] = param.get();
    }
    return data;
  };

  return FormDataFactory;
});
