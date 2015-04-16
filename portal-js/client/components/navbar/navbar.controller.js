'use strict';

//angular.module('portalDniproradaApp', ['ui.bootstrap'])
angular.module('portalDniproradaApp')
/*
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
*/
  .controller('NavbarCtrl', function ($scope, $location) {
    $scope.menu = [{
      'title': 'Home',
      'link': '/'
    }];

    $scope.isCollapsed = true;

    $scope.isActive = function(route) {
      return route === $location.path();
    };
    
    
    
/*    
    
    $scope.formData      = {};
    $scope.formData.date = "";
    $scope.opened        = false;

    //Datepicker
    $scope.dateOptions = {
            'year-format': "'yy'",
            'show-weeks' : false
    };
    
*/
    
    
    
    
  });