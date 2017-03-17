package com.rsdynamics.projects.jdbc.dao;

import com.rsdynamics.projects.jdbc.connection.JDBCEntityRegressor;
import com.rsdynamics.projects.jdbc.connection.JDBCQuery;
import com.codrellica.projects.commons.SQLStyle;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.abstractobjects.FieldColumnName;
import com.rsdynamix.abstractobjects.FieldData;
import com.rsdynamix.projects.annotations.processor.DDLProcessor;
import com.rsdynamics.projects.jdbc.connection.ConnectionSource;
import com.rsdynamics.projects.jdbc.connection.JDBCEAOManager;
import com.rsdynamix.abstractobjects.AbstractDataField;
import com.rsdynamix.abstractobjects.AbstractFieldMetaEntity;
import com.rsdynamix.abstractobjects.PersistConstraintType;
import com.rsdynamix.abstractobjects.PersistenceOrientationType;
import com.rsdynamix.abstractobjects.UlticoreLOVFieldMetaEntity;
import com.rsdynamix.abstractobjects.UlticoreProductFieldMetaEntity;
import com.rsdynamix.abstractobjects.UlticoreScheduleFieldMetaEntity;
import com.rsdynamix.dynamo.applicationdata.ApplicationDataHolder;
import com.rsdynamix.dynamo.common.entities.ActionType;
import com.rsdynamix.dynamo.common.entities.AuditTrailSwitchType;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.DDLExecutionSwitchType;
import com.rsdynamix.finance.constants.FinultimateConstants;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.tns.dataobjects.DatabaseTNSConfig;
import com.rsdynamix.tns.util.Constants;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 *
 * This class implements entity persistence logic declared in
 * <code>JDBCEntityManager</code> interface.
 *
 * @author p-aniah
 */
public class JDBCEntityManagerImpl implements JDBCEntityManager {

    private static String BIZ_ACTION_TRAIL_SERIAL_LEN = "biz_actn_trail_no_serial";
    //
    private String connectionName;
    private ConnectionSource connectionSrc;
    private DatabaseTNSConfig tnsConfig;

    public JDBCEntityManagerImpl() {
        connectionName = "";
    }

    public JDBCQuery createQuery(String queryString) {
        JDBCQuery query = new JDBCQuery();
        query.setConnectionName(connectionName);
        query.setConnectionSrc(connectionSrc);
        query.setQueryString(queryString);

        return query;
    }
    
    public void executeDDLStatement(String ddlStatement) throws Exception {
        JDBCEAOManager eaoManager = new JDBCEAOManager();
        eaoManager.setConnectionName(getConnectionName());
        eaoManager.setConnectionSrc(getConnectionSrc());
        eaoManager.setTnsConfig(tnsConfig);

        try {
            eaoManager.executeDDLStatement(ddlStatement);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void persist(AbstractEntity entity) throws SQLException {
        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);
        if (userManagerBean == null) {
            userManagerBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userManagerBean}",
                    UserManagerBean.class, userManagerBean);
        }

        try {
            entity.setApprover(userManagerBean.getUserAccount().getUserName());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        new JDBCEntityRegressor(connectionName, connectionSrc, tnsConfig).performEntityRegression(entity);

        standardPersist(entity);

        if ((CommonBean.AUDIT_TRAIL_SWITCH == AuditTrailSwitchType.ON) && (!entity.isSuppressAuditTrail())) {
            try {
                persistMaster(entity, ActionType.CREATE);
            } catch (SQLException ex) {
                throw ex;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void persistMaster(AbstractEntity entity, ActionType actionType) throws SQLException, Exception {
        int entityMastID = 0;

        FieldData pkFieldName = null;
        if (actionType != ActionType.DELETE) {
            ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.applicationPropertyBean}", ApplicationPropertyBean.class);
            if (appPropBean == null) {
                appPropBean = new ApplicationPropertyBean();
                CommonBean.setBeanToContext("#{applicationScope.applicationPropertyBean}",
                        ApplicationPropertyBean.class, appPropBean);
            }

            DDLProcessor ddlProcessor = new DDLProcessor();
            int columnCount = 0;

            String tableName = ddlProcessor.getTableNameFromEntity(entity);
            String insertSQL = "INSERT INTO " + tableName + "_MAST (";
            String paramNameSpec = "";
            String paramPlaceHolderSpec = " Values(";

            List<FieldData> fieldDataList = entity.getFieldDataList();

            List<FieldColumnName> columnList = DDLProcessor.buildFieldColumnNameList(entity);
            FieldColumnName pkColumn = DDLProcessor.findPKFieldColumnName(columnList);
            if (pkColumn != null) {
                pkFieldName = ddlProcessor.findFieldDataByFieldName(fieldDataList, pkColumn.getFieldName());

                entityMastID = Integer.parseInt(appPropBean.getValue(
                        tableName + "." + pkFieldName.getFieldName(),
                        FinultimateConstants.ONE_STR, true));

                FieldData mastIDField = new FieldData();
                mastIDField.setFieldName("entityMasterID");
                mastIDField.setFieldValue(entityMastID);
                mastIDField.setPrimaryKey(true);

                pkColumn = new FieldColumnName();
                pkColumn.setFieldName("entityMasterID");
                pkColumn.setColumnName("ENTITY_MASTER_ID");

                fieldDataList.add(mastIDField);
                columnList.add(pkColumn);
            }

            Collections.sort(fieldDataList, new Comparator<FieldData>() {

                public int compare(FieldData o1, FieldData o2) {
                    return (o1.getFieldName().compareTo(o2.getFieldName()));
                }
            });

            Collections.sort(columnList, new Comparator<FieldColumnName>() {

                public int compare(FieldColumnName o1, FieldColumnName o2) {
                    return (o1.getFieldName().compareTo(o2.getFieldName()));
                }
            });

            for (FieldColumnName column : columnList) {
                columnCount++;
                paramNameSpec += column.getColumnName() + ", ";
                paramPlaceHolderSpec += "?, ";
            }

            if (columnCount > 0) {
                insertSQL += paramNameSpec.substring(0, paramNameSpec.length() - 2) + ")"
                        + paramPlaceHolderSpec.substring(0, paramPlaceHolderSpec.length() - 2) + ")";
            }
            System.out.println(">>>>>>>>>>>>> SQL ==>> " + insertSQL);

            JDBCEAOManager eaoManager = new JDBCEAOManager();
            eaoManager.setConnectionName(getConnectionName());
            eaoManager.setConnectionSrc(getConnectionSrc());
            eaoManager.setTnsConfig(tnsConfig);

            eaoManager.persistEntity(fieldDataList, insertSQL);
        }

        executeBusinessActionTrail(entity, entityMastID, actionType, pkFieldName);
    }

    private void standardPersist(AbstractEntity entity) throws SQLException {
        DDLProcessor ddlProcessor = new DDLProcessor();
        int columnCount = 0;

        String insertSQL = "INSERT INTO " + ddlProcessor.getTableNameFromEntity(entity) + " (";
        String paramNameSpec = "";
        String paramPlaceHolderSpec = " Values(";

        List<FieldData> fieldDataList = entity.getFieldDataList();
        List<FieldColumnName> columnList = ddlProcessor.buildFieldColumnNameList(entity);

        Collections.sort(fieldDataList, new Comparator<FieldData>() {

            public int compare(FieldData o1, FieldData o2) {
                return (o1.getFieldName().compareTo(o2.getFieldName()));
            }
        });

        Collections.sort(columnList, new Comparator<FieldColumnName>() {

            public int compare(FieldColumnName o1, FieldColumnName o2) {
                return (o1.getFieldName().compareTo(o2.getFieldName()));
            }
        });

        for (FieldColumnName column : columnList) {
            columnCount++;
            paramNameSpec += column.getColumnName() + ", ";
            paramPlaceHolderSpec += "?, ";
        }

        if (columnCount > 0) {
            insertSQL += paramNameSpec.substring(0, paramNameSpec.length() - 2) + ")"
                    + paramPlaceHolderSpec.substring(0, paramPlaceHolderSpec.length() - 2) + ")";
        }
        System.out.println(">>>>>>>>>>>>> SQL ==>> " + insertSQL);

        JDBCEAOManager eaoManager = new JDBCEAOManager();
        eaoManager.setConnectionName(getConnectionName());
        eaoManager.setConnectionSrc(getConnectionSrc());
        eaoManager.setTnsConfig(tnsConfig);

        eaoManager.persistEntity(fieldDataList, insertSQL);
    }

    public void merge(AbstractEntity entity) throws SQLException {
        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);
        if (userManagerBean == null) {
            userManagerBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userManagerBean}",
                    UserManagerBean.class, userManagerBean);
        }

        try {
            entity.setApprover(userManagerBean.getUserAccount().getUserName());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        new JDBCEntityRegressor(connectionName, connectionSrc, tnsConfig).performEntityRegression(entity);

        DDLProcessor ddlProcessor = new DDLProcessor();
        int columnCount = 0;

        String updateSQL = "UPDATE " + ddlProcessor.getTableNameFromEntity(entity) + " SET ";
        String paramSpec = "";

        List<FieldData> fieldDataList = entity.getFieldDataList();
        List<FieldColumnName> columnList = ddlProcessor.buildFieldColumnNameList(entity);

        Collections.sort(fieldDataList, new Comparator<FieldData>() {

            public int compare(FieldData o1, FieldData o2) {
                return (o1.getFieldName().compareTo(o2.getFieldName()));
            }
        });

        Collections.sort(columnList, new Comparator<FieldColumnName>() {

            public int compare(FieldColumnName o1, FieldColumnName o2) {
                return (o1.getFieldName().compareTo(o2.getFieldName()));
            }
        });

        for (FieldColumnName column : columnList) {
            columnCount++;
            paramSpec += column.getColumnName() + " = ?, ";
        }

        if (columnCount > 0) {
            String idFieldName = ddlProcessor.getFieldNameOfIDField(entity);
            FieldColumnName column = ddlProcessor.findFieldColumnNameByFieldName(columnList, idFieldName);
            FieldData fieldData = ddlProcessor.findFieldDataByFieldName(fieldDataList, idFieldName);

            if (fieldData.getFieldValue() instanceof String) {
                updateSQL += paramSpec.substring(0, paramSpec.length() - 2) + " Where "
                        + column.getColumnName() + " = '" + fieldData.getFieldValue().toString() + "'";
            } else {
                updateSQL += paramSpec.substring(0, paramSpec.length() - 2) + " Where "
                        + column.getColumnName() + " = " + fieldData.getFieldValue().toString();
            }
        }
        System.out.println(">>>>>>>>>>>>> SQL ==>> " + updateSQL);

        JDBCEAOManager eaoManager = new JDBCEAOManager();
        eaoManager.setConnectionName(getConnectionName());
        eaoManager.setConnectionSrc(getConnectionSrc());
        eaoManager.setTnsConfig(tnsConfig);

        eaoManager.mergeEntity(fieldDataList, updateSQL);

        if ((CommonBean.AUDIT_TRAIL_SWITCH == AuditTrailSwitchType.ON) && (!entity.isSuppressAuditTrail())) {
            try {
                persistMaster(entity, ActionType.UPDATE);
            } catch (SQLException ex) {
                throw ex;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void merge(AbstractEntity updateFields, AbstractEntity conditionFields) throws SQLException {
        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);
        if (userManagerBean == null) {
            userManagerBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userManagerBean}",
                    UserManagerBean.class, userManagerBean);
        }

        try {
            updateFields.setApprover(userManagerBean.getUserAccount().getUserName());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        new JDBCEntityRegressor(connectionName, connectionSrc, tnsConfig).performEntityRegression(updateFields);

        DDLProcessor ddlProcessor = new DDLProcessor();
        int columnCount = 0;

        String updateSQL = "UPDATE " + ddlProcessor.getTableNameFromEntity(updateFields) + " SET ";
        String paramSpec = "";

        List<FieldData> fieldDataList = ddlProcessor.buildNonEmptyFieldDataList(updateFields);
        List<FieldColumnName> columnList = ddlProcessor.buildNonEmptyFieldColumnNameList(updateFields);
        List<FieldData> conditionFieldDataList = ddlProcessor.buildNonEmptyFieldDataList(conditionFields);

        for (FieldColumnName column : columnList) {
            columnCount++;
            paramSpec += column.getColumnName() + " = ?, ";
        }

        if (columnCount > 0) {
            updateSQL += paramSpec.substring(0, paramSpec.length() - 2) + " Where ";

            for (FieldData fieldData : conditionFieldDataList) {
                fieldDataList.add(fieldData);
                updateSQL += fieldData.getFieldName() + " = ? AND ";
            }
            updateSQL = updateSQL.substring(0, updateSQL.length() - 5);

            System.out.println(">>>>>>>>>>>>> SQL ==>> " + updateSQL);

            JDBCEAOManager eaoManager = new JDBCEAOManager();
            eaoManager.setConnectionName(getConnectionName());
            eaoManager.setConnectionSrc(getConnectionSrc());
            eaoManager.setTnsConfig(tnsConfig);

            eaoManager.mergeEntity(fieldDataList, updateSQL);
        }

        if ((CommonBean.AUDIT_TRAIL_SWITCH == AuditTrailSwitchType.ON) && (!updateFields.isSuppressAuditTrail())) {
            try {
                persistMaster(updateFields, ActionType.UPDATE);
            } catch (SQLException ex) {
                throw ex;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void executeBusinessActionTrail(
            AbstractEntity entity,
            long entityMasterID,
            ActionType actionType,
            FieldData pkFieldName) throws SQLException, Exception {
        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{applicationScope.applicationPropertyBean}",
                    ApplicationPropertyBean.class, appPropBean);
        }

        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);
        if (userManagerBean == null) {
            userManagerBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userManagerBean}",
                    UserManagerBean.class, userManagerBean);
        }

        BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

        actionTrail.setActionType(actionType);
        actionTrail.setEntryDate(new Date());
        actionTrail.setApprover(userManagerBean.getUserAccount().getUserName());
        actionTrail.setEntityName(entity.getClass().getSimpleName());
        actionTrail.setEntityMasterID(entityMasterID);

        if ((pkFieldName != null) && (CommonBean.isDigitSequence(pkFieldName.getFieldValue().toString()))) {
            actionTrail.setEntityID(Long.parseLong(pkFieldName.getFieldValue().toString()));
        }

        int actionTrailID = Integer.parseInt(appPropBean.getValue(
                BIZ_ACTION_TRAIL_SERIAL_LEN,
                FinultimateConstants.ONE_STR, true));
        actionTrail.setBusinessActionTrailID(actionTrailID);

        standardPersist(actionTrail);
    }

    /**
     * @return the connectionName
     */
    public String getConnectionName() {
        return connectionName;
    }

    /**
     * @param connectionName the connectionName to set
     */
    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    /**
     * @return the connectionSrc
     */
    public ConnectionSource getConnectionSrc() {
        return connectionSrc;
    }

    /**
     * @param connectionSrc the connectionSrc to set
     */
    public void setConnectionSrc(ConnectionSource connectionSrc) {
        this.connectionSrc = connectionSrc;
    }

    /**
     * @return the tnsConfig
     */
    public DatabaseTNSConfig getTnsConfig() {
        return tnsConfig;
    }

    /**
     * @param tnsConfig the tnsConfig to set
     */
    public void setTnsConfig(DatabaseTNSConfig tnsConfig) {
        this.tnsConfig = tnsConfig;
    }

}
