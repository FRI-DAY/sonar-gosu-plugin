It is needlessly complex to invert the result of a boolean comparison. The opposite comparison should be made instead.

## Noncompliant Code Example ##

    if ( !(a == 2)) { ...}  // Noncompliant
    var b = !(i < 10);  // Noncompliant

## Compliant Solution ##

    if (a != 2) { ...}
    var b = (i >= 10);