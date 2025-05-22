FROM mysql:8.0

# ← these will be supplied via env vars at runtime
ENV MYSQL_DATABASE=${MYSQL_DATABASE}
ENV MYSQL_USER=${MYSQL_USER}
ENV MYSQL_PASSWORD=${MYSQL_PASSWORD}
ENV MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}

# init.sql will be run on first launch
COPY init.sql /docker-entrypoint-initdb.d/

EXPOSE 3306
