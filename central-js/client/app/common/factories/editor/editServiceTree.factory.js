angular.module('app')
  .factory('EditServiceTreeFactory', function($http, $modal, CatalogService) {

    var categoryEditor = (function(){
      var openModal = function (category) {
        var modalInstance = $modal.open({
          animation: true,
          templateUrl: 'app/common/factories/editor/editCategory.html',
          controller: 'EditorModalController',
          resolve: {
            entityToEdit: function () {
              if (category){
                return {
                  nID: category.nID,
                  sID: category.sID,
                  sName: category.sName,
                  nOrder: category.nOrder
                };
              }else{
                return undefined;
              }
            }
          }
        });

        modalInstance.result.then(function (editedCategory) {
          var catalogToSend = [ editedCategory ];
          CatalogService.setServicesTree(catalogToSend);
        });
      };

      return {
        add: function(){
          openModal();
        },
        edit: function(category){
          openModal(category)
        },
        remove: function(categoryId){
          CatalogService.removeCategory(categoryId)
        }
      }
    })();

    var subcategoryEditor = (function(){

      var openModal = function (categoryId, subcategory) {
        var modalInstance = $modal.open({
          animation: true,
          templateUrl: 'app/common/factories/editor/editSubcategory.html',
          controller: 'EditorModalController',
          resolve: {
            entityToEdit: function () {
              if (subcategory){
                return {
                  nID: subcategory.nID,
                  sID: subcategory.sID,
                  sName: subcategory.sName,
                  nOrder: subcategory.nOrder
                }
              }
              return undefined;
            }
          }
        });

        modalInstance.result.then(function (editedSubcategory) {

          var catalogToSend = [
            {
              nID: categoryId,
              aSubcategory: [ editedSubcategory ]
            }
          ];

          CatalogService.setServicesTree(catalogToSend);
        });
      };

      return {
        add: function(categoryId){
          openModal(categoryId);
        },
        edit: function(categoryId, subcategory){
          openModal(categoryId, subcategory)
        },
        remove: function(subcategoryId){
          CatalogService.removeSubcategory(subcategoryId)
        }
      }
    })();

    var serviceEditor = (function(){
      var openModal = function (categoryId, subcategoryId, service) {
        var modalInstance = $modal.open({
          animation: true,
          templateUrl: 'app/common/factories/editor/editService.html',
          controller: 'EditorModalController',
          resolve: {
            entityToEdit: function () {
              return angular.copy(service);
            }
          }
        });

        modalInstance.result.then(function (editedService) {

          var catalogToSend = [
            {
              nID: categoryId,
              aSubcategory: [
                {
                  nID: subcategoryId,
                  "aService": [ editedService ]
                }
              ]
            }
          ];

          CatalogService.setServicesTree(catalogToSend);
        });
      };

      return {
        add: function(categoryId, subcategoryId){
          openModal(categoryId, subcategoryId);
        },
        edit: function(categoryId, subcategoryId, serviceToEdit){
          openModal(categoryId, subcategoryId, serviceToEdit)
        },
        remove: function(serviceId){
          CatalogService.removeService(serviceId)
        }
      }
    })();

    return {
      category: categoryEditor,
      subcategory: subcategoryEditor,
      service: serviceEditor
    };
});
