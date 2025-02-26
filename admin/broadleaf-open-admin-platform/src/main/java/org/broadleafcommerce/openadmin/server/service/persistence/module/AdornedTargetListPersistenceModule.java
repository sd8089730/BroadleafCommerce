/*-
 * #%L
 * BroadleafCommerce Open Admin Platform
 * %%
 * Copyright (C) 2009 - 2022 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
package org.broadleafcommerce.openadmin.server.service.persistence.module;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.exception.ExceptionHelper;
import org.broadleafcommerce.common.exception.SecurityServiceException;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.common.presentation.client.OperationType;
import org.broadleafcommerce.common.presentation.client.PersistencePerspectiveItemType;
import org.broadleafcommerce.common.presentation.client.SupportedFieldType;
import org.broadleafcommerce.openadmin.dto.*;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.FieldPath;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.FieldPathBuilder;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.FilterMapping;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.Restriction;
import org.broadleafcommerce.openadmin.server.service.persistence.module.criteria.predicate.PredicateProvider;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author jfischer
 */
@Component("blAdornedTargetListPersistenceModule")
@Scope("prototype")
public class AdornedTargetListPersistenceModule extends BasicPersistenceModule {

    private static final Log LOG = LogFactory.getLog(AdornedTargetListPersistenceModule.class);

    @Override
    public boolean isCompatible(OperationType operationType) {
        return OperationType.ADORNEDTARGETLIST.equals(operationType);
    }

    @Override
    public void extractProperties(Class<?>[] inheritanceLine, Map<MergedPropertyType, Map<String, FieldMetadata>> mergedProperties, List<Property> properties) throws NumberFormatException {
        if (mergedProperties.get(MergedPropertyType.ADORNEDTARGETLIST) != null) {
            extractPropertiesFromMetadata(inheritanceLine, mergedProperties.get(MergedPropertyType.ADORNEDTARGETLIST), properties, true, MergedPropertyType.ADORNEDTARGETLIST);
        }
    }
    
    public List<FilterMapping> getBasicFilterMappings(PersistencePerspective persistencePerspective,
                    CriteriaTransferObject cto, Map<String, FieldMetadata> mergedProperties,
                    String cefqcn) {
        return getFilterMappings(persistencePerspective, cto, cefqcn, mergedProperties);
    }

    public List<FilterMapping> getAdornedTargetFilterMappings(PersistencePerspective persistencePerspective,
                    CriteriaTransferObject cto, Map<String, FieldMetadata> mergedProperties,
                    AdornedTargetList adornedTargetList) throws ClassNotFoundException {
        List<FilterMapping> filterMappings = getFilterMappings(persistencePerspective, cto, adornedTargetList.
                getAdornedTargetEntityClassname(), mergedProperties);
        FilterMapping filterMapping = new FilterMapping()
            .withFieldPath(new FieldPath()
                    .withTargetProperty(adornedTargetList.getLinkedObjectPath() + "." + adornedTargetList.getLinkedIdProperty()))
            .withFilterValues(cto.get(adornedTargetList.getCollectionFieldName()).getFilterValues())
            .withRestriction(new Restriction()
                .withPredicateProvider(new PredicateProvider<Serializable, String>() {
                    @Override
                    public Predicate buildPredicate(CriteriaBuilder builder, FieldPathBuilder fieldPathBuilder, From root,
                                                    String ceilingEntity, String fullPropertyName, Path<Serializable> explicitPath,
                                                    List<String> directValues) {
                        if (String.class.isAssignableFrom(explicitPath.getJavaType())) {
                            return builder.equal(explicitPath, directValues.get(0));
                        } else {
                            return builder.equal(explicitPath, Long.parseLong(directValues.get(0)));
                        }
                    }
                })
            );
        filterMappings.add(filterMapping);
        FilterMapping filterMapping2 = new FilterMapping()
            .withFieldPath(new FieldPath()
                    .withTargetProperty(adornedTargetList.getTargetObjectPath() + "." +
                                        adornedTargetList.getTargetIdProperty()))
            .withFilterValues(cto.get(adornedTargetList.getCollectionFieldName() + "Target").getFilterValues())
            .withRestriction(new Restriction()
                .withPredicateProvider(new PredicateProvider<Serializable, String>() {
                    @Override
                    public Predicate buildPredicate(CriteriaBuilder builder, FieldPathBuilder fieldPathBuilder, From root,
                                                    String ceilingEntity, String fullPropertyName, Path<Serializable> explicitPath,
                                                    List<String> directValues) {
                        if (String.class.isAssignableFrom(explicitPath.getJavaType())) {
                            return builder.equal(explicitPath, directValues.get(0));
                        } else {
                            return builder.equal(explicitPath, Long.parseLong(directValues.get(0)));
                        }
                    }
                })
            );
        filterMappings.add(filterMapping2);

        return filterMappings;
    }

    protected Serializable createPopulatedAdornedTargetInstance(AdornedTargetList adornedTargetList, Entity entity) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NumberFormatException, InvocationTargetException, NoSuchMethodException, FieldNotAvailableException {
        Serializable instance = (Serializable) Class.forName(StringUtils.isEmpty(adornedTargetList
                .getAdornedTargetEntityPolymorphicType())? adornedTargetList.getAdornedTargetEntityClassname(): adornedTargetList.getAdornedTargetEntityPolymorphicType()).newInstance();
        String targetPath = adornedTargetList.getTargetObjectPath() + "." + adornedTargetList.getTargetIdProperty();
        String linkedPath = adornedTargetList.getLinkedObjectPath() + "." + adornedTargetList.getLinkedIdProperty();
        getFieldManager().setFieldValue(instance, linkedPath, Long.valueOf(entity.findProperty(linkedPath).getValue()));

        Object test1 = getFieldManager().getFieldValue(instance, adornedTargetList.getLinkedObjectPath());
        Object test1PersistedObject = persistenceManager.getDynamicEntityDao().retrieve(test1.getClass(), Long.valueOf(entity.findProperty(linkedPath).getValue()));
        Assert.isTrue(test1PersistedObject != null, "Entity not found");

        Class<?> type = getFieldManager().getField(instance.getClass(), targetPath).getType();
        if (String.class.isAssignableFrom(type)) {
            getFieldManager().setFieldValue(instance, targetPath, entity.findProperty(targetPath).getValue());
        } else {
            getFieldManager().setFieldValue(instance, targetPath, Long.valueOf(entity.findProperty(targetPath).getValue()));
        }

        Object test2 = getFieldManager().getFieldValue(instance, adornedTargetList.getTargetObjectPath());
        Object test2PersistedObject;
        if (String.class.isAssignableFrom(type)) {
            test2PersistedObject = persistenceManager.getDynamicEntityDao().retrieve(test2.getClass(), entity.findProperty(targetPath).getValue());
        } else {
            test2PersistedObject = persistenceManager.getDynamicEntityDao().retrieve(test2.getClass(), Long.valueOf(entity.findProperty(targetPath).getValue()));
        }
        Assert.isTrue(test2PersistedObject != null, "Entity not found");

        return instance;
    }

    @Override
    public void updateMergedProperties(PersistencePackage persistencePackage, Map<MergedPropertyType, Map<String, FieldMetadata>> allMergedProperties) throws ServiceException {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        try {
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
            AdornedTargetList adornedTargetList = (AdornedTargetList) persistencePerspective.getPersistencePerspectiveItems().get(PersistencePerspectiveItemType.ADORNEDTARGETLIST);
            if (adornedTargetList != null) {
                Class<?>[] entities = persistenceManager.getPolymorphicEntities(adornedTargetList.getAdornedTargetEntityClassname());
                Map<String, FieldMetadata> joinMergedProperties = persistenceManager.getDynamicEntityDao().getMergedProperties(
                        adornedTargetList.getAdornedTargetEntityClassname(),
                        entities,
                        null,
                        new String[]{},
                        new ForeignKey[]{},
                        MergedPropertyType.ADORNEDTARGETLIST,
                        persistencePerspective.getPopulateToOneFields(),
                        persistencePerspective.getIncludeFields(),
                        persistencePerspective.getExcludeFields(),
                        persistencePerspective.getConfigurationKey(),
                        ""
                );
                String idProp = null;
                for (String key : joinMergedProperties.keySet()) {
                    if (joinMergedProperties.get(key) instanceof BasicFieldMetadata && ((BasicFieldMetadata) joinMergedProperties.get(key)).getFieldType() == SupportedFieldType.ID) {
                        idProp = key;
                        break;
                    }
                }
                if (idProp != null) {
                    joinMergedProperties.remove(idProp);
                }
                allMergedProperties.put(MergedPropertyType.ADORNEDTARGETLIST, joinMergedProperties);
            }
        } catch (Exception e) {
            throw new ServiceException("Unable to fetch results for " + ceilingEntityFullyQualifiedClassname, e);
        }
    }

    @Override
    public Entity add(PersistencePackage persistencePackage) throws ServiceException {
        String[] customCriteria = persistencePackage.getCustomCriteria();
        if (customCriteria != null && customCriteria.length > 0) {
            LOG.warn("custom persistence handlers and custom criteria not supported for add types other than BASIC");
        }
        PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        Entity entity = persistencePackage.getEntity();
        AdornedTargetList adornedTargetList = (AdornedTargetList) persistencePerspective.getPersistencePerspectiveItems().get(PersistencePerspectiveItemType.ADORNEDTARGETLIST);
        if (!adornedTargetList.getMutable()) {
            throw new SecurityServiceException("Field is not mutable");
        }
        Entity payload;
        try {
            Class<?>[] entities = persistenceManager.getPolymorphicEntities(ceilingEntityFullyQualifiedClassname);
            Map<String, FieldMetadata> mergedPropertiesTarget = persistenceManager.getDynamicEntityDao().getMergedProperties(
                    ceilingEntityFullyQualifiedClassname,
                    entities,
                    null,
                    persistencePerspective.getAdditionalNonPersistentProperties(),
                    persistencePerspective.getAdditionalForeignKeys(),
                    MergedPropertyType.PRIMARY,
                    persistencePerspective.getPopulateToOneFields(),
                    persistencePerspective.getIncludeFields(),
                    persistencePerspective.getExcludeFields(),
                    persistencePerspective.getConfigurationKey(),
                    ""
            );
            Class<?>[] entities2 = persistenceManager.getPolymorphicEntities(adornedTargetList.getAdornedTargetEntityClassname());
            Map<String, FieldMetadata> mergedProperties = persistenceManager.getDynamicEntityDao().getMergedProperties(
                    adornedTargetList.getAdornedTargetEntityClassname(),
                    entities2,
                    null,
                    new String[]{},
                    new ForeignKey[]{},
                    MergedPropertyType.ADORNEDTARGETLIST,
                    false,
                    new String[]{},
                    new String[]{},
                    null,
                    ""
            );

            CriteriaTransferObject ctoInserted = new CriteriaTransferObject();
            FilterAndSortCriteria filterCriteriaInsertedLinked = ctoInserted.get(adornedTargetList.getCollectionFieldName());
            String linkedPath;
            String targetPath;
            if (adornedTargetList.getInverse()) {
                linkedPath = adornedTargetList.getTargetObjectPath() + "." + adornedTargetList.getTargetIdProperty();
                targetPath = adornedTargetList.getLinkedObjectPath() + "." + adornedTargetList.getLinkedIdProperty();
            } else {
                targetPath = adornedTargetList.getTargetObjectPath() + "." + adornedTargetList.getTargetIdProperty();
                linkedPath = adornedTargetList.getLinkedObjectPath() + "." + adornedTargetList.getLinkedIdProperty();
            }
            filterCriteriaInsertedLinked.setFilterValue(entity.findProperty(adornedTargetList.getInverse() ? targetPath : linkedPath).getValue());
            FilterAndSortCriteria filterCriteriaInsertedTarget = ctoInserted.get(adornedTargetList.getCollectionFieldName() + "Target");
            filterCriteriaInsertedTarget.setFilterValue(entity.findProperty(adornedTargetList.getInverse() ? linkedPath : targetPath).getValue());
            List<FilterMapping> filterMappingsInserted = getAdornedTargetFilterMappings(persistencePerspective, ctoInserted, mergedProperties, adornedTargetList);
            List<Serializable> recordsInserted = getPersistentRecords(adornedTargetList.getAdornedTargetEntityClassname(), filterMappingsInserted, ctoInserted.getFirstResult(), ctoInserted.getMaxResults());
            if (recordsInserted.size() > 0) {
                payload = getRecords(mergedPropertiesTarget, recordsInserted, mergedProperties, adornedTargetList.getTargetObjectPath(), null)[0];
            } else {
                Serializable instance = createPopulatedAdornedTargetInstance(adornedTargetList, entity);
                instance = createPopulatedInstance(instance, entity, mergedProperties, false, persistencePackage.isValidateUnsubmittedProperties());
                instance = createPopulatedInstance(instance, entity, mergedPropertiesTarget, false, persistencePackage.isValidateUnsubmittedProperties());
                FieldManager fieldManager = getFieldManager();
                if (fieldManager.getField(instance.getClass(), "id") != null) {
                    fieldManager.setFieldValue(instance, "id", null);
                }
                if (adornedTargetList.getSortField() != null) {
                    // Construct a query that gets the last element in the join list ordered by the sort property. This will
                    // ensure that the new record is always the last element in the list
                    CriteriaTransferObject cto = new CriteriaTransferObject();
                    FilterAndSortCriteria filterCriteria = cto.get(adornedTargetList.getCollectionFieldName());
                    filterCriteria.setFilterValue(entity.findProperty(adornedTargetList.getInverse() ? targetPath : linkedPath).getValue());
                    FilterAndSortCriteria sortCriteria = cto.get(adornedTargetList.getSortField());
                    // criteria for which way to sort should be the opposite of how it is normally sorted so that it is
                    // always inserted at the end
                    sortCriteria.setSortAscending(!adornedTargetList.getSortAscending());
                    List<FilterMapping> filterMappings = getAdornedTargetFilterMappings(persistencePerspective, cto,
                            mergedProperties, adornedTargetList);
                    List<Serializable> joinList = getPersistentRecords(adornedTargetList.getAdornedTargetEntityClassname(), filterMappings, 0, 1);
                    
                    Object adornedLastOrdering = null;
                    if (CollectionUtils.isNotEmpty(joinList)) {
                        adornedLastOrdering = fieldManager.getFieldValue(joinList.get(0), adornedTargetList.getSortField());
                    }
                    Field sortFieldDef = fieldManager.getField(instance.getClass(), adornedTargetList.getSortField());
                    int add = (adornedLastOrdering == null) ? 0 : 1;
                    if (sortFieldDef.getType().isAssignableFrom(Long.class)) {
                        adornedLastOrdering = (adornedLastOrdering == null) ? new Long(0) : adornedLastOrdering;
                        fieldManager.setFieldValue(instance, adornedTargetList.getSortField(), new Long(((Long) adornedLastOrdering).longValue() + add));
                    } else if (sortFieldDef.getType().isAssignableFrom(Integer.class)) {
                        adornedLastOrdering = (adornedLastOrdering == null) ? new Integer(0) : adornedLastOrdering;
                        fieldManager.setFieldValue(instance, adornedTargetList.getSortField(), new Integer(((Integer) adornedLastOrdering).intValue() + add));
                    } else if (sortFieldDef.getType().isAssignableFrom(BigDecimal.class)) {
                        adornedLastOrdering = (adornedLastOrdering == null) ? BigDecimal.ZERO : adornedLastOrdering;
                        fieldManager.setFieldValue(instance, adornedTargetList.getSortField(), ((BigDecimal) adornedLastOrdering).add(new BigDecimal(add)));
                    }
                }
                instance = persistenceManager.getDynamicEntityDao().merge(instance);
                persistenceManager.getDynamicEntityDao().clear();

                List<Serializable> recordsInserted2 = getPersistentRecords(adornedTargetList.getAdornedTargetEntityClassname(), filterMappingsInserted, ctoInserted.getFirstResult(), ctoInserted.getMaxResults());

                payload = getRecords(mergedPropertiesTarget, recordsInserted2, mergedProperties, adornedTargetList.getTargetObjectPath(), null)[0];
            }
        } catch (Exception e) {
            throw new ServiceException("Problem adding new entity : " + e.getMessage(), e);
        }

        return payload;
    }

    @Override
    public Entity update(PersistencePackage persistencePackage) throws ServiceException {
        String[] customCriteria = persistencePackage.getCustomCriteria();
        if (customCriteria != null && customCriteria.length > 0) {
            LOG.warn("custom persistence handlers and custom criteria not supported for update types other than BASIC");
        }
        PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
        Entity entity = persistencePackage.getEntity();
        AdornedTargetList adornedTargetList = (AdornedTargetList) persistencePerspective.getPersistencePerspectiveItems().get(PersistencePerspectiveItemType.ADORNEDTARGETLIST);
        if (!adornedTargetList.getMutable()) {
            throw new SecurityServiceException("Field is not mutable");
        }
        try {
            AdornedTargetRetrieval adornedTargetRetrieval = new AdornedTargetRetrieval(persistencePackage, entity, adornedTargetList).invokeForUpdate();
            List<Serializable> records = adornedTargetRetrieval.getRecords();

            Assert.isTrue(!CollectionUtils.isEmpty(records), "Entity not found");

            int index = adornedTargetRetrieval.getIndex();
            Map<String, FieldMetadata> mergedProperties = adornedTargetRetrieval.getMergedProperties();
            FieldManager fieldManager = getFieldManager();
            
            Serializable myRecord;
            if (adornedTargetList.getSortField() != null && entity.findProperty(adornedTargetList.getSortField()).getValue() != null) {
                myRecord = records.get(index);
                
                BigDecimal requestedSequence = new BigDecimal(entity.findProperty(adornedTargetList.getSortField()).getValue());
                BigDecimal previousSequence = new BigDecimal(String.valueOf(getFieldManager().getFieldValue(myRecord, adornedTargetList.getSortField())));
                
                if (!previousSequence.equals(requestedSequence)) {
                    // Sequence has changed. Rebalance the list
                    myRecord = records.remove(index);
                    myRecord = createPopulatedInstance(myRecord, entity, mergedProperties, false);
                    if (CollectionUtils.isEmpty(records)) {
                        records.add(myRecord);
                    } else {
                        records.add(requestedSequence.intValue() - 1, myRecord);
                    }
                    
                    index = 1;
                    Class<?> type = fieldManager.getField(myRecord.getClass(), adornedTargetList.getSortField()).getType();
                    boolean isBigDecimal = BigDecimal.class.isAssignableFrom(type);
                    for (Serializable record : records) {
                        fieldManager.setFieldValue(record, adornedTargetList.getSortField(), isBigDecimal?new BigDecimal(index):Long.valueOf(index));
                        index++;
                    }
                }
            } else {
                myRecord = records.get(index);
            }
            
            String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
            Class<?>[] entities = persistenceManager.getPolymorphicEntities(ceilingEntityFullyQualifiedClassname);
            Map<String, FieldMetadata> mergedPropertiesTarget = persistenceManager.getDynamicEntityDao().getMergedProperties(
                    ceilingEntityFullyQualifiedClassname,
                    entities,
                    null,
                    persistencePerspective.getAdditionalNonPersistentProperties(),
                    persistencePerspective.getAdditionalForeignKeys(),
                    MergedPropertyType.PRIMARY,
                    persistencePerspective.getPopulateToOneFields(),
                    persistencePerspective.getIncludeFields(),
                    persistencePerspective.getExcludeFields(),
                    persistencePerspective.getConfigurationKey(),
                    ""
            );
            myRecord = createPopulatedInstance(myRecord, entity, mergedProperties, false);
            myRecord = persistenceManager.getDynamicEntityDao().merge(myRecord);
            List<Serializable> myList = new ArrayList<Serializable>();
            myList.add(myRecord);
            Entity[] payload = getRecords(mergedPropertiesTarget, myList, mergedProperties, adornedTargetList.getTargetObjectPath(), null);
            entity = payload[0];

            return entity;
        } catch (Exception e) {
            throw new ServiceException("Problem updating entity : " + e.getMessage(), e);
        }
    }

    @Override
    public void remove(PersistencePackage persistencePackage) throws ServiceException {
        String[] customCriteria = persistencePackage.getCustomCriteria();
        if (customCriteria != null && customCriteria.length > 0) {
            LOG.warn("custom persistence handlers and custom criteria not supported for remove types other than BASIC");
        }
        PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
        Entity entity = persistencePackage.getEntity();
        try {
            AdornedTargetList adornedTargetList = (AdornedTargetList) persistencePerspective.getPersistencePerspectiveItems().get(PersistencePerspectiveItemType.ADORNEDTARGETLIST);
            if (!adornedTargetList.getMutable()) {
                throw new SecurityServiceException("Field is not mutable");
            }
            Class<?>[] entities = persistenceManager.getPolymorphicEntities(adornedTargetList.getAdornedTargetEntityClassname());
            Map<String, FieldMetadata> mergedProperties = persistenceManager.getDynamicEntityDao().getMergedProperties(
                    adornedTargetList.getAdornedTargetEntityClassname(),
                    entities,
                    null,
                    new String[]{},
                    new ForeignKey[]{},
                    MergedPropertyType.ADORNEDTARGETLIST,
                    false,
                    new String[]{},
                    new String[]{},
                    null,
                    ""
            );
            CriteriaTransferObject ctoInserted = new CriteriaTransferObject();
            FilterAndSortCriteria filterCriteriaInsertedLinked = ctoInserted.get(adornedTargetList.getCollectionFieldName());
            filterCriteriaInsertedLinked.setFilterValue(entity.findProperty(adornedTargetList.getLinkedObjectPath() + "." + adornedTargetList.getLinkedIdProperty()).getValue());
            FilterAndSortCriteria filterCriteriaInsertedTarget = ctoInserted.get(adornedTargetList.getCollectionFieldName() + "Target");
            filterCriteriaInsertedTarget.setFilterValue(entity.findProperty(adornedTargetList.getTargetObjectPath() + "." + adornedTargetList.getTargetIdProperty()).getValue());
            List<FilterMapping> filterMappings = getAdornedTargetFilterMappings(persistencePerspective, ctoInserted, mergedProperties, adornedTargetList);
            List<Serializable> recordsInserted = getPersistentRecords(adornedTargetList.getAdornedTargetEntityClassname(), filterMappings, ctoInserted.getFirstResult(), ctoInserted.getMaxResults());

            Assert.isTrue(!CollectionUtils.isEmpty(recordsInserted), "Entity not found");

            persistenceManager.getDynamicEntityDao().remove(recordsInserted.get(0));
        } catch (Exception e) {
            throw new ServiceException("Problem removing entity : " + e.getMessage(), e);
        }
    }

    @Override
    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto) throws ServiceException {
        PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        AdornedTargetList adornedTargetList = (AdornedTargetList) persistencePerspective.getPersistencePerspectiveItems().get(PersistencePerspectiveItemType.ADORNEDTARGETLIST);
        Entity[] payload;
        int totalRecords;
        try {
            Class<?>[] entities = persistenceManager.getPolymorphicEntities(ceilingEntityFullyQualifiedClassname);
            Map<String, FieldMetadata> mergedPropertiesTarget = persistenceManager.getDynamicEntityDao().getMergedProperties(
                    ceilingEntityFullyQualifiedClassname,
                    entities,
                    null,
                    persistencePerspective.getAdditionalNonPersistentProperties(),
                    persistencePerspective.getAdditionalForeignKeys(),
                    MergedPropertyType.PRIMARY,
                    persistencePerspective.getPopulateToOneFields(),
                    persistencePerspective.getIncludeFields(),
                    persistencePerspective.getExcludeFields(),
                    persistencePerspective.getConfigurationKey(),
                    ""
            );
            
            AdornedTargetRetrieval adornedTargetRetrieval = new AdornedTargetRetrieval(persistencePackage, adornedTargetList, cto).invokeForFetch();
            List<Serializable> records = adornedTargetRetrieval.getRecords();
            Map<String, FieldMetadata> mergedProperties = adornedTargetRetrieval.getMergedProperties();
            payload = getRecords(mergedPropertiesTarget, records, mergedProperties, adornedTargetList.getTargetObjectPath(), persistencePackage.getCustomCriteria());
            totalRecords = getTotalRecords(adornedTargetList.getAdornedTargetEntityClassname(), adornedTargetRetrieval.getFilterMappings());
        } catch (Exception e) {
            throw new ServiceException("Unable to fetch results for " + adornedTargetList.getAdornedTargetEntityClassname(), e);
        }

        DynamicResultSet results = new DynamicResultSet(null, payload, totalRecords);

        return results;
    }

    public List<Serializable> fetch(PersistencePackage persistencePackage, AdornedTargetList adornedTargetList, CriteriaTransferObject cto) {
        AdornedTargetRetrieval retrieval;
        try {
            retrieval = new AdornedTargetRetrieval(persistencePackage, adornedTargetList, cto).invokeForFetch();
        } catch (Exception e) {
            throw ExceptionHelper.refineException(e);
        }
        return retrieval.getRecords();
    }

    public class AdornedTargetRetrieval {
        private final PersistencePackage persistencePackage;
        private final PersistencePerspective persistencePerspective;
        private Entity entity;
        private final AdornedTargetList adornedTargetList;
        private Map<String, FieldMetadata> mergedProperties;
        private List<Serializable> records;
        private int index;
        private List<FilterMapping> filterMappings;
        private final CriteriaTransferObject cto;

        // This constructor is used by the update method
        public AdornedTargetRetrieval(PersistencePackage persistencePackage, Entity entity, AdornedTargetList adornedTargetList) {
            this(persistencePackage, adornedTargetList, new CriteriaTransferObject());
            this.entity = entity;
        }
        
        // This constructor is used by the fetch method
        public AdornedTargetRetrieval(PersistencePackage persistencePackage, AdornedTargetList adornedTargetList, CriteriaTransferObject cto) {
            this.persistencePackage = persistencePackage;
            this.persistencePerspective = persistencePackage.getPersistencePerspective();
            this.adornedTargetList = adornedTargetList;
            this.cto = cto;
        }

        public Map<String, FieldMetadata> getMergedProperties() {
            return mergedProperties;
        }

        public List<Serializable> getRecords() {
            return records;
        }

        public int getIndex() {
            return index;
        }
        
        public List<FilterMapping> getFilterMappings() {
            return filterMappings;
        }

        public AdornedTargetRetrieval invokeForFetch() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, FieldNotAvailableException, NoSuchFieldException {
            invokeInternal();
            return this;
        }
        
        public AdornedTargetRetrieval invokeForUpdate() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, FieldNotAvailableException, NoSuchFieldException {
            FilterAndSortCriteria filterCriteria = cto.get(adornedTargetList.getCollectionFieldName());
            filterCriteria.setFilterValue(entity.findProperty(adornedTargetList.getLinkedObjectPath() + "." + adornedTargetList.getLinkedIdProperty()).getValue());
            
            invokeInternal();
            
            index = 0;
            Long myEntityId = Long.valueOf(entity.findProperty(adornedTargetList.getTargetObjectPath() + "." + adornedTargetList.getTargetIdProperty()).getValue());
            FieldManager fieldManager = getFieldManager();
            for (Serializable record : records) {
                Long targetId = (Long) fieldManager.getFieldValue(record, adornedTargetList.getTargetObjectPath() + "." + adornedTargetList.getTargetIdProperty());
                if (myEntityId.equals(targetId)) {
                    break;
                }
                index++;
            }
            
            return this;
        }

        private void invokeInternal() throws ClassNotFoundException {
            if (adornedTargetList.getSortField() != null) {
                FilterAndSortCriteria sortCriteria = cto.get(adornedTargetList.getSortField());
                sortCriteria.setSortAscending(adornedTargetList.getSortAscending());
            }
            
            Class<?>[] entities = persistenceManager.getPolymorphicEntities(adornedTargetList
                    .getAdornedTargetEntityClassname());
            mergedProperties = persistenceManager.getDynamicEntityDao().getMergedProperties(
                    adornedTargetList.getAdornedTargetEntityClassname(),
                    entities,
                    null,
                    new String[]{},
                    new ForeignKey[]{},
                    MergedPropertyType.ADORNEDTARGETLIST,
                    persistencePerspective.getPopulateToOneFields(),
                    persistencePerspective.getIncludeFields(),
                    persistencePerspective.getExcludeFields(),
                    persistencePerspective.getConfigurationKey(),
                    ""
            );
            filterMappings = getAdornedTargetFilterMappings(persistencePerspective, cto, mergedProperties, adornedTargetList);

            if (CollectionUtils.isNotEmpty(cto.getAdditionalFilterMappings())) {
                filterMappings.addAll(cto.getAdditionalFilterMappings());
            }
            
            String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
            Class<?>[] entities2 = persistenceManager.getPolymorphicEntities(ceilingEntityFullyQualifiedClassname);
            Map<String, FieldMetadata> mergedPropertiesTarget = persistenceManager.getDynamicEntityDao()
                    .getMergedProperties(
                    ceilingEntityFullyQualifiedClassname,
                    entities2,
                    null,
                    persistencePerspective.getAdditionalNonPersistentProperties(),
                    persistencePerspective.getAdditionalForeignKeys(),
                    MergedPropertyType.PRIMARY,
                    persistencePerspective.getPopulateToOneFields(),
                    persistencePerspective.getIncludeFields(),
                    persistencePerspective.getExcludeFields(),
                    persistencePerspective.getConfigurationKey(),
                    ""
            );
            
            // We need to make sure that the target merged properties have the target object path prefix
            Map<String, FieldMetadata> convertedMergedPropertiesTarget = new HashMap<String, FieldMetadata>();
            String prefix = adornedTargetList.getTargetObjectPath();
            for (Entry<String, FieldMetadata> entry : mergedPropertiesTarget.entrySet()) {
                convertedMergedPropertiesTarget.put(prefix + "." + entry.getKey(), entry.getValue());
            }
            
            records = getPersistentRecords(adornedTargetList.getAdornedTargetEntityClassname(), filterMappings, cto.getFirstResult(), cto.getMaxResults());
        }
    }
}
