FROM openjdk:17-oracle
EXPOSE 8080 9251

ADD target/car-rental-api.jar car-rental-api.jar
ADD docker-image-debug-entrypoint.sh docker-image-debug-entrypoint.sh

ENTRYPOINT ["sh", "/docker-image-debug-entrypoint.sh"]
