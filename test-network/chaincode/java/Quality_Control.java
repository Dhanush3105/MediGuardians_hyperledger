public class QualityControlChaincode extends ChaincodeBase {

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Response addQualityCheck(Context ctx, String batchId, String temperature, String humidity) {
        String checkId = UUID.randomUUID().toString();
        QualityCheck qualityCheck = new QualityCheck(checkId, batchId, temperature, humidity);
        // Store quality check on ledger
        ctx.getStub().putState(checkId, qualityCheck.toJSON());
        return newSuccessResponse("Quality check added successfully");
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Response getQualityCheck(Context ctx, String checkId) {
        String checkData = ctx.getStub().getState(checkId);
        if (checkData == null || checkData.isEmpty()) {
            return newErrorResponse("Quality check not found");
        }
        return newSuccessResponse(checkData);
    }
}
