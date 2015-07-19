angular.module('app').directive('dropdownOrgan', function (OrganListFactory) {
  return {
    restrict: 'EA',
    templateUrl: 'app/common/components/form/directives/dropdownOrgan/dropdownOrgan.html',
    scope: {
      ngModel: "=",
      serviceData: "=",
      ngRequired: "="
    },
    link: function (scope) {
      // init organ list for organ select
      scope.organList = new OrganListFactory();
      scope.loadOrganList = function (search) {
        return scope.organList.load(scope.serviceData, search);
      };
      scope.onSelectOrganList = function (organ) {
        scope.ngModel = organ.nID;
        scope.organList.typeahead.model = organ.sNameUa;
      };
      scope.organList.reset();
      scope.organList.initialize();
      scope.organList.load(scope.serviceData, null).then(function (regions) {
        scope.organList.initialize(regions);
      });
    }
  };
});
