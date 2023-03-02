package checks.UnusedParameterCheck

final class finalClass implements PDFGenerationClient {

  override function overriddenFunction(payload: String, requestUri: String): PDFContentInfoResponse {
    return new PDFContentInfoResponse(HttpStatus.SC_OK, "", new DocumentContentsInfo(
        ContentResponseType.DOCUMENT_CONTENTS, new NullInputStream(0), "application/pdf"))
  }

  function defaultFunction(param1 : int) {
    doSomething()
  }

  public function publicFunction(param1 : int) {
    doSomething()
  }

  private static function doSomething(){

  }
}
