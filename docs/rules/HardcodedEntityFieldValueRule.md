To get entity value you should not use hardcoded Strings. Instead, get entity value by property name.

## Noncompliant Code Example ##

    entity.getFieldValue("LastUpdateTime")

## Compliant Solution ##

    entity.getFieldValue(Person#LastUpdateTime.PropertyInfo.Name)