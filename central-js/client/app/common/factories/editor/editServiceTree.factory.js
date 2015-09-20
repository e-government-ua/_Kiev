angular.module('app')
  .factory('EditServiceTreeFactory', function($http, $modal, CatalogService) {

    var createObjectWithChanges = function(editedObject, originalObject){
      var objectToSend = {
        nID: editedObject.nID
      };

      var addChangedPropertyToObject = function(propertyName){
        if (!originalObject || originalObject[propertyName] !== editedObject[propertyName]){
          objectToSend[propertyName] = editedObject[propertyName];
        }
      };

      addChangedPropertyToObject('sID');
      addChangedPropertyToObject('sName');
      addChangedPropertyToObject('nOrder');
      addChangedPropertyToObject('sSubjectOperatorName');

      return objectToSend;
    };

    var categoryEditor = (function(){
      var openModal = function (category) {
        var modalInstance = $modal.open({
          animation: true,
          size: 'lg',
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
          var categoryToSend = createObjectWithChanges(editedCategory, category);
          var catalogToSend = [ categoryToSend ];
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
          size: 'lg',
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
          var subcategoryToSend = createObjectWithChanges(editedSubcategory, subcategory);
          var catalogToSend = [
            {
              nID: categoryId,
              aSubcategory: [ subcategoryToSend ]
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
          size: 'lg',
          templateUrl: 'app/common/factories/editor/editService.html',
          controller: 'EditorModalController',
          resolve: {
            entityToEdit: function () {
              if (service){
                return {
                  nID: service.nID,
                  sID: service.sID,
                  sName: service.sName,
                  nOrder: service.nOrder,
                  sSubjectOperatorName: service.sSubjectOperatorName
                }
              }
              return undefined;
            }
          }
        });

        modalInstance.result.then(function (editedService) {
          var serviceToSend = createObjectWithChanges(editedService, service);
          var catalogToSend = [
            {
              nID: categoryId,
              aSubcategory: [
                {
                  nID: subcategoryId,
                  "aService": [ serviceToSend ]
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
