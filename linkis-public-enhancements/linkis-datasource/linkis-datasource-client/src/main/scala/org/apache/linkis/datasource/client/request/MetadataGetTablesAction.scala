/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.linkis.datasource.client.request

import org.apache.linkis.datasource.client.config.DatasourceClientConfig.{
  METADATA_OLD_SERVICE_MODULE,
  METADATA_SERVICE_MODULE
}
import org.apache.linkis.datasource.client.errorcode.DatasourceClientErrorCodeSummary._
import org.apache.linkis.datasource.client.exception.DataSourceClientBuilderException
import org.apache.linkis.httpclient.request.GetAction

import org.apache.commons.lang3.StringUtils

class MetadataGetTablesAction extends GetAction with DataSourceAction {

  private var dataSourceId: Long = _
  private var dataSourceName: String = _
  private var database: String = _

  override def suffixURLs: Array[String] = if (StringUtils.isNotBlank(dataSourceName)) {
    Array(METADATA_SERVICE_MODULE.getValue, "getTables")
  } else {
    Array(METADATA_OLD_SERVICE_MODULE.getValue, "tables", dataSourceId.toString, "db", database)
  }

  private var user: String = _

  override def setUser(user: String): Unit = this.user = user

  override def getUser: String = this.user
}

object MetadataGetTablesAction {
  def builder(): Builder = new Builder

  class Builder private[MetadataGetTablesAction] () {
    private var dataSourceId: Long = _
    private var dataSourceName: String = _
    private var database: String = _
    private var system: String = _
    private var user: String = _

    def setUser(user: String): Builder = {
      this.user = user
      this
    }

    def setDataSourceName(dataSourceName: String): Builder = {
      this.dataSourceName = dataSourceName
      this
    }

    /**
     * get value form dataSourceId is deprecated, suggest to use dataSourceName
     *
     * @param dataSourceId
     *   datasourceId
     * @return
     *   builder
     */
    @deprecated
    def setDataSourceId(dataSourceId: Long): Builder = {
      this.dataSourceId = dataSourceId
      this
    }

    def setDatabase(database: String): Builder = {
      this.database = database
      this
    }

    def setSystem(system: String): Builder = {
      this.system = system
      this
    }

    def build(): MetadataGetTablesAction = {
      if (dataSourceName == null && dataSourceId <= 0) {
        throw new DataSourceClientBuilderException(DATASOURCENAME_NEEDED.getErrorDesc)
      }
      if (database == null) throw new DataSourceClientBuilderException(DATABASE_NEEDED.getErrorDesc)
      if (system == null) throw new DataSourceClientBuilderException(SYSTEM_NEEDED.getErrorDesc)
      if (user == null) throw new DataSourceClientBuilderException(USER_NEEDED.getErrorDesc)

      val metadataGetTablesAction = new MetadataGetTablesAction
      metadataGetTablesAction.dataSourceId = this.dataSourceId
      metadataGetTablesAction.dataSourceName = this.dataSourceName
      metadataGetTablesAction.database = this.database
      metadataGetTablesAction.setParameter("system", system)
      metadataGetTablesAction.setUser(user)
      metadataGetTablesAction
    }

  }

}
