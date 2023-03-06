Instantiating a `StringBuilder` or a `StringBuffer` with a character is misleading because most Java(Gosu) developers would expect the character to be the initial value of the `StringBuffer`.

What actually happens is that the int representation of the character is used to determine the initial size of the `StringBuffer`.

## Noncompliant Code Example ##

    var foo = new StringBuffer('x');   //equivalent to var foo = new StringBuffer(120);

## Compliant Solution ##

    var foo = new StringBuffer("x");