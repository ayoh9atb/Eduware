package com.rsdynamics.projects.jdbc.dao;

import com.rsdynamics.projects.jdbc.connection.JDBCQuery;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamics.projects.jdbc.connection.ConnectionSource;
import com.rsdynamix.dynamo.common.entities.ActionType;
import com.rsdynamix.tns.dataobjects.DatabaseTNSConfig;
import java.sql.SQLException;

/**
 *
 * This interface encapsulates entity persistence logic
 *
 * @author p-aniah
 */
public interface JDBCEntityManager {

    public void executeDDLStatement(String ddlStatement) throws Exception;
    
    public JDBCQuery createQuery(String queryString);

    public void persist(AbstractEntity entity) throws SQLException;
    
    public void persistMaster(AbstractEntity entity, ActionType actionType) throws SQLException, Exception;

    public void merge(AbstractEntity entity) throws SQLException;

    public void merge(AbstractEntity updateFields, AbstractEntity conditionFields) throws SQLException;
    
    /**
     * @return the connectionName
     */
    public String getConnectionName();

    /**
     * @param connectionName the connectionName to set
     */
    public void setConnectionName(String connectionName);

    /**
     * @return the connectionSrc
     */
    public ConnectionSource getConnectionSrc();

    /**
     * @param connectionSrc the connectionSrc to set
     */
    public void setConnectionSrc(ConnectionSource connectionSrc);

    /**
     * @return the tnsConfig
     */
    public DatabaseTNSConfig getTnsConfig();

    /**
     * @param tnsConfig the tnsConfig to set
     */
    public void setTnsConfig(DatabaseTNSConfig tnsConfig);
}
