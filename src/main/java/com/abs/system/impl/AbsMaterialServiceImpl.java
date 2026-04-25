package com.abs.system.impl;

import com.abs.system.api.IAbsMaterialService;
import com.abs.system.domain.AbsMaterialInfo;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;
import com.abs.system.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AbsMaterialServiceImpl implements IAbsMaterialService {

    private static final Logger logger = LoggerFactory.getLogger(AbsMaterialServiceImpl.class);
    private static final String UPLOAD_DIR = "D:/upload/materials/";

    @Override
    public void addMaterial(AbsMaterialInfo material) {
        logger.info("开始添加素材: {}", material.getName());
        try {
            AbsDbService dbService = AbsDbHelper.getDbService();
            dbService.addEntity(material);
            logger.info("素材添加成功: {}", material.getName());
        } catch (Exception e) {
            logger.error("素材添加失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void updateMaterial(AbsMaterialInfo material) {
        logger.info("开始更新素材: {}", material.getName());
        try {
            AbsDbService dbService = AbsDbHelper.getDbService();
            Params params = Params.getRecordByObject(material);
            String sql = "update abs_material_info set name={name}, type={type}, size={size}, category={category}, duration={duration}, word_count={wordCount}, tags={tags}, content={content}, file_path={filePath}, upload_time={uploadTime}, status={status}, create_user={createUser} where rowguid={rowguid}";
            dbService.execteSql(sql, params);
            logger.info("素材更新成功: {}", material.getName());
        } catch (Exception e) {
            logger.error("素材更新失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteMaterial(String rowguid) {
        logger.info("开始删除素材: rowguid={}", rowguid);
        try {
            AbsDbService dbService = AbsDbHelper.getDbService();
            Params params = new Params();
            params.put("rowguid", rowguid);
            String sql = "delete from abs_material_info where rowguid={rowguid}";
            dbService.execteSql(sql, params);
            logger.info("素材删除成功: rowguid={}", rowguid);
        } catch (Exception e) {
            logger.error("素材删除失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public AbsMaterialInfo queryMaterialById(String rowguid) {
        logger.info("开始查询素材: rowguid={}", rowguid);
        try {
            AbsDbService dbService = AbsDbHelper.getDbService();
            Params params = new Params();
            params.put("rowguid", rowguid);
            String sql = "select * from abs_material_info where rowguid={rowguid}";
            Map<String, Object> result = dbService.queryMapBySql(sql, params);
            AbsMaterialInfo material = BeanUtil.mapToBean(result, AbsMaterialInfo.class);
            logger.info("素材查询成功: rowguid={}, name={}", rowguid, material != null ? material.getName() : "未找到");
            return material;
        } catch (Exception e) {
            logger.error("素材查询失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> queryMaterialList(Map<String, Object> reqMap) {
        logger.info("开始查询素材列表");
        try {
            AbsDbService dbService = AbsDbHelper.getDbService();
            Params params = new Params(reqMap);
            List<Map<String, Object>> result = dbService.queryPageListMapBySql("AbsMaterialInfo.queryMaterialList", params);
            logger.info("素材列表查询成功，共{}条记录", result != null ? result.size() : 0);
            return result;
        } catch (Exception e) {
            logger.error("素材列表查询失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public long queryMaterialCount(Map<String, Object> reqMap) {
        logger.info("开始查询素材总数");
        try {
            AbsDbService dbService = AbsDbHelper.getDbService();
            Params params = new Params(reqMap);
            long count = dbService.queryCountBySql("AbsMaterialInfo.queryMaterialCount", params);
            logger.info("素材总数查询成功，共{}条记录", count);
            return count;
        } catch (Exception e) {
            logger.error("素材总数查询失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Map<String, Object> uploadFile(String fileType, String base64File, String fileName) {
        logger.info("开始上传文件: {}, 类型: {}", fileName, fileType);
        try {
            // 创建上传目录
            File uploadDir = new File(UPLOAD_DIR + fileType + "/");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
                logger.info("创建上传目录: {}", uploadDir.getPath());
            }

            // 生成唯一文件名
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            String filePath = UPLOAD_DIR + fileType + "/" + uniqueFileName;

            // 解码Base64文件并保存
            byte[] fileBytes = Base64.getDecoder().decode(base64File);
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(fileBytes);
            fos.close();

            // 计算文件大小
            File file = new File(filePath);
            long fileSize = file.length();
            String sizeStr = formatFileSize(fileSize);

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("filePath", filePath.replace(UPLOAD_DIR, ""));
            result.put("fileName", fileName);
            result.put("size", sizeStr);
            result.put("fileSize", fileSize);
            
            logger.info("文件上传成功: {}, 大小: {}", fileName, sizeStr);
            return result;

        } catch (Exception e) {
            logger.error("文件上传失败: {}", e.getMessage(), e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(String filePath) {
        logger.info("开始下载文件: {}", filePath);
        try {
            Path path = Paths.get(UPLOAD_DIR + filePath);
            byte[] fileBytes = Files.readAllBytes(path);
            logger.info("文件下载成功: {}, 大小: {} B", filePath, fileBytes.length);
            return fileBytes;
        } catch (IOException e) {
            logger.error("文件下载失败: {}", e.getMessage(), e);
            throw new RuntimeException("文件下载失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> queryMaterialByType(String type, Map<String, Object> reqMap) {
        logger.info("开始按类型查询素材: type={}", type);
        try {
            AbsDbService dbService = AbsDbHelper.getDbService();
            Params params = new Params(reqMap);
            params.put("type", type);
            List<Map<String, Object>> result = dbService.queryPageListMapBySql("AbsMaterialInfo.queryMaterialByType", params);
            logger.info("按类型查询素材成功，共{}条记录", result != null ? result.size() : 0);
            return result;
        } catch (Exception e) {
            logger.error("按类型查询素材失败: {}", e.getMessage(), e);
            throw e;
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
}
