angular.module('app').controller('ServiceBuiltInBankIDController', function(
    $state, 
    $stateParams, 
    $scope, 
    $timeout, 
    FormDataFactory, 
    ActivitiService, 
    ValidationService, 
    oServiceData, 
    BankIDAccount, 
    ActivitiForm, 
    uiUploader ) {

    'use strict';

  // Удалить это после теста задачи #584
  ActivitiForm.formProperties.push({
    id: "bankIdsID_Country",
    name: "Громадянство",
    type: "invisible",
    value: "UKR",
    readable: true
  });

  $scope.oServiceData = oServiceData;
  $scope.account = BankIDAccount;
  $scope.ActivitiForm = ActivitiForm;

  $scope.data = $scope.data || {};
  $scope.data.formData = new FormDataFactory();
  $scope.data.formData.initialize(ActivitiForm);
  $scope.data.formData.setBankIDAccount(BankIDAccount);

  var currentState = $state.$current;
  $scope.data.region = currentState.data.region;
  $scope.data.city = currentState.data.city;
  
  $scope.ngIfCity = function() {
	if($state.current.name === 'index.service.general.city.built-in') {
		if($scope.data.city) {
			return true;
		} else {
			return false;
		}
	}
	if($state.current.name === 'index.service.general.city.built-in.bankid') {
		if($scope.data.city) {
			return true;
		} else {
			return false;
		}
	}
	return $scope.data.region ? true: false;
  };

    //mock markers
    //$scope.data.formData.params.markers = {
    $scope.markers = {
        validate:{
            PhoneUA:{
                aField_ID:['privatePhone','workPhone', 'phone', 'tel']
            }, Mail:{
                aField_ID:['privateMail','email']
            }, AutoVIN:{
                aField_ID:['vin_code', 'vin_code1', 'vin']
            }
        }
    };


    //var aID_FieldPhoneUA = $scope.data.formData.params.markers.validate.PhoneUA.aField_ID;
    var aID_FieldPhoneUA = $scope.markers.validate.PhoneUA.aField_ID;
    var aID_FieldMail = $scope.markers.validate.Mail.aField_ID;
//    var aID_FieldAutoVIN = $scope.markers.validate.AutoVIN.aField_ID;

    angular.forEach($scope.ActivitiForm.formProperties, function(value, key) {
        var sField = value.name;
        var s;
        if (sField === null) {
          sField = '';
        }
        var a = sField.split(';');
        s = a[0].trim();
        value.sFieldLabel = s;
        s = null;
        if (a.length > 1) {
          s = a[1].trim();
          if (s === '') {
            s = null;
          }
        }
        value.sFieldNotes = s;

        if (_.indexOf(aID_FieldPhoneUA, value.id) !== -1){
          // перетворити input на поле вводу телефону, контрольоване директивою form/directives/tel.js:
          value.type='tel';
          value.sFieldType='tel';
        }
/*        
        if (_.indexOf(aID_FieldAutoVIN, value.id) !== -1){
            value.sFieldType='AutoVIN';
        }
*/        
  });

  $scope.submit = function(form) {
        $scope.isSending = true;
        form.$setSubmitted();
        var bValid=true;

        //$($('input[type=tel]')[0]).removeClass('has-error');
        /*if (!$($('input[type=tel]')[0]).intlTelInput('isValidNumber')){//bValid &&
            bValid = false;
            //$($('input[type=tel]')[0]).addClass('has-error');
            return;
        }*/
        /*
        .has-error .form-control {
          border-color: #a94442;
          box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
        }*/

        ValidationService.validateEmailByMarker( form.email, $scope.markers );
        ValidationService.validateTelephoneByMarker( form.phone, $scope.markers );
        ValidationService.validateAutoVIN( form.vin, $scope.markers );

        if (form.$valid && bValid) {//
            ActivitiService
                .submitForm(oServiceData, $scope.data.formData)
                .then(function(result) {
                    $scope.isSending = false;
                    var state = $state.$current;

                    var submitted = $state.get(state.name + '.submitted');
                    submitted.data.id = result.id;

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
