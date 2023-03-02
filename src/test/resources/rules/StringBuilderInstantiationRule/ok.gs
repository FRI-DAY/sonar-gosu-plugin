package checks.StringBuilderInstantiationRule

class ok {

  function someFunc() {
    var builder = new StringBuilder("someString")
    var secondBuilder = new StringBuilder(24)
    var thirdBuilder = new StringBuilder()
    var buffer = new StringBuffer("someString")
    var secondBuffer = new StringBuffer(24)
    var thirdBuffer = new StringBuffer()

    var noArguments = new String[12]
    var noClasOrInterfaceType : String
    noClasOrInterfaceType = new()
    var date = new Date(123)
  }
}
