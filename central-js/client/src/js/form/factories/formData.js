define('formData/factory', ['angularAMD', 'file/directive', 'parameter/factory', 'datepicker/factory', 'file/factory', 'bankid/documents/factory'], function(angularAMD) {
  angularAMD.factory('FormDataFactory', function(ParameterFactory, DatepickerFactory, FileFactory, BankIDDocumentsFactory) {
    var FormDataFactory = function() {
      this.processDefinitionId = null;

      this.fields = {};
      this.params = {};
      this.response = null;
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
      }
    };

    FormDataFactory.prototype.setResponse = function(data){
      return this.response = data;
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
});
