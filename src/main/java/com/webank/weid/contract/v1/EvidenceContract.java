package com.webank.weid.contract.v1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import org.bcos.channel.client.TransactionSucCallback;
import org.bcos.web3j.abi.EventEncoder;
import org.bcos.web3j.abi.EventValues;
import org.bcos.web3j.abi.TypeReference;
import org.bcos.web3j.abi.datatypes.Address;
import org.bcos.web3j.abi.datatypes.Bool;
import org.bcos.web3j.abi.datatypes.Event;
import org.bcos.web3j.abi.datatypes.Function;
import org.bcos.web3j.abi.datatypes.Type;
import org.bcos.web3j.abi.datatypes.Utf8String;
import org.bcos.web3j.abi.datatypes.generated.Uint256;
import org.bcos.web3j.crypto.Credentials;
import org.bcos.web3j.protocol.Web3j;
import org.bcos.web3j.protocol.core.DefaultBlockParameter;
import org.bcos.web3j.protocol.core.methods.request.EthFilter;
import org.bcos.web3j.protocol.core.methods.response.Log;
import org.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.bcos.web3j.tx.Contract;
import org.bcos.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * Auto generated code.<br>
 * <strong>Do not modify!</strong><br>
 * Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>, or {@link org.bcos.web3j.codegen.SolidityFunctionWrapperGenerator} to update.
 *
 * <p>Generated with web3j version none.
 */
public final class EvidenceContract extends Contract {
    private static String BINARY = "6060604052341561000c57fe5b5b610dc08061001c6000396000f30060606040526000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806348af58da1461005c5780635ca04cfc146101455780637fbfd5aa146101b3578063c7aa107d14610225575bfe5b341561006457fe5b610143600480803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803590602001909190505061030e565b005b341561014d57fe5b61019d600480803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190505061062e565b6040518082815260200191505060405180910390f35b34156101bb57fe5b61020b600480803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919050506106a2565b604051808215151515815260200191505060405180910390f35b341561022d57fe5b61030c600480803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803590602001909190505061072a565b005b610317846106a2565b151561032257610628565b61036283604060405190810160405280600481526020017f696e666f00000000000000000000000000000000000000000000000000000000815250610ca5565b1561036c57610628565b7f2a4e3729f8dfdbe83193e0bbcbc01b20a5353d070b076f91f27a24c0897da5a6843385858560008a6040518082805190602001908083835b602083106103c857805182526020820191506020810190506020830392506103a5565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390205460405180806020018773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001806020018060200186815260200185815260200184810384528a818151815260200191508051906020019080838360008314610491575b8051825260208311156104915760208201915060208101905060208303925061046d565b505050905090810190601f1680156104bd5780820380516001836020036101000a031916815260200191505b50848103835288818151815260200191508051906020019080838360008314610505575b805182526020831115610505576020820191506020810190506020830392506104e1565b505050905090810190601f1680156105315780820380516001836020036101000a031916815260200191505b50848103825287818151815260200191508051906020019080838360008314610579575b80518252602083111561057957602082019150602081019050602083039250610555565b505050905090810190601f1680156105a55780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390a1436000856040518082805190602001908083835b602083106105f057805182526020820191506020810190506020830392506105cd565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020819055505b50505050565b60006000826040518082805190602001908083835b602083106106665780518252602082019150602081019050602083039250610643565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390205490505b919050565b600060006000836040518082805190602001908083835b602083106106dc57805182526020820191506020810190506020830392506106b9565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020541415156107205760019050610725565b600090505b919050565b7f2a4e3729f8dfdbe83193e0bbcbc01b20a5353d070b076f91f27a24c0897da5a68433604060405190810160405280600481526020017f696e666f00000000000000000000000000000000000000000000000000000000815250868560008a6040518082805190602001908083835b602083106107bc5780518252602082019150602081019050602083039250610799565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390205460405180806020018773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001806020018060200186815260200185815260200184810384528a818151815260200191508051906020019080838360008314610885575b80518252602083111561088557602082019150602081019050602083039250610861565b505050905090810190601f1680156108b15780820380516001836020036101000a031916815260200191505b508481038352888181518152602001915080519060200190808383600083146108f9575b8051825260208311156108f9576020820191506020810190506020830392506108d5565b505050905090810190601f1680156109255780820380516001836020036101000a031916815260200191505b5084810382528781815181526020019150805190602001908083836000831461096d575b80518252602083111561096d57602082019150602081019050602083039250610949565b505050905090810190601f1680156109995780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390a17f2a4e3729f8dfdbe83193e0bbcbc01b20a5353d070b076f91f27a24c0897da5a68433604060405190810160405280600581526020017f6578747261000000000000000000000000000000000000000000000000000000815250858560008a6040518082805190602001908083835b60208310610a3f5780518252602082019150602081019050602083039250610a1c565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390205460405180806020018773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001806020018060200186815260200185815260200184810384528a818151815260200191508051906020019080838360008314610b08575b805182526020831115610b0857602082019150602081019050602083039250610ae4565b505050905090810190601f168015610b345780820380516001836020036101000a031916815260200191505b50848103835288818151815260200191508051906020019080838360008314610b7c575b805182526020831115610b7c57602082019150602081019050602083039250610b58565b505050905090810190601f168015610ba85780820380516001836020036101000a031916815260200191505b50848103825287818151815260200191508051906020019080838360008314610bf0575b805182526020831115610bf057602082019150602081019050602083039250610bcc565b505050905090810190601f168015610c1c5780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390a1436000856040518082805190602001908083835b60208310610c675780518252602082019150602081019050602083039250610c44565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020819055505b50505050565b600081518351141515610cbb5760009050610d8e565b816040518082805190602001908083835b60208310610cef5780518252602082019150602081019050602083039250610ccc565b6001836020036101000a038019825116818451168082178552505050505050905001915050604051809103902060001916836040518082805190602001908083835b60208310610d545780518252602082019150602081019050602083039250610d31565b6001836020036101000a038019825116818451168082178552505050505050905001915050604051809103902060001916149050610d8e565b5b929150505600a165627a7a72305820ca68070c3b28d36195cbe8f7a8c4b283eb9ecb3ee6f96c04c6fe10db04908c2f0029";

    public static final String ABI = "[{\"constant\":false,\"inputs\":[{\"name\":\"hash\",\"type\":\"string\"},{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"value\",\"type\":\"string\"},{\"name\":\"updated\",\"type\":\"uint256\"}],\"name\":\"setAttribute\",\"outputs\":[],\"payable\":false,\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"hash\",\"type\":\"string\"}],\"name\":\"getLatestRelatedBlock\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"hash\",\"type\":\"string\"}],\"name\":\"isHashExist\",\"outputs\":[{\"name\":\"\",\"type\":\"bool\"}],\"payable\":false,\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"hash\",\"type\":\"string\"},{\"name\":\"sig\",\"type\":\"string\"},{\"name\":\"extra\",\"type\":\"string\"},{\"name\":\"updated\",\"type\":\"uint256\"}],\"name\":\"createEvidence\",\"outputs\":[],\"payable\":false,\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"hash\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"signer\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"key\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"value\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"updated\",\"type\":\"uint256\"},{\"indexed\":false,\"name\":\"previousBlock\",\"type\":\"uint256\"}],\"name\":\"EvidenceAttributeChanged\",\"type\":\"event\"}]";

    private EvidenceContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, Boolean isInitByName) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit, isInitByName);
    }

    private EvidenceContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, Boolean isInitByName) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit, isInitByName);
    }

    private EvidenceContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit, false);
    }

    private EvidenceContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit, false);
    }

    public static List<EvidenceAttributeChangedEventResponse> getEvidenceAttributeChangedEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("EvidenceAttributeChanged", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<EvidenceAttributeChangedEventResponse> responses = new ArrayList<EvidenceAttributeChangedEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            EvidenceAttributeChangedEventResponse typedResponse = new EvidenceAttributeChangedEventResponse();
            typedResponse.hash = (Utf8String) eventValues.getNonIndexedValues().get(0);
            typedResponse.signer = (Address) eventValues.getNonIndexedValues().get(1);
            typedResponse.key = (Utf8String) eventValues.getNonIndexedValues().get(2);
            typedResponse.value = (Utf8String) eventValues.getNonIndexedValues().get(3);
            typedResponse.updated = (Uint256) eventValues.getNonIndexedValues().get(4);
            typedResponse.previousBlock = (Uint256) eventValues.getNonIndexedValues().get(5);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<EvidenceAttributeChangedEventResponse> evidenceAttributeChangedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("EvidenceAttributeChanged", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, EvidenceAttributeChangedEventResponse>() {
            @Override
            public EvidenceAttributeChangedEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                EvidenceAttributeChangedEventResponse typedResponse = new EvidenceAttributeChangedEventResponse();
                typedResponse.hash = (Utf8String) eventValues.getNonIndexedValues().get(0);
                typedResponse.signer = (Address) eventValues.getNonIndexedValues().get(1);
                typedResponse.key = (Utf8String) eventValues.getNonIndexedValues().get(2);
                typedResponse.value = (Utf8String) eventValues.getNonIndexedValues().get(3);
                typedResponse.updated = (Uint256) eventValues.getNonIndexedValues().get(4);
                typedResponse.previousBlock = (Uint256) eventValues.getNonIndexedValues().get(5);
                return typedResponse;
            }
        });
    }

    public Future<TransactionReceipt> setAttribute(Utf8String hash, Utf8String key, Utf8String value, Uint256 updated) {
        Function function = new Function("setAttribute", Arrays.<Type>asList(hash, key, value, updated), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public void setAttribute(Utf8String hash, Utf8String key, Utf8String value, Uint256 updated, TransactionSucCallback callback) {
        Function function = new Function("setAttribute", Arrays.<Type>asList(hash, key, value, updated), Collections.<TypeReference<?>>emptyList());
        executeTransactionAsync(function, callback);
    }

    public Future<Uint256> getLatestRelatedBlock(Utf8String hash) {
        Function function = new Function("getLatestRelatedBlock", 
                Arrays.<Type>asList(hash), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<Bool> isHashExist(Utf8String hash) {
        Function function = new Function("isHashExist", 
                Arrays.<Type>asList(hash), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> createEvidence(Utf8String hash, Utf8String sig, Utf8String extra, Uint256 updated) {
        Function function = new Function("createEvidence", Arrays.<Type>asList(hash, sig, extra, updated), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public void createEvidence(Utf8String hash, Utf8String sig, Utf8String extra, Uint256 updated, TransactionSucCallback callback) {
        Function function = new Function("createEvidence", Arrays.<Type>asList(hash, sig, extra, updated), Collections.<TypeReference<?>>emptyList());
        executeTransactionAsync(function, callback);
    }

    public static Future<EvidenceContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployAsync(EvidenceContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static Future<EvidenceContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployAsync(EvidenceContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static EvidenceContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new EvidenceContract(contractAddress, web3j, credentials, gasPrice, gasLimit, false);
    }

    public static EvidenceContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new EvidenceContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit, false);
    }

    public static EvidenceContract loadByName(String contractName, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new EvidenceContract(contractName, web3j, credentials, gasPrice, gasLimit, true);
    }

    public static EvidenceContract loadByName(String contractName, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new EvidenceContract(contractName, web3j, transactionManager, gasPrice, gasLimit, true);
    }

    public static class EvidenceAttributeChangedEventResponse {
        public Utf8String hash;

        public Address signer;

        public Utf8String key;

        public Utf8String value;

        public Uint256 updated;

        public Uint256 previousBlock;
    }
}
