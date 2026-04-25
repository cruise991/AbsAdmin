/*
 Navicat Premium Data Transfer

 Source Server         : rm-bp1q6ok5t71ci853jqo.mysql.rds.aliyuncs.com
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : rm-bp1q6ok5t71ci853jqo.mysql.rds.aliyuncs.com:3306
 Source Schema         : absframe

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 07/04/2026 19:45:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for abs_appinfo
-- ----------------------------
DROP TABLE IF EXISTS `abs_appinfo`;
CREATE TABLE `abs_appinfo`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `appname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `introduction` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `appkey` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `appsecret` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `expiredtime` datetime(0) DEFAULT NULL,
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  `userguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `token` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `tokentype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `tokennum` int(0) DEFAULT NULL,
  `tokenexpired` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abs_article
-- ----------------------------
DROP TABLE IF EXISTS `abs_article`;
CREATE TABLE `abs_article`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `authorguid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `authorname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `btype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fmurl` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of abs_article
-- ----------------------------
INSERT INTO `abs_article` VALUES (3, '299e7f37-4ff2-403d-bfea-45eae763c4f4', '高清舒适近视眼镜 - 清晰视界，时尚之选', '<h4>产品特点：</h4><ol><li><p><strong>高清树脂镜片，视觉更清晰</strong><br/>采用优质树脂镜片，高透光率，低色散，有效减少眩光和反射，提供清晰的视觉体验。镜片经过多层镀膜处理，防紫外线、防蓝光，保护眼睛健康。</p></li><li><p><strong>轻量化设计，佩戴舒适</strong><br/>镜框采用超轻&nbsp;TR90&nbsp;材质，重量仅为&nbsp;15&nbsp;克，佩戴轻盈无压力。鼻托和镜腿经过人体工学设计，长时间佩戴也不会感到不适，适合日常使用。</p></li><li><p><strong>时尚设计，百搭造型</strong><br/>经典款式设计，简约大方，适合各种脸型和场合。提供多种颜色选择，包括黑色、银色、金色等，满足不同用户的审美需求。</p></li><li><p><strong>防滑防汗，稳固耐用</strong><br/>镜腿采用防滑材质，即使运动或出汗也能稳固佩戴。镜框韧性极佳，不易变形，耐用性强。</p></li><li><p><strong>个性化定制，精准适配</strong><br/>支持根据用户的近视度数、瞳距等参数定制镜片，确保每副眼镜都能精准适配用户需求。</p></li></ol><hr/><h4>适用人群：</h4><ul><li><p><strong>学生</strong>：长时间学习用眼，防蓝光镜片减少电子屏幕对眼睛的伤害。</p></li><li><p><strong>上班族</strong>：适合长时间面对电脑，缓解视觉疲劳。</p></li><li><p><strong>时尚达人</strong>：百搭设计，提升整体造型感。</p></li><li><p><strong>运动爱好者</strong>：轻便防滑，适合运动时佩戴。</p></li></ul><hr/><h4>产品参数：</h4><ul><li><p>镜片材质：高清树脂</p></li><li><p>镜框材质：TR90</p></li><li><p>镜片功能：防紫外线、防蓝光、防眩光</p></li><li><p>镜框重量：15&nbsp;克</p></li><li><p>镜片宽度：50mm</p></li><li><p>鼻梁宽度：18mm</p></li><li><p>镜腿长度：140mm</p></li><li><p>包装：精美眼镜盒、眼镜布、保修卡</p></li></ul><hr/><h4>用户评价：</h4><ul><li><p><strong>“镜片非常清晰，戴上后看东西特别舒服，防蓝光效果也很好！”</strong></p></li><li><p><strong>“镜框很轻，戴一整天都不会觉得累，设计也很时尚，朋友们都说好看。”</strong></p></li><li><p><strong>“定制服务很贴心，眼镜完全符合我的度数，佩戴起来非常舒适。”</strong></p></li></ul><hr/><h4>价格：</h4><p><strong>¥299</strong>（限时优惠，原价¥399）</p><hr/><h4>购买方式：</h4><ul><li><p><strong>线上购买</strong>：访问我们的官方商城或天猫/京东旗舰店。</p></li><li><p><strong>线下体验</strong>：全国各大眼镜专卖店均有售。</p></li></ul><hr/><h4>温馨提示：</h4><ul><li><p>请根据验光单选择适合的镜片度数。</p></li><li><p>使用后请用附赠的眼镜布清洁镜片，避免刮花。</p></li><li><p>避免将眼镜长时间放置在高温或潮湿环境中。</p></li></ul><hr/><p><strong>选择我们的近视眼镜，享受清晰视界与时尚魅力的完美结合！</strong></p>', '1', 'admin', '2', '产品特点：高清树脂镜片，视觉更清晰</str', 'http://read8686.oss-cn-beijing.aliyuncs.com/mall/f4fb21311f82439d9123f7ad32f0bd4e.jpg', '2', '2025-02-02 17:39:44');
INSERT INTO `abs_article` VALUES (4, '76ad4d3e-f0b4-45df-83a9-0f10820ccb81', '高清舒适近视眼镜 - 清晰视界，时尚之选', '<h4>产品特点：</h4><ol><li><p><strong>高清树脂镜片，视觉更清晰</strong><br/>采用优质树脂镜片，高透光率，低色散，有效减少眩光和反射，提供清晰的视觉体验。镜片经过多层镀膜处理，防紫外线、防蓝光，保护眼睛健康。</p></li><li><p><strong>轻量化设计，佩戴舒适</strong><br/>镜框采用超轻&nbsp;TR90&nbsp;材质，重量仅为&nbsp;15&nbsp;克，佩戴轻盈无压力。鼻托和镜腿经过人体工学设计，长时间佩戴也不会感到不适，适合日常使用。</p></li><li><p><strong>时尚设计，百搭造型</strong><br/>经典款式设计，简约大方，适合各种脸型和场合。提供多种颜色选择，包括黑色、银色、金色等，满足不同用户的审美需求。</p></li><li><p><strong>防滑防汗，稳固耐用</strong><br/>镜腿采用防滑材质，即使运动或出汗也能稳固佩戴。镜框韧性极佳，不易变形，耐用性强。</p></li><li><p><strong>个性化定制，精准适配</strong><br/>支持根据用户的近视度数、瞳距等参数定制镜片，确保每副眼镜都能精准适配用户需求。</p></li></ol><hr/><h4>适用人群：</h4><ul><li><p><strong>学生</strong>：长时间学习用眼，防蓝光镜片减少电子屏幕对眼睛的伤害。</p></li><li><p><strong>上班族</strong>：适合长时间面对电脑，缓解视觉疲劳。</p></li><li><p><strong>时尚达人</strong>：百搭设计，提升整体造型感。</p></li><li><p><strong>运动爱好者</strong>：轻便防滑，适合运动时佩戴。</p></li></ul><hr/><h4>产品参数：</h4><ul><li><p>镜片材质：高清树脂</p></li><li><p>镜框材质：TR90</p></li><li><p>镜片功能：防紫外线、防蓝光、防眩光</p></li><li><p>镜框重量：15&nbsp;克</p></li><li><p>镜片宽度：50mm</p></li><li><p>鼻梁宽度：18mm</p></li><li><p>镜腿长度：140mm</p></li><li><p>包装：精美眼镜盒、眼镜布、保修卡</p></li></ul><hr/><h4>用户评价：</h4><ul><li><p><strong>“镜片非常清晰，戴上后看东西特别舒服，防蓝光效果也很好！”</strong></p></li><li><p><strong>“镜框很轻，戴一整天都不会觉得累，设计也很时尚，朋友们都说好看。”</strong></p></li><li><p><strong>“定制服务很贴心，眼镜完全符合我的度数，佩戴起来非常舒适。”</strong></p></li></ul><hr/><h4>价格：</h4><p><strong>¥299</strong>（限时优惠，原价¥399）</p><hr/><h4>购买方式：</h4><ul><li><p><strong>线上购买</strong>：访问我们的官方商城或天猫/京东旗舰店。</p></li><li><p><strong>线下体验</strong>：全国各大眼镜专卖店均有售。</p></li></ul><hr/><h4>温馨提示：</h4><ul><li><p>请根据验光单选择适合的镜片度数。</p></li><li><p>使用后请用附赠的眼镜布清洁镜片，避免刮花。</p></li><li><p>避免将眼镜长时间放置在高温或潮湿环境中。</p></li></ul><hr/><p><strong>选择我们的近视眼镜，享受清晰视界与时尚魅力的完美结合！</strong></p>', '1', 'admin', '2', '产品特点：高清树脂镜片，视觉更清晰</str', 'http://read8686.oss-cn-beijing.aliyuncs.com/mall/f4fb21311f82439d9123f7ad32f0bd4e.jpg', '2', '2025-02-02 17:39:50');
INSERT INTO `abs_article` VALUES (5, '4b6c357e-0f74-4971-b9a6-b3032586667d', '高清舒适近视眼镜 - 清晰视界，时尚之选', '<h4>产品特点：</h4><ol><li><p><strong>高清树脂镜片，视觉更清晰</strong><br/>采用优质树脂镜片，高透光率，低色散，有效减少眩光和反射，提供清晰的视觉体验。镜片经过多层镀膜处理，防紫外线、防蓝光，保护眼睛健康。</p></li><li><p><strong>轻量化设计，佩戴舒适</strong><br/>镜框采用超轻&nbsp;TR90&nbsp;材质，重量仅为&nbsp;15&nbsp;克，佩戴轻盈无压力。鼻托和镜腿经过人体工学设计，长时间佩戴也不会感到不适，适合日常使用。</p></li><li><p><strong>时尚设计，百搭造型</strong><br/>经典款式设计，简约大方，适合各种脸型和场合。提供多种颜色选择，包括黑色、银色、金色等，满足不同用户的审美需求。</p></li><li><p><strong>防滑防汗，稳固耐用</strong><br/>镜腿采用防滑材质，即使运动或出汗也能稳固佩戴。镜框韧性极佳，不易变形，耐用性强。</p></li><li><p><strong>个性化定制，精准适配</strong><br/>支持根据用户的近视度数、瞳距等参数定制镜片，确保每副眼镜都能精准适配用户需求。</p></li></ol><hr/><h4>适用人群：</h4><ul><li><p><strong>学生</strong>：长时间学习用眼，防蓝光镜片减少电子屏幕对眼睛的伤害。</p></li><li><p><strong>上班族</strong>：适合长时间面对电脑，缓解视觉疲劳。</p></li><li><p><strong>时尚达人</strong>：百搭设计，提升整体造型感。</p></li><li><p><strong>运动爱好者</strong>：轻便防滑，适合运动时佩戴。</p></li></ul><hr/><h4>产品参数：</h4><ul><li><p>镜片材质：高清树脂</p></li><li><p>镜框材质：TR90</p></li><li><p>镜片功能：防紫外线、防蓝光、防眩光</p></li><li><p>镜框重量：15&nbsp;克</p></li><li><p>镜片宽度：50mm</p></li><li><p>鼻梁宽度：18mm</p></li><li><p>镜腿长度：140mm</p></li><li><p>包装：精美眼镜盒、眼镜布、保修卡</p></li></ul><hr/><h4>用户评价：</h4><ul><li><p><strong>“镜片非常清晰，戴上后看东西特别舒服，防蓝光效果也很好！”</strong></p></li><li><p><strong>“镜框很轻，戴一整天都不会觉得累，设计也很时尚，朋友们都说好看。”</strong></p></li><li><p><strong>“定制服务很贴心，眼镜完全符合我的度数，佩戴起来非常舒适。”</strong></p></li></ul><hr/><h4>价格：</h4><p><strong>¥299</strong>（限时优惠，原价¥399）</p><hr/><h4>购买方式：</h4><ul><li><p><strong>线上购买</strong>：访问我们的官方商城或天猫/京东旗舰店。</p></li><li><p><strong>线下体验</strong>：全国各大眼镜专卖店均有售。</p></li></ul><hr/><h4>温馨提示：</h4><ul><li><p>请根据验光单选择适合的镜片度数。</p></li><li><p>使用后请用附赠的眼镜布清洁镜片，避免刮花。</p></li><li><p>避免将眼镜长时间放置在高温或潮湿环境中。</p></li></ul><hr/><p><strong>选择我们的近视眼镜，享受清晰视界与时尚魅力的完美结合！</strong></p>', '1', 'admin', '2', '产品特点：高清树脂镜片，视觉更清晰</str', 'http://read8686.oss-cn-beijing.aliyuncs.com/mall/f4fb21311f82439d9123f7ad32f0bd4e.jpg', '2', '2025-02-02 17:39:51');
INSERT INTO `abs_article` VALUES (7, '64c64f3f-9e88-4903-b487-c82f21a746ac', '高清舒适近视眼镜 - 清晰视界，时尚之选', '<h4>产品特点：</h4><ol><li><p><strong>高清树脂镜片，视觉更清晰</strong><br/>采用优质树脂镜片，高透光率，低色散，有效减少眩光和反射，提供清晰的视觉体验。镜片经过多层镀膜处理，防紫外线、防蓝光，保护眼睛健康。</p></li><li><p><strong>轻量化设计，佩戴舒适</strong><br/>镜框采用超轻&nbsp;TR90&nbsp;材质，重量仅为&nbsp;15&nbsp;克，佩戴轻盈无压力。鼻托和镜腿经过人体工学设计，长时间佩戴也不会感到不适，适合日常使用。</p></li><li><p><strong>时尚设计，百搭造型</strong><br/>经典款式设计，简约大方，适合各种脸型和场合。提供多种颜色选择，包括黑色、银色、金色等，满足不同用户的审美需求。</p></li><li><p><strong>防滑防汗，稳固耐用</strong><br/>镜腿采用防滑材质，即使运动或出汗也能稳固佩戴。镜框韧性极佳，不易变形，耐用性强。</p></li><li><p><strong>个性化定制，精准适配</strong><br/>支持根据用户的近视度数、瞳距等参数定制镜片，确保每副眼镜都能精准适配用户需求。</p></li></ol><hr/><h4>适用人群：</h4><ul><li><p><strong>学生</strong>：长时间学习用眼，防蓝光镜片减少电子屏幕对眼睛的伤害。</p></li><li><p><strong>上班族</strong>：适合长时间面对电脑，缓解视觉疲劳。</p></li><li><p><strong>时尚达人</strong>：百搭设计，提升整体造型感。</p></li><li><p><strong>运动爱好者</strong>：轻便防滑，适合运动时佩戴。</p></li></ul><hr/><h4>产品参数：</h4><ul><li><p>镜片材质：高清树脂</p></li><li><p>镜框材质：TR90</p></li><li><p>镜片功能：防紫外线、防蓝光、防眩光</p></li><li><p>镜框重量：15&nbsp;克</p></li><li><p>镜片宽度：50mm</p></li><li><p>鼻梁宽度：18mm</p></li><li><p>镜腿长度：140mm</p></li><li><p>包装：精美眼镜盒、眼镜布、保修卡</p></li></ul><hr/><h4>用户评价：</h4><ul><li><p><strong>“镜片非常清晰，戴上后看东西特别舒服，防蓝光效果也很好！”</strong></p></li><li><p><strong>“镜框很轻，戴一整天都不会觉得累，设计也很时尚，朋友们都说好看。”</strong></p></li><li><p><strong>“定制服务很贴心，眼镜完全符合我的度数，佩戴起来非常舒适。”</strong></p></li></ul><hr/><h4>价格：</h4><p><strong>¥299</strong>（限时优惠，原价¥399）</p><hr/><h4>购买方式：</h4><ul><li><p><strong>线上购买</strong>：访问我们的官方商城或天猫/京东旗舰店。</p></li><li><p><strong>线下体验</strong>：全国各大眼镜专卖店均有售。</p></li></ul><hr/><h4>温馨提示：</h4><ul><li><p>请根据验光单选择适合的镜片度数。</p></li><li><p>使用后请用附赠的眼镜布清洁镜片，避免刮花。</p></li><li><p>避免将眼镜长时间放置在高温或潮湿环境中。</p></li></ul><hr/><p><strong>选择我们的近视眼镜，享受清晰视界与时尚魅力的完美结合！</strong></p>', '1', 'admin', '2', '产品特点：高清树脂镜片，视觉更清晰</str', 'http://read8686.oss-cn-beijing.aliyuncs.com/mall/f4fb21311f82439d9123f7ad32f0bd4e.jpg', '4', '2025-02-02 17:39:52');
INSERT INTO `abs_article` VALUES (8, '865c4d28-5f96-457d-ae44-e0fd241b35e6', '高清舒适近视眼镜 - 清晰视界，时尚之选', '<h4>产品特点：</h4><ol><li><p><strong>高清树脂镜片，视觉更清晰</strong><br/>采用优质树脂镜片，高透光率，低色散，有效减少眩光和反射，提供清晰的视觉体验。镜片经过多层镀膜处理，防紫外线、防蓝光，保护眼睛健康。</p></li><li><p><strong>轻量化设计，佩戴舒适</strong><br/>镜框采用超轻&nbsp;TR90&nbsp;材质，重量仅为&nbsp;15&nbsp;克，佩戴轻盈无压力。鼻托和镜腿经过人体工学设计，长时间佩戴也不会感到不适，适合日常使用。</p></li><li><p><strong>时尚设计，百搭造型</strong><br/>经典款式设计，简约大方，适合各种脸型和场合。提供多种颜色选择，包括黑色、银色、金色等，满足不同用户的审美需求。</p></li><li><p><strong>防滑防汗，稳固耐用</strong><br/>镜腿采用防滑材质，即使运动或出汗也能稳固佩戴。镜框韧性极佳，不易变形，耐用性强。</p></li><li><p><strong>个性化定制，精准适配</strong><br/>支持根据用户的近视度数、瞳距等参数定制镜片，确保每副眼镜都能精准适配用户需求。</p></li></ol><hr/><h4>适用人群：</h4><ul><li><p><strong>学生</strong>：长时间学习用眼，防蓝光镜片减少电子屏幕对眼睛的伤害。</p></li><li><p><strong>上班族</strong>：适合长时间面对电脑，缓解视觉疲劳。</p></li><li><p><strong>时尚达人</strong>：百搭设计，提升整体造型感。</p></li><li><p><strong>运动爱好者</strong>：轻便防滑，适合运动时佩戴。</p></li></ul><hr/><h4>产品参数：</h4><ul><li><p>镜片材质：高清树脂</p></li><li><p>镜框材质：TR90</p></li><li><p>镜片功能：防紫外线、防蓝光、防眩光</p></li><li><p>镜框重量：15&nbsp;克</p></li><li><p>镜片宽度：50mm</p></li><li><p>鼻梁宽度：18mm</p></li><li><p>镜腿长度：140mm</p></li><li><p>包装：精美眼镜盒、眼镜布、保修卡</p></li></ul><hr/><h4>用户评价：</h4><ul><li><p><strong>“镜片非常清晰，戴上后看东西特别舒服，防蓝光效果也很好！”</strong></p></li><li><p><strong>“镜框很轻，戴一整天都不会觉得累，设计也很时尚，朋友们都说好看。”</strong></p></li><li><p><strong>“定制服务很贴心，眼镜完全符合我的度数，佩戴起来非常舒适。”</strong></p></li></ul><hr/><h4>价格：</h4><p><strong>¥299</strong>（限时优惠，原价¥399）</p><hr/><h4>购买方式：</h4><ul><li><p><strong>线上购买</strong>：访问我们的官方商城或天猫/京东旗舰店。</p></li><li><p><strong>线下体验</strong>：全国各大眼镜专卖店均有售。</p></li></ul><hr/><h4>温馨提示：</h4><ul><li><p>请根据验光单选择适合的镜片度数。</p></li><li><p>使用后请用附赠的眼镜布清洁镜片，避免刮花。</p></li><li><p>避免将眼镜长时间放置在高温或潮湿环境中。</p></li></ul><hr/><p><strong>选择我们的近视眼镜，享受清晰视界与时尚魅力的完美结合！</strong></p>', '1', 'admin', '2', '产品特点：高清树脂镜片，视觉更清晰</str', 'http://read8686.oss-cn-beijing.aliyuncs.com/mall/f4fb21311f82439d9123f7ad32f0bd4e.jpg', '2', '2025-02-02 17:39:52');
INSERT INTO `abs_article` VALUES (9, '0fc3892c-788e-4222-8f07-aa2afbfddc17', '高清舒适近视眼镜 - 清晰视界，时尚之选', '<h4>产品特点：</h4><ol><li><p><strong>高清树脂镜片，视觉更清晰</strong><br/>采用优质树脂镜片，高透光率，低色散，有效减少眩光和反射，提供清晰的视觉体验。镜片经过多层镀膜处理，防紫外线、防蓝光，保护眼睛健康。</p></li><li><p><strong>轻量化设计，佩戴舒适</strong><br/>镜框采用超轻&nbsp;TR90&nbsp;材质，重量仅为&nbsp;15&nbsp;克，佩戴轻盈无压力。鼻托和镜腿经过人体工学设计，长时间佩戴也不会感到不适，适合日常使用。</p></li><li><p><strong>时尚设计，百搭造型</strong><br/>经典款式设计，简约大方，适合各种脸型和场合。提供多种颜色选择，包括黑色、银色、金色等，满足不同用户的审美需求。</p></li><li><p><strong>防滑防汗，稳固耐用</strong><br/>镜腿采用防滑材质，即使运动或出汗也能稳固佩戴。镜框韧性极佳，不易变形，耐用性强。</p></li><li><p><strong>个性化定制，精准适配</strong><br/>支持根据用户的近视度数、瞳距等参数定制镜片，确保每副眼镜都能精准适配用户需求。</p></li></ol><hr/><h4>适用人群：</h4><ul><li><p><strong>学生</strong>：长时间学习用眼，防蓝光镜片减少电子屏幕对眼睛的伤害。</p></li><li><p><strong>上班族</strong>：适合长时间面对电脑，缓解视觉疲劳。</p></li><li><p><strong>时尚达人</strong>：百搭设计，提升整体造型感。</p></li><li><p><strong>运动爱好者</strong>：轻便防滑，适合运动时佩戴。</p></li></ul><hr/><h4>产品参数：</h4><ul><li><p>镜片材质：高清树脂</p></li><li><p>镜框材质：TR90</p></li><li><p>镜片功能：防紫外线、防蓝光、防眩光</p></li><li><p>镜框重量：15&nbsp;克</p></li><li><p>镜片宽度：50mm</p></li><li><p>鼻梁宽度：18mm</p></li><li><p>镜腿长度：140mm</p></li><li><p>包装：精美眼镜盒、眼镜布、保修卡</p></li></ul><hr/><h4>用户评价：</h4><ul><li><p><strong>“镜片非常清晰，戴上后看东西特别舒服，防蓝光效果也很好！”</strong></p></li><li><p><strong>“镜框很轻，戴一整天都不会觉得累，设计也很时尚，朋友们都说好看。”</strong></p></li><li><p><strong>“定制服务很贴心，眼镜完全符合我的度数，佩戴起来非常舒适。”</strong></p></li></ul><hr/><h4>价格：</h4><p><strong>¥299</strong>（限时优惠，原价¥399）</p><hr/><h4>购买方式：</h4><ul><li><p><strong>线上购买</strong>：访问我们的官方商城或天猫/京东旗舰店。</p></li><li><p><strong>线下体验</strong>：全国各大眼镜专卖店均有售。</p></li></ul><hr/><h4>温馨提示：</h4><ul><li><p>请根据验光单选择适合的镜片度数。</p></li><li><p>使用后请用附赠的眼镜布清洁镜片，避免刮花。</p></li><li><p>避免将眼镜长时间放置在高温或潮湿环境中。</p></li></ul><hr/><p><strong>选择我们的近视眼镜，享受清晰视界与时尚魅力的完美结合！</strong></p>', '1', 'admin', '2', '产品特点：高清树脂镜片，视觉更清晰</str', 'http://read8686.oss-cn-beijing.aliyuncs.com/mall/f4fb21311f82439d9123f7ad32f0bd4e.jpg', '2', '2025-02-02 17:39:52');
INSERT INTO `abs_article` VALUES (10, '449ae529-77d9-419d-b5c0-55105c2ed594', '高清舒适近视眼镜 - 清晰视界，时尚之选', '<h4>产品特点：</h4><ol><li><p><strong>高清树脂镜片，视觉更清晰</strong><br/>采用优质树脂镜片，高透光率，低色散，有效减少眩光和反射，提供清晰的视觉体验。镜片经过多层镀膜处理，防紫外线、防蓝光，保护眼睛健康。</p></li><li><p><strong>轻量化设计，佩戴舒适</strong><br/>镜框采用超轻&nbsp;TR90&nbsp;材质，重量仅为&nbsp;15&nbsp;克，佩戴轻盈无压力。鼻托和镜腿经过人体工学设计，长时间佩戴也不会感到不适，适合日常使用。</p></li><li><p><strong>时尚设计，百搭造型</strong><br/>经典款式设计，简约大方，适合各种脸型和场合。提供多种颜色选择，包括黑色、银色、金色等，满足不同用户的审美需求。</p></li><li><p><strong>防滑防汗，稳固耐用</strong><br/>镜腿采用防滑材质，即使运动或出汗也能稳固佩戴。镜框韧性极佳，不易变形，耐用性强。</p></li><li><p><strong>个性化定制，精准适配</strong><br/>支持根据用户的近视度数、瞳距等参数定制镜片，确保每副眼镜都能精准适配用户需求。</p></li></ol><hr/><h4>适用人群：</h4><ul><li><p><strong>学生</strong>：长时间学习用眼，防蓝光镜片减少电子屏幕对眼睛的伤害。</p></li><li><p><strong>上班族</strong>：适合长时间面对电脑，缓解视觉疲劳。</p></li><li><p><strong>时尚达人</strong>：百搭设计，提升整体造型感。</p></li><li><p><strong>运动爱好者</strong>：轻便防滑，适合运动时佩戴。</p></li></ul><hr/><h4>产品参数：</h4><ul><li><p>镜片材质：高清树脂</p></li><li><p>镜框材质：TR90</p></li><li><p>镜片功能：防紫外线、防蓝光、防眩光</p></li><li><p>镜框重量：15&nbsp;克</p></li><li><p>镜片宽度：50mm</p></li><li><p>鼻梁宽度：18mm</p></li><li><p>镜腿长度：140mm</p></li><li><p>包装：精美眼镜盒、眼镜布、保修卡</p></li></ul><hr/><h4>用户评价：</h4><ul><li><p><strong>“镜片非常清晰，戴上后看东西特别舒服，防蓝光效果也很好！”</strong></p></li><li><p><strong>“镜框很轻，戴一整天都不会觉得累，设计也很时尚，朋友们都说好看。”</strong></p></li><li><p><strong>“定制服务很贴心，眼镜完全符合我的度数，佩戴起来非常舒适。”</strong></p></li></ul><hr/><h4>价格：</h4><p><strong>¥299</strong>（限时优惠，原价¥399）</p><hr/><h4>购买方式：</h4><ul><li><p><strong>线上购买</strong>：访问我们的官方商城或天猫/京东旗舰店。</p></li><li><p><strong>线下体验</strong>：全国各大眼镜专卖店均有售。</p></li></ul><hr/><h4>温馨提示：</h4><ul><li><p>请根据验光单选择适合的镜片度数。</p></li><li><p>使用后请用附赠的眼镜布清洁镜片，避免刮花。</p></li><li><p>避免将眼镜长时间放置在高温或潮湿环境中。</p></li></ul><hr/><p><strong>选择我们的近视眼镜，享受清晰视界与时尚魅力的完美结合！</strong></p>', '1', 'admin', '1', '产品特点：高清树脂镜片，视觉更清晰</str', 'http://read8686.oss-cn-beijing.aliyuncs.com/mall/66a74cbc228f429891a5c78320ef7bb0.png', '4', '2025-02-02 17:41:04');

-- ----------------------------
-- Table structure for abs_banner
-- ----------------------------
DROP TABLE IF EXISTS `abs_banner`;
CREATE TABLE `abs_banner`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `picurl` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `linkurl` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abs_codeitem
-- ----------------------------
DROP TABLE IF EXISTS `abs_codeitem`;
CREATE TABLE `abs_codeitem`  (
  `rowguid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `itemorder` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `itemtext` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `itemname` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `itemvalue` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `operatedate` datetime(0) DEFAULT NULL,
  `operateguid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of abs_codeitem
-- ----------------------------
INSERT INTO `abs_codeitem` VALUES ('f39da200-5926-4e33-adb6-98086c32c807', '1001', '是', '是否', '1', '2025-01-02 15:28:48', NULL, NULL);
INSERT INTO `abs_codeitem` VALUES ('04c0999d-5793-4545-a80a-193328c1e017', '1001', '否', '是否', '0', '2025-01-02 15:28:58', NULL, NULL);
INSERT INTO `abs_codeitem` VALUES ('27cb5b9b-c05e-4e18-8e93-f4ba78a7330f', '1001001', '生活用品', '商品类型', '1', '2025-01-15 10:56:09', NULL, NULL);
INSERT INTO `abs_codeitem` VALUES ('4b4a0970-cdae-4110-b59c-8bc975e9da32', '1001001', '食品蔬菜', '商品类型', '2', '2025-01-15 10:56:31', NULL, NULL);
INSERT INTO `abs_codeitem` VALUES ('b7db5f50-e87a-4b5e-8ba6-95caf2881435', '1001002', '包邮', '邮寄类型', '1', '2025-01-15 10:57:13', NULL, NULL);
INSERT INTO `abs_codeitem` VALUES ('9c2b9045-eac9-47af-a2be-504be1b052a6', '1001002', '自负运费', '邮寄类型', '2', '2025-01-15 11:01:03', NULL, NULL);
INSERT INTO `abs_codeitem` VALUES ('41ba52f7-b2a1-4377-9a45-80b3adae6bd7', '1001006', '公司新闻', '文章分类', '1', '2025-02-02 12:41:34', NULL, NULL);
INSERT INTO `abs_codeitem` VALUES ('c1e66192-7313-40fc-be96-61f49d81b4ae', '1001006', '产品发布', '文章分类', '2', '2025-02-02 12:41:44', NULL, NULL);

-- ----------------------------
-- Table structure for abs_fileinfo
-- ----------------------------
DROP TABLE IF EXISTS `abs_fileinfo`;
CREATE TABLE `abs_fileinfo`  (
  `rowguid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '唯一标识',
  `userguid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `filepath` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '存储路径',
  `fileurl` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件地址',
  `addtime` datetime(0) DEFAULT NULL COMMENT '上传日期',
  `filesize` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '文件大小',
  `cliengguid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件组标识',
  `istoali` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '是否上传到阿里',
  `firstkey` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '上传阿里云对应的key',
  `filename` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件名称',
  `filetype` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件类型',
  `remark` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `istmp` int(2) UNSIGNED ZEROFILL DEFAULT NULL
) ENGINE = InnoDB AUTO_INCREMENT = 150 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for abs_flow_edge
-- ----------------------------
DROP TABLE IF EXISTS `abs_flow_edge`;
CREATE TABLE `abs_flow_edge`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `source` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sourcehandle` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `target` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `flowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abs_flow_event
-- ----------------------------
DROP TABLE IF EXISTS `abs_flow_event`;
CREATE TABLE `abs_flow_event`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `startname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `startuser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `grouptype` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `groupname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `flowname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `flowguid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `flowtype` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `pronodeid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `pronodename` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ordernum` int(0) DEFAULT NULL,
  `status` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `startdate` datetime(0) DEFAULT NULL,
  `enddate` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abs_flow_nconfig
-- ----------------------------
DROP TABLE IF EXISTS `abs_flow_nconfig`;
CREATE TABLE `abs_flow_nconfig`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `flowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nodeid` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `page` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `roleguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `action` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `remark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abs_flow_node
-- ----------------------------
DROP TABLE IF EXISTS `abs_flow_node`;
CREATE TABLE `abs_flow_node`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `position` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `data` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `measured` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `selected` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `dragging` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `flowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abs_flow_operate
-- ----------------------------
DROP TABLE IF EXISTS `abs_flow_operate`;
CREATE TABLE `abs_flow_operate`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `eventguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `userguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `operatedate` datetime(0) DEFAULT NULL,
  `operate` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nodeid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `operateid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abs_flow_process
-- ----------------------------
DROP TABLE IF EXISTS `abs_flow_process`;
CREATE TABLE `abs_flow_process`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `eventguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `flowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nodeguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nodename` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `nodeid` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `isend` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ordernum` int(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abs_flowinfo
-- ----------------------------
DROP TABLE IF EXISTS `abs_flowinfo`;
CREATE TABLE `abs_flowinfo`  (
  `rowguid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `groupname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `grouptype` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `flowname` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `userguid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `startrole` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `startname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `flowtype` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `typename` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abs_holiday
-- ----------------------------
DROP TABLE IF EXISTS `abs_holiday`;
CREATE TABLE `abs_holiday`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `userguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `eventguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `startdate` datetime(0) DEFAULT NULL,
  `startrang` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `enddate` datetime(0) DEFAULT NULL,
  `endrang` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abs_home_user
-- ----------------------------
DROP TABLE IF EXISTS `abs_home_user`;
CREATE TABLE `abs_home_user`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `czjguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `zjname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ordernumber` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `userguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for abs_home_zj
-- ----------------------------
DROP TABLE IF EXISTS `abs_home_zj`;
CREATE TABLE `abs_home_zj`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cpath` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ordernum` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for abs_homeproduct
-- ----------------------------
DROP TABLE IF EXISTS `abs_homeproduct`;
CREATE TABLE `abs_homeproduct`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `picurl` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `linkurl` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `catalog` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abs_jiari
-- ----------------------------
DROP TABLE IF EXISTS `abs_jiari`;
CREATE TABLE `abs_jiari`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `isjia` int(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for abs_linkmsg
-- ----------------------------
DROP TABLE IF EXISTS `abs_linkmsg`;
CREATE TABLE `abs_linkmsg`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `issend` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of abs_linkmsg
-- ----------------------------
INSERT INTO `abs_linkmsg` VALUES ('855dcd86-a5c5-4c9e-903a-656fa3dc5a8b', '12', '王登高', '19803899158', '250242100@qq.com', NULL, '2025-02-03 18:23:09');
INSERT INTO `abs_linkmsg` VALUES ('a4bf5c27-4448-408e-81cd-9977828a256c', '11', '王登高', '19803899158', '250242100@qq.com', NULL, '2025-02-03 18:20:21');

-- ----------------------------
-- Table structure for abs_logininfo
-- ----------------------------
DROP TABLE IF EXISTS `abs_logininfo`;
CREATE TABLE `abs_logininfo`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `logindate` datetime(0) DEFAULT NULL,
  `userinfo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `outdate` datetime(0) DEFAULT NULL,
  `usertoken` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `userguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for abs_msginfo
-- ----------------------------
DROP TABLE IF EXISTS `abs_msginfo`;
CREATE TABLE `abs_msginfo`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `isread` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `msgtitle` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `userguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `datasource` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abs_operate
-- ----------------------------
DROP TABLE IF EXISTS `abs_operate`;
CREATE TABLE `abs_operate`  (
  `rowguid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `operate` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `wzoperate` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abs_ouinfo
-- ----------------------------
DROP TABLE IF EXISTS `abs_ouinfo`;
CREATE TABLE `abs_ouinfo`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ouname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `oucode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ouaddresstel` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `oubankaccount` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `remark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of abs_ouinfo
-- ----------------------------
INSERT INTO `abs_ouinfo` VALUES ('5753477c-534c-4ac7-8313-274d4703068c', '会员1群', '00002', '', '', '', '2021-06-11 06:23:21');
INSERT INTO `abs_ouinfo` VALUES ('7aaab287-eb36-4dcd-8acd-c03d81946384', '管理中心', '00001', '', '', '', '2023-07-28 17:24:20');
INSERT INTO `abs_ouinfo` VALUES ('c7561814-7275-4579-9541-0aae99b5de5a', '商户部门', '00003', '', '', '', '2025-01-09 07:53:50');

-- ----------------------------
-- Table structure for abs_picinfo
-- ----------------------------
DROP TABLE IF EXISTS `abs_picinfo`;
CREATE TABLE `abs_picinfo`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `userguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `picurl` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `firstkey` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abs_roleinfo
-- ----------------------------
DROP TABLE IF EXISTS `abs_roleinfo`;
CREATE TABLE `abs_roleinfo`  (
  `rowguid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `rolename` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  `ordernum` int(0) DEFAULT NULL,
  `remark` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of abs_roleinfo
-- ----------------------------
INSERT INTO `abs_roleinfo` VALUES ('433d9aef-c1ba-4de0-a482-b751ad5fb79b', '管理员', '2025-01-15 16:01:00', 0, '管理员 admin');
INSERT INTO `abs_roleinfo` VALUES ('90cf1b51-69b1-4b46-bcb1-98fbae08b766', '公司职员', '2025-01-15 16:00:48', 0, '公司职员');
INSERT INTO `abs_roleinfo` VALUES ('be67952f-32f7-4851-9a88-6e16b461ae7b', '超级管理员', '2024-04-04 11:59:40', 0, '系统运维，系统配置');
INSERT INTO `abs_roleinfo` VALUES ('ee3c4602-4520-4890-a532-96f612528117', '商户', '2025-01-09 07:55:48', 0, '商户管理人员');

-- ----------------------------
-- Table structure for abs_roleview_perm
-- ----------------------------
DROP TABLE IF EXISTS `abs_roleview_perm`;
CREATE TABLE `abs_roleview_perm`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `roleguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `viewguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE,
  INDEX `index_roleguid`(`roleguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of abs_roleview_perm
-- ----------------------------
INSERT INTO `abs_roleview_perm` VALUES ('00cb805f-1416-4679-9403-c78047662c99', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '9378c790-a0f2-4387-9414-a40927ee558c');
INSERT INTO `abs_roleview_perm` VALUES ('01b34d50-ca57-4740-920f-73390d9da74b', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'b7edd95b-ccb4-4388-ba0c-11d68433320a');
INSERT INTO `abs_roleview_perm` VALUES ('0561b48e-6183-4948-985c-dc53d98a5106', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', 'b6116412-304a-4c35-860c-9c79a4852dc9');
INSERT INTO `abs_roleview_perm` VALUES ('059dd431-a398-4d31-aac8-9e3c5a5ed9a8', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'b2c25e03-16fa-4b5a-9f1b-804fc3400af3');
INSERT INTO `abs_roleview_perm` VALUES ('065fc440-5e5b-4215-bcad-318607e42a15', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'be2d87a2-23a9-41bf-8870-3a3b4c819b58');
INSERT INTO `abs_roleview_perm` VALUES ('07a628bf-7b88-4f2c-a03d-3e029055c703', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '6798fcd7-230b-45e6-827a-4e5f2ecf46fd');
INSERT INTO `abs_roleview_perm` VALUES ('07c5bb99-0719-48b2-9057-4447bb78f759', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'ad53c709-32c6-4cf7-a175-9c5c2f315319');
INSERT INTO `abs_roleview_perm` VALUES ('07d9f052-9c79-47d9-bab1-46ccc85597b6', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '4c23322b-55ba-4498-ba25-1c395654b0f6');
INSERT INTO `abs_roleview_perm` VALUES ('093e4c1f-46f7-44aa-bd36-b054635c673e', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '7ff707ae-f442-42ab-8c2f-a95ae251c9ab');
INSERT INTO `abs_roleview_perm` VALUES ('0b4660b6-e239-4d88-81bb-6dfd0c68b74a', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '28b80187-5038-4aa9-a75d-45eb3ea17658');
INSERT INTO `abs_roleview_perm` VALUES ('0c131f9e-6b30-4858-8c07-c279cd01f254', 'ee3c4602-4520-4890-a532-96f612528117', '9298b975-bb26-44e4-8ca0-cc300fc2ded6');
INSERT INTO `abs_roleview_perm` VALUES ('10f09021-f715-4f1c-9ad4-6dceb6d3ae56', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '5e46d69b-8f14-47f9-9a00-b40b06319280');
INSERT INTO `abs_roleview_perm` VALUES ('139a71c1-f2d6-4fab-9305-d161bff2670a', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'eb23831d-471c-490d-bc50-f20e148212d1');
INSERT INTO `abs_roleview_perm` VALUES ('143be2ce-07cc-4bbb-897b-4aa088a29207', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '07421f4f-4279-4d47-b128-fd0838cda770');
INSERT INTO `abs_roleview_perm` VALUES ('146fd9dc-36e0-4ef1-9ce5-3b5fbd8b6ce7', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '14709db2-5480-422a-ab8b-4209908bb763');
INSERT INTO `abs_roleview_perm` VALUES ('164af142-a5b4-406b-8028-457eb6d6086d', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '54dd307c-1140-4ccc-ae73-b1b6c1a63c1f');
INSERT INTO `abs_roleview_perm` VALUES ('172c7ea4-44a4-469e-aa42-c3b40a4c6561', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '96df39ce-6c0c-42b8-87b2-7f5b71436a6c');
INSERT INTO `abs_roleview_perm` VALUES ('1b20ffd9-d9e5-4048-a25a-8aea509fa249', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'c0c5385a-fa87-4031-8a81-bdf9031f83c1');
INSERT INTO `abs_roleview_perm` VALUES ('1e0271b7-4df9-4d53-bb2e-26e4c466975f', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'f06a5045-3216-4d9a-b68f-8e1bf6382ab4');
INSERT INTO `abs_roleview_perm` VALUES ('20bb9e14-4904-47b3-8977-98b98be441eb', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', 'a33fb9d3-f405-45f1-8328-3ac2e7df9bde');
INSERT INTO `abs_roleview_perm` VALUES ('212ff0a8-9364-44f1-8572-f1e981e04cb0', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '7972359a-fc67-4ebb-a412-b50ac8f12caf');
INSERT INTO `abs_roleview_perm` VALUES ('2441f02c-867a-4e71-81e5-a2c051d6b36f', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '58e3975d-4133-4f20-858b-d10665229ed0');
INSERT INTO `abs_roleview_perm` VALUES ('2641dd85-9a58-49f7-a267-9b9e548a28a0', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'a2f61dde-6b1c-4514-b5b9-b0e5d88fa87a');
INSERT INTO `abs_roleview_perm` VALUES ('271e0d1d-a996-428c-aadb-0d2dbe27fae0', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '43783d35-2d41-44db-9200-41b63577cf6e');
INSERT INTO `abs_roleview_perm` VALUES ('27bedc03-f47e-41af-8dab-73cc5a60788c', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'ad53c709-32c6-4cf7-a175-9c5c2f315319');
INSERT INTO `abs_roleview_perm` VALUES ('2807cf61-3aab-4eb2-9045-61715841254e', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'd299b374-a6bc-4f27-9840-529f331188e8');
INSERT INTO `abs_roleview_perm` VALUES ('28d0d7b7-bb73-4060-9150-26b3042d3172', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '58e3975d-4133-4f20-858b-d10665229ed0');
INSERT INTO `abs_roleview_perm` VALUES ('28d3391d-8b6f-49cb-befd-ced182813e26', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '4265cde1-a5ce-4820-9ca8-e727074f6435');
INSERT INTO `abs_roleview_perm` VALUES ('28dc0686-5036-4350-87f4-f4ed4762e09d', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '1caaedd0-18c7-473d-b07d-6bee90dd8dcf');
INSERT INTO `abs_roleview_perm` VALUES ('2d0141dd-5bf8-4629-947c-37f614f5810b', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'f282abfe-9456-443f-b99b-83529264b924');
INSERT INTO `abs_roleview_perm` VALUES ('33f0e90b-8747-41f3-9886-5891605333ff', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'e3ad6d27-edec-4997-9b32-0a1577d87557');
INSERT INTO `abs_roleview_perm` VALUES ('343685a0-9b50-4a45-961d-9ccb9cf8bdfb', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '73c3e2fe-f745-492f-9ad6-09ed4b179810');
INSERT INTO `abs_roleview_perm` VALUES ('3479f421-d4f7-4c90-9791-41092de95176', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'e166efbd-fcf8-40bd-a29e-68af79102e74');
INSERT INTO `abs_roleview_perm` VALUES ('34d6c55a-b89c-4aa8-ba00-7c4896fc2fde', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '802a2500-1467-4f1a-b89c-367619a457c3');
INSERT INTO `abs_roleview_perm` VALUES ('36ecba93-04cc-49c1-b6f6-7b90ebc2b4ff', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', 'd2d33c2e-7e51-472b-99a9-598d4a91ab7b');
INSERT INTO `abs_roleview_perm` VALUES ('37b2d249-75ec-4f3e-b78f-96e6c77172c7', 'ee3c4602-4520-4890-a532-96f612528117', '4e974c87-7a3f-407f-8da6-b0977083d8f7');
INSERT INTO `abs_roleview_perm` VALUES ('37d26d51-ed86-4906-b176-d32b24bc55b2', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '9bd2e322-6e54-479c-ac1b-572b674fb865');
INSERT INTO `abs_roleview_perm` VALUES ('3b24fdf3-6b25-437f-bdf6-8e5b0ad38f36', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '63fb0a73-a88b-4448-a3f8-e9b0e61d9f02');
INSERT INTO `abs_roleview_perm` VALUES ('45cb734e-3858-4b3d-a650-5235e952104d', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '585eef2e-d6fd-4ca2-8e77-cda1b368f706');
INSERT INTO `abs_roleview_perm` VALUES ('48195edc-99ba-408a-a3d6-9ce3209cca65', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '11fcdbc0-766a-40ed-a0d5-afb665018f4e');
INSERT INTO `abs_roleview_perm` VALUES ('482e22b3-fd61-447d-ab38-8d3b4b96deac', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '86819e2f-fb9a-4a8d-8e72-05058f67705b');
INSERT INTO `abs_roleview_perm` VALUES ('49e587de-d6f4-4bf0-bfcb-21980a8bb903', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '414dced9-7bf4-4154-9a85-0628ebbd765d');
INSERT INTO `abs_roleview_perm` VALUES ('4b5836d0-8d33-4233-934a-a66e6a8f4d88', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '7972359a-fc67-4ebb-a412-b50ac8f12caf');
INSERT INTO `abs_roleview_perm` VALUES ('4c0629a8-9649-4d76-ab2f-17a5e712d59d', 'ee3c4602-4520-4890-a532-96f612528117', '1caaedd0-18c7-473d-b07d-6bee90dd8dcf');
INSERT INTO `abs_roleview_perm` VALUES ('54f1a0b9-5d32-4d93-b25f-1bbdf9ec33b0', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '9705a9c1-a723-4bc0-8817-1412443db8e1');
INSERT INTO `abs_roleview_perm` VALUES ('551d4ca7-dc8d-4e64-a28a-532470c583ac', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '96df39ce-6c0c-42b8-87b2-7f5b71436a6c');
INSERT INTO `abs_roleview_perm` VALUES ('55e0d320-2f93-4f94-a2c0-6cd85f38bed6', 'ee3c4602-4520-4890-a532-96f612528117', 'd299b374-a6bc-4f27-9840-529f331188e8');
INSERT INTO `abs_roleview_perm` VALUES ('55eb7bf7-16c1-465f-b5ba-2579ad02473b', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'd9c32be7-ea6f-47c7-af8c-b2c88a2e444e');
INSERT INTO `abs_roleview_perm` VALUES ('5761329c-6f95-4fbc-a088-45037a8202f5', 'ee3c4602-4520-4890-a532-96f612528117', '14709db2-5480-422a-ab8b-4209908bb763');
INSERT INTO `abs_roleview_perm` VALUES ('58b37598-b5a5-4bd9-a4ee-d7ce2f6e87a4', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '5e46d69b-8f14-47f9-9a00-b40b06319280');
INSERT INTO `abs_roleview_perm` VALUES ('5c3d2369-5763-400c-9d44-44fd0bc80bd2', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'c7c890e6-b7b0-4719-bcfc-8c6990ea90b6');
INSERT INTO `abs_roleview_perm` VALUES ('5e23ee8b-5094-404c-8543-3892ba266132', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'f5366a6b-0a8e-46a6-aec7-d366a63c384a');
INSERT INTO `abs_roleview_perm` VALUES ('5e592d52-d5b4-4854-b7b9-49a0be18b071', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '79cb3996-1785-44ac-96c6-610166a9ef88');
INSERT INTO `abs_roleview_perm` VALUES ('612b1350-7e0a-4d36-9c02-df6dd2c685c1', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '54797820-931d-4ebc-bd58-f08a1d8df93c');
INSERT INTO `abs_roleview_perm` VALUES ('6171f2b7-ef4b-40a6-841a-01ec5f0156a3', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '6f00137c-ff73-45b1-bd2d-7cb82cf4761a');
INSERT INTO `abs_roleview_perm` VALUES ('646dd90f-336e-4efd-87ee-a0d937dadc77', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '14709db2-5480-422a-ab8b-4209908bb763');
INSERT INTO `abs_roleview_perm` VALUES ('665b1139-ff26-42bc-ad52-68acc17cac55', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '3347a80b-a7e0-4d3e-bb9a-fdfa949bc8c9');
INSERT INTO `abs_roleview_perm` VALUES ('67cf8760-1c94-492d-8ee2-7f80f941ce9e', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '473319e3-d747-49f7-8c68-03cc88007caf');
INSERT INTO `abs_roleview_perm` VALUES ('68892978-fd6b-43e1-95b0-28165e1b06cb', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '79cb3996-1785-44ac-96c6-610166a9ef88');
INSERT INTO `abs_roleview_perm` VALUES ('691457b7-1a39-4d05-aa61-d94ff2b4faf3', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', 'ff8d546c-1fc9-44ba-be66-34afcf118467');
INSERT INTO `abs_roleview_perm` VALUES ('69563380-8c2f-4bea-a3e8-df1e4afca33a', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '01b6d5f2-2495-407e-855c-d1d9e5e0a46a');
INSERT INTO `abs_roleview_perm` VALUES ('6a188595-4f1b-46f1-92cc-2d9d02c87b00', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '0edffd83-3e3b-449c-9d84-23bd33b48beb');
INSERT INTO `abs_roleview_perm` VALUES ('6acf7403-5160-432d-9f02-4947e640f629', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '9378c790-a0f2-4387-9414-a40927ee558c');
INSERT INTO `abs_roleview_perm` VALUES ('6ad4d117-ca1e-41ef-a25d-aecaa9f1686a', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '9298b975-bb26-44e4-8ca0-cc300fc2ded6');
INSERT INTO `abs_roleview_perm` VALUES ('6bd45e2d-269c-4c5e-9938-49b716fad525', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'a2f61dde-6b1c-4514-b5b9-b0e5d88fa87a');
INSERT INTO `abs_roleview_perm` VALUES ('710aaf7c-9871-4e9c-a096-16546a0354cc', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '74884601-7652-4936-a590-95616bc4ae04');
INSERT INTO `abs_roleview_perm` VALUES ('72384217-be5c-4361-962b-8a54167f88d5', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', 'b418070d-63de-4254-8b5d-a3b786ff5eec');
INSERT INTO `abs_roleview_perm` VALUES ('79d24c8d-f9e5-46a4-b172-cc253c5480de', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'd8f5c679-793c-47e5-b023-981b62eca61e');
INSERT INTO `abs_roleview_perm` VALUES ('7bbd14b9-da10-45e9-bd20-c09c630d1cf7', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '4c23322b-55ba-4498-ba25-1c395654b0f6');
INSERT INTO `abs_roleview_perm` VALUES ('7db96361-aa7c-4695-9186-b9bc86aaff69', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '9705a9c1-a723-4bc0-8817-1412443db8e1');
INSERT INTO `abs_roleview_perm` VALUES ('7e5f3743-640a-4932-b498-e8e13a5955c0', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'f282abfe-9456-443f-b99b-83529264b924');
INSERT INTO `abs_roleview_perm` VALUES ('7ee65b31-9a3a-473f-b3b7-514bb61bf6b3', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '28b80187-5038-4aa9-a75d-45eb3ea17658');
INSERT INTO `abs_roleview_perm` VALUES ('7f8c2275-9b5c-432d-add5-e604459f8f0e', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '63fb0a73-a88b-4448-a3f8-e9b0e61d9f02');
INSERT INTO `abs_roleview_perm` VALUES ('7fca018b-7196-4b15-b150-5c9a34a9afd5', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'eb23831d-471c-490d-bc50-f20e148212d1');
INSERT INTO `abs_roleview_perm` VALUES ('83257fd7-544b-4b4a-9d44-90edaae46401', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'd9c32be7-ea6f-47c7-af8c-b2c88a2e444e');
INSERT INTO `abs_roleview_perm` VALUES ('835715a4-5e8c-44e9-b173-dd0ca52e0d0f', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'a1f849eb-a760-419d-bb7a-eaa835f4b5e5');
INSERT INTO `abs_roleview_perm` VALUES ('8490eb30-9d9c-4cab-afd1-d3bf05d6c96f', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', 'f282abfe-9456-443f-b99b-83529264b924');
INSERT INTO `abs_roleview_perm` VALUES ('8680c32c-9f3a-47b6-be75-35fc87f9533c', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'eb5cf0c8-39df-4f97-96d7-5fe26e4b563a');
INSERT INTO `abs_roleview_perm` VALUES ('88caab49-e76a-4270-b98b-64cde7a9737b', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '2');
INSERT INTO `abs_roleview_perm` VALUES ('8afa0429-3013-43a1-8daa-e27bacff8939', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '89be6a75-53a3-4c03-a70e-cf84c41c2799');
INSERT INTO `abs_roleview_perm` VALUES ('8c58fdc5-4740-4bf6-9041-d548fa779815', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'c240c882-16b9-4fbd-bddd-2bbc23507988');
INSERT INTO `abs_roleview_perm` VALUES ('8cd28a0e-cd06-4ace-831e-53737559e8b6', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '507a47f5-c3ff-4dfd-aeeb-f6596ee2d36f');
INSERT INTO `abs_roleview_perm` VALUES ('8d7d63dc-2901-42e1-9b16-e98d28122cc9', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '473319e3-d747-49f7-8c68-03cc88007caf');
INSERT INTO `abs_roleview_perm` VALUES ('8f1694e9-f7e3-4a0f-af89-a9a444cf0924', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'b2c25e03-16fa-4b5a-9f1b-804fc3400af3');
INSERT INTO `abs_roleview_perm` VALUES ('9029504d-e186-4d53-8b48-e4233aa5f7b0', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '414dced9-7bf4-4154-9a85-0628ebbd765d');
INSERT INTO `abs_roleview_perm` VALUES ('91a67446-2da5-492f-abfd-0bd45daacf13', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '754964ed-9524-4d68-b4c7-629c831f3335');
INSERT INTO `abs_roleview_perm` VALUES ('95013865-4704-43bd-861a-3430efb8b638', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'cfb0236c-6e75-41e8-bc8b-57551f799c40');
INSERT INTO `abs_roleview_perm` VALUES ('976e200a-378c-459a-9220-f8d5617762f3', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', 'ba501da2-f9f7-4482-8326-024e4bca5794');
INSERT INTO `abs_roleview_perm` VALUES ('9995fa79-bd94-4c32-ae25-e74f9abfeabb', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '200a3d44-3173-4519-98ce-14a8ba2de446');
INSERT INTO `abs_roleview_perm` VALUES ('999fe550-5679-47dc-9019-c96bc017fd6a', 'ee3c4602-4520-4890-a532-96f612528117', 'c240c882-16b9-4fbd-bddd-2bbc23507988');
INSERT INTO `abs_roleview_perm` VALUES ('99f4ccc7-00c3-4bee-b8ff-985a567505d9', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'f06a5045-3216-4d9a-b68f-8e1bf6382ab4');
INSERT INTO `abs_roleview_perm` VALUES ('9afd25ad-a0f4-44eb-aa17-859904774f78', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '414dced9-7bf4-4154-9a85-0628ebbd765d');
INSERT INTO `abs_roleview_perm` VALUES ('9ccb1243-c000-4548-84bb-5bd5cfbae2e7', 'ee3c4602-4520-4890-a532-96f612528117', '63fb0a73-a88b-4448-a3f8-e9b0e61d9f02');
INSERT INTO `abs_roleview_perm` VALUES ('9d97652a-e36f-468e-a128-f840936ce31c', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'd299b374-a6bc-4f27-9840-529f331188e8');
INSERT INTO `abs_roleview_perm` VALUES ('9efe41be-5800-47a1-b8ad-0326a567f7f1', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '507a47f5-c3ff-4dfd-aeeb-f6596ee2d36f');
INSERT INTO `abs_roleview_perm` VALUES ('a07a562d-a35d-4a53-be0e-60da7d52452f', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '07421f4f-4279-4d47-b128-fd0838cda770');
INSERT INTO `abs_roleview_perm` VALUES ('a1ef134a-86a6-4928-9342-ce8c6ef95a43', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '802a2500-1467-4f1a-b89c-367619a457c3');
INSERT INTO `abs_roleview_perm` VALUES ('a59bcb29-b738-4951-a4b6-24f8692d329d', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', 'f270bc8d-1810-4b57-9b37-9c04bf8e386c');
INSERT INTO `abs_roleview_perm` VALUES ('a601d6b9-30e0-40c2-b26c-53ae341be5c2', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'ff21a103-ff77-4bed-8a25-57191bead9aa');
INSERT INTO `abs_roleview_perm` VALUES ('a609a571-e1ea-4c06-a3e7-ba850ec7909c', 'ee3c4602-4520-4890-a532-96f612528117', 'd9c32be7-ea6f-47c7-af8c-b2c88a2e444e');
INSERT INTO `abs_roleview_perm` VALUES ('a63452c3-1cd4-4596-9c56-89d44ae35dc4', 'ee3c4602-4520-4890-a532-96f612528117', 'b2c25e03-16fa-4b5a-9f1b-804fc3400af3');
INSERT INTO `abs_roleview_perm` VALUES ('a695a2f5-58e4-4c0c-be1a-5d22b4d3f02e', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', 'be2eff03-7af0-4d9e-a756-0a607f19ebbb');
INSERT INTO `abs_roleview_perm` VALUES ('a72c086a-0cc7-4e16-b509-026ef0abe05e', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'c7c890e6-b7b0-4719-bcfc-8c6990ea90b6');
INSERT INTO `abs_roleview_perm` VALUES ('a90661d9-c047-4ce2-9935-03a6ac430283', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '57ae4dd1-6c35-4a97-ac94-561feac48324');
INSERT INTO `abs_roleview_perm` VALUES ('a95ed15d-f770-421b-ac05-b29758f406ff', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '2');
INSERT INTO `abs_roleview_perm` VALUES ('a9bacbbc-fc51-4f48-a026-ec0c2eeeb55c', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'a1f849eb-a760-419d-bb7a-eaa835f4b5e5');
INSERT INTO `abs_roleview_perm` VALUES ('ab92bec7-8756-4761-9807-3b8353ad64e1', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '73c3e2fe-f745-492f-9ad6-09ed4b179810');
INSERT INTO `abs_roleview_perm` VALUES ('aed369d4-8331-41e3-b2f7-02994e5d6cc9', 'ee3c4602-4520-4890-a532-96f612528117', 'e3ad6d27-edec-4997-9b32-0a1577d87557');
INSERT INTO `abs_roleview_perm` VALUES ('b123b0d4-11c3-4706-ba63-7df4d8533790', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '3702bf08-a0ad-4b93-9303-35da30d113e6');
INSERT INTO `abs_roleview_perm` VALUES ('b3fd008c-9a71-45d8-b05e-2f6e8a1fe1db', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '74884601-7652-4936-a590-95616bc4ae04');
INSERT INTO `abs_roleview_perm` VALUES ('b4e102f0-2679-4569-aa34-0543dc9569ad', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '43783d35-2d41-44db-9200-41b63577cf6e');
INSERT INTO `abs_roleview_perm` VALUES ('b65f1c88-b49f-4dce-9b0e-b5f74bad20a2', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '9705a9c1-a723-4bc0-8817-1412443db8e1');
INSERT INTO `abs_roleview_perm` VALUES ('baa7ae79-a001-4531-8009-1922940b9650', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '585eef2e-d6fd-4ca2-8e77-cda1b368f706');
INSERT INTO `abs_roleview_perm` VALUES ('bb78add9-ad45-48bb-a549-13dc7211d421', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '11fcdbc0-766a-40ed-a0d5-afb665018f4e');
INSERT INTO `abs_roleview_perm` VALUES ('bc7e1290-df34-43cd-83cc-680027cb6fc9', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'b084da8b-f1ad-4444-b7ab-294a5ef5d5b6');
INSERT INTO `abs_roleview_perm` VALUES ('bd42865e-f7b3-4e71-8551-d50ba5c8f038', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '73c3e2fe-f745-492f-9ad6-09ed4b179810');
INSERT INTO `abs_roleview_perm` VALUES ('bd82dd4f-2dea-4d86-ad1d-5a55b737cc0e', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'ff21a103-ff77-4bed-8a25-57191bead9aa');
INSERT INTO `abs_roleview_perm` VALUES ('be034228-52b5-4317-af1b-31fadbdf2e25', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '4c23322b-55ba-4498-ba25-1c395654b0f6');
INSERT INTO `abs_roleview_perm` VALUES ('c0ca1bc6-8e31-48d5-8a88-1b1e6e3db562', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '6f00137c-ff73-45b1-bd2d-7cb82cf4761a');
INSERT INTO `abs_roleview_perm` VALUES ('c20a2cce-5140-40c4-ae3c-f68162f36110', 'ee3c4602-4520-4890-a532-96f612528117', '58e3975d-4133-4f20-858b-d10665229ed0');
INSERT INTO `abs_roleview_perm` VALUES ('c28c5321-b6df-49b8-8554-d23c57374a1e', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '599301b0-2b15-4129-a8e4-726872cd2113');
INSERT INTO `abs_roleview_perm` VALUES ('c326520c-ab5d-4317-8169-9dc967ea73fe', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'f5366a6b-0a8e-46a6-aec7-d366a63c384a');
INSERT INTO `abs_roleview_perm` VALUES ('c3b513f7-b04b-4d95-94f1-7d728d1ff556', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'fe03e5b4-4db7-4ddb-9f2c-ccda125a7266');
INSERT INTO `abs_roleview_perm` VALUES ('c3c01e9e-d6c4-45ff-a374-8dde58341f96', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '9bd2e322-6e54-479c-ac1b-572b674fb865');
INSERT INTO `abs_roleview_perm` VALUES ('c5d9230b-1497-4b7e-9234-25c609185601', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'b7edd95b-ccb4-4388-ba0c-11d68433320a');
INSERT INTO `abs_roleview_perm` VALUES ('c5e514bc-2df4-40a6-b846-6994a9aef818', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '599301b0-2b15-4129-a8e4-726872cd2113');
INSERT INTO `abs_roleview_perm` VALUES ('c64c5db5-8dd8-4e5d-918a-bc3c1b3a8a34', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '54dd307c-1140-4ccc-ae73-b1b6c1a63c1f');
INSERT INTO `abs_roleview_perm` VALUES ('c7928d1a-5158-4b11-9f4a-a2d267908819', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '90b6acc8-15f9-41be-9633-05392345192c');
INSERT INTO `abs_roleview_perm` VALUES ('c7d61dca-64c2-467f-aa75-6554dabedd0e', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', 'd794c82a-5158-4fc2-baf2-64564df8ec90');
INSERT INTO `abs_roleview_perm` VALUES ('c891d99e-26f6-4316-8129-0783f4eb59ef', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '585eef2e-d6fd-4ca2-8e77-cda1b368f706');
INSERT INTO `abs_roleview_perm` VALUES ('cb308361-03e8-4d6e-ba84-017c9d2eaf5d', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '09ad8ebc-b9b5-4d3a-8282-d828f06781ae');
INSERT INTO `abs_roleview_perm` VALUES ('cb8c5a05-3937-427d-a265-4d2dae6ca2e5', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', 'd9c32be7-ea6f-47c7-af8c-b2c88a2e444e');
INSERT INTO `abs_roleview_perm` VALUES ('cb8e80b4-83ba-4d11-860f-1c407729680a', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '86819e2f-fb9a-4a8d-8e72-05058f67705b');
INSERT INTO `abs_roleview_perm` VALUES ('cbe14c04-f186-434d-a3d3-514fd377e09c', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '0f7f6a71-3f76-4629-b14d-6f0d409b795e');
INSERT INTO `abs_roleview_perm` VALUES ('cc9ea220-a12b-42e9-9172-42d307bfb87f', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '3d1b8995-d1cb-46a4-95a5-c4df7fae52d6');
INSERT INTO `abs_roleview_perm` VALUES ('ccaf424a-bcda-484a-a913-97a627c48173', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', 'd552732f-96f6-431e-b39e-6d004a5056f9');
INSERT INTO `abs_roleview_perm` VALUES ('cfc86429-2092-412e-8a21-3766ee50e022', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '43783d35-2d41-44db-9200-41b63577cf6e');
INSERT INTO `abs_roleview_perm` VALUES ('cff8084b-f46b-4b27-963e-cb6815871efe', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'd8f5c679-793c-47e5-b023-981b62eca61e');
INSERT INTO `abs_roleview_perm` VALUES ('d0bbeb3c-5803-4c35-ab80-b52b76011b39', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '0f7f6a71-3f76-4629-b14d-6f0d409b795e');
INSERT INTO `abs_roleview_perm` VALUES ('d2e41a0e-60ee-4e4c-821e-60bf62a13b1f', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '54797820-931d-4ebc-bd58-f08a1d8df93c');
INSERT INTO `abs_roleview_perm` VALUES ('d3dc9054-7461-44cf-9c82-e2bf1af5d5b5', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'b084da8b-f1ad-4444-b7ab-294a5ef5d5b6');
INSERT INTO `abs_roleview_perm` VALUES ('d451f947-61a5-4c94-b286-cb712dc989d3', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', 'fa5f5a44-a9f4-4adc-adbc-f26a13b9e738');
INSERT INTO `abs_roleview_perm` VALUES ('d514e753-363e-4f05-8bec-798545d0c217', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '4aa8a712-1042-4565-9e69-945e78c40bc5');
INSERT INTO `abs_roleview_perm` VALUES ('d7927fa1-fdef-45ea-b7e5-c3b0e5b032b1', 'ee3c4602-4520-4890-a532-96f612528117', '01b6d5f2-2495-407e-855c-d1d9e5e0a46a');
INSERT INTO `abs_roleview_perm` VALUES ('d850adde-19fc-4ecc-93a2-1a66c8b000c7', 'ee3c4602-4520-4890-a532-96f612528117', 'a2f61dde-6b1c-4514-b5b9-b0e5d88fa87a');
INSERT INTO `abs_roleview_perm` VALUES ('d8e8b1e1-8b57-4fce-b1e3-7441efe82c60', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '1caaedd0-18c7-473d-b07d-6bee90dd8dcf');
INSERT INTO `abs_roleview_perm` VALUES ('d93abdb7-62ec-4864-9260-661731665a7f', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '3347a80b-a7e0-4d3e-bb9a-fdfa949bc8c9');
INSERT INTO `abs_roleview_perm` VALUES ('d9995101-a643-40ca-832f-b89347edddc2', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '9298b975-bb26-44e4-8ca0-cc300fc2ded6');
INSERT INTO `abs_roleview_perm` VALUES ('db56cbf8-19a2-4481-b25d-6a5db4f655e5', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'cfb0236c-6e75-41e8-bc8b-57551f799c40');
INSERT INTO `abs_roleview_perm` VALUES ('dc27565e-d8c7-4f14-afda-1db6f0520238', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '0edffd83-3e3b-449c-9d84-23bd33b48beb');
INSERT INTO `abs_roleview_perm` VALUES ('df1c14cc-5eda-4713-b334-e34eaa7e32db', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', 'be1e9b9a-6bd9-45c6-9eef-2ebef4677991');
INSERT INTO `abs_roleview_perm` VALUES ('dfb28240-b3ce-4c5e-be70-2ae126a7616f', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '4e974c87-7a3f-407f-8da6-b0977083d8f7');
INSERT INTO `abs_roleview_perm` VALUES ('e38cf0e1-1cfd-45d7-ba62-05e7e2c5b255', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '754964ed-9524-4d68-b4c7-629c831f3335');
INSERT INTO `abs_roleview_perm` VALUES ('e5232589-d1bc-46c2-a088-4488a3dcff1f', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '01b6d5f2-2495-407e-855c-d1d9e5e0a46a');
INSERT INTO `abs_roleview_perm` VALUES ('eb43d850-7d3d-4679-9bcd-d54c045ac06b', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'be2d87a2-23a9-41bf-8870-3a3b4c819b58');
INSERT INTO `abs_roleview_perm` VALUES ('ec1e612d-0439-447d-ab7e-4501b06b6af5', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', 'f3ee6784-f77d-4c27-b8bb-24499d7baa1d');
INSERT INTO `abs_roleview_perm` VALUES ('ed041000-7475-44cd-bd42-9cf17b7ac90d', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '7d26ae6a-c09c-446d-872a-2cacf5ac6d64');
INSERT INTO `abs_roleview_perm` VALUES ('edaeb77c-6d63-4d0e-a12e-2b5aefd29e41', 'ee3c4602-4520-4890-a532-96f612528117', '54dd307c-1140-4ccc-ae73-b1b6c1a63c1f');
INSERT INTO `abs_roleview_perm` VALUES ('ee9861b6-d985-4b17-8fe1-1ab296cfe43d', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '5e46d69b-8f14-47f9-9a00-b40b06319280');
INSERT INTO `abs_roleview_perm` VALUES ('ef4a8d0a-8947-4d8f-a77e-00675765eff1', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'c0c5385a-fa87-4031-8a81-bdf9031f83c1');
INSERT INTO `abs_roleview_perm` VALUES ('f0a2f39a-96eb-49ce-8f80-6e855d76f11b', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'e166efbd-fcf8-40bd-a29e-68af79102e74');
INSERT INTO `abs_roleview_perm` VALUES ('f17998e8-f888-48ac-82be-470fa16cb092', 'ee3c4602-4520-4890-a532-96f612528117', '54797820-931d-4ebc-bd58-f08a1d8df93c');
INSERT INTO `abs_roleview_perm` VALUES ('f23792f9-2b4d-4908-b8be-d4d07de1f04f', '90cf1b51-69b1-4b46-bcb1-98fbae08b766', '96df39ce-6c0c-42b8-87b2-7f5b71436a6c');
INSERT INTO `abs_roleview_perm` VALUES ('f4fdd58d-4984-4e44-8df6-cfba572c1e81', 'ee3c4602-4520-4890-a532-96f612528117', '7972359a-fc67-4ebb-a412-b50ac8f12caf');
INSERT INTO `abs_roleview_perm` VALUES ('f700dc31-4b0e-4c9d-93e0-a6bf39841eaf', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'eb5cf0c8-39df-4f97-96d7-5fe26e4b563a');
INSERT INTO `abs_roleview_perm` VALUES ('fc9a7bc5-f938-4f83-9a0c-065ba9ceb028', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'c240c882-16b9-4fbd-bddd-2bbc23507988');
INSERT INTO `abs_roleview_perm` VALUES ('fd18cc3a-722c-4953-b7f5-c7090eb9db04', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', 'e3ad6d27-edec-4997-9b32-0a1577d87557');
INSERT INTO `abs_roleview_perm` VALUES ('fed42dfd-98ce-465c-bde7-737c311a34bb', 'be67952f-32f7-4851-9a88-6e16b461ae7b', '4e974c87-7a3f-407f-8da6-b0977083d8f7');
INSERT INTO `abs_roleview_perm` VALUES ('ffb61fec-5cdc-4fb4-ad45-0e7318cf12ec', 'be67952f-32f7-4851-9a88-6e16b461ae7b', 'fe03e5b4-4db7-4ddb-9f2c-ccda125a7266');

-- ----------------------------
-- Table structure for abs_sysconfig
-- ----------------------------
DROP TABLE IF EXISTS `abs_sysconfig`;
CREATE TABLE `abs_sysconfig`  (
  `rowguid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `userguid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `configname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `configvalue` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of abs_sysconfig
-- ----------------------------
INSERT INTO `abs_sysconfig` VALUES ('c73b8957-69e3-4db2-851a-988682df7642', NULL, 'MROUGUID', 'c7561814-7275-4579-9541-0aae99b5de5a', '2025-01-31 18:58:31', '默认注册用户所属部门标识(商户部门)');
INSERT INTO `abs_sysconfig` VALUES ('5237b41f-28de-4893-85f7-9810317e8289', NULL, 'MRROLE', 'ee3c4602-4520-4890-a532-96f612528117', '2025-01-31 18:59:08', '默认注册用户的角色(商户)');
INSERT INTO `abs_sysconfig` VALUES ('5bd851e2-4a74-4de1-8a92-d09ad97c6bd8', '1', 'AdminRoleGuid', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', '2025-03-26 13:51:13', '判断用户是否为管理员');

-- ----------------------------
-- Table structure for abs_sysupdate
-- ----------------------------
DROP TABLE IF EXISTS `abs_sysupdate`;
CREATE TABLE `abs_sysupdate`  (
  `rowguid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '唯一标识',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新内容',
  `updatetype` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备份类型',
  `bfpath` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备份路径',
  `ishy` tinyint(0) DEFAULT NULL COMMENT '是否还原',
  `addtime` datetime(0) DEFAULT NULL COMMENT '新增日期',
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for abs_taskinfo
-- ----------------------------
DROP TABLE IF EXISTS `abs_taskinfo`;
CREATE TABLE `abs_taskinfo`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `taskname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `taskclass` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  `runstatus` int(0) DEFAULT NULL,
  `cronstr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `restartflag` int(0) DEFAULT NULL,
  `restarttime` datetime(0) DEFAULT NULL
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for abs_userinfo
-- ----------------------------
DROP TABLE IF EXISTS `abs_userinfo`;
CREATE TABLE `abs_userinfo`  (
  `rowguid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `loginname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `realname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `userrole` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `openid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `logourl` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `address` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ouguid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `bussguid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `bussname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  `isdelete` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of abs_userinfo
-- ----------------------------
INSERT INTO `abs_userinfo` VALUES ('1', 'admin', '超级管理员', 'e10adc3949ba59abbe56e057f20f883e', '433d9aef-c1ba-4de0-a482-b751ad5fb79b', NULL, 'http://read8686.oss-cn-beijing.aliyuncs.com/mall/6d19b30442a3494c802a7e58911e04f7.png', NULL, '19949148823', NULL, '7aaab287-eb36-4dcd-8acd-c03d81946384', '6a7fc0f2-cdfe-4243-b063-723dde1634a5', '上海华视青视光技术有限公司', '111', NULL, NULL);
INSERT INTO `abs_userinfo` VALUES ('85e93528-6051-41bd-907f-2826c4b20913', 'superadmin', NULL, 'e10adc3949ba59abbe56e057f20f883e', 'be67952f-32f7-4851-9a88-6e16b461ae7b', NULL, 'http://read8686img.oss-cn-beijing.aliyuncs.com/logo/7741c6c10be34805b9de7d9dd09303e7.jpg', '250242100@qq.com', '19949148823', NULL, '7aaab287-eb36-4dcd-8acd-c03d81946384', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for abs_userzm
-- ----------------------------
DROP TABLE IF EXISTS `abs_userzm`;
CREATE TABLE `abs_userzm`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cliengguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  `userguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for abs_vcode
-- ----------------------------
DROP TABLE IF EXISTS `abs_vcode`;
CREATE TABLE `abs_vcode`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `vcode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `sendstatus` int(0) DEFAULT NULL,
  `expiredtime` bigint(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for abs_viewinfo
-- ----------------------------
DROP TABLE IF EXISTS `abs_viewinfo`;
CREATE TABLE `abs_viewinfo`  (
  `rowguid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `parentguid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `viewname` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `addtime` datetime(0) DEFAULT NULL,
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sortnum` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `parentname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `images` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `isroot` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  INDEX `index_parentguid`(`parentguid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 293 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of abs_viewinfo
-- ----------------------------
INSERT INTO `abs_viewinfo` VALUES ('e166efbd-fcf8-40bd-a29e-68af79102e74', NULL, '系统管理', NULL, '', '99', NULL, NULL, '1', '');
INSERT INTO `abs_viewinfo` VALUES ('f5366a6b-0a8e-46a6-aec7-d366a63c384a', 'e166efbd-fcf8-40bd-a29e-68af79102e74', '用户管理', '2019-07-05 04:16:17', '', '997', '系统管理', NULL, '0', './abssystem/UserList');
INSERT INTO `abs_viewinfo` VALUES ('a1f849eb-a760-419d-bb7a-eaa835f4b5e5', 'e166efbd-fcf8-40bd-a29e-68af79102e74', '模块管理', '2019-06-24 08:21:18', 'viewinfo/viewlist.html', '1', '系统设置', NULL, '0', './abssystem/MoudleList');
INSERT INTO `abs_viewinfo` VALUES ('b7edd95b-ccb4-4388-ba0c-11d68433320a', NULL, '权限管理', '2019-06-23 06:22:18', '', '98', NULL, NULL, '1', '');
INSERT INTO `abs_viewinfo` VALUES ('ad53c709-32c6-4cf7-a175-9c5c2f315319', 'e166efbd-fcf8-40bd-a29e-68af79102e74', '角色管理', '2019-06-24 21:26:04', '', '998', '系统管理', NULL, '0', './abssystem/RoleList');
INSERT INTO `abs_viewinfo` VALUES ('6f00137c-ff73-45b1-bd2d-7cb82cf4761a', 'e166efbd-fcf8-40bd-a29e-68af79102e74', '系统参数', '2019-06-26 12:54:28', 'sysconfig/configlist.html', '999', '系统设置', NULL, '0', './abssystem/SysConfig');
INSERT INTO `abs_viewinfo` VALUES ('473319e3-d747-49f7-8c68-03cc88007caf', 'e166efbd-fcf8-40bd-a29e-68af79102e74', '部门管理', '2019-08-31 03:27:25', '', '999', '系统管理', NULL, '0', './abssystem/OuList');
INSERT INTO `abs_viewinfo` VALUES ('2', 'e166efbd-fcf8-40bd-a29e-68af79102e74', '代码选项', NULL, 'codeitem/ht_codelist.html', '1', '系统设置', NULL, '0', './abssystem/CodeItem');
INSERT INTO `abs_viewinfo` VALUES ('414dced9-7bf4-4154-9a85-0628ebbd765d', NULL, '成绩分析', '2021-11-07 12:59:44', '', '969', NULL, NULL, '1', '');
INSERT INTO `abs_viewinfo` VALUES ('585eef2e-d6fd-4ca2-8e77-cda1b368f706', '414dced9-7bf4-4154-9a85-0628ebbd765d', '班级成绩', '2022-04-08 15:02:56', '', '999', '成绩分析', NULL, '0', './grade/BanJiChengInfo');
INSERT INTO `abs_viewinfo` VALUES ('b084da8b-f1ad-4444-b7ab-294a5ef5d5b6', 'e166efbd-fcf8-40bd-a29e-68af79102e74', '首页组件', '2023-07-07 17:50:15', '', '9', '系统设置', NULL, '0', './abssystem/HomeZJ');
INSERT INTO `abs_viewinfo` VALUES ('9705a9c1-a723-4bc0-8817-1412443db8e1', 'd8f5c679-793c-47e5-b023-981b62eca61e', '考试信息', '2025-01-02 15:30:09', '', '967', '基础数据', NULL, '0', './grade/ExamBaseInfo');
INSERT INTO `abs_viewinfo` VALUES ('96df39ce-6c0c-42b8-87b2-7f5b71436a6c', 'd8f5c679-793c-47e5-b023-981b62eca61e', '学科数据', '2025-01-02 15:47:37', '', '998', '基础数据', NULL, '0', './grade/XKBaseInfo');
INSERT INTO `abs_viewinfo` VALUES ('73c3e2fe-f745-492f-9ad6-09ed4b179810', 'd8f5c679-793c-47e5-b023-981b62eca61e', '班级管理', '2025-01-02 15:50:10', '', '997', '基础数据', NULL, '0', './grade/BanJiList');
INSERT INTO `abs_viewinfo` VALUES ('7972359a-fc67-4ebb-a412-b50ac8f12caf', NULL, '数据对比', '2025-01-02 16:04:32', '', '96', NULL, NULL, '1', '');
INSERT INTO `abs_viewinfo` VALUES ('d299b374-a6bc-4f27-9840-529f331188e8', '7972359a-fc67-4ebb-a412-b50ac8f12caf', '历年成绩', '2025-01-02 16:21:07', '', '99', '数据对比', NULL, '0', './warehouse/InWareRecord');
INSERT INTO `abs_viewinfo` VALUES ('9378c790-a0f2-4387-9414-a40927ee558c', 'd9c32be7-ea6f-47c7-af8c-b2c88a2e444e', '奖励统计', '2025-01-02 16:24:10', '', '93', '教学考核', NULL, '0', './warehouse/WareHouseList');
INSERT INTO `abs_viewinfo` VALUES ('d9c32be7-ea6f-47c7-af8c-b2c88a2e444e', NULL, '教学考核', '2025-01-02 16:35:40', '', '968', NULL, NULL, '1', '');
INSERT INTO `abs_viewinfo` VALUES ('28b80187-5038-4aa9-a75d-45eb3ea17658', NULL, '报告中心', '2025-01-02 16:41:29', '', '95', NULL, NULL, '1', '');
INSERT INTO `abs_viewinfo` VALUES ('79cb3996-1785-44ac-96c6-610166a9ef88', '28b80187-5038-4aa9-a75d-45eb3ea17658', '学生报告', '2025-01-02 16:42:21', '', '99', '报告中心', NULL, '0', './statics/InWareStatics');
INSERT INTO `abs_viewinfo` VALUES ('3347a80b-a7e0-4d3e-bb9a-fdfa949bc8c9', '28b80187-5038-4aa9-a75d-45eb3ea17658', '教师报告', '2025-01-02 16:42:51', '', '98', '报告中心', NULL, '0', './statics/OutWareStatics');
INSERT INTO `abs_viewinfo` VALUES ('11fcdbc0-766a-40ed-a0d5-afb665018f4e', '28b80187-5038-4aa9-a75d-45eb3ea17658', '班级报告', '2025-01-02 16:44:38', '', '97', '报告中心', NULL, '0', './statics/SellStatics');
INSERT INTO `abs_viewinfo` VALUES ('0edffd83-3e3b-449c-9d84-23bd33b48beb', '754964ed-9524-4d68-b4c7-629c831f3335', '新闻编辑', '2025-01-02 17:05:34', '', '96', '网站营销', NULL, '0', './article/AddArtilce');
INSERT INTO `abs_viewinfo` VALUES ('01b6d5f2-2495-407e-855c-d1d9e5e0a46a', '7972359a-fc67-4ebb-a412-b50ac8f12caf', '同期成绩', '2025-01-15 00:24:30', '', '999', '数据对比', NULL, '0', './center/MyHourseInfo');
INSERT INTO `abs_viewinfo` VALUES ('63fb0a73-a88b-4448-a3f8-e9b0e61d9f02', 'd9c32be7-ea6f-47c7-af8c-b2c88a2e444e', '教师考核', '2025-01-15 10:01:45', '', '95', '教学考核', NULL, '0', './grade/TeacherKaoList');
INSERT INTO `abs_viewinfo` VALUES ('5e46d69b-8f14-47f9-9a00-b40b06319280', '414dced9-7bf4-4154-9a85-0628ebbd765d', '学科成绩', '2025-01-15 10:32:52', '', '99', '成绩分析', NULL, '0', './grade/XuKeChengInfo');
INSERT INTO `abs_viewinfo` VALUES ('f282abfe-9456-443f-b99b-83529264b924', '414dced9-7bf4-4154-9a85-0628ebbd765d', '个体成绩', '2025-01-15 10:46:26', '', '97', '成绩分析', NULL, '0', './grade/StudentChengInfo');
INSERT INTO `abs_viewinfo` VALUES ('4c23322b-55ba-4498-ba25-1c395654b0f6', 'd9c32be7-ea6f-47c7-af8c-b2c88a2e444e', '规则设置', '2025-01-15 10:49:57', '', '96', '教学考核', NULL, '0', './grade/GzSetting');
INSERT INTO `abs_viewinfo` VALUES ('54dd307c-1140-4ccc-ae73-b1b6c1a63c1f', '7972359a-fc67-4ebb-a412-b50ac8f12caf', '班级成绩', '2025-01-23 14:57:22', '', '998', '数据对比', NULL, '0', './warehouse/MYHouseList');
INSERT INTO `abs_viewinfo` VALUES ('ff21a103-ff77-4bed-8a25-57191bead9aa', '754964ed-9524-4d68-b4c7-629c831f3335', '首页滚图', '2025-01-30 10:52:36', '', '99', '网站营销', NULL, '0', './abssystem/HomeBanner');
INSERT INTO `abs_viewinfo` VALUES ('07421f4f-4279-4d47-b128-fd0838cda770', '754964ed-9524-4d68-b4c7-629c831f3335', '首页产品', '2025-01-30 15:59:33', '', '98', '网站营销', NULL, '0', './abssystem/HomeProduct');
INSERT INTO `abs_viewinfo` VALUES ('a2f61dde-6b1c-4514-b5b9-b0e5d88fa87a', 'd9c32be7-ea6f-47c7-af8c-b2c88a2e444e', '考核结果', '2025-02-04 19:51:17', '', '94', '教学考核', NULL, '0', './orders/MyOrdersDetail');
INSERT INTO `abs_viewinfo` VALUES ('d8f5c679-793c-47e5-b023-981b62eca61e', NULL, '基础数据', '2025-02-07 17:40:13', '', '989', NULL, NULL, '1', '');
INSERT INTO `abs_viewinfo` VALUES ('74884601-7652-4936-a590-95616bc4ae04', 'd8f5c679-793c-47e5-b023-981b62eca61e', '学生数据', '2025-02-07 17:43:41', '', '99', '基础数据', NULL, '0', './grade/StudentBaseInfo');
INSERT INTO `abs_viewinfo` VALUES ('0f7f6a71-3f76-4629-b14d-6f0d409b795e', 'd8f5c679-793c-47e5-b023-981b62eca61e', '教师数据', '2025-02-11 19:47:06', '', '98', '基础数据', NULL, '0', './grade/TeacherBaseInfo');

-- ----------------------------
-- Table structure for abs_visiterinfo
-- ----------------------------
DROP TABLE IF EXISTS `abs_visiterinfo`;
CREATE TABLE `abs_visiterinfo`  (
  `rowguid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `visiterip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `visittime` datetime(0) DEFAULT NULL,
  `position` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abs_wxappinfo
-- ----------------------------
DROP TABLE IF EXISTS `abs_wxappinfo`;
CREATE TABLE `abs_wxappinfo`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `userguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `appid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `appsecret` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `accesstoken` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `expiretime` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for abs_wxuserstatics
-- ----------------------------
DROP TABLE IF EXISTS `abs_wxuserstatics`;
CREATE TABLE `abs_wxuserstatics`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `refdate` datetime(0) DEFAULT NULL,
  `usersource` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `newuser` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `canceluser` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `cumulateuser` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`rowguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for base_class
-- ----------------------------
DROP TABLE IF EXISTS `base_class`;
CREATE TABLE `base_class`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级ID（主键）',
  `class_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级名称（如：高一(1)班）',
  `grade` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '年级（如：高一，高二）',
  `grade_year` int(0) NOT NULL COMMENT '入学年份（如：2023）',
  `status` tinyint(0) NOT NULL DEFAULT 1 COMMENT '状态（1-在用，0-停用）',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`rowguid`) USING BTREE,
  UNIQUE INDEX `idx_class_name`(`class_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '班级基础信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_class
-- ----------------------------
INSERT INTO `base_class` VALUES ('058bd6dd-fd62-47ff-9d3f-1784325cb98e', '数科22rj-17', '2', 0, 1, NULL, '2026-01-21 16:58:49', '2026-01-21 16:58:49');
INSERT INTO `base_class` VALUES ('c9a5d202-778b-4714-9066-1a9d4018c5a0', '数科22rj-21', '1', 0, 1, '123123', '2026-01-21 17:21:29', '2026-01-21 17:21:29');

-- ----------------------------
-- Table structure for base_subject
-- ----------------------------
DROP TABLE IF EXISTS `base_subject`;
CREATE TABLE `base_subject`  (
  `subject_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学科ID（主键）',
  `subject_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学科名称（如：数学，语文）',
  `score_line_600` int(0) DEFAULT NULL COMMENT '600分线分数',
  `special_control_line` int(0) DEFAULT NULL COMMENT '特控线分数',
  `undergraduate_line` int(0) DEFAULT NULL COMMENT '本科线分数',
  `status` tinyint(0) NOT NULL DEFAULT 1 COMMENT '状态（1-在用，0-停用）',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`subject_id`) USING BTREE,
  UNIQUE INDEX `idx_subject_name`(`subject_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学科基础信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for exam_info
-- ----------------------------
DROP TABLE IF EXISTS `exam_info`;
CREATE TABLE `exam_info`  (
  `exam_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '考试ID（主键）',
  `exam_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '考试名称（如：2023-2024学年上学期期末考试）',
  `exam_type` tinyint(0) NOT NULL COMMENT '考试类型（1-期中，2-期末，3-月考，4-高考，5-其他）',
  `exam_term` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '考试学期（如：2023-2024学年上学期）',
  `exam_time` date NOT NULL COMMENT '考试日期',
  `grade` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '适用年级（如：高一，全体）',
  `status` tinyint(0) NOT NULL DEFAULT 1 COMMENT '状态（1-已结束，0-未开始）',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`exam_id`) USING BTREE,
  INDEX `idx_exam_term`(`exam_term`) USING BTREE,
  INDEX `idx_exam_time`(`exam_time`) USING BTREE,
  INDEX `idx_grade`(`grade`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '考试基础信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for student_info
-- ----------------------------
DROP TABLE IF EXISTS `student_info`;
CREATE TABLE `student_info`  (
  `rowguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学生ID（主键）',
  `student_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号（如：2023001）',
  `student_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学生姓名',
  `classguid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级ID',
  `gender` tinyint(0) DEFAULT NULL COMMENT '性别（1-男，2-女）',
  `status` tinyint(0) NOT NULL DEFAULT 1 COMMENT '状态（1-在读，0-毕业/离校）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`rowguid`) USING BTREE,
  UNIQUE INDEX `idx_student_no`(`student_no`) USING BTREE,
  INDEX `idx_class_id`(`classguid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生基本信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student_info
-- ----------------------------
INSERT INTO `student_info` VALUES ('1a5d9c23ac2243eeb55b5456935053ca', '00230123', '1', '058bd6dd-fd62-47ff-9d3f-1784325cb98e', 2, 1, '111', '2026-01-22 00:36:08', '2026-01-22 00:36:08');
INSERT INTO `student_info` VALUES ('7a17ce72f9f349b897da3182fab6566d', '002301231', '1', 'c9a5d202-778b-4714-9066-1a9d4018c5a0', 2, 0, '222', '2026-01-22 00:37:00', '2026-01-22 00:37:00');

-- ----------------------------
-- Table structure for student_score
-- ----------------------------
DROP TABLE IF EXISTS `student_score`;
CREATE TABLE `student_score`  (
  `score_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '成绩记录ID（主键）',
  `exam_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '考试ID',
  `student_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学生ID',
  `subject_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学科ID',
  `score` decimal(5, 1) NOT NULL COMMENT '考试分数',
  `rank_in_class` int(0) DEFAULT NULL COMMENT '班级内排名',
  `rank_in_grade` int(0) DEFAULT NULL COMMENT '年级内排名',
  `is_improve` tinyint(0) DEFAULT NULL COMMENT '是否进步（1-进步，0-退步，2-持平）',
  `improve_range` int(0) DEFAULT NULL COMMENT '进步/退步幅度',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`score_id`) USING BTREE,
  UNIQUE INDEX `idx_exam_student_subject`(`exam_id`, `student_id`, `subject_id`) USING BTREE,
  INDEX `idx_exam_id`(`exam_id`) USING BTREE,
  INDEX `idx_student_id`(`student_id`) USING BTREE,
  INDEX `idx_subject_id`(`subject_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生考试成绩表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for teacher_info
-- ----------------------------
DROP TABLE IF EXISTS `teacher_info`;
CREATE TABLE `teacher_info`  (
  `teacher_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '教师ID（主键）',
  `teacher_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '教师编号（如：T001）',
  `teacher_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '教师姓名',
  `subject_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所教学科ID',
  `class_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所教班级ID（多个班级用逗号分隔）',
  `gender` tinyint(0) DEFAULT NULL COMMENT '性别（1-男，2-女）',
  `status` tinyint(0) NOT NULL DEFAULT 1 COMMENT '状态（1-在职，0-离职）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`teacher_id`) USING BTREE,
  UNIQUE INDEX `idx_teacher_no`(`teacher_no`) USING BTREE,
  INDEX `idx_subject_id`(`subject_id`) USING BTREE,
  INDEX `idx_class_id`(`class_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '教师基本信息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
