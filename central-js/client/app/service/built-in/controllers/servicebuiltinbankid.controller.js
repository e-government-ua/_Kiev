angular.module('app').controller('ServiceBuiltInBankIDController', function(
  $state,
  $stateParams,
  $scope,
  $timeout,
  FormDataFactory,
  ActivitiService,
  ValidationService,
  oService,
  oServiceData,
  BankIDAccount,
  ActivitiForm,
  uiUploader,
  FieldAttributesService,
  MarkersFactory,
  FieldMotionService) {

  'use strict';

  // FIXME: Удалить это после теста задачи #584
  /*var bankIdFound = false;
  angular.forEach(ActivitiForm.formProperties, function(prop) {
    if (prop.id === 'bankIdsID_Country') {
      bankIdFound = true;
    }
  });*/
  /*if (!bankIdFound) {
    ActivitiForm.formProperties.push({
      id: 'bankIdsID_Country',
      name: 'Громадянство',
      type: 'invisible',
      value: 'UKR',
      readable: true
    });
  }*/

  // todo: Удалить после теста задачи #584 п.3
  /*var sID_Country_found = false;
  angular.forEach(ActivitiForm.formProperties, function(prop) {
    if (prop.id === 'sID_Country') {
      sID_Country_found = true;
    }
  });*/
  /*if (!sID_Country_found) {
    ActivitiForm.formProperties.push({
      id: 'sID_Country',
      name: 'Country Code',
      type: 'string',
      value: '',
      readable: true,
      writable: true
    });
  }*/

  $scope.oServiceData = oServiceData;
  $scope.account = BankIDAccount;
  $scope.ActivitiForm = ActivitiForm;

  $scope.data = $scope.data || {};
  $scope.data.formData = new FormDataFactory();
  $scope.data.formData.initialize(ActivitiForm);
  $scope.data.formData.setBankIDAccount(BankIDAccount);
  //TODO uncomment after testing
  $scope.data.formData.uploadScansFromBankID(oServiceData);
  var currentState = $state.$current;

  $scope.data.region = currentState.data.region;
  $scope.data.city = currentState.data.city;

  $scope.ngIfCity = function() {
    if ($state.current.name === 'index.service.general.city.built-in') {
      if ($scope.data.city) {
        return true;
      } else {
        return false;
      }
    }
    if ($state.current.name === 'index.service.general.city.built-in.bankid') {
      if ($scope.data.city) {
        return true;
      } else {
        return false;
      }
    }
    return $scope.data.region ? true : false;
  };

  // TODO try markers override here
  $scope.markers = ValidationService.getValidationMarkers();
  var aID_FieldPhoneUA = $scope.markers.validate.PhoneUA.aField_ID;

  angular.forEach($scope.ActivitiForm.formProperties, function(field) {

    var sFieldName = field.name || '';

    // 'Як працює послуга; посилання на інструкцію' буде розбито на частини по ';'
    var aNameParts = sFieldName.split(';');
    var sFieldNotes = aNameParts[0].trim();

    field.sFieldLabel = sFieldNotes;

    sFieldNotes = null;

    if (aNameParts.length > 1) {
      sFieldNotes = aNameParts[1].trim();
      if (sFieldNotes === '') {
        sFieldNotes = null;
      }
    }
    field.sFieldNotes = sFieldNotes;

    // перетворити input на поле вводу телефону, контрольоване директивою form/directives/tel.js:
    if (_.indexOf(aID_FieldPhoneUA, field.id) !== -1) {
      field.type = 'tel';
      field.sFieldType = 'tel';
    }
    if (field.type == 'markers' && $.trim(field.value)) {
      var sourceObj = null;
      try {
        sourceObj = JSON.parse(field.value);
      } catch (ex) {
        alert('markers attribute ' + field.name + " contain bad formatted json\n" + ex.name + ", " + ex.message
          + "\nfield.value: " + field.value
        );
      }
      if (sourceObj !== null)
        _.merge(MarkersFactory.getMarkers(), sourceObj, function (destVal, sourceVal) {
          if (_.isArray(sourceVal)) return sourceVal;
        });
    }
  });

  $scope.submit = function(form) {
    $scope.isSending = true;
    form.$setSubmitted();
    var bValid = true;

    ValidationService.validateByMarkers(form, null, true);

    if (form.$valid && bValid) { //
      ActivitiService
        .submitForm(oService, oServiceData, $scope.data.formData)
        .then(function(result) {

          $scope.isSending = false;

          var state = $state.$current;

          var submitted = $state.get(state.name + '.submitted');
          if (!result.id) {
            console.log(result);
            return;
          }
          //TODO: Fix Alhoritm Luna
          var nCRC = ValidationService.getLunaValue(result.id);

          submitted.data.id = result.id + nCRC; //11111111

          $scope.isSending = false;

          $scope.$root.data = $scope.data;

          return $state.go(submitted, $stateParams);
        });

    } else {

      $scope.isSending = false;

      return false;

    }
  };

  $scope.cantSubmit = function(form) {
    return $scope.isSending || ($scope.isUploading && !form.$valid);
  };

  $scope.bSending = function(form) {
    return $scope.isSending;
  };

  $scope.isUploading = false;
  $scope.isSending = false;

  var fileKey = function(file) {
    return file.name + file.size;
  };

  $scope.uploadFile = function() {
    uiUploader.startUpload({
      url: ActivitiService.getUploadFileURL(oServiceData),
      concurrency: 2,
      onProgress: function(file) {
        $scope.isUploading = true;
        $scope.$apply();
      },
      onCompleted: function(file, response) {
        $scope.isUploading = false;
        if (response) {
          try {
            JSON.parse(response);
            // alert(response);
          } catch (e) {
            ActivitiService.updateFileField(oServiceData,
              $scope.data.formData, $scope.files[fileKey(file)], response);
          }
        }
        $scope.$apply();
      }
    });
  };

  $scope.files = {};
  $scope.addFile = function(propertyId, event) {
    var files = event.target.files;
    if (files && files.length === 1) {
      uiUploader.addFiles(files);
      if (uiUploader.getFiles()[0]) {
        $scope.files[fileKey(event.target.files[0])] = propertyId;
      }
    }
    $scope.$apply();
  };

  $scope.showFormField = function(property) {
    var fieldES = FieldAttributesService.editableStatusFor(property.id);
    var ES = FieldAttributesService.EditableStatus;
    return (
        !$scope.data.formData.fields[property.id]
        && property.type!='invisible'
        && property.type!='markers'
        && fieldES == ES.NOT_SET
    ) || fieldES == ES.EDITABLE
  };

  $scope.renderAsLabel = function(property) {
    var fieldES = FieldAttributesService.editableStatusFor(property.id);
    var ES = FieldAttributesService.EditableStatus;
    //property.type !== 'file'
    return (
            $scope.data.formData.fields[property.id]
      &&  fieldES == ES.NOT_SET
    ) || fieldES == ES.READ_ONLY;
  };

  $scope.isFieldVisible = function(property) {
    return property.id != 'processName'
    && (FieldMotionService.isFieldMentioned.inShow(property.id) ?
        FieldMotionService.isFieldVisible(property.id, $scope.data.formData.params) : true);
  };

  $scope.isFieldRequired = function(property) {
    return FieldMotionService.isFieldMentioned.inRequired(property.id) ?
      FieldMotionService.isFieldRequired(property.id, $scope.data.formData.params) : property.required;
  };

  // $timeout(function () {
  //     $('input[type=tel]').intlTelInput({
  //         defaultCountry: 'auto',
  //         autoFormat: true,
  //         allowExtensions: true,
  //         preferredCountries: ['ua'],
  //         autoPlaceholder: false,
  //         geoIpLookup: function(callback) {
  //             $.get('http://ipinfo.io', function() {}, 'jsonp').always(function(resp) {
  //                 var countryCode = (resp && resp.country) ? resp.country : '';
  //                 callback(countryCode);
  //             });
  //         }
  //     });
  // });

});
