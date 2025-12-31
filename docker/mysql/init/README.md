# MySQL 初始化脚本说明

将您的 SQL 初始化脚本放在此目录中。文件会按字母顺序执行。

示例：
- 01-create-database.sql
- 02-create-tables.sql
- 03-insert-data.sql

注意：
1. 使用环境变量中的数据库名称、用户名和密码
2. docker-compose 启动时会自动执行这些脚本
3. 只有在首次创建容器时才会执行这些脚本
