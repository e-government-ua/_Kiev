'use strict';

var _ = require('lodash');
var activiti = require('../../components/activiti');

// Get list of tasks
exports.index = function(req, res) {
  var user = JSON.parse(req.cookies.user);
  var query = {};
  //https://test.igov.org.ua/wf/service/runtime/tasks?size=20
  query.size = 500;

  var path = 'runtime/tasks';
  if (req.query.filterType === 'selfAssigned') {
    query.assignee = user.id;
  } else if (req.query.filterType === 'unassigned') {
    query.candidateUser = user.id;
    query.unassigned = true;
  } else if (req.query.filterType === 'finished') {
	  path = 'history/historic-task-instances';
    query.taskAssignee = user.id;
  } else if (req.query.filterType === 'tickets') {
    path = 'flow/getFlowSlotTickets';
    query.sLogin = user.id;
    query.bEmployeeUnassigned = req.query.bEmployeeUnassigned;
    if (req.query.sDate)
      query.sDate = req.query.sDate;
  }

  var options = {
    path: path,
    query: query
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      if (req.query.filterType === 'tickets') {
        result = JSON.stringify({data:JSON.parse(result)});
      }
      res.json(result);
    }
  });
};

// Get list of task events
exports.getAllTaskEvents = function(req, res) {
  var options = {
    path: '/runtime/tasks/' + req.params.taskId + '/events'
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.json(result);
    }
  });

};

exports.getForm = function(req, res) {
  var options = {
    path: 'form/form-data',
    query: {
      'taskId': req.params.taskId
    }
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.status(statusCode).json(result);
    }
  });
};

exports.getFormFromHistory = function(req, res) {
  var options = {
    path: 'history/historic-task-instances',
    query: {
      'taskId': req.params.taskId,
      'includeTaskLocalVariables': true,
      'includeProcessVariables': true
    }

  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.status(statusCode).json(result);
    }
  });
};

exports.uploadFile = function(req, res) {
  var options = {
    url: activiti.getRequestURL({
      path: 'rest/file/upload_file_as_attachment',
      query: {
        taskId: req.params.taskId,
        description: req.query.description
      }
    })
  }

  activiti.fileupload(req, res, options);
}

exports.getAttachments = function(req, res) {
  var options = {
    path: 'runtime/tasks/' + req.params.taskId + '/attachments'
  };

  activiti.get(options, function(error, statusCode, result) {
    if (error) {
      res.send(error);
    } else {
      res.status(statusCode).json(result);
    }
  });
};

exports.getAttachmentContent = function(req, res) {
  var options = {
    path: 'rest/file/download_file_from_db',
    query: {
      'taskId': req.params.taskId,
      'nFile': req.params.nFile
    }
  };
  activiti.filedownload(req, res, options);
};

exports.submitForm = function(req, res) {
  var options = {
    path: 'form/form-data'
  };
  activiti.post(options, function(error, statusCode, result) {
    res.statusCode = statusCode;
    res.send(result);
  }, req.body);
};

exports.updateTask = function(req, res) {
  var options = {
    path: 'runtime/tasks/' + req.params.taskId
  };
  activiti.put(options, function(error, statusCode, result) {
    res.statusCode = statusCode;
    res.send(result);
  }, req.body);
};

exports.getTask = function(req, res) {
  var options = {
    path: 'runtime/tasks/' + req.params.taskId
  };
  activiti.put(options, function(error, statusCode, result) {
    res.statusCode = statusCode;
    res.send(result);
  }, req.body);
};

exports.getTasksByOrder = function(req, res) {
  var options = { path: 'rest/tasks/getTasksByOrder',
    query: { 'nID_Protected': req.params.orderId }
  };
  activiti.get(options, function(error, statusCode, result) {
    error ? res.send(error) : res.status(statusCode).json(result);
  });
};

exports.getTasksByText = function(req, res) {
  var user = JSON.parse(req.cookies.user);
  //query.bEmployeeUnassigned = req.query.bEmployeeUnassigned;
  var options = { path: 'rest/tasks/getTasksByText',
    query: {
        'sFind': req.params.text,
        'sLogin': user.id,//finished,unassigned, selfAssigned
        'bAssigned': req.params.sType === 'selfAssigned' ? true : req.params.sType === 'unassigned' ? false : null //bAssigned
    }
  };
  activiti.get(options, function(error, statusCode, result) {
    error ? res.send(error) : res.status(statusCode).json(result);
    //error ? res.send(error) : res.status(statusCode).json("[\"4585243\"]");
  });
};

exports.getPatternFile = function(req, res) {
  var options = {
    path: '/rest/getPatternFile',
    query: {
      'sPathFile': req.query.sPathFile
    }
  };
  activiti.filedownload(req, res, options);
};

exports.upload_content_as_attachment = function (req, res) {
  activiti.post({
    path: 'rest/file/upload_content_as_attachment',
    query: {
      nTaskId: req.params.taskId,
      sContentType: 'text/html',
      sDescription: req.body.sDescription,
      sFileName: req.body.sFileName
    },
    headers: {
      'Content-Type': 'text/html;charset=utf-8'
    }
  }, function(error, statusCode, result) {
    error ? res.send(error) : res.status(statusCode).json(result);
  }, req.body.sContent, false);
};
