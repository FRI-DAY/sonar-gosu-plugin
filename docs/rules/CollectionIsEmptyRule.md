### Collections ###

Using `Collection.size()` to test for emptiness works, but using `Collection.isEmpty()` or `Collection.Empty` makes the code more readable and can be more performant. The time complexity of any `isEmpty()` method implementation should be `O(1)` whereas some implementations of `size()` can be `O(n)`.

### Queries ###

If you want to know only whether a result or query object fetched anything from the application database, use the `Empty` property instead of the `Count` property. The value of the `Empty` property returns to your Gosu code faster, because the evaluation stops as soon as it counts at least one fetched item. In contrast, the value of the `Count` property returns to your Gosu only after counting all fetched items.

### Length property ###

`length` property is usually the same as `isEmpty()` in performance.

`isEmpty()`, however states what you're actually interested in more clearly

## Noncompliant Code Example ##

    if (myCollection.size() == 0) {  // Noncompliant
      /* ... */
    }

    var result = policyPeriodQuery.select()
        if (result.Count == 0) {  // Noncompliant
     /* ... */
    }

## Compliant Solution ##

    if (myCollection.isEmpty()) {
      /* ... */
    }

    if (myCollection.Empty) {
      /* ... */
    }

    var result = policyPeriodQuery.select()
        if (result.Empty) {
     /* ... */
    }