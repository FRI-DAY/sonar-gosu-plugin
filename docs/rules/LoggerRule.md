Regardless of the logging framework in use (logback, log4j, commons-logging, java.util.logging, ...), loggers should be:

 *  `private`: never be accessible outside of its parent class. If another class needs to log something, it should instantiate its own logger. Remember that fields are `private` by default.
 *  `static`: not be dependent on an instance of a class (an object). When logging something, contextual information can of course be provided in the messages but the logger should be created at class level to prevent creating a logger along with each object.
 *  `final`: be created once and only once per class.

## Noncompliant Code Example ##

With a default regular expression of `LOG(?:GER)?`:

    public var logger = LoggerFactory.getLogger(Foo.class)  // Noncompliant

## Compliant Solution ##

    private static final var LOGGER = LoggerFactory.getLogger(Foo.class)