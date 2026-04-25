var _rootPath = (function () {
    var path = location.pathname;
    if(path.indexOf('/') === 0) {
    	path = path.substring(1);
    }
    return '/' + path.split('/')[0];
}());
(function(win){
	var SrcBoot = {
		getPath: function(path) {
			// 全路径
	       	if (/^(http|https|ftp)/g.test(path)) {
	            return path;
	        }

	        // 用于测试本地mockjs测试用例js，约定以_test最为前缀，debug为false时不在页面输出
			if(path.indexOf('_test') != -1 && !this.debug) {
				return false;
			}

			// 是否是相对路径
			var isRelative = path.indexOf('./') === 0 || path.indexOf('../') === 0;
			path = (isRelative ? (_rootPath + '/'):'')+path;
			return path;
		},

		getExt: function(path) {
			if(path.indexOf('?') != -1) {
				path = path.split('?')[0];
			}

			var dotPos = path.lastIndexOf('.'),
				ext = path.substring(dotPos + 1);

			return ext;
		},

		// 批量输出css|js
		output: function(arr) {
			var i = 0,
				len = arr.length,
				path,
				ext;
			for(; i < len; i++) {
				path = this.getPath(arr[i]);
				if(path) {
					ext = this.getExt(path);
					if(ext == 'js') {
						document.writeln('<script src="' + path + '"></sc' + 'ript>');
					} else {
						document.writeln('<link rel="stylesheet" href="' + path + '">');
					}

				}
			}
		}
	};
	var arr = [
		'/www/bootstrap/css/bootstrap.min.css',
		'/www/css/font-awesome.min.css',
		'/www/css/ionicons.min.css',
		'/www/css/AdminLTE.min.css',
		'/www/css/skins/_all-skins.min.css',
		'/www/js/plugins/iCheck/flat/blue.css',
		'/www/js/plugins/morris/morris.css',
		'/www/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css',
		'/www/js/plugins/jvectormap/jquery-jvectormap-1.2.2.css',
		'/www/js/plugins/daterangepicker/daterangepicker-bs3.css',
		'/www/weui/weui.css',
		'/www/weui/jquery-weui.min.css'
	];
	SrcBoot.output(arr);
	win.SrcBoot = SrcBoot;
}(this));
