angular.module('documents').controller('DocumentsContentController', function($scope, $state, $stateParams, documents, documentTypes, FileFactory, ServiceService, $modal) {
  $scope.documentTypes = documentTypes;
  $scope.file = new FileFactory();

  angular.forEach(documents, function(item) {
    if (item.oDate_Upload === null) {
      item.oDate_Upload = new Date();
    }
  });

  $scope.shareTab = false;
  $scope.documents = documents;
  //$scope.nDays = 1;
  $scope.sTelephone = '+380';
  //$scope.sTelephone = '';
  $scope.nDaysOptions = [{day: 1, title: '1 день'}, {day: 7, title: '1 тиждень'}, {day: 365, title: '1 рік'}];
  $scope.nDays = $scope.nDaysOptions[1].day;
  $scope.getDocumentLink = ServiceService.getDocumentLink;

  $scope.getSignData = function(document){
    var signData = JSON.parse(document.oSignData);
    //return signData && signData.customer && signData.customer.fullName;
    //return signData && signData.customer && signData.customer.organizations && signData.customer.organizations.length>0 && signData.customer.organizations[0].name;
    //return signData && signData.customer && signData.customer.organizations && signData.customer.organizations.length>0 && signData.customer.organizations[0].name;
    if(signData && signData.customer && signData.customer.organizations && signData.customer.organizations.length>0 && signData.customer.organizations[0].name){
        return signData.customer.organizations[0].name;
    }else{
        return signData && signData.customer && signData.customer.fullName;
    }
  };

  $scope.uploadDocument = function(documentTypeForUpload, documentNameForUpload){
    $scope.file.uploadDocument(documentTypeForUpload, documentNameForUpload, function(){
      $state.transitionTo($state.current, $stateParams, {
        reload: true,
        inherit: false,
        notify: true
      });
    });
  };

  $scope.showSignDataPopup = function(doc)
  {
    $modal.open({
      animation: true,
      templateUrl: 'documentsigndata.html',
      controller: 'DocumentSignDataController',
      resolve: {
        oSignData: function() {
          return JSON.parse(doc.oSignData);
        }
      }
    })
  };

  $scope.showShareTab = function(){
    $scope.shareTab = !$scope.shareTab;
  };

  $scope.shareLink = function(document, sFIO, sTelephone, sMail, nDays) {
    ServiceService.shareLink($state.nID_Subject, document.nID, sFIO,
      getTelephone(sTelephone), getMail(sMail), getDaysInMilliseconds(nDays))
      .then(showConfirmationModal);
  };

  function getTelephone (sTelephone) {
    //<b ng-show="isPhoneFormVisible == 'true'"> (через SMS-пароль)</b>
    if($('[name=content]:checked').val()!="true"){
        sTelephone = ''
    }
    if (sTelephone == '+380' || sTelephone == null || sTelephone == '') {
        sTelephone = ' ';
    }
    return sTelephone;
  }

  function getMail (sMail) {
    if (sMail == null || sMail == '') {
        sMail = ' ';
    }
    return sMail;
  }


  function getDaysInMilliseconds (nDays) {
    if (isNaN(nDays.day * 86400000)) {
      return 7 * 86400000;
    }
    return nDays * 86400000;
  }

  function showConfirmationModal (reply) {
    if (reply.code) {
      showShareLinkError(reply);
      return;
    }
    showShareLinkSuccess(reply);
  }

  function showShareLinkSuccess (reply) {
    $modal.open({
      animation: true,
      templateUrl: 'urlmodal.html',
      controller: 'ModalController',
      size: '',
      resolve: {
        url: function() {
          return reply.value
        }
      }
    });
  }

  function showShareLinkError (reply) {
    switch (reply.code) {
      case 'BUSINESS_ERR':
        console.log("Сталася помилка\n" + reply.code + ': ' + reply.message);
        break;
      default:
        console.log("Сталася помилка\n" + reply.code + ': ' + reply.message);
    }
  }
});
