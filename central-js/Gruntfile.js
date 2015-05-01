// wrapper
module.exports = function(grunt) {
    grunt.initConfig({
		express: {
		  options: {
			port: 80
		  },
		  dev: {
			options: {
			  script: 'server/app.js',
			  debug: true
			}
		  },
		  prod: {
			options: {
			  script: 'server/app.js'
			}
		  }
		},
		watch: {
			express: {
				files:  [ 'server/**/*.js' ],
				tasks:  [ 'express:dev' ],
				options: {
					spawn: false // for grunt-contrib-watch v0.5.0+, "nospawn: true" for lower versions. Without this option specified express won't be reloaded
				}
			}
		}
    });

    // load plugins
	grunt.loadNpmTasks('grunt-express-server');
	grunt.loadNpmTasks('grunt-contrib-watch');

    // default task
    grunt.registerTask('default', ['express:prod', 'watch']);
};