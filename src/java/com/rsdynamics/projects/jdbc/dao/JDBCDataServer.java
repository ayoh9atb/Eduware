package com.rsdynamics.projects.jdbc.dao;

import com.codrellica.projects.commons.SQLStyle;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamics.projects.jdbc.connection.ConnectionSource;
import com.rsdynamics.projects.jdbc.dao.procedures.StoredProcedure;
import com.rsdynamix.abstractobjects.AbstractQueryParameter;
import com.rsdynamix.abstractobjects.AbstractDataField;
import com.rsdynamix.abstractobjects.AbstractAdaptableEntity;
import com.rsdynamix.abstractobjects.AbstractFieldMetaEntity;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * This interface encapsulates the JDBC Object/Relational Mapping (O/RM) logic
 *
 * @author p-aniah
 */
public interface JDBCDataServer {
    
    public void executeDDLStatement(String ddlStatement) throws Exception;
    
    void saveData(AbstractEntity entity) throws SQLException;
    
    public AbstractAdaptableEntity findNonORMData(
            AbstractAdaptableEntity template, SQLStyle sqlStyle)throws Exception;
    //
    public List<List<AbstractDataField>> findNonORMData(AbstractAdaptableEntity template,
            List<AbstractFieldMetaEntity> abstractFieldMetaList,
            List<AbstractQueryParameter> queryParamList, SQLStyle sqlStyle) throws Exception;
    

    public void executeArbitraryRoutine(StoredProcedure procedure, SQLStyle sqlStyle) throws Exception;

    List<AbstractEntity> findData(AbstractEntity entity) throws Exception;
    
    public List<AbstractEntity> findMasterData(AbstractEntity entity) throws Exception;
    
    public List<AbstractEntity> findMasterData(AbstractEntity entity, long entityMasterID) throws Exception;

    void updateData(AbstractEntity entity) throws SQLException, Exception;

    void updateData(AbstractEntity updateFields, AbstractEntity conditionFields) throws SQLException, Exception;

    void deleteData(AbstractEntity entity) throws Exception;

    /**
     * @return the connectionName
     */
    String getConnectionName();

    /**
     * @param connectionName the connectionName to set
     */
    void setConnectionName(String connectionName);

    /**
     * @return the connectionSrc
     */
    ConnectionSource getConnectionSrc();

    /**
     * @param connectionSrc the connectionSrc to set
     */
    void setConnectionSrc(ConnectionSource connectionSrc);
}
