package checks

uses friday.cc.gdv.GdvInvoiceWrapper
uses de.friday.database.Relop
uses de.friday.database.Restriction
uses de.friday.products.ProductCodes

uses Relop#Equals
uses ProductCodes#*

class ok {

  function sampleFunc() {
    final var gdvWrapper = new GdvInvoiceWrapper(gdv)
    gdvWrapper.doSth(Equals, Restriction#NotIn, SomeProduct)
  }

}
