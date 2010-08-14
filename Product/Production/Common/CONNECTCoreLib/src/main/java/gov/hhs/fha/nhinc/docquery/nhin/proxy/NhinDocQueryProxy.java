/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docquery.nhin.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 *
 * @author jhoppesc
 */
public interface NhinDocQueryProxy {
    /**
     * Sends a document query to another Gateway.
     *
     * @param request Document Query Request.
     * @return List of Documents that match the search criteria.
     */
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest request, AssertionType assertion, NhinTargetSystemType target);
}