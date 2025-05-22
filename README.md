# JavaReview

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-11+-orange.svg)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen.svg)]()

> 一个基于SpringBoot的本地生活服务点评平台，实现用户注册登录、商户查询、优惠券秒杀、附近商户推荐等功能。

## 📌 项目简介
模仿大众点评实现的本地生活服务类平台，核心功能包括：
- 分布式Session登录
- 商户信息缓存（Redis + 缓存击穿解决方案）
- 优惠券秒杀（Redis分布式锁 + 异步下单）
- 附近商户（GEO地理位置计算）
- 好友关注Feed流（推拉结合模式）



## 🛠️ 技术栈
| 技术       | 说明                  |
|------------|----------------------|
| SpringBoot | 核心框架              |
| MyBatis-Plus | 数据访问层           |
| Redis      | 缓存/分布式锁/GEO     |
| RabbitMQ   | 异步任务队列          |
| MySQL      | 主数据库              |
| Hutool     | Java工具库            |

## 🔍 核心模块
| 模块          | 包路径               | 说明                          |
|---------------|---------------------|-----------------------------|
| 配置层        | `com.hmdp.config`   | Redis/Web/拦截器等配置          |
| 控制层        | `com.hmdp.controller` | 暴露RESTful API接口            |
| 业务逻辑层    | `com.hmdp.service`  | 核心业务实现（含`impl`实现类）    |
| 数据访问层    | `com.hmdp.mapper`   | MyBatis-Plus数据库操作接口      |
| 工具类        | `com.hmdp.utils`    | 通用工具/JWT/Redis工具等        |


## 🚀 快速开始
1. **环境准备**
    - JDK 11+
    - MySQL 8.0
    - Redis 7.0
    - RabbitMQ 3.12

2. **初始化配置**
```bash
# 克隆项目
git clone https://github.com/yourusername/heimdianping.git
cd heimdianping

# 修改application.yml中的数据库和Redis配置
vim src/main/resources/application.yaml

# 开发环境运行
mvn spring-boot:run

# 生产环境打包
mvn clean package -DskipTests
java -jar target/JavaReview-1.0.0.jar
```

## 🔥 特色功能技术实现详解
### 1. 分布式会话与安全校验体系
**技术方案**：
- 基于JWT+Redis实现无状态登录验证，Token存储用户关键信息并设置短期有效期，Redis存储全量会话数据（如设备指纹），通过双重校验杜绝多端并发登录
- 会话刷新采用滑动过期机制，Redis中会话数据设置30分钟TTL，每次请求自动续期，平衡安全性与用户体验  
### 2. 多级缓存抗压架构

- 布隆过滤器拦截非法ID请求（10^6数据量误判率<0.1%）
- 逻辑过期时间+异步刷新机制实现缓存无缝续命，QPS 3w+场景下零穿透



### 3. 分布式锁集群解决方案

技术创新点：
-    Redisson可重入锁+看门狗机制实现锁自动续期（默认30秒检测/续期）
-  锁释放增加线程指纹校验，避免误删其他线程锁（Lua脚本原子操作）


### 4. 异步削峰订单系统

架构亮点：
-    预库存校验：Redis原子操作递减库存（DECR命令）
-    MQ消息设计：
      - 独立死信队列处理超时订单
      - 设置状态机实现消费幂等性
-    分库分表：用户ID取模分散订单数据，缓解MySQL写入压力

## 📜 许可证

本项目采用MIT许可证 - 详情请见[LICENSE](https://opensource.org/license/mit)文件。



















