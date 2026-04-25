// 学生数据管理页面的JavaScript逻辑

$(document).ready(function() {
    // 初始化页面
    initPage();

    // 初始化事件监听
    initEventListeners();
});

// 初始化页面
function initPage() {
    // 加载班级列表
    loadClassList();
    
    // 加载学生列表
    loadStudentList();
}

// 初始化事件监听
function initEventListeners() {
    // 筛选按钮点击事件
    $('#btnFilter').click(function() {
        loadStudentList();
    });

    // 添加学生按钮点击事件
    $('#btnAdd').click(function() {
        openAddStudentModal();
    });

    // 批量删除按钮点击事件
    $('#btnBatchDelete').click(function() {
        batchDeleteStudents();
    });

    // 导入按钮点击事件
    $('#btnImport').click(function() {
        openImportModal();
    });

    // 导出按钮点击事件
    $('#btnExport').click(function() {
        openExportModal();
    });

    // 下载模板按钮点击事件
    $('#btnDownloadTemplate').click(function() {
        downloadTemplate();
    });

    // 保存学生按钮点击事件
    $('#btnSaveStudent').click(function() {
        saveStudent();
    });

    // 上传文件按钮点击事件
    $('#btnUploadFile').click(function() {
        uploadFile();
    });

    // 导出数据按钮点击事件
    $('#btnExportData').click(function() {
        exportData();
    });

    // 全选复选框点击事件
    $('#selectAll').click(function() {
        $('.student-checkbox').prop('checked', $(this).prop('checked'));
        updateBatchDeleteButton();
    });

    // 学生复选框点击事件
    $(document).on('click', '.student-checkbox', function() {
        updateBatchDeleteButton();
    });

    // 学号输入框失去焦点事件
    $('#studentNo').blur(function() {
        checkStudentNo();
    });
}

// 加载班级列表
function loadClassList() {
    $.ajax({
        url: '../class/getallclasslist',
        type: 'POST',
        contentType: 'application/json',
        success: function(response) {
            if (response.code === 200) {
                var classList = response.data;
                var classFilter = $('#classFilter');
                var classguidSelect = $('#classguid');
                
                // 清空现有选项
                classFilter.empty();
                classguidSelect.empty();
                
                // 添加默认选项
                classFilter.append('<option value="">全部班级</option>');
                classguidSelect.append('<option value="">请选择班级</option>');
                
                // 添加班级选项
                $.each(classList, function(index, item) {
                    classFilter.append('<option value="' + item.rowguid + '">' + item.className + '</option>');
                    classguidSelect.append('<option value="' + item.rowguid + '">' + item.className + '</option>');
                });
            }
        },
        error: function() {
            alert('加载班级列表失败');
        }
    });
}

// 加载学生列表
function loadStudentList() {
    // 获取筛选条件
    var classguid = $('#classFilter').val();
    var gender = $('#genderFilter').val();
    var status = $('#statusFilter').val();
    
    // 构建请求参数
    var params = {
        page: 1,
        limit: 10,
        classguid: classguid,
        gender: gender,
        status: status
    };

    $.ajax({
        url: 'list',
        type: 'POST',
        data: JSON.stringify(params),
        contentType: 'application/json',
        success: function(response) {
            if (response.code === 0) {
                var studentList = response.data;
                var tbody = $('#studentTable tbody');
                
                // 清空现有数据
                tbody.empty();
                
                // 添加学生数据
                $.each(studentList, function(index, item) {
                    var row = '<tr>' +
                        '<td><input type="checkbox" class="student-checkbox" value="' + item.rowguid + '"></td>' +
                        '<td>' + item.studentNo + '</td>' +
                        '<td>' + item.studentName + '</td>' +
                        '<td>' + getClassName(item.classguid) + '</td>' +
                        '<td>' + (item.gender === 1 ? '男' : item.gender === 2 ? '女' : '') + '</td>' +
                        '<td>' + (item.status === 1 ? '在读' : '毕业/离校') + '</td>' +
                        '<td>' + (item.remark || '') + '</td>' +
                        '<td>' + formatDate(item.createTime) + '</td>' +
                        '<td>' +
                            '<button type="button" class="btn btn-xs btn-primary" onclick="editStudent(\'' + item.rowguid + '\')">编辑</button> ' +
                            '<button type="button" class="btn btn-xs btn-danger" onclick="deleteStudent(\'' + item.rowguid + '\')">删除</button>' +
                        '</td>' +
                    '</tr>';
                    tbody.append(row);
                });
                
                // 更新批量删除按钮状态
                updateBatchDeleteButton();
            }
        },
        error: function() {
            alert('加载学生列表失败');
        }
    });
}

// 根据班级ID获取班级名称
function getClassName(classguid) {
    var className = '';
    $('#classguid option').each(function() {
        if ($(this).val() === classguid) {
            className = $(this).text();
            return false;
        }
    });
    return className;
}

// 格式化日期
function formatDate(dateStr) {
    if (!dateStr) return '';
    var date = new Date(dateStr);
    return date.getFullYear() + '-' + (date.getMonth() + 1).toString().padStart(2, '0') + '-' + date.getDate().toString().padStart(2, '0') + ' ' +
        date.getHours().toString().padStart(2, '0') + ':' + date.getMinutes().toString().padStart(2, '0') + ':' + date.getSeconds().toString().padStart(2, '0');
}

// 更新批量删除按钮状态
function updateBatchDeleteButton() {
    var checkedCount = $('.student-checkbox:checked').length;
    $('#btnBatchDelete').prop('disabled', checkedCount === 0);
}

// 打开添加学生模态框
function openAddStudentModal() {
    // 重置表单
    $('#addStudentForm')[0].reset();
    $('#rowguid').val('');
    $('#studentNoError').text('');
    
    // 打开模态框
    $('#addStudentModal').modal('show');
}

// 打开编辑学生模态框
function editStudent(rowguid) {
    // 根据rowguid获取学生信息
    $.ajax({
        url: 'getbyid',
        type: 'GET',
        data: { rowguid: rowguid },
        success: function(response) {
            if (response.code === 200) {
                var student = response.data;
                
                // 填充表单
                $('#rowguid').val(student.rowguid);
                $('#studentNo').val(student.studentNo);
                $('#studentName').val(student.studentName);
                $('#classguid').val(student.classguid);
                $('#gender').val(student.gender);
                $('#status').val(student.status);
                $('#remark').val(student.remark);
                
                // 打开模态框
                $('#addStudentModal').modal('show');
            } else {
                alert('获取学生信息失败');
            }
        },
        error: function() {
            alert('获取学生信息失败');
        }
    });
}

// 保存学生信息
function saveStudent() {
    // 验证表单
    if (!$('#addStudentForm')[0].checkValidity()) {
        $('#addStudentForm')[0].reportValidity();
        return;
    }

    // 检查学号是否已存在
    if (!checkStudentNo()) {
        return;
    }

    // 构建请求参数
    var params = {
        rowguid: $('#rowguid').val(),
        studentNo: $('#studentNo').val(),
        studentName: $('#studentName').val(),
        classguid: $('#classguid').val(),
        gender: $('#gender').val(),
        status: $('#status').val(),
        remark: $('#remark').val()
    };

    // 发送请求
    var url = $('#rowguid').val() ? 'update' : 'add';
    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(params),
        contentType: 'application/json',
        success: function(response) {
            if (response.code === 200) {
                alert('保存成功');
                $('#addStudentModal').modal('hide');
                loadStudentList();
            } else {
                alert('保存失败: ' + response.message);
            }
        },
        error: function() {
            alert('保存失败');
        }
    });
}

// 检查学号是否已存在
function checkStudentNo() {
    var studentNo = $('#studentNo').val();
    var rowguid = $('#rowguid').val();

    if (!studentNo) {
        $('#studentNoError').text('学号不能为空');
        return false;
    }

    $.ajax({
        url: 'checkstudentno',
        type: 'POST',
        data: JSON.stringify({ studentNo: studentNo, rowguid: rowguid }),
        contentType: 'application/json',
        async: false,
        success: function(response) {
            if (response.code === 200) {
                if (response.data.isExist) {
                    $('#studentNoError').text('学号已存在');
                    return false;
                } else {
                    $('#studentNoError').text('');
                    return true;
                }
            } else {
                $('#studentNoError').text('检查学号失败');
                return false;
            }
        },
        error: function() {
            $('#studentNoError').text('检查学号失败');
            return false;
        }
    });
}

// 删除学生
function deleteStudent(rowguid) {
    if (confirm('确定要删除这个学生吗？')) {
        $.ajax({
            url: 'delete',
            type: 'POST',
            data: JSON.stringify({ rowguid: rowguid }),
            contentType: 'application/json',
            success: function(response) {
                if (response.code === 200) {
                    alert('删除成功');
                    loadStudentList();
                } else {
                    alert('删除失败: ' + response.message);
                }
            },
            error: function() {
                alert('删除失败');
            }
        });
    }
}

// 批量删除学生
function batchDeleteStudents() {
    var rowguids = [];
    $('.student-checkbox:checked').each(function() {
        rowguids.push($(this).val());
    });

    if (rowguids.length === 0) {
        alert('请选择要删除的学生');
        return;
    }

    if (confirm('确定要删除选中的学生吗？')) {
        $.ajax({
            url: 'batchdelete',
            type: 'POST',
            data: JSON.stringify({ rowguids: rowguids }),
            contentType: 'application/json',
            success: function(response) {
                if (response.code === 200) {
                    alert('删除成功');
                    loadStudentList();
                } else {
                    alert('删除失败: ' + response.message);
                }
            },
            error: function() {
                alert('删除失败');
            }
        });
    }
}

// 打开导入模态框
function openImportModal() {
    // 重置表单
    $('#importForm')[0].reset();
    $('#importResult').hide();
    
    // 打开模态框
    $('#importModal').modal('show');
}

// 上传文件
function uploadFile() {
    var formData = new FormData();
    formData.append('file', $('#importFile')[0].files[0]);

    $.ajax({
        url: 'import',
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        success: function(response) {
            if (response.code === 200) {
                var result = response.data;
                
                // 显示导入结果
                $('#importSuccess').text('成功导入 ' + result.successCount + ' 条数据');
                $('#importFail').text('失败 ' + result.failCount + ' 条数据');
                if (result.failMessages && result.failMessages.length > 0) {
                    $('#importFail').append('<br>失败原因：<br>' + result.failMessages.join('<br>'));
                }
                $('#importResult').show();
                
                // 重新加载学生列表
                loadStudentList();
            } else {
                alert('导入失败: ' + response.message);
            }
        },
        error: function() {
            alert('导入失败');
        }
    });
}

// 打开导出模态框
function openExportModal() {
    // 打开模态框
    $('#exportModal').modal('show');
}

// 导出数据
function exportData() {
    // 获取导出参数
    var exportFormat = $('#exportFormat').val();
    var selectedFields = [];
    $('input[name="exportFields"]:checked').each(function() {
        selectedFields.push($(this).val());
    });

    // 构建请求参数
    var params = {
        selected_fields: selectedFields,
        export_format: exportFormat,
        classguid: $('#classFilter').val(),
        gender: $('#genderFilter').val(),
        status: $('#statusFilter').val()
    };

    // 发送请求
    $.ajax({
        url: 'export',
        type: 'POST',
        data: JSON.stringify(params),
        contentType: 'application/json',
        xhrFields: {
            responseType: 'blob'
        },
        success: function(response) {
            // 创建下载链接
            var url = window.URL.createObjectURL(new Blob([response]));
            var link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', '学生数据.' + (exportFormat === 'excel' ? 'xls' : 'pdf'));
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            
            // 关闭模态框
            $('#exportModal').modal('hide');
        },
        error: function() {
            alert('导出失败');
        }
    });
}

// 下载导入模板
function downloadTemplate() {
    // 发送请求
    window.location.href = 'downloadtemplate';
}
