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

    //mock markers
    //$scope.data.formData.params.markers = {
    $scope.markers = {
        validate:{
            PhoneUA:{
                aField_ID:['privatePhone','workPhone', 'phone', 'tel']
            }, Mail:{
                aField_ID:['privateMail','email']
//            }, AutoVIN:{
//                aField_ID:['vin_code', 'vin_code1', 'vin']
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
            // перетворити звичайний input на поле вводу телефону, контрольоване директивою form/directives/tel.js:
            value.type='tel';
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

        ValidationService.validateEmailByMarker( form.email, $scope.markers );
        ValidationService.validateTelephoneByMarker( form.phone, $scope.markers );
//        ValidationService.validateAutoVIN( form.vin, $scope.markers );

        if (form.$valid) {//
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

});
