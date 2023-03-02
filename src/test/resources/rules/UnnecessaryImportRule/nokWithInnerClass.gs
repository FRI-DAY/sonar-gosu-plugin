package de.friday.policies.api

uses de.friday.api.rest.Response
uses de.friday.api.rest.validations.OutputValidator
uses de.friday.service.policies.OrderService
uses de.friday.service.OrderServiceCommand

final class PoliciesApi extends BaseApi<PolicyRequest, OrderServiceCommand> {

  static class PoliciesInboundValidations extends InboundValidations {
    construct() {
      super({
          new IbanValidation(),
          new PostalCodesValidation()
      })
    }


    class AnotherInnerClass {

    }
  }

  construct() {
    super(
        new OrderService(),
        new PoliciesInboundValidations()
    )
  }

    override function processRequest(body: PolicyRequest): Response {
      return super.processRequest(body)
    }
}
