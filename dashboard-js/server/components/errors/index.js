/**
 * Error responses
 */
'use strict';

module.exports.codes = {
  EXTERNAL_SERVICE_ERROR : 'ESE',
  INPUT_PARAMETER_ERROR : 'IPE',
  LOGIC_SERVICE_ERROR : 'LSE'
};

module.exports.createError = function (code, error_description, error) {
  return {
    code: code,
    message: error_description,
    nested : error
  };
};

module.exports[404] = function pageNotFound(req, res) {
  var viewFilePath = '404';
  var statusCode = 404;
  var result = {
    status: statusCode
  };

  res.status(result.status);
  res.render(viewFilePath, function (err) {
    if (err) { return res.json(result, result.status); }

    res.render(viewFilePath);
  });
};
