angular.module('app')
  .factory('EditServiceTreeFactory', function($http, $modal, CatalogService) {

    var createObjectOnlyWithChanges = function(editedObject, originalObject){
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
          var categoryToSend = createObjectOnlyWithChanges(editedCategory, category);
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
          var subcategoryToSend = createObjectOnlyWithChanges(editedSubcategory, subcategory);

          if (!subcategoryToSend.nID){
            subcategoryToSend.oCategory = {
              nID : categoryId
            };
          }

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
          var serviceToSend = createObjectOnlyWithChanges(editedService, service);

          if (!serviceToSend.nID){
            serviceToSend.oSubcategory = {
              nID : subcategoryId
            };
            serviceToSend.sFAQ  = "";
            serviceToSend.sInfo  = "";
            serviceToSend.sLaw  = "";
          }

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
