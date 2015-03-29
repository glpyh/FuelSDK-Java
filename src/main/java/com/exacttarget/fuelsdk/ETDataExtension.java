//
// This file is part of the Fuel Java client library.
//
// Copyright (c) 2013, 2014, 2015, ExactTarget, Inc.
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
//
// * Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
//
// * Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// * Neither the name of ExactTarget, Inc. nor the names of its
// contributors may be used to endorse or promote products derived
// from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//

package com.exacttarget.fuelsdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

import com.exacttarget.fuelsdk.annotations.ExternalName;
import com.exacttarget.fuelsdk.annotations.InternalName;
import com.exacttarget.fuelsdk.annotations.RestObject;
import com.exacttarget.fuelsdk.annotations.SoapObject;
import com.exacttarget.fuelsdk.ETDataExtensionColumn.Type;
import com.exacttarget.fuelsdk.internal.APIObject;
import com.exacttarget.fuelsdk.internal.APIProperty;
import com.exacttarget.fuelsdk.internal.DataExtension;
import com.exacttarget.fuelsdk.internal.DataExtensionObject;

/**
 * An <code>ETDataExtension</code> object represents a data extension
 * in the Salesforce Marketing Cloud.
 */

@RestObject(path = "/data/v1/customobjectdata/key/{key}/rowset",
            primaryKey = "key",
            collection = "items",
            totalCount = "count")
@SoapObject(internalType = DataExtension.class, unretrievable = {
    "ID", "Fields"
})
public class ETDataExtension extends ETSoapObject {
    private static Logger logger = Logger.getLogger(ETDataExtension.class);

    @ExternalName("id")
    @InternalName("objectID")
    private String id = null;
    @ExternalName("key")
    @InternalName("customerKey")
    private String key = null;
    @ExternalName("name")
    private String name = null;
    @ExternalName("description")
    private String description = null;
    @ExternalName("createdDate")
    private Date createdDate = null;
    @ExternalName("modifiedDate")
    private Date modifiedDate = null;
    @ExternalName("folderId")
    @InternalName("categoryID")
    private Integer folderId = null;
    @ExternalName("columns")
    @InternalName("fields")
    private List<ETDataExtensionColumn> columns = new ArrayList<ETDataExtensionColumn>();
    @ExternalName("isSendable")
    private Boolean isSendable = null;
    @ExternalName("isTestable")
    private Boolean isTestable = null;

    private boolean isHydrated = false;

    private static CacheManager cacheManager = null;
    private static Cache cache = null;

    static {
        cacheManager = CacheManager.create();
        cacheManager.addCache("ETDataExtension");
        cache = cacheManager.getCache("ETDataExtension");
    }

    public ETDataExtension() {}

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public List<ETDataExtensionColumn> getColumns() {
        return columns;
    }

    public ETDataExtensionColumn getColumn(String name) {
        for (ETDataExtensionColumn column : columns) {
            if (column.getName().equals(name.toLowerCase())) {
                return column;
            }
        }
        return null;
    }

    public void addColumn(String name) {
        addColumn(name, null, null, null, null, null, null, null);
    }

    public void addColumn(String name, Type type) {
        addColumn(name, type, null, null, null, null, null, null);
    }

    public void addColumn(String name, Boolean isPrimaryKey) {
        addColumn(name, null, null, null, null, isPrimaryKey, null, null);
    }

    public void addColumn(String name, Type type, Boolean isPrimaryKey) {
        addColumn(name, type, null, null, null, isPrimaryKey, null, null);
    }

    public void addColumn(String name,
                          Type type,
                          Integer length,
                          Integer precision,
                          Integer scale,
                          Boolean isPrimaryKey,
                          Boolean isRequired,
                          String defaultValue)
    {
        ETDataExtensionColumn column = new ETDataExtensionColumn();
        column.setName(name.toLowerCase());
        if (type != null) {
            column.setType(type);
        } else {
            // default is text type
            column.setType(Type.TEXT);
        }
        // mimics the UI default values for length, precision, and scale
        if (column.getType() == Type.TEXT) {
            if (length != null) {
                column.setLength(length);
            } else {
                column.setLength(50);
            }
        }
        if (column.getType() == Type.DECIMAL) {
            if (precision != null) {
                column.setPrecision(precision);
            } else {
                column.setPrecision(18);
            }
            if (scale != null) {
                column.setScale(scale);
            } else {
                column.setScale(0);
            }
        }
        column.setIsPrimaryKey(isPrimaryKey);
        if (isPrimaryKey != null && isPrimaryKey) {
            column.setIsRequired(true);
        } else {
            column.setIsRequired(isRequired);
        }
        column.setDefaultValue(defaultValue);
        addColumn(column);
    }

    public void addColumn(ETDataExtensionColumn column) {
        columns.add(column);
    }

    public Boolean getIsSendable() {
        return isSendable;
    }

    public void setIsSendable(Boolean isSendable) {
        this.isSendable = isSendable;
    }

    public Boolean getIsTestable() {
        return isTestable;
    }

    public void setIsTestable(Boolean isTestable) {
        this.isTestable = isTestable;
    }

    /**
     * @deprecated
     * Use <code>getKey()</code>.
     */
    @Deprecated
    public String getCustomerKey() {
        return getKey();
    }

    /**
     * @deprecated
     * Use <code>setKey()</code>.
     */
    @Deprecated
    public void setCustomerKey(String customerKey) {
        setKey(customerKey);
    }

    /**
     * @deprecated
     * Use <code>getFolderId()</code>.
     */
    @Deprecated
    public Integer getCategoryId() {
        return getFolderId();
    }

    /**
     * @deprecated
     * Use <code>setFolderId()</code>.
     */
    @Deprecated
    public void setCategoryId(Integer categoryId) {
        setFolderId(categoryId);
    }

    public static ETResponse<ETDataExtensionRow> select(ETClient client,
                                                        String dataExtension,
                                                        ETFilter filter)
        throws ETSdkException
    {
        return select(client, dataExtension, null, null, filter);
    }

    public static ETResponse<ETDataExtensionRow> select(ETClient client,
                                                        String dataExtension,
                                                        String... filter)
        throws ETSdkException
    {
        return select(client, dataExtension, null, null, ETFilter.parse(filter));
    }

    public static ETResponse<ETDataExtensionRow> select(ETClient client,
                                                        String dataExtension,
                                                        Integer page,
                                                        Integer pageSize,
                                                        ETFilter filter)
        throws ETSdkException
    {
        String dataExtensionKey = null;

        //
        // The data extension can be specified using key or name:
        //

        ETExpression e = ETExpression.parse(dataExtension);
        if (e.getProperty().toLowerCase().equals("key")
                && e.getOperator() == ETExpression.Operator.EQUALS) {
            dataExtensionKey = e.getValue();
        } else if (e.getProperty().toLowerCase().equals("name")
                && e.getOperator() == ETExpression.Operator.EQUALS) {
            dataExtensionKey = e.getValue();
        } else {
            throw new ETSdkException("invalid data extension filter string");
        }

        String object = "DataExtensionObject[" + dataExtensionKey + "]";

        ETResponse<ETDataExtensionRow> response = null;

        if (client.getConfiguration().equals("enableDataExtensionPagination",
                                             "true")) {
            if (page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = ETSoapObject.PAGE_SIZE;
            }

            Integer firstItem = (page - 1) * pageSize;
            logger.trace("firstItem = " + firstItem);
            Integer lastItem = (firstItem + pageSize) - 1;
            logger.trace("lastItem = " + lastItem);
            Integer firstPage = firstItem / ETSoapObject.PAGE_SIZE;
            logger.trace("firstPage = " + firstPage);
            Integer lastPage = lastItem / ETSoapObject.PAGE_SIZE;
            logger.trace("lastPage = " + lastPage);

            String key = dataExtensionKey + "#" + filter;

            //
            // Ensure all pages are cached:
            //

            boolean allCached = true;
            for (int i = firstPage; i <= lastPage; i++) {
                String k = key + "#" + i;
                logger.trace("checking cache for page " + k);
                if (cache.get(k) == null) {
                    logger.trace("    cache miss for page " + k);
                    allCached = false;
                    break;
                }
            }

            //
            // If all pages are not cached load them into the cache
            // (we have to start from the first page because multiple
            // pages of SOAP objects can only be read sequentially):
            //

            if (!allCached) {
                String k = key + "#0";
                logger.trace("           loading page " + k);
                ETResponse<ETDataExtensionRow> r = ETSoapObject.retrieve(client,
                                                                         object,
                                                                         filter,
                                                                         ETDataExtensionRow.class);
                logger.trace("            loaded page " + k);
                cache.put(new Element(key + "#0", r));
                logger.trace("            cached page " + k);
                for (int i = 1; i <= lastPage; i++) {
                    k = key + "#" + i;
                    logger.trace("           loading page " + k);
                    r = ETSoapObject.retrieve(client,
                                              r.getRequestId(),
                                              ETDataExtensionRow.class);
                    logger.trace("            loaded page " + k);
                    cache.put(new Element(key + "#" + i, r));
                    logger.trace("            cached page " + k);
                }
            }

            //
            // Return results from the cache:
            //

            response = new ETResponse<ETDataExtensionRow>();

            int items = 0;
            while (items < pageSize) {
                int i = (firstItem + items) / ETSoapObject.PAGE_SIZE;
                int j = (firstItem + items) % ETSoapObject.PAGE_SIZE;

                Element element = cache.get(key + "#" + i);

                @SuppressWarnings("unchecked")
                ETResponse<ETDataExtensionRow> cachedResponse =
                    (ETResponse<ETDataExtensionRow>) element.getObjectValue();

                if (j >= cachedResponse.getResults().size()) {
                    break;
                }

                response.addResult(cachedResponse.getResults().get(j));

                items++;
            }

            response.setPage(page);
            response.setPageSize(items);
        } else {
            response = ETSoapObject.retrieve(client,
                                             object,
                                             filter,
                                             ETDataExtensionRow.class);
        }

        return response;
    }

    public static ETResponse<ETDataExtensionRow> select(ETClient client,
                                                        String dataExtension,
                                                        Integer page,
                                                        Integer pageSize,
                                                        String... filter)
        throws ETSdkException
    {
        return select(client, dataExtension, page, pageSize, ETFilter.parse(filter));
    }

    /**
     * @deprecated
     * Pass columns in <code>filter</code> argument.
     */
    @Deprecated
    public static ETResponse<ETDataExtensionRow> select(ETClient client,
                                                        String dataExtension,
                                                        ETFilter filter,
                                                        String... columns)
        throws ETSdkException
    {
        // make a copy so we're not modifying argument itself
        ETFilter f = new ETFilter();
        f.setExpression(filter.getExpression());
        for (String column : columns) {
            f.addProperty(column);
        }
        return select(client, dataExtension, f);
    }

    /**
     * @deprecated
     * Pass columns in <code>filter</code> argument.
     */
    @Deprecated
    public static ETResponse<ETDataExtensionRow> select(ETClient client,
                                                        String dataExtension,
                                                        ETFilter filter,
                                                        Integer page,
                                                        Integer pageSize,
                                                        String... columns)
        throws ETSdkException
    {
        // make a copy so we're not modifying argument itself
        ETFilter f = new ETFilter();
        f.setExpression(filter.getExpression());
        for (String column : columns) {
            f.addProperty(column);
        }
        return select(client, dataExtension, page, pageSize, f);
    }

    /**
     * @deprecated
     * Pass columns in <code>filter</code> argument.
     */
    @Deprecated
    public static ETResponse<ETDataExtensionRow> select(ETClient client,
                                                        String dataExtension,
                                                        String filter,
                                                        Integer page,
                                                        Integer pageSize,
                                                        String... columns)
        throws ETSdkException
    {
        // make a copy so we're not modifying argument itself
        ETFilter f = new ETFilter();
        f.setExpression(ETExpression.parse(filter));
        for (String column : columns) {
            f.addProperty(column);
        }
        return select(client, dataExtension, page, pageSize, f);
    }

    public ETResponse<ETDataExtensionRow> select(ETFilter filter)
        throws ETSdkException
    {
        // if no columns are explicitly requested retrieve all columns
        if (filter.getProperties().isEmpty()) {
            filter.setProperties(getColumnNames());
        }
        return ETDataExtension.select(getClient(), "key=" + getKey(), filter);
    }

    public ETResponse<ETDataExtensionRow> select(String... filter)
        throws ETSdkException
    {
        return select(ETFilter.parse(filter));
    }

    public ETResponse<ETDataExtensionRow> select(Integer page,
                                                 Integer pageSize,
                                                 ETFilter filter)
        throws ETSdkException
    {
        // if no columns are explicitly requested retrieve all columns
        if (filter.getProperties().isEmpty()) {
            filter.setProperties(getColumnNames());
        }
        return ETDataExtension.select(getClient(), "key=" + getKey(), page, pageSize, filter);
    }

    public ETResponse<ETDataExtensionRow> select(Integer page,
                                                 Integer pageSize,
                                                 String... filter)
        throws ETSdkException
    {
        return select(page, pageSize, ETFilter.parse(filter));
    }

    /**
     * @deprecated
     * Pass columns in <code>filter</code> argument.
     */
    @Deprecated
    public ETResponse<ETDataExtensionRow> select(ETFilter filter,
                                                 String... columns)
        throws ETSdkException
    {
        return ETDataExtension.select(getClient(), "key=" + getKey(), filter, columns);
    }

    /**
     * @deprecated
     * Pass columns in <code>filter</code> argument.
     */
    @Deprecated
    public ETResponse<ETDataExtensionRow> select(ETFilter filter,
                                                 Integer page,
                                                 Integer pageSize,
                                                 String... columns)
        throws ETSdkException
    {
        return ETDataExtension.select(getClient(), "key=" + getKey(), filter, page, pageSize, columns);
    }

    /**
     * @deprecated
     * Pass columns in <code>filter</code> argument.
     */
    @Deprecated
    public ETResponse<ETDataExtensionRow> select(String filter,
                                                 Integer page,
                                                 Integer pageSize,
                                                 String... columns)
        throws ETSdkException
    {
        return ETDataExtension.select(getClient(), "key=" + getKey(), filter, page, pageSize, columns);
    }

    public ETResponse<ETDataExtensionRow> insert(ETDataExtensionRow... rows)
        throws ETSdkException
    {
        return insert(Arrays.asList(rows));
    }

    public ETResponse<ETDataExtensionRow> insert(List<ETDataExtensionRow> rows)
        throws ETSdkException
    {
        for (ETDataExtensionRow row : rows) {
            //
            // Set the data extension name if it isn't already set:
            //

            if (row.getDataExtensionKey() == null) {
                row.setDataExtensionKey(key);
            }
        }

        return super.create(getClient(), rows);
    }

    public ETResponse<ETDataExtensionRow> update(ETDataExtensionRow... rows)
        throws ETSdkException
    {
        return update(Arrays.asList(rows));
    }

    public ETResponse<ETDataExtensionRow> update(List<ETDataExtensionRow> rows)
        throws ETSdkException
    {
        for (ETDataExtensionRow row : rows) {
            //
            // Set the data extension name if it isn't already set:
            //

            if (row.getDataExtensionKey() == null) {
                row.setDataExtensionKey(key);
            }
        }

        return super.update(getClient(), rows);
    }

    public ETResponse<ETDataExtensionRow> delete(ETDataExtensionRow... rows)
        throws ETSdkException
    {
        return delete(Arrays.asList(rows));
    }

    public ETResponse<ETDataExtensionRow> delete(List<ETDataExtensionRow> rows)
        throws ETSdkException
    {
        List<APIObject> internalRows = new ArrayList<APIObject>();

        for (ETDataExtensionRow row : rows) {
            //
            // We hand construct this one, since all we need
            // to pass in are the primary keys, and we pass them
            // in to DeleteRequest differently (in the Keys
            // property) than we received it from
            // RetrieveRequest (in the Properties property):
            //

            DataExtensionObject internalRow = new DataExtensionObject();
            DataExtensionObject.Keys keys = new DataExtensionObject.Keys();
            hydrate(); // make sure we've retrieved all columns
            for (ETDataExtensionColumn column : columns) {
                if (column.getIsPrimaryKey()) {
                    APIProperty property = new APIProperty();
                    property.setName(column.getName());
                    property.setValue(row.getColumn(property.getName()));
                    keys.getKey().add(property);
                }
            }
            internalRow.setName(name);
            internalRow.setKeys(keys);
            internalRows.add(internalRow);
        }

        // call delete method that operates on internal objects
        return super.delete(getClient(), internalRows, true);
    }

    public ETResponse<ETDataExtensionRow> update(String filter, String... values)
        throws ETSdkException
    {
        List<ETDataExtensionRow> rows = getMatchingRows(filter);
        for (ETDataExtensionRow row : rows) {
            for (String value : values) {
                ETExpression expression = ETExpression.parse(value);
                // must be an assign operation
                if (expression.getOperator() != ETExpression.Operator.EQUALS) {
                    throw new ETSdkException("must be an assign operation: " + expression);
                }
                row.setColumn(expression.getProperty(), expression.getValue());
            }
        }
        return update(rows);
    }

    public ETResponse<ETDataExtensionRow> delete(String filter)
        throws ETSdkException
    {
        List<ETDataExtensionRow> rows = getMatchingRows(filter);
        return delete(rows);
    }

    public void hydrate()
        throws ETSdkException
    {
        if (isHydrated) {
            return;
        }

        ETClient client = getClient();

        //
        // Automatically refresh the token if necessary:
        //

        client.refreshToken();

        //
        // Retrieve all columns with CustomerKey = this data extension:
        //

        ETExpression expression = new ETExpression();
        expression.setProperty("DataExtension.CustomerKey");
        expression.setOperator(ETExpression.Operator.EQUALS);
        expression.addValue(getKey());

        ETFilter filter = new ETFilter();
        filter.setExpression(expression);

        ETResponse<ETDataExtensionColumn> response =
                ETDataExtensionColumn.retrieve(client,
                                               ETDataExtensionColumn.class,
                                               null, // page
                                               null, // pageSize
                                               filter);

        columns = response.getObjects();

        // XXX deal with partially loaded DataExtension objects too

        isHydrated = true;
    }

    private List<ETDataExtensionRow> getMatchingRows(String filter)
        throws ETSdkException
    {
        List<ETDataExtensionRow> rows = new ArrayList<ETDataExtensionRow>();

        hydrate(); // make sure we've retrieved all columns

        //
        // Only retrieve primary key columns:
        //

        List<String> primaryKeyColumnNames = new ArrayList<String>();
        for (ETDataExtensionColumn column : columns) {
            if (column.getIsPrimaryKey()) {
                primaryKeyColumnNames.add(column.getName());
            }
        }

        int page = 1;
        int page_size = 2500;

        ETResponse<ETDataExtensionRow> response = null;
        do {
            ETFilter parsedFilter = ETFilter.parse(filter);
            parsedFilter.setProperties(primaryKeyColumnNames);
            response = select(page++, page_size, parsedFilter);
            rows.addAll(response.getObjects());
        } while (response.hasMoreResults() == true);

        return rows;
    }

    private List<String> getColumnNames()
        throws ETSdkException
    {
        hydrate(); // make sure we've retrieved all columns
        List<String> columnNames = new ArrayList<String>();
        for (ETDataExtensionColumn column : columns) {
            columnNames.add(column.getName());
        }
        return columnNames;
    }
}
