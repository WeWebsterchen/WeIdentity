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

package com.webank.weid.rpc;

import java.util.List;

import com.webank.weid.protocol.base.EvidenceInfo;
import com.webank.weid.protocol.base.HashString;
import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.protocol.inf.Hashable;
import com.webank.weid.protocol.response.ResponseData;

/**
 * Service inf for operations on Evidence for Credentials.
 *
 * @author chaoxinhu 2019.1
 */
public interface EvidenceService {

    /**
     * Create a new evidence to blockchain, and return the evidence's hash value on-chain. Supports
     * following types of input: Credential, CredentialWrapper, CredentialPojo, plain hash String,
     * After a successful creation, the hash value will be recorded onto blockchain, and this hash
     * value can be used as key to lookup on blockchain.
     *
     * @param object the given Java object
     * @param weIdPrivateKey the signer WeID's private key
     * @return evidence address. Return empty string if failed due to any reason.
     */
    ResponseData<String> createEvidence(Hashable object, WeIdPrivateKey weIdPrivateKey);

    /**
     * Get the evidence from blockchain.
     *
     * @param evidenceKey the evidence address on chain
     * @return The EvidenceInfo
     */
    ResponseData<EvidenceInfo> getEvidence(String evidenceKey);

    /**
     * Verify an object based against the provided Evidence info. Supports following types of input:
     * Credential, CredentialWrapper, CredentialPojo, and plain hash value. This will traverse all
     * the listed signatures against its singers.
     *
     * @param object the given Java object
     * @param evidenceKey the evidence key to be verified
     * @return true if succeeds, false otherwise
     */
    ResponseData<Boolean> verify(Hashable object, String evidenceKey);

    /**
     * Generate hash value of any passed-in param.
     *
     * @param object param to be hashed
     * @param <T> type of param
     * @return the hash string
     */
    <T> ResponseData<HashString> generateHash(T object);

    /**
     * Validate whether an evidence is signed by this WeID.
     *
     * @param evidenceInfo the evidence info fetched from chain
     * @param weId the WeID
     * @return true if yes, false otherwise
     */
    ResponseData<Boolean> validateSigner(EvidenceInfo evidenceInfo, String weId);
}
