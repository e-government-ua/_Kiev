angular.module('documents').controller('DocumentsContentController', function($scope, $state, documents, FileFactory, ServiceService, $modal) {
  $scope.file = new FileFactory();

  angular.forEach(documents, function(item) {
    if (item.oDate_Upload === null) {
      item.oDate_Upload = new Date();
    }
  });

  $scope.shareTab = false;
  $scope.documents = documents;
  $scope.nDays = 1;
  //$scope.sTelephone = '+380';
  $scope.sTelephone = '';
  $scope.nDaysOptions = [{day: 1, title: '1 день'}, {day: 7, title: '1 тиждень'}, {day: 365, title: '1 рік'}];
  $scope.nDays = $scope.nDaysOptions[1];
  $scope.getDocumentLink = ServiceService.getDocumentLink;

  $scope.showShareTab = function(){
    $scope.shareTab = !$scope.shareTab;
  }

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
        alert("Сталася помилка\n" + reply.code + ': ' + reply.message);
        break;
      default:
        alert("Сталася помилка\n" + reply.code + ': ' + reply.message);
    }
  }
});
