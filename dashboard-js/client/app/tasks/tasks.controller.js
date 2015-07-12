'use strict';
angular.module('dashboardJsApp').controller('TasksCtrl', function ($scope, $window, tasks, processes, Modal, Auth, $localStorage) {
  $scope.tasks = [];
  $scope.selectedTasks = {};
  $scope.$storage = $localStorage.$default({
    menuType: tasks.filterTypes.selfAssigned
  });
  $scope.menus = [{
    title: 'В роботі',
    type: tasks.filterTypes.selfAssigned,
    count: 0
  }, {
    title: 'Необроблені',
    type: tasks.filterTypes.unassigned,
    count: 0
  }, {
    title: 'Оброблені',
    type: tasks.filterTypes.finished,
    count: 0
  }];

  var findPrintTemplate = function (form) {
    var printTemplateResult = form.filter(function (item) {
      return item.id === 'sBody';
    });
    return printTemplateResult.length !== 0 ? printTemplateResult[0].value : "";
  };

  var processPrintTemplate = function (form, printTemplate, reg, fieldGetter) {
    var _printTemplate = printTemplate;
    var templatesAndIDs = _printTemplate.match(reg);
    if (templatesAndIDs) {

      var templates = templatesAndIDs.filter(function (item, i) {
        return i % 2 !== 0;
      });
      var ids = templatesAndIDs.filter(function (item, i) {
        return i !== 0 && i % 2 === 0;
      });

      templates.forEach(function (templateID, i) {
        var id = ids[i];
        if (id) {
          var item = form.filter(function (item) {
            return item.id === id;
          })[0];
          if (item) {
            _printTemplate = _printTemplate.replace(templateID, fieldGetter(item));
          }
        }
      });
      return processPrintTemplate(form, _printTemplate, reg, fieldGetter);
    } else {
      return _printTemplate;
    }
  };

  $scope.containsPrintTemplate = function () {
    return $scope.printObj && $scope.printObj.form && findPrintTemplate($scope.printObj.form) !== "";
  };

  $scope.getPrintTemplate = function (item) {
    if (!($scope.printObj && $scope.printObj.form)) {
      return "";
    } else {
      var printTemplate = findPrintTemplate($scope.printObj.form);
      printTemplate = processPrintTemplate($scope.printObj.form, printTemplate, /(\[(\w+)])/, function (item) {
        if (item.type === 'enum') {
          var enumID = item.value;
          return item.enumValues.filter(function (enumObj) {
            return enumObj.id === enumID;
          })[0].name;
        } else {
          return item.value;
        }
      });
      printTemplate = processPrintTemplate($scope.printObj.form, printTemplate, /(\[label=(\w+)])/, function (item) {
        return item.name;
      });
      return printTemplate;
    }
  };

  $scope.print = function () {
    if ($scope.selectedTask && $scope.taskForm) {
      $scope.taskForm.push({
        id: 'sBody',
        value: '<div>[label=bankIdlastName] : [bankIdlastName] <p> [label=bankIdfirstName] [bankIdfirstName]</p> <p>[label=decide] : [decide]</p></div>',
        name: 'sBody',
        type: 'invisible'
      });
      $scope.printObj = {task: $scope.selectedTask, form: $scope.taskForm};
      $scope.showPrintModal = !$scope.showPrintModal;
    }
  };

  $scope.hasUnPopulatedFields = function (){
    if ($scope.selectedTask && $scope.taskForm) {
      var unpopulated = $scope.taskForm.filter(function (item) {
        return item.value === undefined || item.value === null;
      });
      return unpopulated.length > 0;
    } else {
      return true;
    }
  }

  $scope.isFormPropertyDisabled = function (formProperty) {
    if ($scope.selectedTask && $scope.selectedTask.assignee === null) {
      return true;
    } else if ($scope.selectedTask && $scope.selectedTask.assignee !== null && !formProperty.writable) {
      return true;
    }
    return false;
  };

  $scope.isTaskFilterActive = function (taskType) {
    return $scope.$storage.menuType === taskType;
  };

  $scope.isTaskSelected = function (task) {
    return $scope.selectedTask && $scope.selectedTask.id === task.id;
  };

  $scope.hasAttachment = function () {
    return $scope.taskAttachments !== undefined && $scope.taskAttachments !== null && $scope.taskAttachments.length !== 0;
  };

  $scope.downloadAttachment = function () {
    tasks.downloadDocument($scope.selectedTask.id);
  };

  $scope.applyTaskFilter = function (menuType) {
    $scope.selectedTask = $scope.selectedTasks[menuType];
    $scope.$storage.menuType = menuType;
    $scope.taskForm = null;
    $scope.taskId = null;
    $scope.attachments = null;
    $scope.error = null;
    $scope.taskAttachments = null;

    tasks
      .list(menuType)
      .then(function (result) {
        result = JSON.parse(result);
        $scope.tasks = result.data;
        updateTaskSelection();
      })
      .catch(function (err) {
        $scope.errors.other = err.message;
      });
  };

  $scope.selectTask = function (task) {
    $scope.selectedTask = task;
    $scope.selectedTasks[$scope.$storage.menuType] = task;
    $scope.taskForm = null;
    $scope.taskId = task.id;
    $scope.attachments = null;
    $scope.error = null;
    $scope.taskAttachments = null;

    tasks
      .taskForm(task.id)
      .then(function (result) {
        result = JSON.parse(result);
        $scope.taskForm = result.formProperties;
        $scope.taskForm = addIndexForFileItems($scope.taskForm);
      })
      .catch(function (err) {
        err = JSON.parse(err);
        $scope.error = err;
      });

    tasks
      .taskAttachments(task.id)
      .then(function (result) {
        result = JSON.parse(result);
        $scope.attachments = result;
      })
      .catch(function (err) {
        console.log(err);
      });

    tasks.getTaskAttachments(task.id)
      .then(function (result) {
        $scope.taskAttachments = result;
      })
      .catch(function (err) {
        $scope.error = err.message;
      });
  };

  $scope.submitTask = function () {
    if ($scope.selectedTask && $scope.taskForm) {
      tasks.submitTaskForm($scope.selectedTask.id, $scope.taskForm)
        .then(function (result) {
          Modal.inform.success(function (event) {
            $scope.selectedTasks[$scope.$storage.menuType] = null;
            loadTaskCounters();
            $scope.applyTaskFilter($scope.$storage.menuType);
          })('Форма відправлена : ' + result);
        })
        .catch(function (err) {
          Modal.inform.error()('Помилка. ' + err.code + ' ' + err.message);
        });
    }
  };

  $scope.assignTask = function () {
    tasks.assignTask($scope.selectedTask.id, Auth.getCurrentUser().id).then(function (result) {
      Modal.inform.success(function (event) {
        $scope.selectedTasks[$scope.$storage.menuType] = null;
        loadTaskCounters();
        $scope.applyTaskFilter($scope.$storage.menuType);
      })('Задача у вас в роботі');
    })
      .catch(function (err) {
        Modal.inform.error()('Помилка. ' + err.code + ' ' + err.message);
      });
  };

  $scope.upload = function (files, propertyID) {
    tasks.upload(files, $scope.taskId).then(function (result) {
      var filterResult = $scope.taskForm.filter(function (property) {
        return property.id === propertyID;
      });
      if (filterResult && filterResult.length === 1) {
        filterResult[0].value = result.response.id;
        filterResult[0].fileName = result.response.name;
      }
    }).catch(function (err) {
      Modal.inform.error()('Помилка. ' + err.code + ' ' + err.message);
    });
  };

  $scope.sDateShort = function (sDateLong) {
    if (sDateLong !== null) {
      var o = new Date(sDateLong); //'2015-04-27T13:19:44.098+03:00'
      return o.getFullYear() + '-' + (o.getMonth() + 1) + '-' + o.getDate() + ' ' + o.getHours() + ':' + o.getMinutes();
      //"2015-05-21T00:40:28.801+03:00\"
    }
  };

  $scope.sFieldLabel = function (sField) {
    var s = '';
    if (sField !== null) {
      var a = sField.split(';');
      s = a[0].trim();
    }
    return s;
  };

  $scope.nID_FlowSlotTicket_FieldQueueData = function (sValue) {
    var nAt = sValue.indexOf(":");
    var nTo = sValue.indexOf(",");
    var s = sValue.substring(nAt + 1, nTo);
    var nID_FlowSlotTicket = 0;
    try {
      nID_FlowSlotTicket = s;
    } catch (_) {
      nID_FlowSlotTicket = 1;
    }
    return nID_FlowSlotTicket;
  };

  $scope.sDate_FieldQueueData = function (sValue) {
    var nAt = sValue.indexOf("sDate");
    var nTo = sValue.indexOf("}");
    var s = sValue.substring(nAt + 5 + 1 + 1, nTo - 1 - 6);
    var sDate = "Дата назначена!";
    try {
      sDate = s;
    } catch (_) {
      sDate = "Дата назначена!";
    }
    return sDate;
  };


  $scope.sEnumValue = function (aItem, sID) {
    var s = sID;
    _.forEach(aItem, function (oItem) {
      if (oItem.id == sID) {
        s = oItem.name;
      }
    });
    return s;
  };


  $scope.sFieldNotes = function (sField) {
    var s = null;
    if (sField !== null) {
      var a = sField.split(';');
      if (a.length > 1) {
        s = a[1].trim();
        if (s === '') {
          s = null;
        }
      }
    }
    return s;
  };

  $scope.getProcessName = function (processDefinitionId) {
    return processes.getProcessName(processDefinitionId);
  };

  $scope.init = function () {
    loadTaskCounters();
    loadSelfAssignedTasks();
  };

  function loadTaskCounters() {
    _.forEach($scope.menus, function (menu) {
      tasks.list(menu.type)
        .then(function (result) {
          result = JSON.parse(result);
          menu.count = result.data.length;
        });
    });
  }

  function loadSelfAssignedTasks() {
    processes.list().then(function (processesDefinitions) {
      $scope.applyTaskFilter($scope.$storage.menuType);
    }).catch(function (err) {
      err = JSON.parse(err);
      $scope.error = err;
    });
  }

  function addIndexForFileItems(val) {
    var idx = 0;
    return (val || []).map(function (item) {
      if (item.type === 'file') {
        item.nFileIdx = idx;
        idx++;
      }
      return item;
    });
  }

  function updateTaskSelection() {
    if ($scope.selectedTask) {
      $scope.selectTask($scope.selectedTask);
    } else if ($scope.tasks[0]) {
      $scope.selectTask($scope.tasks[0]);
    }
  }
});
