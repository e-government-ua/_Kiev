angular.module('app').factory('FormDataFactory', function(ParameterFactory, DatepickerFactory, FileFactory, BankIDDocumentsFactory, CountryService, $q) {
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
      var self = this;
      if (property.id == 'resident') {
        // todo: #584 для теста п.2 закомментировать эту строку. после теста - удалить
        this.params[property.id].value = 'Україна';
        if (this.params[property.id].value) {
          // #584 п.3 автоподстановка зачения sID_Three в поле sID_Country
          angular.forEach(ActivitiForm.formProperties, function (prop) {
            if (prop.id == 'sID_Country') {
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
              CountryService.getCountryBy_sID_Three(prop.value).then(function (response) {
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
    return angular.forEach(BankIDAccount.customer, function(value, key) {
      switch (key) {
        case 'documents':
          var documents = new BankIDDocumentsFactory();
          documents.initialize(value);

          angular.forEach(documents.list, function(document) {
            var field = null;
            switch (document.type) {
              case 'passport':
                field = 'bankIdPassport';
            }
            if (field == null) {
              return;
            }
            if (this.hasParam(field)) {
              this.fields[field] = true;
              this.params[field].value = documents.getPassport();
            }
          }, this);
          break;
        default:
          var field = 'bankId' + key;

          if (this.hasParam(field)) {
            this.fields[field] = true;
            this.params[field].value = value;
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
