Base.Platform Project

=====================
Base.Facade
=====================

Base prototypes for Facade layer(EJB/MDB, REST):

1. Provides a bridge between REST API web layer (Spring Rest controllers) and API function (commands) implementation
in Spring core throw EJB interfaces. Using EJB give us a strong transacted environment.

   A final AbstractAPIController can be used for implement owns REST controller connected with business logic via
   APICommandFactory pattern from Base.Services subproject.

2. Exports some strategy-pattern-based implementation for Message-Driven Enterprise Beans and creates also a bridge 
to spring beans to write business logic for message processing.      


3. Exports additional JmsConfig for fast configuring access to Spring JMS Template.

4. Uses a Embedded Glassfish to JUnit test and exports some configuration for writing such tests.
 