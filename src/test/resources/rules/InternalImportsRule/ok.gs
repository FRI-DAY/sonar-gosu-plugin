package checks.InternalImportsRule

uses com.friday.pl.system.bundle.TransactionUtil
uses friday.internal.packages.*
uses com.friday.*
uses de.friday.database.Relop

uses Relop#Equals

class ok {
  function withInternalImport() {
    var simpleMethodCall = someMethod()
    var api = com.friday.pl.system.friday.InternalMethods.asWorkflowInternal(writableWorkflow)
    var api2 = com.gw.pl.system.friday.InternalMethods.asWorkflowInternal(writableWorkflow).SomeMethod()
  }

  class innerClass {
    function withInternalImport2(){
      var api = com.friday.pl.system.internal.InternalMethods.asWorkflowInternal(writableWorkflow)
    }
  }
}
