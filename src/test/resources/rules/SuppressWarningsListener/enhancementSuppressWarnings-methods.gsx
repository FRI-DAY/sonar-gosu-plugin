package friday.enhancement

uses java.math.BigDecimal

enhancement SampleEnhancement: BigDecimal {

  @SuppressWarnings("gosu:MagicNumbersRule")
      property get DummyRandomNumber(): BigDecimal {
    return BigDecimal.ONE.multiply(345bd)
  }

  @SuppressWarnings("gosu:MagicNumbersRule")
      property set SecondRandomNumber(value: int) {
    if (value < 0 or value > 100) {
      throw new IllegalArgumentException("Number must be between 0 and 100, as it's a percentage.")
    }
    var reductionAsDecimal = BigDecimal.valueOf(value).divide(100bd)
  }

  @SuppressWarnings("gosu:MagicNumbersRule")
  function createDummyNumber() {
    var no = BigDecimal.ZERO.subtract(-9.5bd)
  }

}
