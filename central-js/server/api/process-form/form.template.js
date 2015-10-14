var jade = require('jade');
var path = require('path');

module.exports.createHtml = function (templateData) {
  var fn = jade.compileFile(path.join(__dirname, '../../views/print.jade'), {});
  var html = fn(templateData);
  return html;
};
