/*
 *       Copyright© (2018-2020) WeBank Co., Ltd.
 *
 *       This file is part of weid-java-sdk.
 *
 *       weid-java-sdk is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       weid-java-sdk is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with weid-java-sdk.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.webank.weid.service.impl;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.bcos.web3j.abi.datatypes.Address;
import org.bcos.web3j.crypto.ECKeyPair;
import org.bcos.web3j.crypto.Keys;
import org.bcos.web3j.crypto.Sign;
import org.bcos.web3j.crypto.Sign.SignatureData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webank.weid.constant.ErrorCode;
import com.webank.weid.constant.WeIdConstant;
import com.webank.weid.exception.WeIdBaseException;
import com.webank.weid.protocol.base.EvidenceInfo;
import com.webank.weid.protocol.base.EvidenceSignInfo;
import com.webank.weid.protocol.base.HashString;
import com.webank.weid.protocol.base.WeIdDocument;
import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.protocol.inf.Hashable;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.EvidenceService;
import com.webank.weid.rpc.WeIdService;
import com.webank.weid.service.BaseService;
import com.webank.weid.service.impl.engine.EngineFactory;
import com.webank.weid.service.impl.engine.EvidenceServiceEngine;
import com.webank.weid.util.DataToolUtils;
import com.webank.weid.util.WeIdUtils;

/**
 * Service implementations for operations on Evidence.
 *
 * @author chaoxinhu 2019.1
 */
public class EvidenceServiceImpl extends BaseService implements EvidenceService {

    private static final Logger logger = LoggerFactory.getLogger(EvidenceServiceImpl.class);

    private WeIdService weIdService = new WeIdServiceImpl();

    private EvidenceServiceEngine evidenceServiceEngine =
        EngineFactory.createEvidenceServiceEngine();

    /**
     * Create a new evidence to the blockchain and get the evidence address.
     *
     * @param object the given Java object
     * @param weIdPrivateKey the caller WeID Authentication
     * @return Evidence address
     */
    @Override
    public ResponseData<String> createEvidence(Hashable object, WeIdPrivateKey weIdPrivateKey) {
        ResponseData<String> hashResp = getHashValue(object);
        if (StringUtils.isEmpty(hashResp.getResult())) {
            return new ResponseData<>(StringUtils.EMPTY, hashResp.getErrorCode(),
                hashResp.getErrorMessage());
        }
        if (!WeIdUtils.isPrivateKeyValid(weIdPrivateKey)) {
            return new ResponseData<>(StringUtils.EMPTY,
                ErrorCode.CREDENTIAL_PRIVATE_KEY_NOT_EXISTS);
        }
        return hashToNewEvidence(hashResp.getResult(), weIdPrivateKey.getPrivateKey(), null);
    }

    private String getSignerFromPrivKey(List<String> signers, String privateKey) {
        ECKeyPair keyPair = ECKeyPair.create(new BigInteger(privateKey));
        String keyWeId = WeIdUtils
            .convertAddressToWeId(new Address(Keys.getAddress(keyPair)).toString());
        for (String weId : signers) {
            if (weId.equalsIgnoreCase(keyWeId)) {
                return keyWeId;
            }
        }
        return StringUtils.EMPTY;
    }

    /* (non-Javadoc)
     * @see com.webank.weid.rpc.generateHash
     * #generateHash(T object)
     */
    @Override
    public <T> ResponseData<HashString> generateHash(T object) {
        if (object == null) {
            return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
        }
        if (object instanceof Hashable) {
            ResponseData<String> hashResp = getHashValue((Hashable) object);
            if (StringUtils.isEmpty(hashResp.getResult())) {
                return new ResponseData<>(null, hashResp.getErrorCode(),
                    hashResp.getErrorMessage());
            }
            return new ResponseData<>(new HashString(hashResp.getResult()), ErrorCode.SUCCESS);
        }
        if (object instanceof File) {
            // This will convert all types of file into String stream
            String rawData = convertFileToString((File) object);
            if (StringUtils.isEmpty(rawData)) {
                logger.error("Failed to convert file into String: {}", ((File) object).getName());
                return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
            }
            return new ResponseData<>(new HashString(DataToolUtils.sha3(rawData)),
                ErrorCode.SUCCESS);
        }
        if (object instanceof String) {
            if (StringUtils.isEmpty((String) object)) {
                logger.error("Input String is blank, ignored..");
                return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
            }
            return new ResponseData<>(new HashString(DataToolUtils.sha3((String) object)),
                ErrorCode.SUCCESS);
        }
        logger.error("Unsupported input object type: {}", object.getClass().getCanonicalName());
        return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
    }

    private String convertFileToString(File file) {
        try {
            return Files.asByteSource(file).asCharSource(Charsets.UTF_8).read();
        } catch (Exception e) {
            logger.error("Failed to load file as String.");
            return StringUtils.EMPTY;
        }
    }

    /**
     * Obtain the hash value of a given object - supports Credential, Wrapper and Pojo, and also
     * plain hash value (no extra hashing required).
     *
     * @param object any object
     * @return hash value
     */
    private ResponseData<String> getHashValue(Hashable object) {
        if (object == null) {
            // Support empty hash value
            return new ResponseData<>(WeIdConstant.HEX_PREFIX, ErrorCode.SUCCESS);
        }
        try {
            String hashValue = object.getHash();
            if (StringUtils.isEmpty(hashValue)) {
                return new ResponseData<>(StringUtils.EMPTY, ErrorCode.ILLEGAL_INPUT);
            }
            return new ResponseData<>(hashValue, ErrorCode.SUCCESS);
        } catch (Exception e) {
            logger.error("Input Object type unsupported: " + object.getClass().getName());
            return new ResponseData<>(StringUtils.EMPTY, ErrorCode.ILLEGAL_INPUT);
        }
    }

    /**
     * Actual method to upload to blockchain, varied in different blockchain versions.
     *
     * @param hashValue the hash value to be uploaded
     * @param privateKey the private key to reload contract and sign txn
     * @param signers the uploading signers - only used in create case
     */
    private ResponseData<String> hashToNewEvidence(String hashValue, String privateKey,
        List<String> signers) {
        try {
            String credentialHashOnChain = hashValue
                .replaceAll(WeIdConstant.HEX_PREFIX, StringUtils.EMPTY);
            List<String> hashAttributes = new ArrayList<>();
            Sign.SignatureData sigData;
            if (!StringUtils.isEmpty(credentialHashOnChain)) {
                hashAttributes.add(
                    credentialHashOnChain.substring(0, WeIdConstant.BYTES32_FIXED_LENGTH));
                hashAttributes.add(
                    credentialHashOnChain.substring(
                        WeIdConstant.BYTES32_FIXED_LENGTH,
                        WeIdConstant.BYTES32_FIXED_LENGTH * 2
                    ));
                sigData =
                    DataToolUtils.signMessage(hashValue, privateKey);
            } else {
                hashAttributes.add(StringUtils.EMPTY);
                hashAttributes.add(StringUtils.EMPTY);
                byte v = (byte) 0;
                byte[] r = new byte[32];
                byte[] s = new byte[32];
                sigData = new SignatureData(v, r, s);
            }
            List<String> extraValueList = new ArrayList<>();
            extraValueList.add(StringUtils.EMPTY);
            return evidenceServiceEngine.createEvidence(
                sigData,
                hashAttributes,
                extraValueList,
                privateKey,
                signers
            );
        } catch (Exception e) {
            logger.error("create evidence failed due to system error. ", e);
            return new ResponseData<>(StringUtils.EMPTY, ErrorCode.CREDENTIAL_EVIDENCE_BASE_ERROR);
        }
    }

    /**
     * Get the evidence from blockchain.
     *
     * @param evidenceKey the evidence address on chain
     * @return The EvidenceInfo
     */
    @Override
    public ResponseData<EvidenceInfo> getEvidence(String evidenceKey) {
        if (StringUtils.isEmpty(evidenceKey) || !WeIdUtils.isValidAddress(evidenceKey)) {
            logger.error("Evidence argument illegal input: address. ");
            return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
        }
        try {
            ResponseData<EvidenceInfo> resp = evidenceServiceEngine.getInfo(evidenceKey);
            if (resp.getResult().getCredentialHash().equalsIgnoreCase(WeIdConstant.HEX_PREFIX)) {
                resp.getResult().setCredentialHash(StringUtils.EMPTY);
            }
            return resp;
        } catch (Exception e) {
            logger.error("get evidence failed.", e);
            return new ResponseData<>(null, ErrorCode.CREDENTIAL_EVIDENCE_BASE_ERROR);
        }
    }

    private ResponseData<Boolean> verify(String hashValue, String evidenceAddress) {
        if (!verifyHashValueFormat(hashValue)) {
            return new ResponseData<>(false, ErrorCode.ILLEGAL_INPUT);
        }
        // Step 1: Get EvidenceInfo from chain
        ResponseData<EvidenceInfo> evidenceInfoResponseData = verifyAndGetEvidenceFromChain(
            evidenceAddress);
        if (evidenceInfoResponseData.getResult() == null) {
            return new ResponseData<>(false, evidenceInfoResponseData.getErrorCode(),
                evidenceInfoResponseData.getErrorMessage());
        }
        EvidenceInfo evidenceInfo = evidenceInfoResponseData.getResult();

        // Step 2: Verify each signature value in EvidenceInfo wrt the signer based on their their
        // publickeys from WeIDContract. Here each signature/signer pair must pass the verification.
        return verifyHashToEvidenceSignature(hashValue, evidenceInfo);
    }

    /**
     * Verify a Credential based on the provided Evidence info.
     *
     * @param object the given Java object
     * @param evidenceKey the evidence address
     * @return true if succeeds, false otherwise
     */
    @Override
    public ResponseData<Boolean> verify(Hashable object, String evidenceKey) {
        ResponseData<String> hashResp = getHashValue(object);
        if (StringUtils.isEmpty(hashResp.getResult())) {
            return new ResponseData<>(false, hashResp.getErrorCode(),
                hashResp.getErrorMessage());
        }
        return verify(hashResp.getResult(), evidenceKey);
    }

    private ResponseData<EvidenceInfo> verifyAndGetEvidenceFromChain(String evidenceAddress) {
        if (!WeIdUtils.isValidAddress(evidenceAddress)) {
            logger.error("Verify EvidenceInfo input illegal: evidenceInfo address");
            return new ResponseData<>(null, ErrorCode.ILLEGAL_INPUT);
        }
        ResponseData<EvidenceInfo> innerEvidenceResponseData = getEvidence(evidenceAddress);
        if (innerEvidenceResponseData.getResult() == null) {
            return new ResponseData<>(
                null,
                ErrorCode.getTypeByErrorCode(innerEvidenceResponseData.getErrorCode())
            );
        }
        return new ResponseData<>(innerEvidenceResponseData.getResult(), ErrorCode.SUCCESS);
    }

    private ResponseData<Boolean> verifyHashToEvidenceSignature(String hashOffChain,
        EvidenceInfo evidenceInfo) {
        if (!StringUtils.equalsIgnoreCase(hashOffChain, evidenceInfo.getCredentialHash())) {
            logger.error(
                "credential hash mismatches. Off-chain: {}, on-chain: {}", hashOffChain,
                evidenceInfo.getCredentialHash());
            return new ResponseData<>(false, ErrorCode.CREDENTIAL_EVIDENCE_HASH_MISMATCH);
        }
        try {
            for (int i = 0; i < evidenceInfo.getSignatures().size(); i++) {
                String signature = evidenceInfo.getSignatures().get(i);
                // iterate through each signer
                boolean foundMatchedSigner = false;
                for (int j = 0; j < evidenceInfo.getSigners().size(); j++) {
                    String signerWeId = evidenceInfo.getSigners().get(j);
                    if (WeIdUtils
                        .isEmptyAddress(new Address(WeIdUtils.convertWeIdToAddress(signerWeId)))) {
                        break;
                    }
                    SignatureData signatureData =
                        DataToolUtils.simpleSignatureDeserialization(
                            DataToolUtils.base64Decode(signature.getBytes(StandardCharsets.UTF_8))
                        );
                    ResponseData<Boolean> innerResponseData = verifySignatureToSigner(
                        hashOffChain,
                        WeIdUtils.convertAddressToWeId(signerWeId),
                        signatureData
                    );
                    if (innerResponseData.getResult()) {
                        foundMatchedSigner = true;
                        break;
                    }
                }
                if (!foundMatchedSigner) {
                    logger.error("Signature: " + signature + ", signer mismatch.");
                    return new ResponseData<>(false, ErrorCode.CREDENTIAL_ISSUER_MISMATCH);
                }
            }
        } catch (WeIdBaseException e) {
            logger.error(
                "Generic error occurred during verify evidenceInfo: ", e);
            return new ResponseData<>(false, ErrorCode.CREDENTIAL_EVIDENCE_SIGNATURE_BROKEN);
        } catch (Exception e) {
            logger.error(
                "Generic error occurred during verify evidenceInfo: ", e);
            return new ResponseData<>(false, ErrorCode.CREDENTIAL_EVIDENCE_BASE_ERROR);
        }
        return new ResponseData<>(true, ErrorCode.SUCCESS);
    }

    private ResponseData<Boolean> verifySignatureToSigner(
        String rawData,
        String signerWeId,
        SignatureData signatureData
    ) {
        try {
            ResponseData<WeIdDocument> innerResponseData =
                weIdService.getWeIdDocument(signerWeId);
            if (innerResponseData.getErrorCode() != ErrorCode.SUCCESS.getCode()) {
                logger.error(
                    "Error occurred when fetching WeIdentity DID document for: {}, msg: {}",
                    signerWeId, innerResponseData.getErrorMessage());
                return new ResponseData<>(false, ErrorCode.CREDENTIAL_WEID_DOCUMENT_ILLEGAL);
            }
            WeIdDocument weIdDocument = innerResponseData.getResult();
            ErrorCode errorCode = DataToolUtils
                .verifySignatureFromWeId(rawData, signatureData, weIdDocument);
            if (errorCode.getCode() != ErrorCode.SUCCESS.getCode()) {
                return new ResponseData<>(false, errorCode);
            }
            return new ResponseData<>(true, ErrorCode.SUCCESS);
        } catch (Exception e) {
            logger.error("error occurred during verifying signatures from chain: ", e);
            return new ResponseData<>(false, ErrorCode.CREDENTIAL_EVIDENCE_BASE_ERROR);
        }
    }

    private boolean verifyHashValueFormat(String hashValue) {
        return !StringUtils.isEmpty(hashValue)
            && Pattern.compile(WeIdConstant.HASH_VALUE_PATTERN).matcher(hashValue).matches();
    }

    /**
     * Validate whether an evidence is signed by this WeID.
     *
     * @param evidenceInfo the evidence info fetched from chain
     * @param weId the WeID
     * @return true if yes, false otherwise
     */
    @Override
    public ResponseData<Boolean> validateSigner(EvidenceInfo evidenceInfo, String weId) {
        if (evidenceInfo == null || evidenceInfo.getSigners().isEmpty()) {
            return new ResponseData<>(false, ErrorCode.ILLEGAL_INPUT);
        }
        if (WeIdUtils.isWeIdValid(weId)) {
            return new ResponseData<>(false, ErrorCode.WEID_INVALID);
        }
        if (!evidenceInfo.getSigners().contains(weId)) {
            logger.error("This Evidence does not contain the provided WeID: {}", weId);
            return new ResponseData<>(false, ErrorCode.WEID_DOES_NOT_EXIST);
        }
        EvidenceSignInfo signInfo = evidenceInfo.getSignInfo().get(weId);
        String signature = signInfo.getSignature();
        if (!DataToolUtils.isValidBase64String(signature)) {
            return new ResponseData<>(false, ErrorCode.CREDENTIAL_EVIDENCE_SIGNATURE_BROKEN);
        }
        SignatureData signatureData =
            DataToolUtils.simpleSignatureDeserialization(
                DataToolUtils.base64Decode(signature.getBytes(StandardCharsets.UTF_8))
            );
        return verifySignatureToSigner(
            evidenceInfo.getCredentialHash(),
            WeIdUtils.convertAddressToWeId(weId),
            signatureData
        );
    }


}
