FROM postgis/postgis:17-master AS final
COPY script/init.sql /docker-entrypoint-initdb.d/
EXPOSE 5432
