package com.abs.system.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.abs.system.api.IAbsFileInfo;
import com.abs.system.api.IAbsMaterialService;
import com.abs.system.api.IAbsSysConfig;
import com.abs.system.domain.AbsMaterialInfo;
import com.abs.system.impl.AbsMaterialServiceImpl;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;

@RestController
@RequestMapping("/api/material")
public class AbsMaterialController {

    private static final Logger logger = LoggerFactory.getLogger(AbsMaterialController.class);
    private IAbsMaterialService materialService = new AbsMaterialServiceImpl();
    
    @Autowired
    private IAbsFileInfo fileService;
    
    @Autowired
    private IAbsSysConfig configService;

    /**
     * 通用响应结构
     */
    private Map<String, Object> createResponse(String code, Object data, String message) {
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("code", code);
        response.put("data", data);
        response.put("message", message);
        return response;
    }

    /**
     * 添加素材
     */
    @PostMapping("/add")
    @ResponseBody
    public String addMaterial(@RequestBody AbsMaterialInfo material) {
        logger.info("接收添加素材请求: {}", material.getName());
        try {
            material.setRowguid(UUID.randomUUID().toString());
            material.setStatus("正常");
            materialService.addMaterial(material);
            logger.info("添加素材成功: {}", material.getName());
            return JSON.toJSONString(createResponse(MSG.SUCCESSCODE, null, MSG.AddSuccess));
        } catch (Exception e) {
            logger.error("添加素材失败: {}", e.getMessage(), e);
            return JSON.toJSONString(createResponse(MSG.FAILCODE, null, "添加失败: " + e.getMessage()));
        }
    }

    /**
     * 更新素材
     */
    @PostMapping("/update")
    @ResponseBody
    public String updateMaterial(@RequestBody AbsMaterialInfo material) {
        logger.info("接收更新素材请求: {}", material.getName());
        try {
            materialService.updateMaterial(material);
            logger.info("更新素材成功: {}", material.getName());
            return JSON.toJSONString(createResponse(MSG.SUCCESSCODE, null, MSG.updateSuccess));
        } catch (Exception e) {
            logger.error("更新素材失败: {}", e.getMessage(), e);
            return JSON.toJSONString(createResponse(MSG.FAILCODE, null, "更新失败: " + e.getMessage()));
        }
    }

    /**
     * 删除素材
     */
    @PostMapping("/delete")
    @ResponseBody
    public String deleteMaterial(@RequestBody Map<String, Object> reqMap) {
        String rowguid = reqMap.get("rowguid") != null ? reqMap.get("rowguid").toString() : "";
        logger.info("接收删除素材请求: rowguid={}", rowguid);
        try {
            // 从abs_fileinfo表中查询文件信息
            AbsDbService dbService = AbsDbHelper.getDbService();
            Params params = new Params();
            params.put("rowguid", rowguid);
            String sql = "select fileurl, firstkey, istoali from abs_fileinfo where rowguid={rowguid}";
            Map<String, Object> fileInfo = dbService.queryMapBySql(sql, params);
            
            if (fileInfo != null) {
                // 检查是否上传到了阿里云
                if (MSG.ToAliYes.equals(fileInfo.get("istoali"))) {
                    // 获取OSS配置
                    String endpoint = configService.getByConfigValueByName("endpoint", null);
                    String accessKeyId = configService.getByConfigValueByName("accessKeyId", null);
                    String accessKeySecret = configService.getByConfigValueByName("accessKeySecret", null);
                    String bucketName = "read8686img";
                    
                    if (endpoint != null && accessKeyId != null && accessKeySecret != null) {
                        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
                        try {
                            // 从firstkey或fileurl中提取objectKey
                            String objectKey = null;
                            if (fileInfo.get("firstkey") != null) {
                                objectKey = fileInfo.get("firstkey").toString();
                            } else if (fileInfo.get("fileurl") != null) {
                                String fileurl = fileInfo.get("fileurl").toString();
                                if (fileurl.contains("://")) {
                                    int hostEndIndex = fileurl.indexOf("/", fileurl.indexOf("://") + 3);
                                    if (hostEndIndex != -1) {
                                        objectKey = fileurl.substring(hostEndIndex + 1);
                                    }
                                }
                            }
                            
                            // 删除阿里云OSS中的文件
                            if (objectKey != null) {
                                ossClient.deleteObject(bucketName, objectKey);
                                logger.info("删除阿里云OSS文件成功: {}", objectKey);
                            }
                        } catch (Exception e) {
                            logger.error("删除阿里云OSS文件失败: {}", e.getMessage(), e);
                            // 继续执行，即使OSS删除失败也要删除数据库记录
                        } finally {
                            ossClient.shutdown();
                        }
                    }
                }
                
                // 从abs_fileinfo表中删除记录
                String deleteSql = "delete from abs_fileinfo where rowguid={rowguid}";
                dbService.execteSql(deleteSql, params);
                logger.info("删除素材成功: rowguid={}", rowguid);
            } else {
                logger.warn("未找到素材记录: rowguid={}", rowguid);
            }
            
            return JSON.toJSONString(createResponse(MSG.SUCCESSCODE, null, MSG.DeleteSuccess));
        } catch (Exception e) {
            logger.error("删除素材失败: {}", e.getMessage(), e);
            return JSON.toJSONString(createResponse(MSG.FAILCODE, null, "删除失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID查询素材
     */
    @GetMapping("/queryById")
    @ResponseBody
    public String queryMaterialById(@RequestParam String rowguid) {
        logger.info("接收查询素材请求: rowguid={}", rowguid);
        try {
            AbsMaterialInfo material = materialService.queryMaterialById(rowguid);
            logger.info("查询素材成功: rowguid={}", rowguid);
            return JSON.toJSONString(createResponse(MSG.SUCCESSCODE, material, "查询成功"));
        } catch (Exception e) {
            logger.error("查询素材失败: {}", e.getMessage(), e);
            return JSON.toJSONString(createResponse(MSG.FAILCODE, null, "查询失败: " + e.getMessage()));
        }
    }

    /**
     * 分页查询素材列表
     */
    @PostMapping("/queryList")
    @ResponseBody
    public String queryMaterialList(@RequestBody Map<String, Object> reqMap) {
        logger.info("接收查询素材列表请求");
        try {
            List<Map<String, Object>> list = materialService.queryMaterialList(reqMap);
            long count = materialService.queryMaterialCount(reqMap);
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("list", list);
            result.put("count", count);
            logger.info("查询素材列表成功，共{}条记录", count);
            return JSON.toJSONString(createResponse(MSG.SUCCESSCODE, result, "查询成功"));
        } catch (Exception e) {
            logger.error("查询素材列表失败: {}", e.getMessage(), e);
            return JSON.toJSONString(createResponse(MSG.FAILCODE, null, "查询失败: " + e.getMessage()));
        }
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @ResponseBody
    public String uploadFile(@RequestParam String fileType, @RequestParam String base64File, @RequestParam String fileName) {
        logger.info("接收文件上传请求: {}, 类型: {}", fileName, fileType);
        try {
            Map<String, Object> result = materialService.uploadFile(fileType, base64File, fileName);
            logger.info("文件上传成功: {}", fileName);
            return JSON.toJSONString(createResponse(MSG.SUCCESSCODE, result, "上传成功"));
        } catch (Exception e) {
            logger.error("文件上传失败: {}", e.getMessage(), e);
            return JSON.toJSONString(createResponse(MSG.FAILCODE, null, "上传失败: " + e.getMessage()));
        }
    }

    /**
     * 下载文件
     */
    @GetMapping("/download")
    @ResponseBody
    public byte[] downloadFile(@RequestParam String filePath) {
        logger.info("接收文件下载请求: {}", filePath);
        try {
            byte[] fileBytes = materialService.downloadFile(filePath);
            logger.info("文件下载成功: {}, 大小: {} B", filePath, fileBytes.length);
            return fileBytes;
        } catch (Exception e) {
            logger.error("文件下载失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 根据类型查询素材
     * @param string 
     */
    @PostMapping("/queryByType")
    @ResponseBody
    public String queryMaterialByType( @RequestBody Map<String, Object> reqMap) {
        String type = reqMap.get("type") != null ? reqMap.get("type").toString() : "";
        logger.info("接收按类型查询素材请求: type={}", type);
        try {
            // 使用fileService查询文件数据
            AbsDbService dbService = AbsDbHelper.getDbService();
            Params params = new Params(reqMap);
            
            // 根据类型构建不同的查询条件
            String fileTypeCondition = "";
            switch (type) {
                case "image":
                    fileTypeCondition = "and filetype in ('.png','.jpg','.jpeg','.gif','.svg')";
                    break;
                case "music":
                    fileTypeCondition = "and filetype in ('.mp3','.wav','.flac','.ogg')";
                    break;
                case "video":
                    fileTypeCondition = "and filetype in ('.mp4','.avi','.mov','.wmv')";
                    break;
                case "word":
                    fileTypeCondition = "and filetype in ('.txt','.doc','.docx','.pdf')";
                    break;
                default:
                    fileTypeCondition = "";
            }
            
            // 构建SQL查询
            String sql = "select rowguid, fileurl, addtime as upload_time, filename as name, filetype as type, filesize as size, remark as tags from abs_fileinfo where 1=1 " + fileTypeCondition + " order by addtime desc";
            List<Map<String, Object>> list = dbService.queryListForMapBySql(sql, params);
            
            // 处理数据格式，确保与前端期望的格式一致
            if (list != null && !list.isEmpty()) {
                // 获取OSS配置
                String endpoint = configService.getByConfigValueByName("endpoint", null);
                String accessKeyId = configService.getByConfigValueByName("accessKeyId", null);
                String accessKeySecret = configService.getByConfigValueByName("accessKeySecret", null);
                String bucketName = "read8686img";
                
                OSS ossClient = null;
                if (endpoint != null && accessKeyId != null && accessKeySecret != null) {
                    ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
                }
                
                try {
                    for (Map<String, Object> item : list) {
                        // 格式化文件大小
                        if (item.get("size") != null) {
                            long filesize = Long.parseLong(item.get("size").toString());
                            item.put("size", formatFileSize(filesize));
                        }
                        // 格式化上传时间
                        if (item.get("upload_time") != null) {
                            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            LocalDateTime date=(LocalDateTime) item.get("upload_time");
                            item.put("upload_time", date.format(sdf));
                        }
                        // 添加状态字段
                        item.put("status", "正常");
                        // 处理标签
                        if (item.get("tags") != null) {
                            item.put("tags", item.get("tags").toString().split(","));
                        }
                        // 为私有OSS文件生成预签名URL
                        if (ossClient != null && item.get("fileurl") != null) {
                            String originalFileUrl = item.get("fileurl").toString();
                            // 提取objectKey
                            String objectKey = originalFileUrl;
                            if (objectKey.contains("://")) {
                                // 从URL中提取objectKey，包括完整路径
                                int hostEndIndex = objectKey.indexOf("/", objectKey.indexOf("://") + 3);
                                if (hostEndIndex != -1) {
                                    objectKey = objectKey.substring(hostEndIndex + 1);
                                }
                            }
                            // 生成预签名URL，有效期15分钟
                            Date expiration = new Date(System.currentTimeMillis() + 15 * 60 * 1000);
                            
                            // 为原文件生成预签名URL
                            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectKey);
                            request.setExpiration(expiration);
                            URL signedUrl = ossClient.generatePresignedUrl(request);
                            item.put("fileurl", signedUrl.toString());
                            
                            // 为缩略图生成预签名URL
                            GeneratePresignedUrlRequest thumbnailRequest = new GeneratePresignedUrlRequest(bucketName, objectKey);
                            thumbnailRequest.setExpiration(expiration);
                            // 添加缩略图处理参数
                            String fileType = item.get("type") != null ? item.get("type").toString() : "";
                            // 根据原始文件URL的扩展名判断文件类型
                            String fileExt = "";
                            if (originalFileUrl.contains(".")) {
                                fileExt = originalFileUrl.substring(originalFileUrl.lastIndexOf(".") + 1).toLowerCase();
                                // 移除URL参数部分
                                if (fileExt.contains("?")) {
                                    fileExt = fileExt.substring(0, fileExt.indexOf("?"));
                                }
                            }
                            
                            // 图片文件
                            if (fileType.equals("image") || fileType.equals("jpg") || fileType.equals("png") || fileType.equals("gif") || fileType.equals("svg") ||
                                fileExt.equals("jpg") || fileExt.equals("jpeg") || fileExt.equals("png") || fileExt.equals("gif") || fileExt.equals("svg")) {
                                // 图片缩略图
                                thumbnailRequest.addQueryParameter("x-oss-process", "image/resize,w_200");
                            } 
                            // 视频文件
                            else if (fileType.equals("video") || fileType.equals("mp4") || fileType.equals("mov") || fileType.equals("avi") || fileType.equals("wmv") ||
                                     fileExt.equals("mp4") || fileExt.equals("mov") || fileExt.equals("avi") || fileExt.equals("wmv") || fileExt.equals("flv")) {
                                // 视频缩略图（截取第一帧）
                                thumbnailRequest.addQueryParameter("x-oss-process", "video/snapshot,t_1000,f_jpg,w_200,h_150,m_fast");
                            }
                            // 其他文件类型
                            else {
                                // 默认使用图片处理参数，或者不添加处理参数
                                thumbnailRequest.addQueryParameter("x-oss-process", "image/resize,w_200");
                            }
                            URL thumbnailSignedUrl = ossClient.generatePresignedUrl(thumbnailRequest);
                            item.put("thumbnailUrl", thumbnailSignedUrl.toString());
                        }
                    }
                } catch (Exception e) {
                    logger.error("生成预签名URL失败: {}", e.getMessage(), e);
                } finally {
                    if (ossClient != null) {
                        ossClient.shutdown();
                    }
                }
            }
            
            logger.info("按类型查询素材成功，共{}条记录", list != null ? list.size() : 0);
            return JSON.toJSONString(createResponse(MSG.SUCCESSCODE, list, "查询成功"));
        } catch (Exception e) {
            logger.error("按类型查询素材失败: {}", e.getMessage(), e);
            return JSON.toJSONString(createResponse(MSG.FAILCODE, null, "查询失败: " + e.getMessage()));
        }
    }
    
    /**
     * 格式化文件大小
     */
    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return (size / 1024) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", (double) size / (1024 * 1024));
        } else {
            return String.format("%.1f GB", (double) size / (1024 * 1024 * 1024));
        }
    }

    /**
     * 图片素材列表
     */
    @PostMapping("/image/list")
    @ResponseBody
    public String queryImageList(@RequestBody Map<String, Object> reqMap) {
        return queryMaterialByType(reqMap);
    }

    /**
     * 音乐素材列表
     */
    @PostMapping("/music/list")
    @ResponseBody
    public String queryMusicList(@RequestBody Map<String, Object> reqMap) {
        return queryMaterialByType(reqMap);
    }

    /**
     * 视频素材列表
     */
    @PostMapping("/video/list")
    @ResponseBody
    public String queryVideoList(@RequestBody Map<String, Object> reqMap) {
        return queryMaterialByType(reqMap);
    }

    /**
     * 文字素材列表
     */
    @PostMapping("/word/list")
    @ResponseBody
    public String queryWordList(@RequestBody Map<String, Object> reqMap) {
        return queryMaterialByType(reqMap);
    }
}
