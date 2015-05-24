define('formData/factory', ['angularAMD', 'file/directive', 'parameter/factory', 'datepicker/factory', 'file/factory'], function(angularAMD) {
	angularAMD.factory('FormDataFactory', ['ParameterFactory', 'DatepickerFactory', 'FileFactory',
		function(ParameterFactory, DatepickerFactory, FileFactory) {
			var capitalizeFirst = function(input) {
				if (input) {
					return input.substring(0, 1).toUpperCase() + input.substring(1);
				}
				return input;
			};

			var FormDataFactory = function() {
				this.processDefinitionId = null;

				this.fields = {};
				this.params = {};
			};

			FormDataFactory.prototype.initialize = function(ActivitiForm) {
				this.processDefinitionId = ActivitiForm.processDefinitionId.sProcessDefinitionKeyWithVersion;
				//this.sProcessDefinitionName = ActivitiForm.processDefinitionId.sProcessDefinitionName;
				this.sProcessDefinitionName = "тест";
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

			FormDataFactory.prototype.hasParam = function(param) {
				return this.params.hasOwnProperty(param);
			};

			FormDataFactory.prototype.setBankIDAccount = function(BankIDAccount) {
				return angular.forEach(BankIDAccount.customer, function(value, key) {
					var field = 'bankId'+key;
					var finalValue = value;
					if (key === 'documents') {
						if (value && value.length === 1 && value[0]) {
							var documentObject = value[0];
							if (documentObject.type === 'passport') {
								finalValue =
									documentObject.series +
									documentObject.number + ' ' +
									documentObject.issue + ' ' +
									documentObject.dateIssue;

								field = 'bankId' + capitalizeFirst(documentObject.type);								
							}
						}
					/*} else if (key === 'addresses') {
						if (value && value.length === 1 && value[0]) {
							var oAddress = value[0];
							if (oAddress.type === 'factual') {
								finalValue =
									oAddress.country
                                                                        + ' ' + oAddress.state
                                                                        + ', ' + oAddress.area
                                                                        + ', ' + oAddress.city
                                                                        + ', ' + oAddress.street
                                                                        + ', ' + oAddress.houseNo
                                                                        + ', к.' + oAddress.flatNo
                                                                ;
								field = 'bankId' + capitalizeFirst(oAddress.type);								
							}
						}*/
					} else {
						//field = 'bankId' + capitalizeFirst(key);
						field = 'bankId' + key;
						finalValue = value;
					}

					if (this.hasParam(field)) {
						this.fields[field] = true;
						this.params[field].value = finalValue;
					}
				}, this);
			};
			
			FormDataFactory.prototype.setFile = function(name, file) {
				var parameter = this.params[name];
				parameter.remove(file);
				parameter.addFiles(file);
			};
			
			FormDataFactory.prototype.setFiles = function(name, files) {
				var parameter = this.params[name];
				parameter.removeAll();
				parameter.addFiles(files);
			};
			
			FormDataFactory.prototype.addFile = function(name, file) {
				var parameter = this.params[name];
				parameter.addFiles(file);
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
		}
	]);
});