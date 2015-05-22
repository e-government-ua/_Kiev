**Initial configuration**:

    npm install
    cd client
    bower install
    npm install
    grunt debug
    cd ..
    grunt

**After source changes**:

    cd client
    grunt debug
    cd ..
    grunt

OR

**For development**:
    cd client
    grunt dev
and in another terminal run the server as usual:
    grunt

If you need to customize settings in config.js locally, don't change the file locally,
but create local_config.js and define section to be updated, for example:

    module.exports = {
        'server': {
            'protocol': 'http',
            'key': '/sybase/cert/server.key',
            'cert': '/sybase/cert/server.crt',
            'port': '8001'
        }
    };
Do not add it to repository though.
