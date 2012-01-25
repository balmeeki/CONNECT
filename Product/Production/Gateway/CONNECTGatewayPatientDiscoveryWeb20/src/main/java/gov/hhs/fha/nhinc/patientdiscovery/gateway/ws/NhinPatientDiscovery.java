/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryImpl;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 * 
 * @author Neil Webb
 */
@WebService(serviceName = "RespondingGateway_Service", portName = "RespondingGateway_Port_Soap", endpointInterface = "ihe.iti.xcpd._2009.RespondingGatewayPortType", targetNamespace = "urn:ihe:iti:xcpd:2009", wsdlLocation = "WEB-INF/wsdl/NhinPatientDiscovery/NhinPatientDiscovery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class NhinPatientDiscovery extends PatientDiscoveryBase {

	@Resource
	private WebServiceContext context;

	public NhinPatientDiscovery() {
		super();
	}

	public NhinPatientDiscovery(PatientDiscoveryServiceFactory serviceFactory) {
		super(serviceFactory);
	}

	public org.hl7.v3.PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(
			org.hl7.v3.PRPAIN201305UV02 body) {
		return getNhinPatientDiscoveryService()
				.respondingGatewayPRPAIN201305UV02(body, context);
	}

	protected NhinPatientDiscoveryImpl getNhinPatientDiscoveryService() {
		return getServiceFactory().getNhinPatientDiscoveryService();
	}

}
