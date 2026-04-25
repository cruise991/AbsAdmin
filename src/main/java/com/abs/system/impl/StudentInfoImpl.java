package com.abs.system.impl;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.abs.system.api.IStudentInfoService;
import com.abs.system.domain.StudentInfo;
import com.abs.system.mapper.StudentInfoMapper;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

@Service
public class StudentInfoImpl implements IStudentInfoService {

    @Autowired
    private StudentInfoMapper studentInfoMapper;

    @Override
    public List<Map<String, Object>> getStudentList(Params params) {
        // 构建查询条件
        QueryWrapper<StudentInfo> queryWrapper = new QueryWrapper<>();
        
        String studentName = params.getString("studentName");
        if (StrUtil.isNotBlank(studentName)) {
            queryWrapper.like("student_name", studentName);
        }
        
        String studentNo = params.getString("studentNo");
        if (StrUtil.isNotBlank(studentNo)) {
            queryWrapper.like("student_no", studentNo);
        }
        
        String classguid = params.getString("classguid");
        if (StrUtil.isNotBlank(classguid)) {
            queryWrapper.eq("classguid", classguid);
        }
        
        Integer gender = params.getInteger("gender");
        if (gender != null) {
            queryWrapper.eq("gender", gender);
        }
        
        Integer status = params.getInteger("status");
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        // 排序
        queryWrapper.orderByDesc("create_time");
        
        // 分页
        int pageNum = params.getInteger("page") == null ? 1 : params.getInteger("page");
        int pageSize = params.getInteger("limit") == null ? 10 : params.getInteger("limit");
        Page<StudentInfo> page = new Page<>(pageNum, pageSize);
        IPage<StudentInfo> resultPage = studentInfoMapper.selectPage(page, queryWrapper);
        
        // 转换结果
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (StudentInfo studentInfo : resultPage.getRecords()) {
            Map<String, Object> map = new HashMap<>();
            map.put("rowguid", studentInfo.getRowguid());
            map.put("studentNo", studentInfo.getStudentNo());
            map.put("studentName", studentInfo.getStudentName());
            map.put("classguid", studentInfo.getClassguid());
            map.put("gender", studentInfo.getGender());
            map.put("status", studentInfo.getStatus());
            map.put("remark", studentInfo.getRemark());
            map.put("createTime", studentInfo.getCreateTime());
            map.put("updateTime", studentInfo.getUpdateTime());
            resultList.add(map);
        }
        
        return resultList;
    }

    @Override
    public long getStudentCount(Params params) {
        // 构建查询条件
        QueryWrapper<StudentInfo> queryWrapper = new QueryWrapper<>();
        
        String studentName = params.getString("studentName");
        if (StrUtil.isNotBlank(studentName)) {
            queryWrapper.like("student_name", studentName);
        }
        
        String studentNo = params.getString("studentNo");
        if (StrUtil.isNotBlank(studentNo)) {
            queryWrapper.like("student_no", studentNo);
        }
        
        String classguid = params.getString("classguid");
        if (StrUtil.isNotBlank(classguid)) {
            queryWrapper.eq("classguid", classguid);
        }
        
        Integer gender = params.getInteger("gender");
        if (gender != null) {
            queryWrapper.eq("gender", gender);
        }
        
        Integer status = params.getInteger("status");
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        return studentInfoMapper.selectCount(queryWrapper);
    }

    @Override
    public StudentInfo getStudentByGuid(String rowguid) {
        QueryWrapper<StudentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rowguid", rowguid);
        return studentInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public Map<String, Object> getStudentMapByGuid(String rowguid) {
        StudentInfo studentInfo = getStudentByGuid(rowguid);
        if (studentInfo == null) {
            return null;
        }
        
        Map<String, Object> map = new HashMap<>();
        map.put("rowguid", studentInfo.getRowguid());
        map.put("studentNo", studentInfo.getStudentNo());
        map.put("studentName", studentInfo.getStudentName());
        map.put("classguid", studentInfo.getClassguid());
        map.put("gender", studentInfo.getGender());
        map.put("status", studentInfo.getStatus());
        map.put("remark", studentInfo.getRemark());
        map.put("createTime", studentInfo.getCreateTime());
        map.put("updateTime", studentInfo.getUpdateTime());
        
        return map;
    }

    @Override
    public boolean addStudent(StudentInfo studentInfo) {
        try {
            // 生成rowguid
            if (StrUtil.isStrBlank(studentInfo.getRowguid())) {
                studentInfo.setRowguid(UUID.randomUUID().toString().replaceAll("-", ""));
            }
            
            // 设置默认值
            if (studentInfo.getStatus() == null) {
                studentInfo.setStatus(1);
            }
            
            if (studentInfo.getCreateTime() == null) {
                studentInfo.setCreateTime(LocalDateTime.now());
            }
            
            if (studentInfo.getUpdateTime() == null) {
                studentInfo.setUpdateTime(LocalDateTime.now());
            }
            
            // 使用MyBatis-Plus的insert方法
            return studentInfoMapper.insert(studentInfo) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStudent(StudentInfo studentInfo) {
        try {
            // 使用UpdateWrapper手动构建更新语句
            UpdateWrapper<StudentInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("rowguid", studentInfo.getRowguid())
                        .set("student_no", studentInfo.getStudentNo())
                        .set("student_name", studentInfo.getStudentName())
                        .set("classguid", studentInfo.getClassguid())
                        .set("gender", studentInfo.getGender())
                        .set("status", studentInfo.getStatus())
                        .set("remark", studentInfo.getRemark())
                        .set("update_time", LocalDateTime.now());
            
            return studentInfoMapper.update(null, updateWrapper) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteStudent(String rowguid) {
        try {
            // 使用QueryWrapper手动构建删除语句
            QueryWrapper<StudentInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("rowguid", rowguid);
            return studentInfoMapper.delete(queryWrapper) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean batchDeleteStudent(List<String> rowguids) {
        if (rowguids == null || rowguids.isEmpty()) {
            return false;
        }
        
        try {
            // 使用QueryWrapper手动构建批量删除语句
            QueryWrapper<StudentInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("rowguid", rowguids);
            return studentInfoMapper.delete(queryWrapper) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isStudentNoExist(String studentNo) {
        QueryWrapper<StudentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no", studentNo);
        return studentInfoMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean isStudentNoExist(String studentNo, String rowguid) {
        QueryWrapper<StudentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no", studentNo)
                   .ne("rowguid", rowguid);
        return studentInfoMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public Map<String, Object> importStudent(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> failMessages = new ArrayList<>();
        
        try {
            // 读取Excel文件
            InputStream inputStream = file.getInputStream();
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            
            // 遍历行（从第二行开始，第一行是表头）
            for (int i = 1; i < sheet.getRows(); i++) {
                try {
                    // 读取单元格
                    Cell[] cells = sheet.getRow(i);
                    if (cells.length < 4) {
                        failCount++;
                        failMessages.add("第" + (i + 1) + "行：数据不完整");
                        continue;
                    }
                    
                    // 解析数据
                    String studentNo = cells[0].getContents().trim();
                    String studentName = cells[1].getContents().trim();
                    String classguid = cells[2].getContents().trim();
                    String genderStr = cells[3].getContents().trim();
                    String statusStr = cells.length > 4 ? cells[4].getContents().trim() : "1";
                    String remark = cells.length > 5 ? cells[5].getContents().trim() : "";
                    
                    // 验证数据
                    if (StrUtil.isStrBlank(studentNo)) {
                        failCount++;
                        failMessages.add("第" + (i + 1) + "行：学号不能为空");
                        continue;
                    }
                    
                    if (StrUtil.isStrBlank(studentName)) {
                        failCount++;
                        failMessages.add("第" + (i + 1) + "行：姓名不能为空");
                        continue;
                    }
                    
                    if (StrUtil.isStrBlank(classguid)) {
                        failCount++;
                        failMessages.add("第" + (i + 1) + "行：班级ID不能为空");
                        continue;
                    }
                    
                    // 检查学号是否已存在
                    if (isStudentNoExist(studentNo)) {
                        failCount++;
                        failMessages.add("第" + (i + 1) + "行：学号" + studentNo + "已存在");
                        continue;
                    }
                    
                    // 转换数据类型
                    Integer gender = StrUtil.isStrBlank(genderStr) ? null : Integer.parseInt(genderStr);
                    Integer status = StrUtil.isStrBlank(statusStr) ? 1 : Integer.parseInt(statusStr);
                    
                    // 创建学生信息对象
                    StudentInfo studentInfo = new StudentInfo();
                    studentInfo.setRowguid(UUID.randomUUID().toString().replaceAll("-", ""));
                    studentInfo.setStudentNo(studentNo);
                    studentInfo.setStudentName(studentName);
                    studentInfo.setClassguid(classguid);
                    studentInfo.setGender(gender);
                    studentInfo.setStatus(status);
                    studentInfo.setRemark(remark);
                    studentInfo.setCreateTime(LocalDateTime.now());
                    studentInfo.setUpdateTime(LocalDateTime.now());
                    
                    // 保存数据
                    if (addStudent(studentInfo)) {
                        successCount++;
                    } else {
                        failCount++;
                        failMessages.add("第" + (i + 1) + "行：导入失败");
                    }
                } catch (Exception e) {
                    failCount++;
                    failMessages.add("第" + (i + 1) + "行：" + e.getMessage());
                }
            }
            
            // 关闭资源
            workbook.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "导入失败：" + e.getMessage());
            return result;
        }
        
        // 构建结果
        result.put("success", true);
        result.put("message", "导入完成");
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("failMessages", failMessages);
        
        return result;
    }

    @Override
    public List<Map<String, Object>> exportStudentList(Params params) {
        // 构建查询条件
        QueryWrapper<StudentInfo> queryWrapper = new QueryWrapper<>();
        
        String studentName = params.getString("studentName");
        if (StrUtil.isNotBlank(studentName)) {
            queryWrapper.like("student_name", studentName);
        }
        
        String studentNo = params.getString("studentNo");
        if (StrUtil.isNotBlank(studentNo)) {
            queryWrapper.like("student_no", studentNo);
        }
        
        String classguid = params.getString("classguid");
        if (StrUtil.isNotBlank(classguid)) {
            queryWrapper.eq("classguid", classguid);
        }
        
        Integer gender = params.getInteger("gender");
        if (gender != null) {
            queryWrapper.eq("gender", gender);
        }
        
        Integer status = params.getInteger("status");
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        // 排序
        queryWrapper.orderByDesc("create_time");
        
        // 查询数据
        List<StudentInfo> list = studentInfoMapper.selectList(queryWrapper);
        
        // 转换结果
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (StudentInfo studentInfo : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("rowguid", studentInfo.getRowguid());
            map.put("studentNo", studentInfo.getStudentNo());
            map.put("studentName", studentInfo.getStudentName());
            map.put("classguid", studentInfo.getClassguid());
            map.put("gender", studentInfo.getGender());
            map.put("status", studentInfo.getStatus());
            map.put("remark", studentInfo.getRemark());
            map.put("createTime", studentInfo.getCreateTime());
            map.put("updateTime", studentInfo.getUpdateTime());
            resultList.add(map);
        }
        
        return resultList;
    }

    @Override
    public List<Map<String, Object>> getImportTemplate() {
        // 创建模板数据
        List<Map<String, Object>> templateData = new ArrayList<>();
        Map<String, Object> header = new HashMap<>();
        header.put("学号", "示例：2023001");
        header.put("姓名", "示例：张三");
        header.put("班级ID", "示例：34072854-3dc1-4f98-b24b-1411e0b0a9f5");
        header.put("性别", "示例：1-男，2-女");
        header.put("状态", "示例：1-在读，0-毕业/离校");
        header.put("备注", "示例：备注信息");
        templateData.add(header);
        
        // 添加示例数据
        Map<String, Object> example = new HashMap<>();
        example.put("学号", "2023001");
        example.put("姓名", "张三");
        example.put("班级ID", "34072854-3dc1-4f98-b24b-1411e0b0a9f5");
        example.put("性别", "1");
        example.put("状态", "1");
        example.put("备注", "示例学生");
        templateData.add(example);
        
        return templateData;
    }
}