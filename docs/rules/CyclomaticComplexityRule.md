Cyclomatic complexity should not be greater than threshold

Complex code can perform poorly and will in any case be difficult to understand and therefore to maintain.

## Exceptions ##

While having a large number of fields in a class may indicate that it should be split, this rule nonetheless ignores high complexity in `equals` and `hashCode` methods.