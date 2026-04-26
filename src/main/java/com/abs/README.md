# Backend 模块结构说明

## 📁 当前目录结构

```
src/main/java/com/abs/
├── system/                 # 系统管理模块
│   ├── api/               # API 接口定义（24个）
│   ├── controller/        # 控制器层（23个）
│   ├── impl/              # 服务实现（24个）
│   ├── domain/            # 实体类（33个）
│   ├── mapper/            # MyBatis Mapper（2个）
│   ├── filter/            # 过滤器和拦截器（9个）
│   ├── util/              # 工具类（25个）
│   └── config/            # 配置类（1个）
│
├── article/               # 文章管理模块
│   ├── api/               # API 接口
│   ├── controller/        # 控制器
│   ├── impl/              # 服务实现
│   └── domain/            # 实体类
│
└── openapi/               # OpenAPI 模块
    ├── api/               # API 接口
    ├── controller/        # 控制器
    └── impl/              # 服务实现
```

## 🎯 模块职责

### system 模块

**核心功能**：
- 用户管理（AbsUserController）
- 角色管理（AbsRoleController）
- 组织管理（AbsOuInfoController）
- 菜单权限（AbsViewInfoController）
- 系统配置（AbsSysConfigController）
- 代码字典（AbsCodeItemController）
- 文件管理（AbsFileInfoController）
- 工作流（AbsFlowInfoController）
- 学生管理（StudentInfoController）
- 班级管理（BaseClassController）
- 考试管理（TsInfoController）

**子目录说明**：

#### api/ - API 接口定义
- 定义服务接口
- 共 24 个接口文件
- 示例：`IAbsUserService.java`, `IAbsRoleService.java`

#### controller/ - 控制器层
- 处理 HTTP 请求
- 共 23 个控制器
- 示例：`AbsUserController.java`, `AbsRoleController.java`

#### impl/ - 服务实现
- 实现业务逻辑
- 共 24 个实现类
- 示例：`AbsUserServiceImpl.java`, `AbsRoleServiceImpl.java`

#### domain/ - 实体类
- 数据库表映射
- 共 33 个实体类
- 示例：`AbsUser.java`, `AbsRole.java`

#### mapper/ - MyBatis Mapper
- 数据访问层
- 共 2 个 Mapper 接口
- XML 配置在 `src/main/resources/mapper/`

#### filter/ - 过滤器和拦截器
- **认证相关**：
  - `AbsHandlerInterceptor.java` - 登录拦截器
  - `NoNeedLogin.java` - 免登录注解
  - `ToToken.java` - Token 参数解析
  - `ToParams.java` - 参数解析
  
- **配置相关**：
  - `AbsWebMvcConfigurerAdapter.java` - MVC 配置
  - `CorsConfig.java` - CORS 配置
  - `AbsHandlerMethodArgumentResolver.java` - 参数解析器
  
- **监听器**：
  - `AbsApplicationListener.java` - 应用监听器
  - `AbsApplicationClosedEventListener.java` - 关闭事件监听

#### util/ - 工具类（25个）
- **认证工具**：
  - `AbsSessionHelper.java` - 会话管理
  - `TokenUtil.java` - Token 工具
  
- **HTTP 工具**：
  - `HttpClientFactory.java` - HTTP 客户端工厂
  - `OkHttpUtil.java` - OkHttp 封装
  
- **数据处理**：
  - `BaseBean.java` - 基础 Bean
  - `ConstantUtil.java` - 常量定义
  - `MSG.java` - 消息常量
  
- **浏览器自动化**：
  - `ChromeKiller.java` - Chrome 进程管理
  - `SeleniumUtil.java` - Selenium 工具
  
- **其他工具**：
  - 共 25 个工具类，涵盖各种辅助功能

#### config/ - 配置类
- Spring Boot 配置
- 目前 1 个配置文件

### article 模块

**核心功能**：
- 文章发布管理
- 文章分类管理
- 文章状态管理（草稿/待审核/审核通过）

**结构**：
- `api/` - 文章服务接口
- `controller/` - ArticleController（文章管理控制器）
- `impl/` - 文章服务实现
- `domain/` - 文章实体类

### openapi 模块

**核心功能**：
- 第三方 API 集成
- 股票数据接口
- 应用密钥管理

**结构**：
- `api/` - OpenAPI 服务接口
- `controller/` - StockOpenApiController
- `impl/` - 服务实现

## 📝 最佳实践

### 1. 分层架构

```
Controller → Service (api) → Service Impl → Mapper → Database
```

**示例**：
```java
// Controller 层
@RestController
@RequestMapping("/user")
public class AbsUserController {
    @Autowired
    private IAbsUserService userService;
    
    @PostMapping("/list")
    public JSONObject getUserList(@RequestBody Map<String, Object> params) {
        return userService.getUserList(params);
    }
}

// Service 接口
public interface IAbsUserService {
    JSONObject getUserList(Map<String, Object> params);
}

// Service 实现
@Service
public class AbsUserServiceImpl implements IAbsUserService {
    @Override
    public JSONObject getUserList(Map<String, Object> params) {
        // 业务逻辑
        return result;
    }
}
```

### 2. 命名规范

- **Controller**：`*Controller.java`
- **Service 接口**：`I*Service.java`
- **Service 实现**：`*ServiceImpl.java`
- **Entity**：`*.java`（与表名对应）
- **Mapper**：`*Mapper.java`

### 3. 工具类使用

**通用工具**放在 `com.abs.system.util/`：
- 所有模块都可以使用
- 无状态、线程安全
- 静态方法为主

**模块专用工具**放在各自模块的 `util/` 子目录

### 4. 过滤器配置

**拦截器注册**：
```java
@Configuration
public class AbsWebMvcConfigurerAdapter implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AbsHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/api/ai/*");
    }
}
```

**免登录注解**：
```java
@NoNeedLogin  // 添加此注解的接口不需要登录
@PostMapping("/public/data")
public JSONObject getPublicData() {
    return data;
}
```

## 🔧 开发指南

### 添加新功能模块

1. **创建模块目录**
   ```
   com/abs/newmodule/
   ├── api/
   ├── controller/
   ├── impl/
   ├── domain/
   └── mapper/
   ```

2. **创建实体类**（domain/）
   ```java
   @Data
   @TableName("new_table")
   public class NewEntity {
       @TableId
       private String rowguid;
       // 其他字段
   }
   ```

3. **创建 Mapper**（mapper/）
   ```java
   @Mapper
   public interface NewMapper extends BaseMapper<NewEntity> {
   }
   ```

4. **创建 Service 接口**（api/）
   ```java
   public interface INewService {
       JSONObject getList(Map<String, Object> params);
   }
   ```

5. **创建 Service 实现**（impl/）
   ```java
   @Service
   public class NewServiceImpl implements INewService {
       @Autowired
       private NewMapper newMapper;
       
       @Override
       public JSONObject getList(Map<String, Object> params) {
           // 业务逻辑
           return result;
       }
   }
   ```

6. **创建 Controller**（controller/）
   ```java
   @RestController
   @RequestMapping("/newmodule")
   public class NewController {
       @Autowired
       private INewService newService;
       
       @PostMapping("/list")
       public JSONObject getList(@RequestBody Map<String, Object> params) {
           return newService.getList(params);
       }
   }
   ```

7. **更新组件扫描**
   ```java
   // EduFlowMain.java
   @ComponentScan({ "com.abs.system", "com.abs.article", "com.abs.openapi", "com.abs.newmodule" })
   ```

### 添加工具类

1. 在 `com.abs.system.util/` 创建工具类
2. 使用 `@Component` 或静态方法
3. 确保线程安全
4. 添加详细注释

---

**最后更新**：2026-04-26  
**维护者**：EduFlow Backend Team
