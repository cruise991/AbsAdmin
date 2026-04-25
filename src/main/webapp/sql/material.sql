-- 素材表
CREATE TABLE `abs_material_info` (
  `rowguid` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `type` varchar(20) NOT NULL COMMENT '素材类型：image、music、video、word',
  `size` varchar(20) DEFAULT NULL COMMENT '文件大小',
  `category` varchar(50) DEFAULT NULL COMMENT '分类',
  `duration` varchar(20) DEFAULT NULL COMMENT '时长（音频/视频）',
  `word_count` varchar(20) DEFAULT NULL COMMENT '字数（文字）',
  `tags` varchar(200) DEFAULT NULL COMMENT '标签',
  `content` text COMMENT '内容预览',
  `file_path` varchar(200) DEFAULT NULL COMMENT '文件路径',
  `upload_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `status` varchar(20) DEFAULT '正常' COMMENT '状态',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`rowguid`),
  KEY `idx_type` (`type`),
  KEY `idx_category` (`category`),
  KEY `idx_upload_time` (`upload_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='素材表';

-- 初始化数据
INSERT INTO `abs_material_info` (`rowguid`, `name`, `type`, `size`, `category`, `duration`, `word_count`, `tags`, `content`, `file_path`, `upload_time`, `status`, `create_user`) VALUES
('1', '背景图1', 'image', '2.5MB', '背景', NULL, NULL, '首页,banner', '这是背景图片', 'image/bg1.jpg', NOW(), '正常', 'admin'),
('2', '图标1', 'image', '0.8MB', '图标', NULL, NULL, '功能,按钮', '这是图标', 'image/icon1.png', NOW(), '正常', 'admin'),
('3', '背景音乐1', 'music', '3.2MB', '背景音乐', '2:45', NULL, '欢快,轻松', '这是背景音乐', 'music/bg_music1.mp3', NOW(), '正常', 'admin'),
('4', '音效1', 'music', '0.5MB', '音效', '0:15', NULL, '按钮,提示', '这是音效', 'music/sound1.wav', NOW(), '正常', 'admin'),
('5', '教程视频1', 'video', '50.2MB', '教程', '15:30', NULL, '教学,入门', '这是教程视频', 'video/tutorial1.mp4', NOW(), '正常', 'admin'),
('6', '产品演示', 'video', '28.5MB', '演示', '8:25', NULL, '产品,功能', '这是产品演示', 'video/demo1.mp4', NOW(), '正常', 'admin'),
('7', '产品介绍文案', 'word', '2.5KB', '文案', NULL, '1200', '产品,推广', '这是产品介绍文案内容...', 'word/product_intro.txt', NOW(), '正常', 'admin'),
('8', '活动策划方案', 'word', '8.2KB', '方案', NULL, '3500', '活动,策划', '这是活动策划方案内容...', 'word/activity_plan.doc', NOW(), '正常', 'admin');
