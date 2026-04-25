package com.abs.system.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abs.system.api.IBaseClassService;
import com.abs.system.domain.BaseClass;
import com.abs.system.util.BeanUtil;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.JxlExcelUtils;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/class")
public class BaseClassController {

    private static Logger logger = LoggerFactory.getLogger(BaseClassController.class);

    @Autowired
    private IBaseClassService classService;

    /**
     * 获取班级列表（分页）
     * @param reqMap 请求参数
     * @return 班级列表
     */
    @ResponseBody
    @RequestMapping("/getpagelist")
    public JSONObject getPageList(@RequestBody Map<String, Object> reqMap) {
        logger.info("[开始] 获取班级列表，参数: {}", reqMap.toString());
        try {
            String className = reqMap.get("className") == null ? "" : reqMap.get("className").toString();
            String grade = reqMap.get("grade") == null ? "" : reqMap.get("grade").toString();
            Integer status = reqMap.get("status") == null ? null : Integer.valueOf(reqMap.get("status").toString());
            int pagenum = Integer.parseInt(reqMap.get("pagenum").toString());
            int pagesize = Integer.parseInt(reqMap.get("pagesize").toString());

            Params params = new Params();
            params.put("className", className);
            params.put("grade", grade);
            params.put("status", status);
            params.put("pageSize", pagesize);
            params.put("startIndex", (pagenum - 1) * pagesize);

            List<Map<String, Object>> list = classService.getClassList(params);
            long count = classService.getClassCount(params);

            // 格式化日期时间字段
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (Map<String, Object> item : list) {
                if (item.get("createTime") != null) {
                    item.put("createTime", formatter.format((java.time.LocalDateTime) item.get("createTime")));
                }
                if (item.get("updateTime") != null) {
                    item.put("updateTime", formatter.format((java.time.LocalDateTime) item.get("updateTime")));
                }
            }

            JSONObject result = new JSONObject();
            result.put("list", list);
            result.put("count", count);
            result.put("pagenum", pagenum);
            result.put("pagesize", pagesize);

            logger.info("[结束] 获取班级列表");
            return BuildJsonOfObject.getJsonString(result, MSG.SUCCESSCODE);
        } catch (Exception e) {
            logger.error("[错误] 获取班级列表: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 获取单个班级信息
     * @param reqMap 请求参数
     * @return 班级信息
     */
    @ResponseBody
    @RequestMapping("/getclassinfo")
    public JSONObject getClassInfo(@RequestBody Map<String, Object> reqMap) {
        logger.info("[开始] 获取单个班级信息，参数: {}", reqMap.toString());
        try {
            String rowguid = reqMap.get("rowguid").toString();
            Map<String, Object> baseClass = classService.getClassMapByGuid(rowguid);
            
            // 格式化日期时间字段
            if (baseClass != null) {
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                if (baseClass.get("createTime") != null) {
                    baseClass.put("createTime", formatter.format((java.time.LocalDateTime) baseClass.get("createTime")));
                }
                if (baseClass.get("updateTime") != null) {
                    baseClass.put("updateTime", formatter.format((java.time.LocalDateTime) baseClass.get("updateTime")));
                }
            }
            
            logger.info("[结束] 获取单个班级信息");
            return BuildJsonOfObject.getJsonString(baseClass, MSG.SUCCESSCODE);
        } catch (Exception e) {
            logger.error("[错误] 获取单个班级信息: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 添加班级信息
     * @param reqMap 请求参数
     * @return 添加结果
     */
    @ResponseBody
    @RequestMapping("/addclass")
    public JSONObject addClass(@RequestBody Map<String, Object> reqMap) {
        logger.info("[开始] 添加班级信息，参数: {}", reqMap.toString());
        try {
            // 处理参数映射
            Map<String, Object> paramMap = new java.util.HashMap<>();
            paramMap.put("className", reqMap.get("class_name"));
            paramMap.put("grade", reqMap.get("grade"));
            paramMap.put("gradeYear", reqMap.get("enrollment_year"));
            paramMap.put("status", reqMap.get("status"));
            paramMap.put("remark", reqMap.get("remark"));
            
            // 验证班级名称是否已存在
            String className = paramMap.get("className").toString();
            if (classService.isClassNameExist(className)) {
                logger.error("[错误] 添加班级信息: 班级名称已存在");
                return BuildJsonOfObject.getJsonString("班级名称已存在", MSG.FAILCODE);
            }

            // 转换为实体类
            BaseClass baseClass = BeanUtil.mapToBean(paramMap, BaseClass.class);
            baseClass.setRowguid(UUID.randomUUID().toString());
            baseClass.setStatus(baseClass.getStatus() == null ? 1 : baseClass.getStatus());
            baseClass.setGradeYear(baseClass.getGradeYear() == null ? 0 : baseClass.getGradeYear());
            // 不设置remark字段的默认值，保持原样
            // baseClass.setRemark(baseClass.getRemark() == null ? "" : baseClass.getRemark());

            
            System.out.println(baseClass.toString());
            // 打印baseClass对象的所有字段值，确认remark字段确实有值
            System.out.println("BaseClass object before insert: " + baseClass.toString());
            System.out.println("remark value before insert: " + baseClass.getRemark());
            System.out.println("remark is null before insert: " + (baseClass.getRemark() == null));
            System.out.println("remark is empty before insert: " + (baseClass.getRemark() != null && baseClass.getRemark().isEmpty()));
            
            // 添加班级信息
            boolean result = classService.addClass(baseClass);
            if (result) {
                logger.info("[结束] 添加班级信息成功");
                return BuildJsonOfObject.getJsonString(MSG.saveSuccess, MSG.SUCCESSCODE);
            } else {
                logger.error("[错误] 添加班级信息失败");
                return BuildJsonOfObject.getJsonString(MSG.saveFail, MSG.FAILCODE);
            }
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("[错误] 添加班级信息: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 更新班级信息
     * @param reqMap 请求参数
     * @return 更新结果
     */
    @ResponseBody
    @RequestMapping("/updateclass")
    public JSONObject updateClass(@RequestBody Map<String, Object> reqMap) {
        logger.info("[开始] 更新班级信息，参数: {}", reqMap.toString());
        try {
            String rowguid = reqMap.get("rowguid").toString();
            String className = reqMap.get("class_name").toString();

            // 验证班级名称是否已存在（排除当前班级）
            if (classService.isClassNameExist(className, rowguid)) {
                logger.error("[错误] 更新班级信息: 班级名称已存在");
                return BuildJsonOfObject.getJsonString("班级名称已存在", MSG.FAILCODE);
            }

            // 处理参数映射
            Map<String, Object> paramMap = new java.util.HashMap<>();
            paramMap.put("rowguid", rowguid);
            paramMap.put("className", className);
            paramMap.put("grade", reqMap.get("grade"));
            paramMap.put("gradeYear", reqMap.get("enrollment_year"));
            paramMap.put("status", reqMap.get("status"));
            paramMap.put("remark", reqMap.get("remark"));

            // 转换为实体类
            BaseClass baseClass = BeanUtil.mapToBean(paramMap, BaseClass.class);
            baseClass.setGradeYear(baseClass.getGradeYear() == null ? 0 : baseClass.getGradeYear());
            baseClass.setRemark(baseClass.getRemark() == null ? "" : baseClass.getRemark());

            // 更新班级信息
            boolean result = classService.updateClass(baseClass);
            if (result) {
                logger.info("[结束] 更新班级信息成功");
                return BuildJsonOfObject.getJsonString(MSG.updateSuccess, MSG.SUCCESSCODE);
            } else {
                logger.error("[错误] 更新班级信息失败");
                return BuildJsonOfObject.getJsonString(MSG.updateFail, MSG.FAILCODE);
            }
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("[错误] 更新班级信息: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 删除班级信息
     * @param reqMap 请求参数
     * @return 删除结果
     */
    @ResponseBody
    @RequestMapping("/deleteclass")
    public JSONObject deleteClass(@RequestBody Map<String, Object> reqMap) {
        logger.info("[开始] 删除班级信息，参数: {}", reqMap.toString());
        try {
            String rowguid = reqMap.get("rowguid").toString();
            boolean result = classService.deleteClass(rowguid);
            if (result) {
                logger.info("[结束] 删除班级信息成功");
                return BuildJsonOfObject.getJsonString(MSG.DeleteSuccess, MSG.SUCCESSCODE);
            } else {
                logger.error("[错误] 删除班级信息失败");
                return BuildJsonOfObject.getJsonString(MSG.deleteFail, MSG.FAILCODE);
            }
        } catch (Exception e) {
            logger.error("[错误] 删除班级信息: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 批量删除班级信息
     * @param reqMap 请求参数
     * @return 删除结果
     */
    @ResponseBody
    @RequestMapping("/batchdeleteclass")
    public JSONObject batchDeleteClass(@RequestBody Map<String, Object> reqMap) {
        logger.info("[开始] 批量删除班级信息，参数: {}", reqMap.toString());
        try {
            List<String> rowguids = (List<String>) reqMap.get("rowguids");
            boolean result = classService.batchDeleteClass(rowguids);
            if (result) {
                logger.info("[结束] 批量删除班级信息成功");
                return BuildJsonOfObject.getJsonString("批量删除成功", MSG.SUCCESSCODE);
            } else {
                logger.error("[错误] 批量删除班级信息失败");
                return BuildJsonOfObject.getJsonString("批量删除失败", MSG.FAILCODE);
            }
        } catch (Exception e) {
            logger.error("[错误] 批量删除班级信息: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 检查班级名称是否已存在
     * @param reqMap 请求参数
     * @return 检查结果
     */
    @ResponseBody
    @RequestMapping("/checkclassname")
    public JSONObject checkClassName(@RequestBody Map<String, Object> reqMap) {
        logger.info("[开始] 检查班级名称是否已存在，参数: {}", reqMap.toString());
        try {
            // 处理className参数，支持class_name和className两种格式
            String className = null;
            if (reqMap.containsKey("class_name")) {
                className = reqMap.get("class_name").toString();
            } else if (reqMap.containsKey("className")) {
                className = reqMap.get("className").toString();
            }
            
            String rowguid = reqMap.get("rowguid") == null ? "" : reqMap.get("rowguid").toString();

            boolean isExist;
            if (StrUtil.isStrBlank(rowguid)) {
                isExist = classService.isClassNameExist(className);
            } else {
                isExist = classService.isClassNameExist(className, rowguid);
            }

            JSONObject result = new JSONObject();
            result.put("isExist", isExist);
            logger.info("[结束] 检查班级名称是否已存在");
            return BuildJsonOfObject.getJsonString(result, MSG.SUCCESSCODE);
        } catch (Exception e) {
            logger.error("[错误] 检查班级名称是否已存在: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 获取所有班级列表（用于下拉选择）
     * @return 班级列表
     */
    @ResponseBody
    @RequestMapping("/getallclasslist")
    public JSONObject getAllClassList() {
        logger.info("[开始] 获取所有班级列表");
        try {
            List<Map<String, Object>> list = classService.getAllClassList();
            logger.info("[结束] 获取所有班级列表");
            return BuildJsonOfObject.getJsonString(list, MSG.SUCCESSCODE);
        } catch (Exception e) {
            logger.error("[错误] 获取所有班级列表: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 导出班级列表
     * @param reqMap 请求参数
     * @return 导出结果
     */
    @ResponseBody
    @RequestMapping("/exportclass")
    public JSONObject exportClass(@RequestBody Map<String, Object> reqMap) {
        logger.info("[开始] 导出班级列表，参数: {}", reqMap.toString());
        try {
            // 构建导出参数
            Params params = new Params();
            String className = reqMap.get("className") == null ? "" : reqMap.get("className").toString();
            String grade = reqMap.get("grade") == null ? "" : reqMap.get("grade").toString();
            Integer status = reqMap.get("status") == null ? null : Integer.valueOf(reqMap.get("status").toString());
            params.put("className", className);
            params.put("grade", grade);
            params.put("status", status);

            // 获取导出数据
            List<Map<String, Object>> list = classService.exportClassList(params);

            logger.info("[结束] 导出班级列表，共{}条数据", list.size());
            return BuildJsonOfObject.getJsonString(list, MSG.SUCCESSCODE);
        } catch (Exception e) {
            logger.error("[错误] 导出班级列表: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 导出班级数据（支持指定字段和格式）
     * @param reqMap 请求参数
     * @param response 响应对象
     */
    @RequestMapping("/export")
    public void export(@RequestBody Map<String, Object> reqMap, jakarta.servlet.http.HttpServletResponse response) {
        logger.info("[开始] 导出班级数据，参数: {}", reqMap.toString());
        try {
            // 获取导出参数
            List<String> selectedFields = (List<String>) reqMap.get("selected_fields");
            String exportFormat = reqMap.get("export_format") == null ? "excel" : reqMap.get("export_format").toString();
            String className = reqMap.get("class_name") == null ? "" : reqMap.get("class_name").toString();
            String grade = reqMap.get("grade") == null ? "" : reqMap.get("grade").toString();

            // 构建查询参数
            Params params = new Params();
            params.put("className", className);
            params.put("grade", grade);

            // 获取原始数据
            List<Map<String, Object>> originalList = classService.exportClassList(params);

            // 根据选择的字段过滤数据
            List<Map<String, Object>> filteredList = new java.util.ArrayList<>();
            for (Map<String, Object> map : originalList) {
                Map<String, Object> filteredMap = new java.util.HashMap<>();
                for (String field : selectedFields) {
                    // 映射字段名，前端可能使用的是驼峰命名，而数据库是下划线命名
                    String dbField = field;
                    if (field.equals("className")) {
                        dbField = "className";
                    } else if (field.equals("gradeYear")) {
                        dbField = "gradeYear";
                    } else if (field.equals("createTime")) {
                        dbField = "createTime";
                    } else if (field.equals("updateTime")) {
                        dbField = "updateTime";
                    }
                    if (map.containsKey(dbField)) {
                        filteredMap.put(field, map.get(dbField));
                    }
                }
                filteredList.add(filteredMap);
            }

            // 生成列名
            List<String> columns = new java.util.ArrayList<>();
            for (String field : selectedFields) {
                // 转换字段名为中文
                String columnName = field;
                if (field.equals("className")) {
                    columnName = "班级名称";
                } else if (field.equals("grade")) {
                    columnName = "年级";
                } else if (field.equals("gradeYear")) {
                    columnName = "入学年份";
                } else if (field.equals("status")) {
                    columnName = "状态";
                } else if (field.equals("remark")) {
                    columnName = "备注";
                } else if (field.equals("createTime")) {
                    columnName = "创建时间";
                } else if (field.equals("updateTime")) {
                    columnName = "更新时间";
                }
                columns.add(columnName);
            }

            // 生成文件名
            String filename = "班级数据";
            if (exportFormat.equals("excel")) {
                filename += ".xls";
                // 导出为Excel
                JxlExcelUtils.exportexcel(response, filename, filteredList, columns);
            } else if (exportFormat.equals("pdf")) {
                // TODO: 实现PDF导出
                filename += ".pdf";
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("gb2312"), "ISO8859-1"));
                // 这里需要实现PDF导出的逻辑
            }

            logger.info("[结束] 导出班级数据，共{}条数据", filteredList.size());
        } catch (Exception e) {
            logger.error("[错误] 导出班级数据: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}