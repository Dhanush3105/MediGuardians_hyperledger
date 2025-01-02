package org.example.chaincode;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ChaincodeResponse;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

public class DataStorageChaincode extends ChaincodeBase {

    @Override
    public ChaincodeResponse init(ChaincodeStub stub) {
        // Initialization (if any)
        return ChaincodeResponse.makeSuccessResponse("Initialization complete");
    }

    @Override
    public ChaincodeResponse invoke(ChaincodeStub stub) {
        String func = stub.getFunction();
        String[] params = stub.getParameters().toArray(new String[0]);

        switch (func) {
            case "set": 
                return setData(stub, params);
            case "get": 
                return getData(stub, params);
            default:
                return ChaincodeResponse.makeFailureResponse("Invalid function name");
        }
    }

    private ChaincodeResponse setData(ChaincodeStub stub, String[] params) {
        if (params.length != 2) {
            return ChaincodeResponse.makeFailureResponse("Incorrect number of arguments. Expecting 2");
        }

        // Save the data to the ledger using a key-value pair
        String key = params[0];
        String value = params[1];

        stub.putState(key, value.getBytes());

        return ChaincodeResponse.makeSuccessResponse("Data inserted with key: " + key + " and value: " + value);
    }

    private ChaincodeResponse getData(ChaincodeStub stub, String[] params) {
        if (params.length != 1) {
            return ChaincodeResponse.makeFailureResponse("Incorrect number of arguments. Expecting 1");
        }

        String key = params[0];
        byte[] value = stub.getState(key);

        if (value == null || value.length == 0) {
            return ChaincodeResponse.makeFailureResponse("No data found for key: " + key);
        }

        return ChaincodeResponse.makeSuccessResponse(new String(value));
    }

    public static void main(String[] args) {
        DataStorageChaincode chaincode = new DataStorageChaincode();
        try {
            // Start the chaincode
            Runtime.getRuntime().exec("peer chaincode start");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
