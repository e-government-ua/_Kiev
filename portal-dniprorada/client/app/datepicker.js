// Code goes here
angular.module('foo', ['ui.bootstrap'])

//Datepicker
.directive("mydatepicker", function(){
  return {
    restrict: "E",
    scope:{
      ngModel: "=",
      dateOptions: "=",
      opened: "=",
    },
    link: function($scope, element, attrs) {
      $scope.open = function(event){
        console.log("open");
        event.preventDefault();
        event.stopPropagation();
        $scope.opened = true;
      };

      $scope.clear = function () {
        $scope.ngModel = null;
      };
    },
    templateUrl: 'datepicker.html'
  }
})

.controller('myCtrl', function ($scope, $http) {
  
	$scope.formData      = {};
  $scope.formData.date = "";
  $scope.opened        = false;
  
	//Datepicker
	$scope.dateOptions = {
		'year-format': "'yy'",
		'show-weeks' : false
	};
});