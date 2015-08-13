// Karma configuration
// http://karma-runner.github.io/0.10/config/configuration-file.html

/* global module */

'use strict';

module.exports = function(config) {
  config.set({
    // base path, that will be used to resolve files and exclude
    basePath: '',

    // testing framework to use (jasmine/mocha/qunit/...)
    frameworks: ['jasmine'],

    // list of files / patterns to load in the browser
    files: [
      'client/bower_components/jquery/dist/jquery.js',
      'client/bower_components/angular/angular.js',
      'client/bower_components/angular-mocks/angular-mocks.js',
      'client/bower_components/angular-resource/angular-resource.js',
      'client/bower_components/angular-cookies/angular-cookies.js',
      'client/bower_components/angular-sanitize/angular-sanitize.js',
      // 'client/bower_components/angular-route/angular-route.js',
      'client/bower_components/moment/moment.js',
      'client/bower_components/moment/locale/uk.js',
      'client/bower_components/moment/locale/ru.js',
      // 'client/bower_components/moment-timezone/moment-timezone.js',
      'client/bower_components/angular-moment/angular-moment.js',
      'client/bower_components/angular-messages/angular-messages.js',
      'client/bower_components/angular-ui-utils/ui-utils.js',
      'client/bower_components/ng-clip/dest/ng-clip.min.js',
      'client/bower_components/zeroclipboard/dist/ZeroClipboard.js',
      'client/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
      'client/bower_components/lodash/dist/lodash.compat.js',
      'client/bower_components/angular-ui-router/release/angular-ui-router.js',
      'client/app/app.js',
      'client/app/**/*.js'
      // 'client/app/app.coffee',
      // 'client/app/**/*.jade',
      // 'client/app/**/*.coffee',
      // 'client/components/**/*.js',
      // 'client/components/**/*.jade',
      // 'client/components/**/*.coffee',
      // 'client/app/**/*.html',
      // 'client/components/**/*.html'
    ],

    preprocessors: {
      '**/*.jade': 'ng-jade2js',
      '**/*.html': 'html2js',
      '**/*.coffee': 'coffee'
    },

    ngHtml2JsPreprocessor: {
      stripPrefix: 'client/'
    },

    ngJade2JsPreprocessor: {
      stripPrefix: 'client/'
    },

    // list of files / patterns to exclude
    exclude: [],

    // web server port
    port: 8080,

    // level of logging
    // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
    logLevel: config.LOG_INFO,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,


    // Start these browsers, currently available:
    // - Chrome
    // - ChromeCanary
    // - Firefox
    // - Opera
    // - Safari (only Mac)
    // - PhantomJS
    // - IE (only Windows)
    browsers: ['PhantomJS'],

    // you can define custom flags
    // customLaunchers: {
    //   Chrome_without_security: {
    //     base: 'Chrome',
    //     flags: ['--disable-web-security']
    //   }
    // }
    
    // Continuous Integration mode
    // if true, it capture browsers, run tests and exit
    singleRun: false
  });
};
