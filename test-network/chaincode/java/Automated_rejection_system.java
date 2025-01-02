public class RejectionChaincode extends ChaincodeBase {

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Response rejectMedicine(Context ctx, String batchId, String reason) {
        Rejection rejection = new Rejection(batchId, reason, LocalDateTime.now().toString());
        // Store rejection on ledger
        ctx.getStub().putState(batchId + "_rejected", rejection.toJSON());
        return newSuccessResponse("Medicine rejected due to: " + reason);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Response getRejectionLog(Context ctx, String batchId) {
        String rejectionData = ctx.getStub().getState(batchId + "_rejected");
        if (rejectionData == null || rejectionData.isEmpty()) {
            return newErrorResponse("No rejection record found for this batch");
        }
        return newSuccessResponse(rejectionData);
    }
}
