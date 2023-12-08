FROM openjdk:17-oracle
EXPOSE 8081 9252

ADD target/car-rental-manager.jar car-rental-manager.jar
ADD docker-image-debug-entrypoint.sh docker-image-debug-entrypoint.sh

ENTRYPOINT ["sh", "/docker-image-debug-entrypoint.sh"]
