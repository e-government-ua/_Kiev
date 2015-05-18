var request = require('request');

module.exports.index = function(options, callback) {
    var url = options.protocol+'://'+options.hostname+options.path+'/services/getDocuments';
    console.log(url);
    return request.get({
        'url': url,
        'auth': {
            'username': options.username,
            'password': options.password
        },
        'qs': {
            'sID_Subject': options.params.sID_Subject
        }
    }, callback);
};