package de.friday.batches

uses de.friday.command.Argument
uses de.friday.command.DefaultMethod
uses de.friday.batches.utils.BatchProcessUtil

@Export
@DefaultMethod("withDefault")
class BatchProcesses extends BaseBatchCommand {

  function financialsEscalation() {
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_FINANCIALSESC, 100000, {})
  }

  function bulkInvoicesEscalation() {
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_BULKINVOICEESC, 100000, {})
  }

  function exchangeRate() {
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_EXCHANGERATE, 100000, {})
  }

  //Added the full list of available batch processes
  @Argument("Batch Process", BatchProcessType.getTypeKeys(false)*.toString())
  function byName() {
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(typekey.BatchProcessType.get(Argument.toLowerCase()), 100000, {})
  }

  function withDefault() {
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_ACTIVITYESC, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_AGGLIMITCALC, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_BULKINVOICEESC, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_BULKINVOICESUBMISSION, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_BULKINVOICEWF, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_BULKPURGE, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_CATASTROPHECLAIMFINDER, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_CLAIMCONTACTSCALC, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_CLAIMEXCEPTION, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_CLAIMHEALTHCALC, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_CONTACTAUTOSYNC, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_DASHBOARDSTATISTICS, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_EXCHANGERATE, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_FINANCIALSCALC, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_FINANCIALSESC, 100000, {})
    //BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd( BatchProcessType.TC_GEOCODE, 100000, {} )
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_GROUPEXCEPTION, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_REVIEWSYNC, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_STATISTICS, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_TACCOUNTESC, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_USEREXCEPTION, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_WORKFLOW, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_WORKQUEUEINSTRUMENTATIONPURGE, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_DATADISTRIBUTION, 100000, {})
    BatchProcessUtil.startAndWaitUntilWorkFinishedEndToEnd(BatchProcessType.TC_DBSTATS, 100000, null)
  }
}
