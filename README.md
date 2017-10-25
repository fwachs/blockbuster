# blockbuster

How to start the blockbuster application
---

1. Run `mvn clean install` to build your application
2. Execute `java -jar target/blockbuster-1.0-SNAPSHOT.jar db migrate config.yml`
3. Start application with `java -jar target/blockbuster-1.0-SNAPSHOT.jar server config.yml`
4. To check that your application is running enter url `http://localhost:8080/film`
