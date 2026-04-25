package com.abs.system.api;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abs.system.domain.StudentInfo;
import com.abs.system.util.Params;

@Service
public interface IStudentInfoService {

    /**
     * 获取学生列表（支持分页和筛选）
     * @param params 包含分页参数和筛选条件
     * @return 学生列表
     */
    public List<Map<String, Object>> getStudentList(Params params);

    /**
     * 获取学生总数（用于分页）
     * @param params 筛选条件
     * @return 学生总数
     */
    public long getStudentCount(Params params);

    /**
     * 根据学生ID获取学生信息
     * @param rowguid 学生ID
     * @return 学生信息
     */
    public StudentInfo getStudentByGuid(String rowguid);

    /**
     * 根据学生ID获取学生信息（Map格式）
     * @param rowguid 学生ID
     * @return 学生信息Map
     */
    public Map<String, Object> getStudentMapByGuid(String rowguid);

    /**
     * 添加学生信息
     * @param studentInfo 学生信息
     * @return 是否添加成功
     */
    public boolean addStudent(StudentInfo studentInfo);

    /**
     * 更新学生信息
     * @param studentInfo 学生信息
     * @return 是否更新成功
     */
    public boolean updateStudent(StudentInfo studentInfo);

    /**
     * 删除学生信息
     * @param rowguid 学生ID
     * @return 是否删除成功
     */
    public boolean deleteStudent(String rowguid);

    /**
     * 批量删除学生信息
     * @param rowguids 学生ID列表
     * @return 是否删除成功
     */
    public boolean batchDeleteStudent(List<String> rowguids);

    /**
     * 检查学号是否已存在
     * @param studentNo 学号
     * @return 是否存在
     */
    public boolean isStudentNoExist(String studentNo);

    /**
     * 检查学号是否已存在（排除指定学生ID）
     * @param studentNo 学号
     * @param rowguid 学生ID
     * @return 是否存在
     */
    public boolean isStudentNoExist(String studentNo, String rowguid);

    /**
     * 导入学生信息
     * @param file 上传的Excel文件
     * @return 导入结果
     */
    public Map<String, Object> importStudent(MultipartFile file);

    /**
     * 导出学生信息
     * @param params 筛选条件
     * @return 学生列表
     */
    public List<Map<String, Object>> exportStudentList(Params params);

    /**
     * 下载导入模板
     * @return 模板数据
     */
    public List<Map<String, Object>> getImportTemplate();
}