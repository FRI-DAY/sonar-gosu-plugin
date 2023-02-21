package checks.InternalImportsCheck

uses com.guidewire.pl.system.bundle.TransactionUtil
uses friday.internal.packages.*
uses com.guidewire.*

class nok {
  function withInternalImport() {
    var api = com.guidewire.pl.system.internal.InternalMethods.asWorkflowInternal(writableWorkflow)
    var api2 = com.gw.pl.system.internal.InternalMethods.asWorkflowInternal(writableWorkflow).SomeMethod()
  }

  class innerClass {
    function withInternalImport2(){
      var api = com.sth.guidewire.pl.system.internal.InternalMethods.asWorkflowInternal(writableWorkflow)
    }
  }
}
