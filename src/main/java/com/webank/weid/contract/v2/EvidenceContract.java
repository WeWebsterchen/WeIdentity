package com.webank.weid.contract.v2;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.abi.datatypes.Bool;
import org.fisco.bcos.web3j.abi.datatypes.Event;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.Utf8String;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.DefaultBlockParameter;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.request.BcosFilter;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tx.Contract;
import org.fisco.bcos.web3j.tx.TransactionManager;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.fisco.bcos.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version none.
 */
@SuppressWarnings("unchecked")
public class EvidenceContract extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50610d9d806100206000396000f300608060405260043610610062576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806348af58da146100675780635ca04cfc146101665780637fbfd5aa146101e3578063c7aa107d14610264575b600080fd5b34801561007357600080fd5b50610164600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929080359060200190929190505050610363565b005b34801561017257600080fd5b506101cd600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061065c565b6040518082815260200191505060405180910390f35b3480156101ef57600080fd5b5061024a600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506106d0565b604051808215151515815260200191505060405180910390f35b34801561027057600080fd5b50610361600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929080359060200190929190505050610759565b005b61036c846106d0565b151561037757610656565b6103b6836040805190810160405280600481526020017f696e666f00000000000000000000000000000000000000000000000000000000815250610c83565b156103c057610656565b7f2a4e3729f8dfdbe83193e0bbcbc01b20a5353d070b076f91f27a24c0897da5a6843385858560008a6040518082805190602001908083835b60208310151561041e57805182526020820191506020810190506020830392506103f9565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390205460405180806020018773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001806020018060200186815260200185815260200184810384528a818151815260200191508051906020019080838360005b838110156104d85780820151818401526020810190506104bd565b50505050905090810190601f1680156105055780820380516001836020036101000a031916815260200191505b50848103835288818151815260200191508051906020019080838360005b8381101561053e578082015181840152602081019050610523565b50505050905090810190601f16801561056b5780820380516001836020036101000a031916815260200191505b50848103825287818151815260200191508051906020019080838360005b838110156105a4578082015181840152602081019050610589565b50505050905090810190601f1680156105d15780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390a1436000856040518082805190602001908083835b60208310151561061e57805182526020820191506020810190506020830392506105f9565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020819055505b50505050565b600080826040518082805190602001908083835b6020831015156106955780518252602082019150602081019050602083039250610670565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020549050919050565b6000806000836040518082805190602001908083835b60208310151561070b57805182526020820191506020810190506020830392506106e6565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390205414151561074f5760019050610754565b600090505b919050565b7f2a4e3729f8dfdbe83193e0bbcbc01b20a5353d070b076f91f27a24c0897da5a684336040805190810160405280600481526020017f696e666f00000000000000000000000000000000000000000000000000000000815250868560008a6040518082805190602001908083835b6020831015156107ec57805182526020820191506020810190506020830392506107c7565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390205460405180806020018773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001806020018060200186815260200185815260200184810384528a818151815260200191508051906020019080838360005b838110156108a657808201518184015260208101905061088b565b50505050905090810190601f1680156108d35780820380516001836020036101000a031916815260200191505b50848103835288818151815260200191508051906020019080838360005b8381101561090c5780820151818401526020810190506108f1565b50505050905090810190601f1680156109395780820380516001836020036101000a031916815260200191505b50848103825287818151815260200191508051906020019080838360005b83811015610972578082015181840152602081019050610957565b50505050905090810190601f16801561099f5780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390a17f2a4e3729f8dfdbe83193e0bbcbc01b20a5353d070b076f91f27a24c0897da5a684336040805190810160405280600581526020017f6578747261000000000000000000000000000000000000000000000000000000815250858560008a6040518082805190602001908083835b602083101515610a465780518252602082019150602081019050602083039250610a21565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390205460405180806020018773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001806020018060200186815260200185815260200184810384528a818151815260200191508051906020019080838360005b83811015610b00578082015181840152602081019050610ae5565b50505050905090810190601f168015610b2d5780820380516001836020036101000a031916815260200191505b50848103835288818151815260200191508051906020019080838360005b83811015610b66578082015181840152602081019050610b4b565b50505050905090810190601f168015610b935780820380516001836020036101000a031916815260200191505b50848103825287818151815260200191508051906020019080838360005b83811015610bcc578082015181840152602081019050610bb1565b50505050905090810190601f168015610bf95780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390a1436000856040518082805190602001908083835b602083101515610c465780518252602082019150602081019050602083039250610c21565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208190555050505050565b600081518351141515610c995760009050610d6b565b816040518082805190602001908083835b602083101515610ccf5780518252602082019150602081019050602083039250610caa565b6001836020036101000a038019825116818451168082178552505050505050905001915050604051809103902060001916836040518082805190602001908083835b602083101515610d365780518252602082019150602081019050602083039250610d11565b6001836020036101000a0380198251168184511680821785525050505050509050019150506040518091039020600019161490505b929150505600a165627a7a72305820f2ad4c4f5e49880d62443808f166579b381eb4744ae39e9d98ef251971a154bb0029";

    public static final String ABI = "[{\"constant\":false,\"inputs\":[{\"indexed\":false,\"name\":\"hash\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"key\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"value\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"updated\",\"type\":\"uint256\"}],\"name\":\"setAttribute\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"indexed\":false,\"name\":\"hash\",\"type\":\"string\"}],\"name\":\"getLatestRelatedBlock\",\"outputs\":[{\"indexed\":false,\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"indexed\":false,\"name\":\"hash\",\"type\":\"string\"}],\"name\":\"isHashExist\",\"outputs\":[{\"indexed\":false,\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"indexed\":false,\"name\":\"hash\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"sig\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"extra\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"updated\",\"type\":\"uint256\"}],\"name\":\"createEvidence\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"indexed\":false,\"name\":\"hash\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"signer\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"key\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"value\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"updated\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"previousBlock\",\"type\":\"uint256\"}],\"name\":\"EvidenceAttributeChanged\",\"payable\":false,\"type\":\"event\"}]";

    public static final String FUNC_SETATTRIBUTE = "setAttribute";

    public static final String FUNC_GETLATESTRELATEDBLOCK = "getLatestRelatedBlock";

    public static final String FUNC_ISHASHEXIST = "isHashExist";

    public static final String FUNC_CREATEEVIDENCE = "createEvidence";

    public static final Event EVIDENCEATTRIBUTECHANGED_EVENT = new Event("EvidenceAttributeChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected EvidenceContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected EvidenceContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected EvidenceContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected EvidenceContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> setAttribute(String hash, String key, String value, BigInteger updated) {
        final Function function = new Function(
                FUNC_SETATTRIBUTE, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(hash), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(key), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(value), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(updated)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void setAttribute(String hash, String key, String value, BigInteger updated, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_SETATTRIBUTE, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(hash), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(key), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(value), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(updated)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String setAttributeSeq(String hash, String key, String value, BigInteger updated) {
        final Function function = new Function(
                FUNC_SETATTRIBUTE, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(hash), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(key), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(value), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(updated)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<BigInteger> getLatestRelatedBlock(String hash) {
        final Function function = new Function(FUNC_GETLATESTRELATEDBLOCK, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(hash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> isHashExist(String hash) {
        final Function function = new Function(FUNC_ISHASHEXIST, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(hash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> createEvidence(String hash, String sig, String extra, BigInteger updated) {
        final Function function = new Function(
                FUNC_CREATEEVIDENCE, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(hash), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(sig), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(extra), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(updated)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void createEvidence(String hash, String sig, String extra, BigInteger updated, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_CREATEEVIDENCE, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(hash), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(sig), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(extra), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(updated)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String createEvidenceSeq(String hash, String sig, String extra, BigInteger updated) {
        final Function function = new Function(
                FUNC_CREATEEVIDENCE, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(hash), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(sig), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(extra), 
                new org.fisco.bcos.web3j.abi.datatypes.generated.Uint256(updated)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public List<EvidenceAttributeChangedEventResponse> getEvidenceAttributeChangedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(EVIDENCEATTRIBUTECHANGED_EVENT, transactionReceipt);
        ArrayList<EvidenceAttributeChangedEventResponse> responses = new ArrayList<EvidenceAttributeChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            EvidenceAttributeChangedEventResponse typedResponse = new EvidenceAttributeChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.hash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.signer = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.key = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.value = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.updated = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.previousBlock = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<EvidenceAttributeChangedEventResponse> evidenceAttributeChangedEventFlowable(BcosFilter filter) {
        return web3j.logFlowable(filter).map(new io.reactivex.functions.Function<Log, EvidenceAttributeChangedEventResponse>() {
            @Override
            public EvidenceAttributeChangedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(EVIDENCEATTRIBUTECHANGED_EVENT, log);
                EvidenceAttributeChangedEventResponse typedResponse = new EvidenceAttributeChangedEventResponse();
                typedResponse.log = log;
                typedResponse.hash = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.signer = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.key = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.value = (String) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.updated = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                typedResponse.previousBlock = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<EvidenceAttributeChangedEventResponse> evidenceAttributeChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        BcosFilter filter = new BcosFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(EVIDENCEATTRIBUTECHANGED_EVENT));
        return evidenceAttributeChangedEventFlowable(filter);
    }

    @Deprecated
    public static EvidenceContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new EvidenceContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static EvidenceContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new EvidenceContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static EvidenceContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new EvidenceContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static EvidenceContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new EvidenceContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<EvidenceContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(EvidenceContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<EvidenceContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(EvidenceContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<EvidenceContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(EvidenceContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<EvidenceContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(EvidenceContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class EvidenceAttributeChangedEventResponse {
        public Log log;

        public String hash;

        public String signer;

        public String key;

        public String value;

        public BigInteger updated;

        public BigInteger previousBlock;
    }
}
