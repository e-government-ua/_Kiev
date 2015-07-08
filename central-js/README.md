npm install <br>
bower install <br>
grunt serve <br>


If you need to customize settings in config.js locally, don't change the file locally, but create local_config.js and define section to be updated, for example:

module.exports = {
    'server': {
        'protocol': 'http',
        'key': '/sybase/cert/server.key',
        'cert': '/sybase/cert/server.crt',
        'port': '8001'
    }
};
Do not add it to repository though.