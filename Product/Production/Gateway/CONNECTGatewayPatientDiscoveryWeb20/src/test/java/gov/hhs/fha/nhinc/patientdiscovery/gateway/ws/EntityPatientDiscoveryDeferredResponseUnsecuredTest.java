package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.EntityPatientDiscoveryDeferredResponseImpl;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class EntityPatientDiscoveryDeferredResponseUnsecuredTest {
 	Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    @Test
    public void testDefaultConstructor() {
    	EntityPatientDiscoveryDeferredResponseUnsecured ws = new EntityPatientDiscoveryDeferredResponseUnsecured();
    	assertNotNull(ws);
    }
    
    @Test
    public void testMockService() {
    	final PatientDiscoveryServiceFactory mockFactory = context.mock(PatientDiscoveryServiceFactory.class);
    	final RespondingGatewayPRPAIN201306UV02RequestType mockRequest = context.mock(RespondingGatewayPRPAIN201306UV02RequestType.class);
		final MCCIIN000002UV01 expectedResponse = context.mock(MCCIIN000002UV01.class);
		final EntityPatientDiscoveryDeferredResponseImpl mockService = context.mock(EntityPatientDiscoveryDeferredResponseImpl.class);
		
		context.checking(new Expectations() {{
	        oneOf(mockService).processPatientDiscoveryAsyncResp(with(same(mockRequest)), with(any(WebServiceContext.class)));
	        will(returnValue(expectedResponse));
	        
	        oneOf(mockFactory).getEntityPatientDiscoveryDeferredResponseImpl();
	        will(returnValue(mockService));
	    }});
    	
    	
		EntityPatientDiscoveryDeferredResponseUnsecured ws = new EntityPatientDiscoveryDeferredResponseUnsecured(mockFactory);
    	
		MCCIIN000002UV01 actualResponse = ws.processPatientDiscoveryAsyncResp(mockRequest);
		
		assertSame(expectedResponse, actualResponse);
    }
	
}
