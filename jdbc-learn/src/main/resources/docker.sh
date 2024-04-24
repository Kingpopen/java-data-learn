# 创建docker的脚本
docker run -d \
--name mysql \
-p 13306:3306 \
-v mysql_data:/var/lib/mysql \
-v mysql_log:/var/log/mysql \
-v mysql_config:/etc/mysql \
-e MYSQL_ROOT_PASSWORD=root \
mysql:latest