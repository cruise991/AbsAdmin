package com.abs.system.api;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.domain.BaseClass;
import com.abs.system.util.Params;

@Service
public interface IBaseClassService {

    /**
     * 获取班级列表（支持分页和筛选）
     * @param params 包含分页参数和筛选条件
     * @return 班级列表
     */
    public List<Map<String, Object>> getClassList(Params params);

    /**
     * 获取班级总数（用于分页）
     * @param params 筛选条件
     * @return 班级总数
     */
    public long getClassCount(Params params);

    /**
     * 根据班级ID获取班级信息
     * @param rowguid 班级ID
     * @return 班级信息
     */
    public BaseClass getClassByGuid(String rowguid);

    /**
     * 根据班级ID获取班级信息（Map格式）
     * @param rowguid 班级ID
     * @return 班级信息Map
     */
    public Map<String, Object> getClassMapByGuid(String rowguid);

    /**
     * 添加班级信息
     * @param baseClass 班级信息
     * @return 是否添加成功
     */
    public boolean addClass(BaseClass baseClass);

    /**
     * 更新班级信息
     * @param baseClass 班级信息
     * @return 是否更新成功
     */
    public boolean updateClass(BaseClass baseClass);

    /**
     * 删除班级信息
     * @param rowguid 班级ID
     * @return 是否删除成功
     */
    public boolean deleteClass(String rowguid);

    /**
     * 检查班级名称是否已存在
     * @param className 班级名称
     * @return 是否存在
     */
    public boolean isClassNameExist(String className);

    /**
     * 检查班级名称是否已存在（排除指定班级ID）
     * @param className 班级名称
     * @param rowguid 班级ID
     * @return 是否存在
     */
    public boolean isClassNameExist(String className, String rowguid);

    /**
     * 获取所有班级列表（用于下拉选择）
     * @return 班级列表
     */
    public List<Map<String, Object>> getAllClassList();

    /**
     * 导出班级列表
     * @param params 筛选条件
     * @return 班级列表
     */
    public List<Map<String, Object>> exportClassList(Params params);

    /**
     * 批量删除班级信息
     * @param rowguids 班级ID列表
     * @return 是否删除成功
     */
    public boolean batchDeleteClass(List<String> rowguids);
}