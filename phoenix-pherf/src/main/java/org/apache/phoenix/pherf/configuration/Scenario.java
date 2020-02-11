/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.apache.phoenix.pherf.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.phoenix.pherf.util.PhoenixUtil;

@XmlRootElement(namespace = "org.apache.phoenix.pherf.configuration.DataModel")
public class Scenario {
    private String tableName;
    private int rowCount;
    private Map<String, String> phoenixProperties;
    private DataOverride dataOverride;
    private List<QuerySet> querySet = new ArrayList<>();
    private WriteParams writeParams = null;
    private String name;
    private String tenantId;
    private List<Ddl> preScenarioDdls;
    private List<Ddl> postScenarioDdls;
    private int timeoutDuration;
    
   
    public Scenario() {
    }

    /**
     * Scenarios have to have unique table names
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Scenario)) {
            return false;
        }
        Scenario scenario = (Scenario) object;
        return (this.tableName.equals(scenario.getTableName()));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 38).appendSuper(super.hashCode())
                .append(tableName)
                .toHashCode();
    }

    /**
     * Table name for a scenario
     *
     * @return
     */
    @XmlAttribute()
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Row count for a table
     *
     * @return
     */
    @XmlAttribute()
    public int getRowCount() {
        return PhoenixUtil.getRowCountOverride() == 0 ?
                rowCount : PhoenixUtil.getRowCountOverride();
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    @XmlAttribute()
    public int getTimeoutDuration() {
        return timeoutDuration;
    }

    public void setTimeoutDuration(int timeoutDuration) {
        this.timeoutDuration = timeoutDuration;
    }
    
    /**
     * Phoenix properties
     *
     * @return
     */
    public Map<String, String> getPhoenixProperties() {
        return phoenixProperties;
    }

    public void setPhoenixProperties(Map<String, String> phoenixProperty) {
        this.phoenixProperties = phoenixProperty;
    }

    /**
     * Data override
     *
     * @return
     */
    @XmlElement()
    public DataOverride getDataOverride() {
        return dataOverride;
    }

    public void setDataOverride(DataOverride dataOverride) {
        this.dataOverride = dataOverride;
    }

    /**
     * List of Query Set
     *
     * @return
     */
    public List<QuerySet> getQuerySet() {
        return querySet;
    }

    @SuppressWarnings("unused")
    public void setQuerySet(List<QuerySet> querySet) {
        this.querySet = querySet;
    }

    /**
     * Extract schema name from table name
     *
     * @return
     */
    public String getSchemaName() {
        return XMLConfigParser.parseSchemaName(this.tableName);
    }

    /**
     * Extract table name without schema name
     *
     * @return
     */
    public String getTableNameWithoutSchemaName() {
        return XMLConfigParser.parseTableName(this.tableName);
    }

    /**
     * Name of scenario
     *
     * @return
     */
    @XmlAttribute()
    public String getName() {
        Preconditions.checkNotNull(name);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Tenant Id used by connection of this query
     * @return
     */
    @XmlAttribute
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public WriteParams getWriteParams() {
        return writeParams;
    }

    public void setWriteParams(WriteParams writeParams) {
        this.writeParams = writeParams;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Name:" + name);
        stringBuilder.append("Table Name:" + tableName);
        stringBuilder.append("Row Count:" + rowCount);
        stringBuilder.append("Data Override:" + dataOverride);
        stringBuilder.append("Timeout Duration:" + timeoutDuration);
        for (QuerySet query : querySet) {
            stringBuilder.append(query + ";");
        }
        return stringBuilder.toString();
    }

	public List<Ddl> getPreScenarioDdls() {
		return preScenarioDdls;
	}

    /**
     * Scenario level DDLs (for views/index/async) that are executed before data load
     */
    @XmlElementWrapper(name = "preScenarioDdls")
    @XmlElement(name = "ddl")
	public void setPreScenarioDdls(List<Ddl> preScenarioDdls) {
		this.preScenarioDdls = preScenarioDdls;
	}

	public List<Ddl> getPostScenarioDdls() {
		return postScenarioDdls;
	}

    /**
     * Scenario level DDLs (for views/index/async) that are executed after data load
     */
    @XmlElementWrapper(name = "postScenarioDdls")
    @XmlElement(name = "ddl")
	public void setPostScenarioDdls(List<Ddl> postScenarioDdls) {
		this.postScenarioDdls = postScenarioDdls;
	}
}
