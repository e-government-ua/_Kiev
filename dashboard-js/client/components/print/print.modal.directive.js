'use strict';

angular.module('dashboardJsApp').directive('printModal', ['$window', function($window) {
	return {
		scope: {
			show: '=info'
		},
		restrict: 'E',
		link: function(scope, element, attrs, ngModel) {
			scope.dialogStyle = {};
			if (attrs.width)
				scope.dialogStyle.width = attrs.width;
			if (attrs.height)
				scope.dialogStyle.height = attrs.height;

			scope.hideModal = function() {
				scope.show = false;
			};
			scope.printContent = function() {
				var elementToPrint = element[0].getElementsByClassName('ng-modal-dialog-content')[0];
				var printContents = elementToPrint.innerHTML;
				var popupWin = window.open('', '_blank');
				popupWin.document.open()
				popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</html>');
				popupWin.document.close();
			}
		},
		template: "<div class='ng-modal' ng-show='show'><div class='ng-modal-overlay' ng-click='hideModal()'></div><div class='ng-modal-dialog' ng-style='dialogStyle'><div class='ng-modal-close' ng-click='hideModal()'>X</div><div class='ng-modal-dialog-content' ng-transclude></div><div class='ng-modal-dialog-print-button'><button ng-click='printContent()' class='btn btn-success'>Роздрукувати</button></div></div></div>",
		replace: true,
		transclude: true
	};
}]);