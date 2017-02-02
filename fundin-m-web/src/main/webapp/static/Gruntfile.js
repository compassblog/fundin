module.exports = function(grunt) {

	grunt.initConfig({
		devPath: "./dev",
		distPath: "./dist",
		
		clean: {
			all: '<%= distPath%>/**',
		},
			 
		copy: {
			main: {
				files: [
				  {
				    expand: true,
				    cwd: '<%= devPath%>/font/',
				    src: '**/*',
				    dest: "<%= distPath%>/font/"
				  },
				  {
				 	expand: true,
				    cwd: '<%= devPath%>/image/',
				    src: '**/*',
				    dest: "<%= distPath%>/image/"
				  },
				  {
					expand: true,
				    cwd: '<%= devPath%>/js/lib/',
				    src: '**/*',
				    dest: "<%= distPath%>/js/lib/"
				  },
				  {
					expand: true,
				    cwd: '<%= devPath%>/css/',
				    src: ['weui.min.css', 'frozen.min.css', 'wangEditor-mobile.min.css'],
				    dest: "<%= distPath%>/css/",
				  }
				]
			}
		},
		
		cssmin: {
	      options: {
	        shorthandCompacting: false,
	        keepSpecialComments: 0
	      },
	      target: {
	        files: {
	          '<%= distPath%>/css/app.css': ['<%= devPath%>/css/app.css']
	        }
	      }
	    },
	    
	    uglify: {
//	    	options: {
//                mangle: false		// 混淆选项
//	    	},
	    	build: {
	    		files: [{
	                expand:true,
	                cwd:'<%= devPath%>/js/',
	                src:'*.js',
	                dest: '<%= distPath%>/js/'
	            }]
	    	},
	    }
	});
  
	grunt.loadNpmTasks('grunt-contrib-clean');
	grunt.loadNpmTasks('grunt-contrib-copy');
	grunt.loadNpmTasks('grunt-contrib-cssmin');
	grunt.loadNpmTasks('grunt-contrib-uglify');
	
	grunt.registerTask('default', ['clean', 'copy', 'cssmin', 'uglify']);
	
};