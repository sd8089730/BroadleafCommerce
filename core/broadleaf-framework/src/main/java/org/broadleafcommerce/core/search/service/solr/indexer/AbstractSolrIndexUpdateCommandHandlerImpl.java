package org.broadleafcommerce.core.search.service.solr.indexer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.core.search.service.solr.SolrConfiguration;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;

/**
 * Component to provide basic functionality around handling SolrUpdateCommands.
 * @author Kelly Tisdell
 *
 */
public abstract class AbstractSolrIndexUpdateCommandHandlerImpl implements SolrIndexUpdateCommandHandler {
    
    private static final Log LOG = LogFactory.getLog(AbstractSolrIndexUpdateCommandHandlerImpl.class);
    
    private final String commandGroup;
    private final SolrConfiguration solrConfiguration;
    
    public AbstractSolrIndexUpdateCommandHandlerImpl(String commandGroup, SolrConfiguration solrConfiguration) {
        Assert.notNull(solrConfiguration, "SolrConfiguration cannot be null.");
        Assert.notNull(commandGroup, "Command group cannot be null.");
        this.commandGroup = commandGroup.trim();
        Assert.hasText(this.commandGroup, "Command group must not be empty and should not contain white spaces.");
        this.solrConfiguration = solrConfiguration;
    }
    
    @Override
    public String getRelevantCommandGroup() {
        return commandGroup;
    }

    /**
     * By default, this will update the foreground collection.  Deletes, if available, will be applied first.  Then, updates. This should be considered an autonomous method.
     * Do not use this to make incremental updates within the scope of a larger update process because this will apply commits, by default.
     * 
     * @param command
     * @throws ServiceException
     */
    protected void executeCommandInternal(IncrementalUpdateCommand command) throws ServiceException {
        executeCommandInternal(command, getForegroundCollectionName());
    }
    
    /**
     * This will apply updates in the specified collection, and will commit, when finished, if no errors occur. 
     * Deletes, if available, will be applied first.  Then, updates.  This should be considered an autonomous method.
     * Do not use this to make incremental updates within the scope of a larger update process because this will apply commits, by default.
     * 
     * @param command
     * @param collectionName
     * @throws ServiceException
     */
    protected void executeCommandInternal(IncrementalUpdateCommand command, String collectionName) throws ServiceException {
        Assert.notNull(command, "The command cannot be null.");
        Assert.notNull(collectionName, "The collection name cannot be null.");
        
        boolean changeMade = false;
        try {
            
            if (command.getDeleteQueries() != null && ! command.getDeleteQueries().isEmpty()) {
                deleteByQueries(collectionName, command.getDeleteQueries());
                changeMade = true;
            }
            
            if (command.getSolrInputDocuments() != null && ! command.getSolrInputDocuments().isEmpty()) {
                addDocuments(collectionName, command.getSolrInputDocuments());
                changeMade = true;
            }
            
            try {
                if (changeMade) {
                    commit(collectionName, true);
                }
            } catch (Exception e) {
                throw new ServiceException("An error occured during commit while incrementally updating the Solr collection '" + collectionName + "' with: \n" + command.toString(), e);
            }
            
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            if (changeMade) {
                try {
                    rollback(collectionName);
                } catch (Exception se) {
                    throw new ServiceException("An error occured rolling back the changes in Solr after an error occured.", se);
                }
            }
        }
    }
    
    /**
     * Hook point for implementors to handle new command types.
     * @param command
     * @throws Exception
     */
    protected void executeCommandInternalNoDefaultCommandType(SolrUpdateCommand command) throws Exception {
        if (command == null) {
            LOG.warn("Unable to process SolrUpdateCommand as the command was null.");
        } else {
            LOG.warn("Unable to process SolrUpdateCommand of type: " 
                    + command.getClass().getName() + ". Consider overriding the executeCommandInternalNoDefaultCommandType method in " 
                    + this.getClass().getName() + ".");
        }
    }
    
    /**
     * Issues a global commit command to Solr.  Take care as anyone can issue a commit and since it's global it affects all updates. 
     * It is recommended that you off Solr's autoCommit and autoSoftCommit features.
     * 
     * @param collectionName
     * @throws IOException
     * @throws SolrServerException
     */
    protected void commit(String collectionName, boolean waitSearcher) throws IOException, SolrServerException {
        solrConfiguration.getReindexServer().commit(collectionName, true, waitSearcher, false);
    }
    
    /**
     * Issues a global rollback of all items that have not yet been committed.  Take care as anyone can issue a commit and since it's global it affects all updates. 
     * It is recommended that you off Solr's autoCommit and autoSoftCommit features.
     * 
     * @param collectionName
     * @throws IOException
     * @throws SolrServerException
     */
    protected void rollback(String collectionName) throws IOException, SolrServerException {
        solrConfiguration.getReindexServer().rollback(collectionName);
    }
    
    /**
     * Adds the documents to the specified collection but does not issue a commit.
     * 
     * @param collection
     * @param docs
     * @throws IOException
     * @throws SolrServerException
     */
    protected void addDocuments(String collection, List<SolrInputDocument> docs) throws IOException, SolrServerException {
        if (docs != null && !docs.isEmpty()) {
            solrConfiguration.getReindexServer().add(collection, docs);
        }
    }
    
    /**
     * Deletes items for the provided query.  This does not issue a commit.
     * 
     * @param collection
     * @param query
     * @throws IOException
     * @throws SolrServerException
     */
    protected void deleteByQuery(String collection, String query) throws IOException, SolrServerException {
        if (query != null) {
            solrConfiguration.getReindexServer().deleteByQuery(collection, query);
        }
    }
    
    /**
     * Deletes items for the provided queries.  This does not issue a commit.
     * 
     * @param collection
     * @param queries
     * @throws IOException
     * @throws SolrServerException
     */
    protected void deleteByQueries(String collection, List<String> queries) throws IOException, SolrServerException {
        if (queries != null) {
            for (String query : queries) {
                deleteByQuery(collection, query);
            }
        }
    }
    
    /**
     * Deletes items by ids.  This does not issue a commit.
     * 
     * @param collection
     * @param ids
     * @throws IOException
     * @throws SolrServerException
     */
    protected void deleteByIds(String collection, List<String> ids) throws IOException, SolrServerException {
        if (ids != null && !ids.isEmpty()) {
            solrConfiguration.getReindexServer().deleteById(collection, ids);
        }
    }
    
    @Override
    public String getForegroundCollectionName() {
        return solrConfiguration.getPrimaryName();
    }
    
    @Override
    public String getBackgroundCollectionName() {
        return solrConfiguration.getReindexName();
    }

}