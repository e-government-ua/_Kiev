(function () {

  var sSeparator = ' - ';

  var defaultConfig = {
    singleDatePicker: true,
    timePicker: true,
    timePicker24Hour: true,
    autoApply: true,
    locale: {
      format: 'YYYY.MM.DD HH:mm',
      separator: sSeparator
    },
    opens: 'right',
    drops: 'down',
    buttonClasses: 'btn btn-sm',
    applyClass: 'btn-success',
    cancelClass: 'btn-default',
    applyLabel: 'Прийняти',
    cancelLabel: 'Відминити',
    customRangeLabel: 'Вільний вибір проміжку'
  };

  var adjustConfig = function(config, defaultValue) {
    var asDate = defaultValue.split(sSeparator);

    /*
    config.startDate = new Date(asDate[0]);

    if (asDate[1]) {
      config.endDate = new Date(asDate[1]);
    }
    */
   
    
    if (!asDate) {
        config.startDate = new Date();
        config.endDate = config.startDate;
    }else{
        config.startDate = new Date(asDate[0]);
        if (asDate.length > 0 && asDate[1]) {
          config.endDate = new Date(asDate[1]);
        }else{
          config.endDate = config.startDate;
        }
    }
    
    
  };

  var initDateRangePicker = function ($scope, element, ngModel, config) {

    var isInitialized = false;

    $scope.$watch(
      function () {
        return ngModel.$modelValue;
      },
      function (newValue) {
        if (!isInitialized) {
          if (newValue) {
            adjustConfig(config, newValue);
            element.daterangepicker(config);
          } else {
            element.daterangepicker(config);
            ngModel.$setViewValue(undefined);
            ngModel.$render();
          }

          isInitialized = true;
        }
      }
    );
  };

  angular.module('dashboardJsApp')
    .directive('datetimepicker', function () {
      return {
        restrict: 'AEC',
        require: 'ngModel',
        link: function ($scope, element, attrs, ngModel) {

          var config = angular.copy(defaultConfig);
          config.singleDatePicker = true;
          if (attrs.hasOwnProperty('options') && attrs.options) {
            var options = JSON.parse(attrs.options);
            for (var attrname in options) {
              config[attrname] = options[attrname];
            }
          }
          if (attrs.hasOwnProperty('format') && attrs.format) {
            config.locale.format = attrs.format;
          }

          initDateRangePicker($scope, element, ngModel, config);
        }
      };
    })
    .directive('datetimerangepicker', function () {
      return {
        restrict: 'AEC',
        require: 'ngModel',
        link: function ($scope, element, attrs, ngModel) {

          var config = angular.copy(defaultConfig);
          config.singleDatePicker = false;
          config.ranges = {
            'Наступні 7 днів': [moment(), moment().add(6, 'days')],
            'Наступні 30 днів': [moment(), moment().add(29, 'days')],
            'До кінця цього місяця': [moment(), moment().endOf('month')],
            'Наступний місяць': [moment().add(1, 'month').startOf('month'), moment().add(1, 'month').endOf('month')],
            'До кінця цього року': [moment(), moment().endOf('year')]
          };

          initDateRangePicker($scope, element, ngModel, config);
        }
      };
    });
})();
