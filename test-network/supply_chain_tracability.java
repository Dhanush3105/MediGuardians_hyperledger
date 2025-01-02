public class TraceabilityChaincode extends ChaincodeBase {

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Response traceBatch(Context ctx, String batchId, String event) {
        TraceEvent traceEvent = new TraceEvent(batchId, event, LocalDateTime.now().toString());
        // Add trace event to ledger
        ctx.getStub().putState(batchId + "trace" + LocalDateTime.now().toString(), traceEvent.toJSON());
        return newSuccessResponse("Trace event added successfully");
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Response getTrace(Context ctx, String batchId) {
        // Retrieve all trace events for the batch
        String traceData = ctx.getStub().getState(batchId);
        if (traceData == null || traceData.isEmpty()) {
            return newErrorResponse("No trace data found for this batch");
        }
        return newSuccessResponse(traceData);
    }
}
