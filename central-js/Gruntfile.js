// wrapper
module.exports = function(grunt) {
    grunt.initConfig({
		express: {
		  build: {
			options: {
			  script: './server/app.js'
			}
		  }
		},
		watch: {
			express: {
				files:  [ 'server/**/*.js' ],
				tasks:  [ ]
			}
		}
    });

    // load plugins
	grunt.loadNpmTasks('grunt-express-server');
	grunt.loadNpmTasks('grunt-contrib-watch');

    // default task
    grunt.registerTask('default', ['express:build', 'watch']);
};