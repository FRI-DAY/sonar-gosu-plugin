package checks

uses friday.cc.gdv.GdvInvoiceWrapper

class ok {

  function sampleFunc() {
    final var gdvWrapper = new GdvInvoiceWrapper(gdv)
    gdvWrapper.doSth()
  }

}
