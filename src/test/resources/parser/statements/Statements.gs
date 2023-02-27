package parser.statements

class Statements {

  function assignment() {
    //var argOne : int
    //argOne = 3

    //argOne = 5


    //argOne = new Integer(13)
  }

  function ifStatement(a : int, b : int) {
    if (a == b) {
      //Do something
    } else if (a < b) {
      //Do something
    } else if (a > b) {
      //Do something
    } else {
      //
    }

    if (a == b) {
      //No else statement
    }

    if (a == b) {
      //One else statement
    } else {
      //Do something
    }
  }

  function tryCatchFinally() {
    try {
      var abc = new Integer(2)
    } catch (e : InternalError) {
      //Do something
    } catch (e : Exception) {
      //Do something
    } finally {
      //Do something
    }

    try {
      var abc = new Integer(2)
    } finally {
      //Do something
    }
  }

  function throwStatement() {
    throw new AssertionError()
  }

  function returnStatement0() : int {
    return 0
  }

  function returnStatement1() : int {
    return 1 * 2 - 3 + 4
  }

  function forEachStatement() {
    var a = 3
    var b = 5

    for (i in 0..5) {
    }
    for (i in 0..|5) {
    }
    for (i in 0|..|5) {
    }
    for (i in 0|..5) {
    }

    for (i in a..b) {
    }
    for (i in a|..b) {
    }
    for (i in a..|b) {
    }
    for (i in a|..|b) {
    }

    for (i in 0..b) {
    }
    for (i in 0|..b) {
    }
    for (i in 0..|b) {
    }
    for (i in 0|..|b) {
    }

    for (i in a..0) {
    }
    for (i in a|..0) {
    }
    for (i in a..|0) {
    }
    for (i in a|..|0) {
    }
  }

  function builderCase() {
    (\-> {
      new de.friday.samples.RegionZoneBuilder()
          .withCode("IL:Henderson")
          .withCountry(TC_US)
          .withPublicId("demo_sample:230")
          .onRegion(regionDemoSample26)
          .withZoneType(TC_COUNTY)
          .create(bundle)

    })()
  }

  function stringLiteral() {
    var a = "Some String \ -> some other String"
    var b = "Invalid JSON-RPC 2.0: jsonrpc version must be \"2.0\"."
    var c = b.replace(':', '-').replace('\\', '$').replace('/', '_')
    var d = "$"
    var e = '\\$'
  }

  function advancedComments() {

    /*
      SQL =SELECT
      /* KeyTable:px_pddestructionrequest; */
      qroot.id col0
      FROM   px_pddestructionrequest qRoot
      WHERE  NOT EXISTS
      (
      SELECT groot.pddestructionrequest col0
      FROM   px_contactdestructionreq gRoot
      WHERE  groot.pddestructionrequest = qroot.id
      AND    groot.status NOT IN (?,
      ?,
      ?)
      AND    groot.pddestructionrequest = qroot.id)
      AND    qroot.requestersnotified = ? [1 (typekey), 4 (typekey), 5 (typekey), false (bit)]
     */


    /**
     SQL =SELECT
     /* KeyTable:px_pddestructionrequest; */
     qroot.id col0
     FROM   px_pddestructionrequest qRoot
     WHERE  NOT EXISTS
     (
     SELECT groot.pddestructionrequest col0
     FROM   px_contactdestructionreq gRoot
     WHERE  groot.pddestructionrequest = qroot.id
     AND    groot.status NOT IN (?,
     ?,
     ?)
     AND    groot.pddestructionrequest = qroot.id)
     AND    qroot.requestersnotified = ? [1 (typekey), 4 (typekey), 5 (typekey), false (bit)]
     */
  }

  //private static function getAbsolutePath(path : String, rootPath : String) : String {
  //  var retVal = path
  //  if (path.startsWith("\\") || path.startsWith("/") || (path.length() > 1 && path.charAt(1) == Coercions.makePCharFrom(":"))) {
  //    retVal = path
  //  } else {
  //    retVal = rootPath + File.separator + path
  //  }
  //  try {
  //    retVal = (new File(retVal)).getCanonicalPath()
  //  } catch (e : IOException) {
  //    throw new RuntimeException("Could not get absolute path from relative path: ${path}", e)
  //  }
  //  return retVal.replaceAll("\\\\","/")
  //}
}
