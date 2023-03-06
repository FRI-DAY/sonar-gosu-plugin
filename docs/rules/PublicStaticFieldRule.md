There is no good reason to declare a field "public" and "static" without also declaring it "final". Most of the time this is a kludge to share a state among several objects. But with this approach, any object can do whatever it wants with the shared state, such as setting it to `null`.

## Noncompliant Code Example ##

    public class Greeter {
      public static var FOO : Foo = new Foo();
      ...
    }

## Compliant Solution ##

    public class Greeter {
      public static final var FOO : Foo = new Foo();
      ...
    }

## See ##

 *  [MITRE, CWE-500][MITRE_ CWE-500] \- Public Static Field Not Marked Final
 *  [CERT OBJ10-J.][] \- Do not use public static nonfinal fields


[MITRE_ CWE-500]: http://cwe.mitre.org/data/definitions/500.html
[CERT OBJ10-J.]: https://www.securecoding.cert.org/confluence/x/QQBqAQ