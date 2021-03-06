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
package gov.hhs.fha.nhinc.direct.addressparsing;

import gov.hhs.fha.nhinc.direct.DirectBaseTest;
import gov.hhs.fha.nhinc.direct.DirectException;
import java.util.ArrayList;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.nhindirect.xd.common.DirectDocuments;
import org.nhindirect.xd.common.DirectDocuments.SubmissionSet;

/**
 *
 * @author svalluripalli
 */
public class SmtpOnlyToAddresParserTest extends DirectBaseTest {

    /**
     * Test of parse method, of class SmtpOnlyToAddresParser.
     */
    @Test
    public void testParseHappy1() {
        String addresses = "test@connectopensource.org";
        DirectDocuments documents = null;
        SmtpOnlyToAddresParser instance = new SmtpOnlyToAddresParser();
        Set result = instance.parse(addresses, documents);
        assertNotNull(result);
    }

    @Test
    public void testParseHappy2() {
        String addresses = null;
        DirectDocuments documents = new DirectDocuments();
        SubmissionSet oSubmissionSet = new SubmissionSet();
        String intendentRcpt = "||^^^vlewis@lewistower.com";
        oSubmissionSet.getIntendedRecipient().add(intendentRcpt);
        documents.setSubmissionSet(oSubmissionSet);
        SmtpOnlyToAddresParser instance = new SmtpOnlyToAddresParser();
        Set result = instance.parse(addresses, documents);
        assertNotNull(result);
    }

    @Test(expected = DirectException.class)
    public void testParseFailure() {
        String addresses = null;
        DirectDocuments documents = mock(DirectDocuments.class);
        SubmissionSet submissionSet = mock(SubmissionSet.class);
        Set result = null;
        SmtpOnlyToAddresParser instance = new SmtpOnlyToAddresParser();

        when(documents.getSubmissionSet()).thenReturn(submissionSet);
        when(submissionSet.getIntendedRecipient()).thenReturn(new ArrayList<String>());

        try {
            result = instance.parse(addresses, documents);
        } catch (DirectException e) {
            System.out.println(e.getMessage());
            assertEquals(e.getMessage(), "There were no SMTP endpoints provided.");
            throw e;
        }

    }
}