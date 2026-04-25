var _rootPath = (function() {
	var path = location.pathname;

	if (path.indexOf('/') === 0) {
		path = path.substring(1);
	}

	return '/' + path.split('/')[0];
}());

(function(win) {
	var alljs = [ '/www/js/plugins/jQuery/jQuery-2.1.4.min.js',
			      '/www/bootstrap/js/bootstrap.min.js',
			      '/www/bootstrap/js/bootstrap-paginator.js',
			      '/www/js/plugins/fastclick/fastclick.min.js',
			      '/www/js/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js',
			      '/www/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js',
			      '/www/js/app.min.js',
			      '/www/js/common.js',
			      '/www/js/vue.min.js',
			      '/www/js/raphael-min.js',
			      '/www/js/jquery-ui.min.js',
			      '/www/js/plugins/morris/morris.min.js',
			      '/www/js/plugins/sparkline/jquery.sparkline.min.js',
                  '/www/weui/jquery-weui.min.js'
			      ];
	var SrcBoot = {
		getPath : function(path) {
			// 全路径
			if (/^(http|https|ftp)/g.test(path)) {
				return path;
			}
			// 用于测试本地mockjs测试用例js，约定以_test最为前缀，debug为false时不在页面输出
			if (path.indexOf('_test') != -1 && !this.debug) {
				return false;
			}
			// 是否是相对路径
			var isRelative = path.indexOf('./') === 0 || path.indexOf('../') === 0;
			path = (isRelative ? (_rootPath + '/') :'')+path;
			return path;
		},

		getExt : function(path) {
			if (path.indexOf('?') != -1) {
				path = path.split('?')[0];
			}

			var dotPos = path.lastIndexOf('.'), ext = path.substring(dotPos + 1);

			return ext;
		},

		// 批量输出css|js
		output : function(arr) {
			var i = 0, len = arr.length, path, ext;
			for (; i < len; i++) {
				path = this.getPath(arr[i]);
				if (path) {
					ext = this.getExt(path);
					if (ext == 'js') {
						document.writeln('<script src="' + path + '"></sc'+ 'ript>');
					} else {
						document.writeln('<link rel="stylesheet" href="' + path+ '">');
					}
				}
			}
		}

	};
	SrcBoot.output(alljs); 
}(this));