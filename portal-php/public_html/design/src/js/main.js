requirejs.config({
    waitSeconds: 30,
    baseUrl: './js',
    paths: {
        'bower': './bower',
		'angularAMD': './angularAMD/angularAMD',
		'ngload': './angularAMD/ngload',
		'script': './script'
    },
    shim: {
        'angularAMD': ['bower'],
		'ngload': ['bower'],
        'script': ['angularAMD', 'ngload']
    },
    deps: ['script']
});