package com.abs.system.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.abs.system.api.IStudentInfoService;
import com.abs.system.domain.StudentInfo;
import com.abs.system.util.BeanUtil;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.JxlExcelUtils;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/student")
public class StudentInfoController {

    private static final Logger logger = LoggerFactory.getLogger(StudentInfoController.class);

    @Autowired
    private IStudentInfoService studentService;

    /**
     * 获取学生列表（支持分页和筛选）
     * @param params 请求参数
     * @return 学生列表
     */
    @ResponseBody
    @RequestMapping("/list")
    public JSONObject getStudentList(@RequestBody Map<String, Object> params) {
        logger.info("[开始] 获取学生列表，参数: {}", params.toString());
        try {
            // 构建查询参数
            Params queryParams = new Params(params);
           

            // 获取学生列表
            List<Map<String, Object>> list = studentService.getStudentList(queryParams);
            long total = studentService.getStudentCount(queryParams);

            JSONObject result = new JSONObject();
            result.put("code", 0);
            result.put("msg", "success");
            result.put("count", total);
            result.put("data", list);

            logger.info("[结束] 获取学生列表，共{}条数据", list.size());
            return result;
        } catch (Exception e) {
            logger.error("[错误] 获取学生列表: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 根据学生ID获取学生信息
     * @param rowguid 学生ID
     * @return 学生信息
     */
    @ResponseBody
    @RequestMapping("/getbyid")
    public JSONObject getStudentById(@RequestParam("rowguid") String rowguid) {
        logger.info("[开始] 根据学生ID获取学生信息，参数: {}", rowguid);
        try {
            Map<String, Object> student = studentService.getStudentMapByGuid(rowguid);
            if (student != null) {
                logger.info("[结束] 根据学生ID获取学生信息成功");
                return BuildJsonOfObject.getJsonString(student, MSG.SUCCESSCODE);
            } else {
                logger.error("[错误] 根据学生ID获取学生信息: 学生不存在");
                return BuildJsonOfObject.getJsonString("学生不存在", MSG.FAILCODE);
            }
        } catch (Exception e) {
            logger.error("[错误] 根据学生ID获取学生信息: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 添加学生信息
     * @param reqMap 请求参数
     * @return 添加结果
     */
    @ResponseBody
    @RequestMapping("/add")
    public JSONObject addStudent(@RequestBody Map<String, Object> reqMap) {
        logger.info("[开始] 添加学生信息，参数: {}", reqMap.toString());
        try {
            String studentNo = reqMap.get("studentNo") == null ? "" : reqMap.get("studentNo").toString();

            // 验证学号是否已存在
            if (studentService.isStudentNoExist(studentNo)) {
                logger.error("[错误] 添加学生信息: 学号已存在");
                return BuildJsonOfObject.getJsonString("学号已存在", MSG.FAILCODE);
            }

            // 处理参数映射
            Map<String, Object> paramMap = new java.util.HashMap<>();
            paramMap.put("rowguid", reqMap.get("rowguid"));
            paramMap.put("studentNo", studentNo);
            paramMap.put("studentName", reqMap.get("studentName"));
            paramMap.put("classguid", reqMap.get("classguid"));
            paramMap.put("gender", reqMap.get("gender"));
            paramMap.put("status", reqMap.get("status"));
            paramMap.put("remark", reqMap.get("remark"));

            // 转换为实体类
            StudentInfo studentInfo = BeanUtil.mapToBean(paramMap, StudentInfo.class);
            studentInfo.setStatus(studentInfo.getStatus() == null ? 1 : studentInfo.getStatus());
            studentInfo.setRemark(studentInfo.getRemark() == null ? "" : studentInfo.getRemark());

            // 添加学生信息
            boolean result = studentService.addStudent(studentInfo);
            if (result) {
                logger.info("[结束] 添加学生信息成功");
                return BuildJsonOfObject.getJsonString(MSG.saveSuccess, MSG.SUCCESSCODE);
            } else {
                logger.error("[错误] 添加学生信息失败");
                return BuildJsonOfObject.getJsonString(MSG.saveFail, MSG.FAILCODE);
            }
        } catch (Exception e) {
            logger.error("[错误] 添加学生信息: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 更新学生信息
     * @param reqMap 请求参数
     * @return 更新结果
     */
    @ResponseBody
    @RequestMapping("/update")
    public JSONObject updateStudent(@RequestBody Map<String, Object> reqMap) {
        logger.info("[开始] 更新学生信息，参数: {}", reqMap.toString());
        try {
            String rowguid = reqMap.get("rowguid").toString();
            String studentNo = reqMap.get("studentNo").toString();

            // 验证学号是否已存在（排除当前学生）
            if (studentService.isStudentNoExist(studentNo, rowguid)) {
                logger.error("[错误] 更新学生信息: 学号已存在");
                return BuildJsonOfObject.getJsonString("学号已存在", MSG.FAILCODE);
            }

            // 处理参数映射
            Map<String, Object> paramMap = new java.util.HashMap<>();
            paramMap.put("rowguid", rowguid);
            paramMap.put("studentNo", studentNo);
            paramMap.put("studentName", reqMap.get("studentName"));
            paramMap.put("classguid", reqMap.get("classguid"));
            paramMap.put("gender", reqMap.get("gender"));
            paramMap.put("status", reqMap.get("status"));
            paramMap.put("remark", reqMap.get("remark"));

            // 转换为实体类
            StudentInfo studentInfo = BeanUtil.mapToBean(paramMap, StudentInfo.class);
            studentInfo.setStatus(studentInfo.getStatus() == null ? 1 : studentInfo.getStatus());
            studentInfo.setRemark(studentInfo.getRemark() == null ? "" : studentInfo.getRemark());

            // 更新学生信息
            boolean result = studentService.updateStudent(studentInfo);
            if (result) {
                logger.info("[结束] 更新学生信息成功");
                return BuildJsonOfObject.getJsonString(MSG.updateSuccess, MSG.SUCCESSCODE);
            } else {
                logger.error("[错误] 更新学生信息失败");
                return BuildJsonOfObject.getJsonString(MSG.updateFail, MSG.FAILCODE);
            }
        } catch (Exception e) {
            logger.error("[错误] 更新学生信息: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 删除学生信息
     * @param reqMap 请求参数
     * @return 删除结果
     */
    @ResponseBody
    @RequestMapping("/delete")
    public JSONObject deleteStudent(@RequestBody Map<String, Object> reqMap) {
        logger.info("[开始] 删除学生信息，参数: {}", reqMap.toString());
        try {
            String rowguid = reqMap.get("rowguid").toString();
            boolean result = studentService.deleteStudent(rowguid);
            if (result) {
                logger.info("[结束] 删除学生信息成功");
                return BuildJsonOfObject.getJsonString(MSG.DeleteSuccess, MSG.SUCCESSCODE);
            } else {
                logger.error("[错误] 删除学生信息失败");
                return BuildJsonOfObject.getJsonString(MSG.deleteFail, MSG.FAILCODE);
            }
        } catch (Exception e) {
            logger.error("[错误] 删除学生信息: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 批量删除学生信息
     * @param reqMap 请求参数
     * @return 删除结果
     */
    @ResponseBody
    @RequestMapping("/batchdelete")
    public JSONObject batchDeleteStudent(@RequestBody Map<String, Object> reqMap) {
        logger.info("[开始] 批量删除学生信息，参数: {}", reqMap.toString());
        try {
            List<String> rowguids = (List<String>) reqMap.get("rowguids");
            boolean result = studentService.batchDeleteStudent(rowguids);
            if (result) {
                logger.info("[结束] 批量删除学生信息成功");
                return BuildJsonOfObject.getJsonString("批量删除成功", MSG.SUCCESSCODE);
            } else {
                logger.error("[错误] 批量删除学生信息失败");
                return BuildJsonOfObject.getJsonString("批量删除失败", MSG.FAILCODE);
            }
        } catch (Exception e) {
            logger.error("[错误] 批量删除学生信息: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 检查学号是否已存在
     * @param reqMap 请求参数
     * @return 检查结果
     */
    @ResponseBody
    @RequestMapping("/checkstudentno")
    public JSONObject checkStudentNo(@RequestBody Map<String, Object> reqMap) {
        logger.info("[开始] 检查学号是否已存在，参数: {}", reqMap.toString());
        try {
            String studentNo = reqMap.get("studentNo").toString();
            String rowguid = reqMap.get("rowguid") == null ? "" : reqMap.get("rowguid").toString();

            boolean isExist;
            if (StrUtil.isStrBlank(rowguid)) {
                isExist = studentService.isStudentNoExist(studentNo);
            } else {
                isExist = studentService.isStudentNoExist(studentNo, rowguid);
            }

            JSONObject result = new JSONObject();
            result.put("isExist", isExist);
            logger.info("[结束] 检查学号是否已存在");
            return BuildJsonOfObject.getJsonString(result, MSG.SUCCESSCODE);
        } catch (Exception e) {
            logger.error("[错误] 检查学号是否已存在: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 导入学生信息
     * @param file 上传的Excel文件
     * @return 导入结果
     */
    @ResponseBody
    @RequestMapping("/import")
    public JSONObject importStudent(@RequestParam("file") MultipartFile file) {
        logger.info("[开始] 导入学生信息");
        try {
            Map<String, Object> result = studentService.importStudent(file);
            logger.info("[结束] 导入学生信息完成");
            return BuildJsonOfObject.getJsonString(result, MSG.SUCCESSCODE);
        } catch (Exception e) {
            logger.error("[错误] 导入学生信息: {}", e.getMessage());
            return BuildJsonOfObject.getJsonString(MSG.Fail, MSG.FAILCODE);
        }
    }

    /**
     * 导出学生信息
     * @param reqMap 请求参数
     * @param response 响应对象
     */
    @RequestMapping("/export")
    public void exportStudent(@RequestBody Map<String, Object> reqMap, HttpServletResponse response) {
        logger.info("[开始] 导出学生信息，参数: {}", reqMap.toString());
        try {
            // 获取导出参数
            List<String> selectedFields = (List<String>) reqMap.get("selected_fields");
            String exportFormat = reqMap.get("export_format") == null ? "excel" : reqMap.get("export_format").toString();
            String studentName = reqMap.get("student_name") == null ? "" : reqMap.get("student_name").toString();
            String studentNo = reqMap.get("student_no") == null ? "" : reqMap.get("student_no").toString();
            String classguid = reqMap.get("classguid") == null ? "" : reqMap.get("classguid").toString();

            // 构建查询参数
            Params params = new Params();
            params.put("studentName", studentName);
            params.put("studentNo", studentNo);
            params.put("classguid", classguid);

            // 获取原始数据
            List<Map<String, Object>> originalList = studentService.exportStudentList(params);

            // 根据选择的字段过滤数据
            List<Map<String, Object>> filteredList = new java.util.ArrayList<>();
            for (Map<String, Object> map : originalList) {
                Map<String, Object> filteredMap = new java.util.HashMap<>();
                for (String field : selectedFields) {
                    // 映射字段名
                    String dbField = field;
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
                if (field.equals("studentNo")) {
                    columnName = "学号";
                } else if (field.equals("studentName")) {
                    columnName = "姓名";
                } else if (field.equals("classguid")) {
                    columnName = "班级ID";
                } else if (field.equals("gender")) {
                    columnName = "性别";
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
            String filename = "学生数据";
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

            logger.info("[结束] 导出学生数据，共{}条数据", filteredList.size());
        } catch (Exception e) {
            logger.error("[错误] 导出学生数据: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 下载导入模板
     * @param response 响应对象
     */
    @RequestMapping("/downloadtemplate")
    public void downloadTemplate(HttpServletResponse response) {
        logger.info("[开始] 下载导入模板");
        try {
            // 获取模板数据
            List<Map<String, Object>> templateData = studentService.getImportTemplate();

            // 生成列名
            List<String> columns = new java.util.ArrayList<>();
            columns.add("学号");
            columns.add("姓名");
            columns.add("班级ID");
            columns.add("性别");
            columns.add("状态");
            columns.add("备注");

            // 导出为Excel
            JxlExcelUtils.exportexcel(response, "学生导入模板.xls", templateData, columns);

            logger.info("[结束] 下载导入模板成功");
        } catch (Exception e) {
            logger.error("[错误] 下载导入模板: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}