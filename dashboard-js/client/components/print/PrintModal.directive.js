'use strict';

angular.module('dashboardJsApp').directive('printModal', ['$window', function ($window) {
  return {
    restrict: 'E',
    link: function (scope, element, attrs, ngModel) {
      scope.dialogStyle = {};
      if (attrs.width)
        scope.dialogStyle.width = attrs.width;
      if (attrs.height)
        scope.dialogStyle.height = attrs.height;

      scope.hideModal = function () {
        scope.printModalState.show = false;
      };
      scope.printContent = function () {
        var elementToPrint = element[0].getElementsByClassName('ng-modal-dialog-content')[0];
        var printContents = elementToPrint.innerHTML;
        var popupWin = window.open('', '_blank');
        popupWin.document.open();
        popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</html>');
        popupWin.document.close();
        scope.hideModal();
      }
    },
    templateUrl: 'components/print/PrintModal.html',
    replace: true,
    transclude: true
  };
}]);
