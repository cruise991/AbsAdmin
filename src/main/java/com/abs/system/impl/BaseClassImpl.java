package com.abs.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.abs.system.api.IBaseClassService;
import com.abs.system.domain.BaseClass;
import com.abs.system.mapper.BaseClassMapper;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;

@Service
public class BaseClassImpl implements IBaseClassService {

    @Autowired
    private BaseClassMapper baseClassMapper;

    @Override
    public List<Map<String, Object>> getClassList(Params params) {
        // 构建查询条件
        QueryWrapper<BaseClass> queryWrapper = new QueryWrapper<>();
        
        String className = params.getString("className");
        if (StrUtil.isNotBlank(className)) {
            queryWrapper.like("class_name", className);
        }
        
        String grade = params.getString("grade");
        if (StrUtil.isNotBlank(grade)) {
            queryWrapper.like("grade", grade);
        }
        
        Integer status = params.getInteger("status");
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        // 排序
        queryWrapper.orderByDesc("grade_year").orderByAsc("class_name");
        
        // 分页
        int pageSize = params.getInteger("pageSize", 10);
        int startIndex = params.getInteger("startIndex", 0);
        int pageNum = startIndex / pageSize + 1;
        
        Page<BaseClass> page = new Page<>(pageNum, pageSize);
        IPage<BaseClass> resultPage = baseClassMapper.selectPage(page, queryWrapper);
        
        // 转换为Map列表
        List<BaseClass> records = resultPage.getRecords();
        List<Map<String, Object>> list = new java.util.ArrayList<>();
        for (BaseClass baseClass : records) {
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("rowguid", baseClass.getRowguid());
            map.put("className", baseClass.getClassName());
            map.put("grade", baseClass.getGrade());
            map.put("gradeYear", baseClass.getGradeYear());
            map.put("status", baseClass.getStatus());
            map.put("remark", baseClass.getRemark());
            map.put("createTime", baseClass.getCreateTime());
            map.put("updateTime", baseClass.getUpdateTime());
            list.add(map);
        }
        
        return list;
    }

    @Override
    public long getClassCount(Params params) {
        // 构建查询条件
        QueryWrapper<BaseClass> queryWrapper = new QueryWrapper<>();
        
        String className = params.getString("className");
        if (StrUtil.isNotBlank(className)) {
            queryWrapper.like("class_name", className);
        }
        
        String grade = params.getString("grade");
        if (StrUtil.isNotBlank(grade)) {
            queryWrapper.like("grade", grade);
        }
        
        Integer status = params.getInteger("status");
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        return baseClassMapper.selectCount(queryWrapper);
    }

    @Override
    public BaseClass getClassByGuid(String rowguid) {
        return baseClassMapper.selectById(rowguid);
    }

    @Override
    public Map<String, Object> getClassMapByGuid(String rowguid) {
        BaseClass baseClass = baseClassMapper.selectById(rowguid);
        if (baseClass == null) {
            return null;
        }
        
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("rowguid", baseClass.getRowguid());
        map.put("className", baseClass.getClassName());
        map.put("grade", baseClass.getGrade());
        map.put("gradeYear", baseClass.getGradeYear());
        map.put("status", baseClass.getStatus());
        map.put("remark", baseClass.getRemark());
        map.put("createTime", baseClass.getCreateTime());
        map.put("updateTime", baseClass.getUpdateTime());
        
        return map;
    }

    @Override
    public boolean addClass(BaseClass baseClass) {
        // 打印baseClass对象的所有字段值，确认remark字段确实有值
        System.out.println("BaseClass object: " + baseClass.toString());
        System.out.println("remark value: " + baseClass.getRemark());
        System.out.println("remark is null: " + (baseClass.getRemark() == null));
        System.out.println("remark is empty: " + (baseClass.getRemark() != null && baseClass.getRemark().isEmpty()));
        
        try {
            // 使用MyBatis-Plus的insert方法
            int result = baseClassMapper.insert(baseClass);
            System.out.println("Insert result: " + result);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateClass(BaseClass baseClass) {
        // 使用UpdateWrapper手动构建更新语句
        com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<BaseClass> updateWrapper = new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
        updateWrapper.eq("rowguid", baseClass.getRowguid())
                    .set("class_name", baseClass.getClassName())
                    .set("grade", baseClass.getGrade())
                    .set("grade_year", baseClass.getGradeYear())
                    .set("status", baseClass.getStatus())
                    .set("remark", baseClass.getRemark())
                    .set("update_time", java.time.LocalDateTime.now());
        return baseClassMapper.update(null, updateWrapper) > 0;
    }

    @Override
    public boolean deleteClass(String rowguid) {
        // 使用DeleteWrapper手动构建删除语句
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<BaseClass> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        queryWrapper.eq("rowguid", rowguid);
        return baseClassMapper.delete(queryWrapper) > 0;
    }

    @Override
    public boolean isClassNameExist(String className) {
        LambdaQueryWrapper<BaseClass> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseClass::getClassName, className);
        return baseClassMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean isClassNameExist(String className, String rowguid) {
        LambdaQueryWrapper<BaseClass> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseClass::getClassName, className)
                   .ne(BaseClass::getRowguid, rowguid);
        return baseClassMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public List<Map<String, Object>> getAllClassList() {
        LambdaQueryWrapper<BaseClass> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseClass::getStatus, 1)
                   .orderByDesc(BaseClass::getGradeYear)
                   .orderByAsc(BaseClass::getClassName);
        
        List<BaseClass> list = baseClassMapper.selectList(queryWrapper);
        List<Map<String, Object>> resultList = new java.util.ArrayList<>();
        
        for (BaseClass baseClass : list) {
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("rowguid", baseClass.getRowguid());
            map.put("className", baseClass.getClassName());
            map.put("grade", baseClass.getGrade());
            map.put("gradeYear", baseClass.getGradeYear());
            resultList.add(map);
        }
        
        return resultList;
    }

    @Override
    public List<Map<String, Object>> exportClassList(Params params) {
        // 构建查询条件
        QueryWrapper<BaseClass> queryWrapper = new QueryWrapper<>();
        
        String className = params.getString("className");
        if (StrUtil.isNotBlank(className)) {
            queryWrapper.like("class_name", className);
        }
        
        String grade = params.getString("grade");
        if (StrUtil.isNotBlank(grade)) {
            queryWrapper.like("grade", grade);
        }
        
        Integer status = params.getInteger("status");
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        // 排序
        queryWrapper.orderByDesc("grade_year").orderByAsc("class_name");
        
        List<BaseClass> list = baseClassMapper.selectList(queryWrapper);
        List<Map<String, Object>> resultList = new java.util.ArrayList<>();
        
        for (BaseClass baseClass : list) {
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("rowguid", baseClass.getRowguid());
            map.put("className", baseClass.getClassName());
            map.put("grade", baseClass.getGrade());
            map.put("gradeYear", baseClass.getGradeYear());
            map.put("status", baseClass.getStatus());
            map.put("remark", baseClass.getRemark());
            map.put("createTime", baseClass.getCreateTime());
            map.put("updateTime", baseClass.getUpdateTime());
            resultList.add(map);
        }
        
        return resultList;
    }

    @Override
    public boolean batchDeleteClass(List<String> rowguids) {
        if (rowguids == null || rowguids.isEmpty()) {
            return false;
        }
        // 使用QueryWrapper手动构建批量删除语句
        try {
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<BaseClass> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            queryWrapper.in("rowguid", rowguids);
            int result = baseClassMapper.delete(queryWrapper);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}