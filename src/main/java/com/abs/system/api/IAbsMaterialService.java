package com.abs.system.api;

import com.abs.system.domain.AbsMaterialInfo;

import java.util.List;
import java.util.Map;

public interface IAbsMaterialService {

    /**
     * 添加素材
     */
    void addMaterial(AbsMaterialInfo material);

    /**
     * 更新素材
     */
    void updateMaterial(AbsMaterialInfo material);

    /**
     * 删除素材
     */
    void deleteMaterial(String rowguid);

    /**
     * 根据ID查询素材
     */
    AbsMaterialInfo queryMaterialById(String rowguid);

    /**
     * 分页查询素材列表
     */
    List<Map<String, Object>> queryMaterialList(Map<String, Object> reqMap);

    /**
     * 查询素材总数
     */
    long queryMaterialCount(Map<String, Object> reqMap);

    /**
     * 上传文件
     */
    Map<String, Object> uploadFile(String fileType, String base64File, String fileName);

    /**
     * 下载文件
     */
    byte[] downloadFile(String filePath);

    /**
     * 根据类型查询素材
     */
    List<Map<String, Object>> queryMaterialByType(String type, Map<String, Object> reqMap);
}
