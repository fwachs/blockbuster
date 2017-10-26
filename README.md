# blockbuster

How to start the blockbuster application
---

1. Run `mvn clean install` to build your application
2. Execute `java -jar target/blockbuster-1.0-SNAPSHOT.jar db migrate config.yml`
3. Start application with `java -jar target/blockbuster-1.0-SNAPSHOT.jar server config.yml`
4. To check that your application is running enter url `http://localhost:8080/film`
5. Check HTTP examples here: `https://www.getpostman.com/collections/5ce1f6619c74d7692100`

Notes:

Why I didn't use the Strategy Pattern ?
---
Because I didn't like lack of flexibility that this provides. 
If we were to have a new Film Type for any reason, we would need to modify the code and release a new version of the application.
By having those types on the db, my app can be flexible and won't need downtime to perform updates on the price, extra days, and so on.

Why test coverage is so low?
---
I did my best with the little time I have. I have a 6 months old baby boy who's chewing up my time :)

If time was short, wasn't TDD better?
---
Looking back at all the time I spent working on this, yes. I could have left out the db part and make sure that the entire service and model parts were working perfectly

Hibernate
---
I haven't used Hibernate for around 5 years. You may be able to tell by my code already. I was used to working with Spring IOC and Spring MVC with plain JDBC, which required a lot of code ( the jdbc part ). Having to use dropwizard.io was a fun ride but it came with a cost on some parts I believe. The DAO section was one of those. I am not sure I'm handling everything there as good as I should, perhaps with more time I could make a better effort!

