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
package gov.hhs.fha.nhinc.messaging.builder.impl;

import gov.hhs.fha.nhinc.messaging.builder.impl.AbstractAssertionBuilder;
import gov.hhs.fha.nhinc.messaging.builder.AssertionBuilder;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthnStatementType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * This class extends from AbstractAssertionBuilder and implements the AssertionBuilderinterface.
 * <p>
 * The assertions are read from assertioninfo.properties file.
 *
 * @author tjafri
 */
public class AssertionBuilderImpl extends AbstractAssertionBuilder implements AssertionBuilder {

    private static final Logger LOG = Logger.getLogger(AssertionBuilderImpl.class);

    @Override
    public void build() {
        super.build();
        assertionType = new AssertionType();
        // building UserInfo assertion
        if (StringUtils.isBlank(userName)) {
            throw new IllegalArgumentException("User Name is required.");
        }

        UserType userInfo = new UserType();
        userInfo.setUserName(this.userName);

        PersonNameType personName = new PersonNameType();
        personName.setGivenName(this.userFirstName);
        personName.setFamilyName(this.userLastName);
        personName.setSecondNameOrInitials(this.userMiddleName);
        userInfo.setPersonName(personName);

        // User organization attribute statement
        if (StringUtils.isBlank(this.userOrganization)) {
            throw new IllegalArgumentException("User Organization is required.");
        }
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setName(this.userOrganization);
        userInfo.setOrg(homeCommunity);

        // User role
        if (StringUtils.isBlank(this.userCode) || StringUtils.isBlank(this.userSystem)
            || StringUtils.isBlank(this.userSystemName) || StringUtils.isBlank(this.userDisplay)) {
            CeType userRole = new CeType();
            userRole.setCode(this.userCode);
            userRole.setCodeSystem(this.userSystem);
            userRole.setCodeSystemName(this.userSystemName);
            userRole.setDisplayName(this.userDisplay);
            userInfo.setRoleCoded(userRole);
        }
        assertionType.setUserInfo(userInfo);
        //end UserInfo Assertion

        // building purposeCoded assertion
        CeType purposeCoded = new CeType();
        purposeCoded.setCode(this.purposeCode);
        purposeCoded.setCodeSystem(this.purposeCodeSystem);
        purposeCoded.setCodeSystemName(this.purposeCodeSystemName);
        purposeCoded.setDisplayName(this.purposeDisplayName);
        assertionType.setPurposeOfDisclosureCoded(purposeCoded);
        //end purposeCoded assertion

        // authentication instant
        SamlAuthnStatementType authnStatement = new SamlAuthnStatementType();
        authnStatement.setAuthInstant(this.samlAuthInstant);
        authnStatement.setAuthContextClassRef(this.samlAuthClass);
        assertionType.setSamlAuthnStatement(authnStatement);
    }

    @Override
    public AssertionType getAssertion() {
        return assertionType;
    }

}
