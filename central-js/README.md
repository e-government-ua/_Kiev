**Initial configuration**:

`./0_install_build_start.sh` on Linux or 
`0_install_build_start.bat` on Windows

**After source changes**:

`./1_build_start.sh` on Linux or
`./1_build_start.bat` on Windows

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
