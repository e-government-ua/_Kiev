'use strict';

var activiti = require('../../components/activiti');

// Get list of processs
exports.index = function(req, res) {
    
        var query = {};
        query.size = 500;
    
	var options = {
		path: 'repository/process-definitions', 
		query: query
	};

	activiti.get(options, function(error, statusCode, result) {
		if (error) {
			res.send(error);
		} else {
			res.json(result);
		}
	});
};