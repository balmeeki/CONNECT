/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.proxy20;

import gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.docsubmission.aspect.DeferredResponseDescriptionBuilder;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import ihe.iti.xdr._2007.XDRDeferredResponse20PortType;
import java.lang.reflect.Method;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author achidamb
 *
 */
public class NhinDocSubmissionDeferredResponseProxyWebServiceSecuredImplTest {

    @SuppressWarnings("unchecked")
    private final CONNECTClient<XDRDeferredResponse20PortType> client = mock(CONNECTClient.class);
    private RegistryResponseType request = mock(RegistryResponseType.class);
    private AssertionType assertion;

    @Test
    public void hasNwhinInvocationEvent() throws Exception {
        Class<NhinDocSubmissionDeferredResponseProxyWebServiceSecuredImpl> clazz = NhinDocSubmissionDeferredResponseProxyWebServiceSecuredImpl.class;
        Method method = clazz.getMethod("provideAndRegisterDocumentSetBDeferredResponse20", RegistryResponseType.class,
                AssertionType.class, NhinTargetSystemType.class);
        NwhinInvocationEvent annotation = method.getAnnotation(NwhinInvocationEvent.class);
        assertNotNull(annotation);
        assertEquals(DeferredResponseDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(DeferredResponseDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Document Submission Deferred Response", annotation.serviceType());
        assertEquals("", annotation.version());
    }

    @Test
    public void testMtom() {
        NhinDocSubmissionDeferredResponseProxyWebServiceSecuredImpl impl = getImpl();
        impl.provideAndRegisterDocumentSetBDeferredResponse20(request, assertion, getTarget("1.1"));
        verify(client).enableMtom();
    }

    /**
     * @param hcidValue
     * @return
     */
    private NhinTargetSystemType getTarget(String hcidValue) {
        NhinTargetSystemType target = new NhinTargetSystemType();
        HomeCommunityType hcid = new HomeCommunityType();
        hcid.setHomeCommunityId(hcidValue);
        target.setHomeCommunity(hcid);
        return target;
    }

    /**
     * @return
     */
    private NhinDocSubmissionDeferredResponseProxyWebServiceSecuredImpl getImpl() {
        return new NhinDocSubmissionDeferredResponseProxyWebServiceSecuredImpl() {

            /*
             * (non-Javadoc)
             *
             * @see #getCONNECTClientSecured(gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor,
             * java.lang.String, gov.hhs.fha.nhinc.common.nhinccommon.AssertionType, java.lang.String, java.lang.String)
             */
            @Override
            protected CONNECTClient<XDRDeferredResponse20PortType> getCONNECTClientSecured(
                    ServicePortDescriptor<XDRDeferredResponse20PortType> portDescriptor, String url,
                    AssertionType assertion, String target, String serviceName) {
                return client;
            }

            /*
             * (non-Javadoc)
             *
             * @see gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response.proxy11.
             * NhinDocSubmissionDeferredResponseProxyWebServiceSecuredImpl
             * #getUrl(gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType)
             */
            @Override
            protected String getUrl(NhinTargetSystemType target) throws IllegalArgumentException,
                    ConnectionManagerException, Exception {
                return "endpoint";
            }
        };
    }
}
