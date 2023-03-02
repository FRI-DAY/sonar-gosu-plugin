package checks.HardcodedEntityFieldValueRule

class nok {
  var contactContacts = new ArrayList<Contact>();

  function visit(node : KeyableBean) {
    if (node.PublicID != null && (node typeis AddressBookLinkable || node typeis AddressBookConvertable) && !contactContacts.contains(node.getFieldValue("AddressBookUID"))) {
      node.setFieldValue("AddressBookUID", null)
    }
  }

  public function somefunc(person : Person) {
    person.getFieldValue("LastUpdateTime");                   //Noncompliant
  }
}
