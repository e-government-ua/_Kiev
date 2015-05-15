var log4js = require('log4js');
var callerId = require('caller-id');
var path = require('path');
var startpoint = path.dirname(require.main.filename);

log4js.configure({
	appenders: [{
		type: 'console',
		layout: {
			type: 'pattern',
			pattern: "%d{yyyy-MM-dd_hh:mm:ss.SSS} [%[%-5p%]] %c : %m%n"
		}
	}]
});


exports.setup = function(){	
	var caller = callerId.getData();
	return log4js.getLogger(caller.filePath.split(startpoint, 2)[1]);
}