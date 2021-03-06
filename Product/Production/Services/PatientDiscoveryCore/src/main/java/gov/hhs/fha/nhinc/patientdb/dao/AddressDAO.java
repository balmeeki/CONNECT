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
package gov.hhs.fha.nhinc.patientdb.dao;

import gov.hhs.fha.nhinc.patientdb.model.Address;
import gov.hhs.fha.nhinc.patientdb.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.patientdb.persistence.HibernateUtilFactory;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * AddressDAO Class provides methods to query and update Address Data to/from MySQL Database using Hibernate.
 *
 * @author richard.ettema
 */
public class AddressDAO {

    private static final Logger LOG = LoggerFactory.getLogger(AddressDAO.class);
    private static AddressDAO addressDAO = new AddressDAO();

    /**
     * Constructor.
     */
    private AddressDAO() {
        LOG.info("AddressDAO - Initialized");
    }

    /**
     * Singleton instance returned...
     *
     * @return AddressDAO
     */
    public static AddressDAO getAddressDAOInstance() {
        LOG.debug("getAddressDAOInstance()..");
        return addressDAO;
    }

    // =========================
    // Standard CRUD DML Methods
    // =========================
    /**
     * Create a single <code>Address</code> record. The generated id will be available in the addressRecord.
     *
     * @param addressRecord
     * @return boolean
     */
    public boolean create(Address addressRecord) {

        LOG.debug("AddressDAO.create() - Begin");

        Session session = null;

        Transaction tx = null;

        boolean result = true;

        if (addressRecord != null) {

            try {

                SessionFactory sessionFactory = getSessionFactory();

                session = sessionFactory.openSession();

                tx = session.beginTransaction();

                LOG.info("Inserting Record...");

                session.persist(addressRecord);

                LOG.info("Address Inserted seccussfully...");

                tx.commit();

            } catch (HibernateException | NullPointerException e) {

                result = false;

                if (tx != null) {
                    tx.rollback();
                }

                LOG.error("Exception during insertion caused by : {}", e.getMessage(), e);

            } finally {

                // Actual Address insertion will happen at this step
                if (session != null) {
                    try {
                        session.close();
                    } catch (HibernateException e) {
                        LOG.error("Exception while closing the session: {}", e.getMessage(), e);
                    }

                }
            }

        }

        LOG.debug("AddressDAO.create() - End");

        return result;

    }

    /**
     *
     * Read (Query) the database to get a <code>Address</code> record based
     *
     * on a known id.
     *
     * @param id
     *
     * @return Address
     */
    public Address read(Long id) {

        LOG.debug("AddressDAO.read() - Begin");

        if (id == null) {

            LOG.info("-- id Parameter is required for Address Query --");

            LOG.debug("AddressDAO.read() - End");

            return null;

        }

        Session session = null;

        List<Address> queryList;

        Address foundRecord = null;

        try {

            SessionFactory sessionFactory = getSessionFactory();

            session = sessionFactory.openSession();

            LOG.info("Reading Record...");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(Address.class);

            aCriteria.add(Expression.eq("id", id));

            queryList = aCriteria.list();

            if (queryList != null && !queryList.isEmpty()) {

                foundRecord = queryList.get(0);

            }

        } catch (HibernateException | NullPointerException e) {

            LOG.error("Exception during read occured due to : {}", e.getMessage(), e);

        } finally {

            // Flush and close session
            if (session != null) {

                try {
                    session.flush();
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session after a read: {}", e.getMessage(), e);
                }

            }
        }

        LOG.debug("AddressDAO.read() - End");

        return foundRecord;

    }

    /**
     *
     * Update a single <code>Address</code> record.
     *
     * @param addressRecord
     *
     * @return boolean
     */
    public boolean update(Address addressRecord) {

        LOG.debug("AddressDAO.update() - Begin");

        Session session = null;

        Transaction tx = null;

        boolean result = true;

        if (addressRecord != null) {

            try {

                SessionFactory sessionFactory = getSessionFactory();

                session = sessionFactory.openSession();

                tx = session.beginTransaction();

                LOG.info("Updating Record...");

                session.saveOrUpdate(addressRecord);

                LOG.info("Address Updated seccussfully...");

                tx.commit();

            } catch (HibernateException | NullPointerException e) {

                result = false;

                if (tx != null) {

                    tx.rollback();

                }

                LOG.error("Exception during update caused by : {}", e.getMessage(), e);

            } finally {

                // Actual Address update will happen at this step
                if (session != null) {
                    try {
                        session.close();
                    } catch (HibernateException e) {
                        LOG.error("Exception while closing the session after update: {}", e.getMessage(), e);
                    }

                }
            }

        }

        LOG.debug("AddressDAO.update() - End");

        return result;

    }

    /**
     *
     * Delete a <code>Address</code> record from the database.
     *
     * @param addressRecord
     */
    public void delete(Address addressRecord) {

        LOG.debug("AddressDAO.delete() - Begin");

        Session session = null;

        try {

            SessionFactory sessionFactory = getSessionFactory();

            session = sessionFactory.openSession();

            LOG.info("Deleting Record...");

            // Delete the Address record
            session.delete(addressRecord);

        } catch (HibernateException | NullPointerException e) {

            LOG.error("Exception during delete occured due to : {}", e.getMessage(), e);

        } finally {

            // Flush and close session
            if (session != null) {

                try {
                    session.flush();
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session after a delete: {}", e.getMessage(), e);
                }

            }
        }

        LOG.debug("AddressDAO.delete() - End");

    }

    // =========================
    // Find DML Methods
    // =========================
    /**
     *
     * Read (Query) the database to get all <code>Address</code> records based
     *
     * on a known patientId.
     *
     * @param patientId
     *
     * @return List<Address>
     */
    public List<Address> findPatientAddresses(Long patientId) {

        LOG.debug("AddressDAO.readPatientAddresses() - Begin");

        List<Address> queryList = new ArrayList<>();
        Session session = null;

        if (patientId == null) {

            LOG.info("-- patientId Parameter is required for Address Query --");

            LOG.debug("AddressDAO.readPatientAddresses() - End");

            return queryList;
        }

        try {

            SessionFactory sessionFactory = getSessionFactory();

            session = sessionFactory.openSession();

            LOG.info("Reading Record...");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(Address.class);

            aCriteria.add(Expression.eq("patient.patientId", patientId));

            queryList = aCriteria.list();

        } catch (HibernateException | NullPointerException e) {

            LOG.error("Exception during read occured due to : {}", e.getMessage(), e);

        } finally {

            // Flush and close session
            if (session != null) {

                try {
                    session.flush();
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session after looking for patient address: {}",
                            e.getMessage(), e);
                }

            }

        }

        LOG.debug("readPatientAddresses.read() - End");

        return queryList;

    }

    protected SessionFactory getSessionFactory() {
        SessionFactory fact = null;
        HibernateUtil util = HibernateUtilFactory.getPatientDiscHibernateUtil();
        if (util != null) {
            fact = util.getSessionFactory();
        }
        return fact;
    }

}
