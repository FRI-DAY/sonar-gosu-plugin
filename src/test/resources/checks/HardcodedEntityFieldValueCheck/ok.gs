package checks.HardcodedEntityFieldValueCheck

class ok {
  var contactContacts = new ArrayList<Contact>();

  function somefunc(person : Person) {
    person.getFieldValue(Person#LastName.PropertyInfo.Name);
  }

  function visit(node : KeyableBean) {
    if (node.PublicID != null && (node typeis AddressBookLinkable || node typeis AddressBookConvertable) && !contactContacts.contains(node.getFieldValue(Contact#AddressBookUID.PropertyInfo.Name))) {
      node.setFieldValue(Contact#AddressBookUID.PropertyInfo.Name, null)
    }
  }

  function methodCalls(person : Person){
    var methodCallWithoutArgument = person.getCellPhone()
    Bundle.add(op)
    Bumdle<T>(op)
  }
}
