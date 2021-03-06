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
package gov.hhs.fha.nhinc.docquery.model.builder.impl;

import gov.hhs.fha.nhinc.docquery.model.DocumentMetadataResult;
import gov.hhs.fha.nhinc.docquery.model.DocumentMetadataResults;
import gov.hhs.fha.nhinc.docquery.model.builder.DocumentMetadataResultModelBuilder;
import gov.hhs.fha.nhinc.docquery.model.builder.DocumentMetadataResultsModelBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;

/**
 *
 * @author tjafri
 */
public class DocumentMetadataResultsModelBuilderImpl implements DocumentMetadataResultsModelBuilder {

    /**
     * The results holder.
     */
    private List<DocumentMetadataResult> resultsHolder = null;

    /**
     * The results.
     */
    private DocumentMetadataResults results = null;

    /**
     * The adhocQuery response.
     */
    private AdhocQueryResponse ahqResponse = null;

    /**
     * Instantiates a new document metadata results model builder impl.
     */
    public DocumentMetadataResultsModelBuilderImpl() {
        resultsHolder = new ArrayList<>();
    }

    @Override
    public void build() {
        results = new DocumentMetadataResults();
        for (DocumentMetadataResult r : resultsHolder) {
            if (r != null) {
                results.addResult(r);
            }
        }

        if (ahqResponse != null && ahqResponse.getRegistryObjectList() != null) {
            for (JAXBElement<? extends IdentifiableType> i : ahqResponse.getRegistryObjectList().getIdentifiable()) {
                IdentifiableType iType = i.getValue();
                if (iType instanceof ExtrinsicObjectType) {
                    ExtrinsicObjectType registryObject = (ExtrinsicObjectType) iType;
                    DocumentMetadataResultModelBuilder resultBuilder = new DocumentMetadataResultModelBuilderImpl();
                    resultBuilder.setRegistryObjectType(registryObject);
                    resultBuilder.build();
                    results.addResult(resultBuilder.getDocumentMetadataResult());
                }
            }
        }
    }

    @Override
    public void add(DocumentMetadataResult documentMetadataResult) {
        resultsHolder.add(documentMetadataResult);
    }

    @Override
    public DocumentMetadataResults getResults() {
        return results;
    }

    @Override
    public void setAdhocQueryResponse(AdhocQueryResponse response) {
        this.ahqResponse = response;
    }

}
