var request = require('request');

module.exports.index = function(options, callback) {
    var url = options.protocol+'://'+options.hostname+options.path+'/subject/syncSubject';
    console.log(url);
    return request.get({
        'url': url,
        'auth': {
            'username': options.username,
            'password': options.password
        },
        'qs': {
            'sINN': options.params.sINN
        }
    }, callback);
};