public class InventoryChaincode extends ChaincodeBase {

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Response addBatch(Context ctx, String batchId, String medicineName, String quantity, String expirationDate) {
        Batch batch = new Batch(batchId, medicineName, quantity, expirationDate);
        // Add batch to the ledger
        ctx.getStub().putState(batchId, batch.toJSON());
        return newSuccessResponse("Batch added to inventory");
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Response getBatch(Context ctx, String batchId) {
        String batchData = ctx.getStub().getState(batchId);
        if (batchData == null || batchData.isEmpty()) {
            return newErrorResponse("Batch not found");
        }
        return newSuccessResponse(batchData);
    }
}
