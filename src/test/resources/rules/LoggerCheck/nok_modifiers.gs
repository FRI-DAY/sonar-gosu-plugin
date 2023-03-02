package checks.LoggerCheck

uses org.slf4j.LoggerFactory

class nok_modifiers {

  public static final var LOGGER = LoggerFactory.getLogger(nok_modifiers)
  protected static final var LOG = LoggerFactory.getLogger(nok_modifiers)

  function sampleFunc() {
  }
}
