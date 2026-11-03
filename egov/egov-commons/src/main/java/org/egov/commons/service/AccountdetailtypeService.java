/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.commons.service;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;
import javax.validation.ValidationException;

import org.apache.poi.ss.usermodel.*;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.contracts.AccountDetailTypeSearchRequest;
import org.egov.commons.dao.AccountdetailtypeUploadResult;
import org.egov.commons.dao.AccountdetailtypeUploadRow;
import org.egov.commons.repository.AccountdetailtypeRepository;
import org.egov.infra.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AccountdetailtypeService {

    private final AccountdetailtypeRepository accountdetailtypeRepository;
    @PersistenceContext
    private EntityManager entityManager;

   /* @Autowired
    private SecurityUtils securityUtils;*/

    @Autowired
    public AccountdetailtypeService(final AccountdetailtypeRepository accountdetailtypeRepository) {
        this.accountdetailtypeRepository = accountdetailtypeRepository;
    }

    @Transactional
    public Accountdetailtype create(final Accountdetailtype accountdetailtype) {
        return accountdetailtypeRepository.save(accountdetailtype);
    }

    @Transactional
    public Accountdetailtype update(final Accountdetailtype accountdetailtype) {
        return accountdetailtypeRepository.save(accountdetailtype);
    }

    public List<Accountdetailtype> findAll() {
        return accountdetailtypeRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public Accountdetailtype findByName(final String name) {
        return accountdetailtypeRepository.findByName(name);
    }
    
    public Accountdetailtype findOne(final Integer id) {
        return accountdetailtypeRepository.findOne(id);
    }

    public List<Accountdetailtype> findByFullQualifiedName(final String fullQualifiedName) {
        return accountdetailtypeRepository.findByFullQualifiedName(fullQualifiedName);
    }

    public List<Accountdetailtype> search(final AccountDetailTypeSearchRequest accountDetailTypeSearchRequest, final String mode) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Accountdetailtype> createQuery = cb.createQuery(Accountdetailtype.class);
        final Root<Accountdetailtype> accountdetailtypes = createQuery.from(Accountdetailtype.class);
        createQuery.select(accountdetailtypes);
        final Metamodel m = entityManager.getMetamodel();
        final javax.persistence.metamodel.EntityType<Accountdetailtype> tempAccountDetailType = m.entity(Accountdetailtype.class);

        final List<Predicate> predicates = new ArrayList<>();
        if (accountDetailTypeSearchRequest.getName() != null) {
            final String name = "%" + accountDetailTypeSearchRequest.getName().toLowerCase() + "%";
            predicates.add(cb.isNotNull(accountdetailtypes.get("name")));
            predicates.add(cb.like(
                    cb.lower(accountdetailtypes.get(tempAccountDetailType.getDeclaredSingularAttribute("name", String.class))),
                    name));
        }
        if (accountDetailTypeSearchRequest.getDescription() != null) {
            final String code = "%" + accountDetailTypeSearchRequest.getDescription().toLowerCase() + "%";
            predicates.add(cb.isNotNull(accountdetailtypes.get("description")));
            predicates.add(cb.like(
                    cb.lower(
                            accountdetailtypes
                                    .get(tempAccountDetailType.getDeclaredSingularAttribute("description", String.class))),
                    code));
        }
        if ("edit".equalsIgnoreCase(mode))
            predicates.add(cb.equal(accountdetailtypes.get("fullQualifiedName"), "org.egov.masters.model.AccountEntity"));
        createQuery.where(predicates.toArray(new Predicate[] {}));
        final TypedQuery<Accountdetailtype> query = entityManager.createQuery(createQuery);

        return query.getResultList();
    }

    public List<Accountdetailtype> findByGlcodeId(final Long glcodeId) {
        return accountdetailtypeRepository.findByGlcodeId(glcodeId);
    }


/*
    @Transactional
    public AccountdetailtypeUploadResult createFromUpload(List<AccountdetailtypeUploadRow> rows) {
        if (rows == null || rows.isEmpty())
            throw new ValidationException("File has no data rows");

        Set<String> seenNames = new HashSet<>();
        List<String> errors = new ArrayList<>();
        AccountdetailtypeUploadResult result = new AccountdetailtypeUploadResult();
        List<AccountdetailtypeUploadRow> toCreate = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            AccountdetailtypeUploadRow r = rows.get(i);
            int line = i + 2; // Excel row number (header is row 1)
            String name = r.getName() == null ? "" : r.getName().trim();
            String description = r.getDescription() == null ? "" : r.getDescription().trim();

            if (name.isEmpty() || description.isEmpty()) {
                errors.add("Row " + line + ": Name and Description are mandatory");
                continue;
            }
            if (name.length() > 50) {
                errors.add("Row " + line + ": Name is too long (max 50 characters)");
                continue;
            }
            if (!seenNames.add(name.toLowerCase())) {
                result.getSkipped().add("Row " + line + ": Duplicate name in file, skipped - " + name);
                continue;
            }
            if (accountdetailtypeRepository.findByName(name) != null) {
                result.getSkipped().add("Row " + (line-1) + ": Already exists, skipped - " + name);
                continue;
            }
            toCreate.add(r);
        }
        if (!errors.isEmpty())
            throw new ValidationException(String.join("; ", errors));

        for (AccountdetailtypeUploadRow r : toCreate) {
            String name = r.getName().trim();
            Accountdetailtype adt = new Accountdetailtype();
            adt.setName(name);
            adt.setDescription(r.getDescription().trim());
            adt.setIsactive(r.isActive());
            adt.setTablename("accountEntityMaster");
            adt.setColumnname("id");
            adt.setAttributename(name+"_id");
            adt.setNbroflevels(BigDecimal.ONE);
            adt.setFullQualifiedName("org.egov.masters.model.AccountEntity");
            adt.setCreatedDate(new Date());
            adt.setLastModifiedDate(new Date());
            adt.setLastModifiedBy(securityUtils.getCurrentUser().getId());
            accountdetailtypeRepository.save(adt);
        }
        result.setSaved(toCreate.size());
        return result;
    }*/
}