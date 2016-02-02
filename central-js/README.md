## What is this?
This is the front end for igov.org.ua portal. It's also called a main portal
(ru: "главный портал")
It consist of the 'client' part which is the SPA (single page application)
written in JS/Angular, and a 'server' part which is a node js server which
serves static files from 'client' and provides api endpoint. The api endpoint
is only an proxy/adaptor for the real back-end server.


## Install
### Pre-requisites
You need to have `npm`. Also you need to have `ruby-sass` installed in order
to provide SASS support.
You have to install `bower` js package manager and `grunt` task runner in order
to launch the project.

Example (Ubuntu):

    sudo apt-get install npm ruby-sass
    sudo npm install -g bower grunt-cli

### Project dependencies

    npm install
    bower install
    grunt serve


## Configuration
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

##BankID Chipering
For production:<br>
You should add these parameters to process.json file:<br>
<li>"BANKID_ENABLE_CIPHER":"true",
<li>"BANKID_PRIVATE_KEY":"path to private key that is used to encrypt data from bankid",
<li>"BANKID_PRIVATE_KEY_PASSPHRASE":"passphrase of private key above"

For test-alpha, test-beta:<br>
Private key for tests is used, you should add only this parameter:<br>
<li>"BANKID_ENABLE_CIPHER":"true"
