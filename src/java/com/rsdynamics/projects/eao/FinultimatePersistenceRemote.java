/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamics.projects.eao;

import com.codrellica.projects.commons.SQLStyle;
import com.rsdynamics.persistence.config.PersistenceProtocolType;
import com.rsdynamics.persistence.config.PersistenceUnitName;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamics.projects.jdbc.connection.JDBCColumn;
import com.rsdynamix.abstractobjects.AbstractQueryParameter;
import com.rsdynamix.abstractobjects.AbstractDataField;
import com.rsdynamics.projects.jdbc.dao.procedures.StoredProcedure;
import com.rsdynamix.abstractobjects.AbstractAdaptableEntity;
import com.rsdynamix.abstractobjects.AbstractFieldMetaEntity;
import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author p-aniah
 */
@Remote
public interface FinultimatePersistenceRemote {
    
    public void rollbackTransaction();
    
    public void beginTransaction();
    
    public void endTransaction();
    
    void executeDDLStatement(String ddlStatement) throws Exception;
    
    void saveData(AbstractEntity entity) throws SQLException;

    java.util.List<AbstractEntity> findData(AbstractEntity entity)throws Exception;
    
    public List<AbstractEntity> findMasterData(AbstractEntity entity) throws Exception;
    
    public List<AbstractEntity> findMasterData(AbstractEntity entity, long entityMasterID) throws Exception;
    
    List<JDBCColumn> getFieldMetaDataList(String tnsEntryKey, 
            String queryString, 
            String TNSConfigFileName) throws Exception;
    
    public AbstractAdaptableEntity findNonORMData(AbstractAdaptableEntity template, SQLStyle sqlStyle) throws Exception;
    //
    public List<List<AbstractDataField>> findNonORMData(AbstractAdaptableEntity template,
            List<AbstractFieldMetaEntity> abstractFieldMetaList,
            List<AbstractQueryParameter> queryParamList, SQLStyle sqlStyle) throws Exception;
    
    public void executeArbitraryRoutine(StoredProcedure procedure, SQLStyle sqlStyle) throws Exception;

    void updateData(AbstractEntity entity) throws SQLException, Exception;

    void updateData(AbstractEntity entity, PersistenceProtocolType persistenceProtocolType) throws SQLException, Exception;

    void updateData(AbstractEntity updateFields, AbstractEntity conditionFields) throws SQLException, Exception;

    void deleteData(AbstractEntity entity) throws Exception;

    void setPersistenceUnitName(PersistenceUnitName unitName);

    /**
     * @return the persistenceProtocolType
     */
    PersistenceProtocolType getPersistenceProtocolType();

    /**
     * @param persistenceProtocolType the persistenceProtocolType to set
     */
    void setPersistenceProtocolType(PersistenceProtocolType persistenceProtocolType);

    List<String> getFieldMetaNameList(String tnsEntryKey, String queryString) throws Exception;
    //
    public void setSelectedTnsName(String selectedTnsName);
    
    public String getSelectedTnsName();
}
