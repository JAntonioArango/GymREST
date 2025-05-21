FROM mysql:8.0

ENV MYSQL_DATABASE=gymdb \
    MYSQL_USER=gymuser \
    MYSQL_PASSWORD=gympass \
    MYSQL_ROOT_PASSWORD=rootpass

COPY init.sql /docker-entrypoint-initdb.d/
