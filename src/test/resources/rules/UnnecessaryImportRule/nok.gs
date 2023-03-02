package checks

uses friday.cc.gdv.GdvInvoiceWrapper
uses java.lang.Object
uses typekey.SalvageStatus
uses entity.Claim
uses checks.OtherClass
uses checks.nested.UnusedImport
uses checks.nested.SomethingElse
uses friday.cc.gdv.GdvInvoiceWrapper

class nok {

  function sampleFunc(s: String, i: int, gdvWrapper: GdvInvoiceWrapper): Claim {
    final var object = new Object()
    final var otherClass = new OtherClass()
    final var somethingElse = new SomethingElse()
    final var claim = new Claim() {
      :SalvageStatus = SalvageStatus.FakeStatus,
      :Object = object
    }

    return claim
  }

}
