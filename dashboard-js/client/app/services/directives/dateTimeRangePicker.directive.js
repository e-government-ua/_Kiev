(function () {

  var separator = ' - ';

  var defaultConfig = {
    singleDatePicker: true,
    timePicker: true,
    timePicker24Hour: true,
    autoApply: true,
    locale: {
      format: 'YYYY.MM.DD HH:mm',
      separator: separator
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
    var values = defaultValue.split(separator);

    config.startDate = new Date(values[0]);

    if (values[1]) {
      config.endDate = new Date(values[1]);
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
