// wrapper
module.exports = function(grunt) {
    grunt.initConfig({
		htmlbuild: {
			dist: {
				src: './src/index.html',
				dest: './build',
				options: {
					relative: true,
					styles: {
						bundle: ['./build/css/bower.css', './build/css/style.css']
					},
					sections: {
						layout: {
							header: './src/html/layouts/header.html',
							footer: './src/html/layouts/footer.html'
						}
					}
				}
			}
		},
		bower_concat: {
			all: {
				dest: './tmp/js/concat/bower.js',
				cssDest: './tmp/css/concat/bower.css',
				exclude: [
					'requirejs',
					'angularAMD'
				],
				dependencies: {
					'angular-boostrap': 'angular',
					'angular-ui-router': 'angular',
					'ui-router-extras': 'angular-ui-router',
				},
				bowerOptions: {
					relative: false
				}
			},
			require: {
				dest: './tmp/js/concat/require.js',
				include: [
					'requirejs'
				],
				bowerOptions: {
					relative: false
				}
			},
		},
		concat: {
			main: {
                files: [
					{
						src: ['./src/css/**/*.css'],
						dest: './tmp/css/concat/style.css'
					},
					{
						src: ['./src/js/main.js'],
						dest: './tmp/js/concat/main.js'
					},
					{
						src: ['./bower_components/angularAMD/angularAMD.js'],
						dest: './tmp/js/concat/angularAMD/angularAMD.js'
					},
					{
						src: ['./bower_components/angularAMD/ngload.js'],
						dest: './tmp/js/concat/angularAMD/ngload.js'
					},
					{
						src: ['./src/js/main/*.js'],
						dest: './tmp/js/concat/script.js'
					}
				]
			}
		},
		uglify: {
			main: {
				files: {
					'./tmp/js/uglify/require.js': ['./tmp/js/concat/require.js'],
					'./tmp/js/uglify/main.js': ['./tmp/js/concat/main.js'],
					'./tmp/js/uglify/angularAMD/angularAMD.js': ['./tmp/js/concat/angularAMD/angularAMD.js'],
					'./tmp/js/uglify/angularAMD/ngload.js': ['./tmp/js/concat/angularAMD/ngload.js'],
					'./tmp/js/uglify/bower.js': ['./tmp/js/concat/bower.js'],
					'./tmp/js/uglify/script.js': ['./tmp/js/concat/script.js']
				}
			}
		},
		cssmin: {
			options: {
				shorthandCompacting: false,
				roundingPrecision: -1
			},
			target: {
				files: {
					'./tmp/css/cssmin/bower.css': ['./tmp/css/concat/bower.css'],
					'./tmp/css/cssmin/style.css': ['./tmp/css/concat/style.css']
				}
			}
		},
		compress: {
			concat: {
				files: [
					{
						expand: true,
						src: [
							'bower.css',
							'style.css'
						],
						cwd: './tmp/css/concat/',
						dest: './tmp/css/compress',
						ext: '.css'
					},
					{
						expand: true,
						src: [
							'require.js',
							'main.js',
							'bower.js',
							'script.js'
						],
						cwd: './tmp/js/concat/',
						dest: './tmp/js/compress',
						ext: '.js'
					},
					{
						expand: true,
						src: [
							'angularAMD.js',
							'ngload.js',
						],
						cwd: './tmp/js/concat/angularAMD',
						dest: './tmp/js/compress/angularAMD',
						ext: '.js'
					}
				]
			},
			min: {
				files: [
					{
						expand: true,
						src: [
							'bower.css',
							'style.css'
						],
						cwd: './tmp/css/cssmin/',
						dest: './tmp/css/compress',
						ext: '.css'
					},
					{
						expand: true,
						src: [
							'require.js',
							'main.js',
							'bower.js',
							'script.js'
						],
						cwd: './tmp/js/uglify/',
						dest: './tmp/js/compress',
						ext: '.js'
					},
					{
						expand: true,
						src: [
							'angularAMD.js',
							'ngload.js',
						],
						cwd: './tmp/js/uglify/angularAMD',
						dest: './tmp/js/compress/angularAMD',
						ext: '.js'
					}
				]
			}
		},
		copy: {
			concat: {
				files: [
					{expand: true, cwd: './tmp/css/concat', src: ['**'], dest: './build/css/' },
					{expand: true, cwd: './tmp/js/concat', src: ['**'], dest: './build/js/' }
				]
			},
			min: {
				files: [
					{expand: true, cwd: './tmp/css/cssmin', src: ['**'], dest: './build/css/' },
					{expand: true, cwd: './tmp/js/uglify', src: ['**'], dest: './build/js/' }
				]
			},
			compress: {
				files: [
					{expand: true, cwd: './tmp/css/compress', src: ['**'], dest: './build/css/' },
					{expand: true, cwd: './tmp/js/compress', src: ['**'], dest: './build/js/' }
				]
			}
		}
    });

    // load plugins
	grunt.loadNpmTasks('grunt-html-build');
	grunt.loadNpmTasks('grunt-bower-concat');
	grunt.loadNpmTasks('grunt-contrib-concat');
	grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-contrib-cssmin');
	grunt.loadNpmTasks('grunt-contrib-compress');
	grunt.loadNpmTasks('grunt-contrib-copy');

    // default task
    grunt.registerTask('default', ['bower_concat', 'concat', 'uglify', 'cssmin', 'compress:min', 'copy:concat', 'htmlbuild']);
};