[![Build Status](https://travis-ci.org/anr-ru/base.facade.svg?branch=master)](https://travis-ci.org/anr-ru/base.facade)

## A part of [Base.Platform Project](https://github.com/anr-ru/base.platform.parent)

# Base.Facade

Base prototypes for the Facade layer(EJB/MDB, REST):

1. Provides a bridge between REST API web layer (Spring Rest controllers) and API function (commands) implementation
in the business logic via EJB interfaces. Usage of EJB gives a strong transaction environment.

   A AbstractAPIController class can be used for implementing own REST controllers connected to the business logic via
   APICommandFactory pattern from the [Base.Services](https://github.com/anr-ru/base.services) project.

2. Exports some strategy-pattern-based implementation for Message-Driven Enterprise (MDB) Beans and creates also a bridge 
to spring beans enabling to create a business logic for message processing.      

3. Exports additional JmsConfig for fast configuring access to Spring JMS Template.

4. Uses a Embedded Glassfish for JUnit tests and exports some configuration for writing such tests.
 