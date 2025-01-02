public class TransactionChaincode extends ChaincodeBase {

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Response logTransaction(Context ctx, String transactionId, String batchId, String from, String to, String timestamp) {
        TransactionLog transactionLog = new TransactionLog(transactionId, batchId, from, to, timestamp);
        // Store transaction on ledger
        ctx.getStub().putState(transactionId, transactionLog.toJSON());
        return newSuccessResponse("Transaction logged successfully");
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Response getTransactionLog(Context ctx, String transactionId) {
        String transactionData = ctx.getStub().getState(transactionId);
        if (transactionData == null || transactionData.isEmpty()) {
            return newErrorResponse("Transaction not found");
        }
        return newSuccessResponse(transactionData);
    }
}
