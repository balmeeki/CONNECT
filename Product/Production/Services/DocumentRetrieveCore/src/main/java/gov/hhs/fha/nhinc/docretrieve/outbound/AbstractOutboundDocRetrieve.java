/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.docretrieve.outbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docretrieve.audit.DocRetrieveAuditLogger;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.xdcommon.XDCommonResponseHelper;
import gov.hhs.fha.nhinc.xdcommon.XDCommonResponseHelper.ErrorCodes;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.lang.StringUtils;

/**
 * @author msw
 *
 */
public abstract class AbstractOutboundDocRetrieve {

    private DocRetrieveAuditLogger docRetrieveAuditLogger;

    /**
     *
     */
    public AbstractOutboundDocRetrieve() {
        super();
        docRetrieveAuditLogger = new DocRetrieveAuditLogger();
    }

    public AbstractOutboundDocRetrieve(DocRetrieveAuditLogger docRetrieveAuditLogger) {
        docRetrieveAuditLogger = new DocRetrieveAuditLogger();
    }

    /**
     * @param targets
     * @param entityAPILevel
     * @return
     */
    protected boolean validateGuidance(NhinTargetCommunitiesType targets, ADAPTER_API_LEVEL entityAPILevel) {
        boolean valid = false;
        String guidance = targets.getUseSpecVersion();

        if (StringUtils.isBlank(guidance)) {
            // assume guidance
            if (ADAPTER_API_LEVEL.LEVEL_a0.equals(entityAPILevel)) {
                guidance = "2.0";
            } else {
                guidance = "3.0";
            }
            targets.setUseSpecVersion(guidance);
        }

        if (("3.0".equals(guidance) || ("2.0".equals(guidance))) && entityAPILevel.equals(ADAPTER_API_LEVEL.LEVEL_a1)) {
            valid = true;
        } else if ("2.0".equals(guidance) && entityAPILevel.equals(ADAPTER_API_LEVEL.LEVEL_a0)) {
            valid = true;
        }
        return valid;
    }

    protected RetrieveDocumentSetResponseType createGuidanceErrorResponse(ADAPTER_API_LEVEL entityAPILevel) {
        RetrieveDocumentSetResponseType response = new RetrieveDocumentSetResponseType();
        XDCommonResponseHelper helper = new XDCommonResponseHelper();
        response.setRegistryResponse(helper.createError(NhincConstants.INIT_MULTISPEC_ERROR_UNSUPPORTED_GUIDANCE,
            ErrorCodes.XDSRepositoryError, NhincConstants.INIT_MULTISPEC_LOC_ENTITY_DR + entityAPILevel.toString()));

        return response;
    }

    public void auditRequest(RetrieveDocumentSetRequestType request, AssertionType assertion,
        NhinTargetSystemType targetSystem) {

        getAuditLogger().auditRequestMessage(request, assertion, targetSystem,
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
            Boolean.TRUE, null, NhincConstants.DOC_RETRIEVE_SERVICE_NAME);

    }

    abstract DocRetrieveAuditLogger getAuditLogger();
}
