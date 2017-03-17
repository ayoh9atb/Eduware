package com.rsdynamics.projects.jdbc.dao;

import com.rsdynamics.projects.jdbc.connection.JDBCEntityRegressor;
import com.rsdynamics.projects.jdbc.connection.JDBCQuery;
import com.codrellica.projects.commons.DateUtil;
import com.codrellica.projects.commons.SQLStyle;
import com.rsdynamics.persistence.config.PersistenceUnitName;
import com.rsdynamics.projects.jdbc.connection.ConnectionSource;
import com.rsdynamics.projects.jdbc.connection.JDBCConnectionManager;
import com.rsdynamics.projects.jdbc.dao.procedures.ParameterMode;
import com.rsdynamics.projects.jdbc.dao.procedures.SPParameter;
import com.rsdynamics.projects.jdbc.dao.procedures.StoredProcedure;
import com.rsdynamics.projects.query.builder.ClauseCondition;
import com.rsdynamics.projects.query.builder.QueryBuilder;
import com.rsdynamics.projects.query.builder.QueryBuilderImpl;
import com.rsdynamics.projects.query.criteria.QueryCriteria;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.abstractobjects.FieldColumnName;
import com.rsdynamix.projects.annotations.processor.DDLProcessor;
import com.rsdynamix.abstractobjects.AbstractQueryParameter;
import com.rsdynamix.abstractobjects.AbstractDataField;
import com.rsdynamix.abstractobjects.AbstractAdaptableEntity;
import com.rsdynamix.abstractobjects.AbstractFieldMetaEntity;
import com.rsdynamix.abstractobjects.QueryParameterEntity;
import com.rsdynamix.dynamo.common.entities.ActionType;
import com.rsdynamix.dynamo.common.entities.AuditTrailSwitchType;
import com.rsdynamics.persistence.config.PersistenceProtocolType;
import com.rsdynamix.projects.report.entities.SQLStatementType;
import com.rsdynamix.projects.report.entities.UlticoreReportEntity;
import com.rsdynamix.projects.security.bean.UserManagerBean;
//import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.tns.dataobjects.DatabaseTNSConfig;
import com.rsdynamix.tns.endpoint.TNSManager;
import com.rsdynamix.tns.endpoint.TNSManagerImpl;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleTypes;

/**
 *
 * This class implements the O/RM logic declared in the
 * <code>JDBCDataServer</code> interface.
 *
 * @author p-aniah
 */
public class JDBCDataServerImpl implements JDBCDataServer {

    private String connectionName;
    private ConnectionSource connectionSrc;
    private JDBCEntityManager entityManager;
    private DatabaseTNSConfig tnsConfig;

    public JDBCDataServerImpl(String connectionName, ConnectionSource connectionSrc) {
        this.connectionName = connectionName;
        this.connectionSrc = connectionSrc;

        entityManager = new JDBCEntityManagerImpl();
        entityManager.setConnectionName(connectionName);
        entityManager.setConnectionSrc(connectionSrc);

        TNSManager tnsManager = new TNSManagerImpl();
        try {
            tnsManager.unmarshallTNSConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        tnsConfig = tnsManager.findTNSConfigByTnsName(connectionName);
        entityManager.setTnsConfig(tnsConfig);
    }

    public JDBCDataServerImpl() {
    }

    public void setPersistenceUnitName(PersistenceUnitName unitName) {
    }
    
    public void executeDDLStatement(String ddlStatement) throws Exception {
        getEntityManager().executeDDLStatement(ddlStatement);
    }
    
    public void saveData(AbstractEntity entity) throws SQLException {
        getEntityManager().persist(entity);
    }

    public void updateData(AbstractEntity entity) throws SQLException {
        getEntityManager().merge(entity);
    }
    
    public void updateData(AbstractEntity updateFields, AbstractEntity conditionFields) throws SQLException {
        getEntityManager().merge(updateFields, conditionFields);
    }

    /**
     * Finds Non-Object/Relation-ally Mapped Data: means the data returned
     * doesn't have to match any of the application objects.
     *
     * @param template
     * @return an instance of AbstractAdaptableEntity
     */
    public AbstractAdaptableEntity findNonORMData(AbstractAdaptableEntity template, SQLStyle sqlStyle) throws Exception {
        if (template.getQueryExpression().trim().length() > 0) {
            if (template.getSqlStatementType() == SQLStatementType.SELECT_STATEMENT) {
                JDBCQuery query = new JDBCQuery();

                query.setConnectionName(connectionName);
                query.setConnectionSrc(connectionSrc);

                template = query.getResultList(template, template.getQueryExpression());
            } else if (template.getSqlStatementType() == SQLStatementType.CALL_STATEMENT) {
                StoredProcedure procedure = new StoredProcedure();
                procedure.setProcedureName(template.getQueryExpression());

                for (QueryParameterEntity queryParam : template.getQueryParamList()) {
                    SPParameter spParam = new SPParameter();
                    spParam.setParameterName(queryParam.getParameterName());
                    spParam.setParameterMode(ParameterMode.IN);
                    spParam.setParameterType(queryParam.getParameterType());
                    spParam.setParameterValue(queryParam.getParamterValue());

                    procedure.getParamList().add(spParam);
                }

                template = executeSPForReport(procedure, template, sqlStyle);
            }
        } else {
            String queryString = "SELECT ";

            for (AbstractFieldMetaEntity fieldMeta : template.getAbstractFieldMetaList()) {
                queryString += fieldMeta.getDatabaseFieldName() + ", ";
            }
            queryString = queryString.substring(0, queryString.length() - 2) + " FROM " + template.getDatabaseTableName();

            if (template.getQueryParamList().size() > 0) {
                queryString += " WHERE ";
            }

            for (QueryParameterEntity param : template.getQueryParamList()) {
                queryString += param.getParameterName() + " " + param.getOperator() + " ? And ";
            }

            if (template.getQueryParamList().size() > 0) {
                queryString = queryString.substring(0, queryString.length() - 5);
            }

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>> " + queryString);

            JDBCQuery query = new JDBCQuery();
            query.setConnectionName(connectionName);
            query.setConnectionSrc(connectionSrc);

            template = query.getResultList(template, queryString);
        }

        return template;
    }

    //
    public List<List<AbstractDataField>> findNonORMData(AbstractAdaptableEntity template,
            List<AbstractFieldMetaEntity> abstractFieldMetaList,
            List<AbstractQueryParameter> queryParamList,
            SQLStyle sqlStyle) throws Exception {
        List<List<AbstractDataField>> fieldMatrix = null;

        if (template.getQueryExpression().trim().length() > 0) {
            if (template.getSqlStatementType() == SQLStatementType.SELECT_STATEMENT) {
                JDBCQuery query = new JDBCQuery();
                query.setConnectionName(connectionName);
                query.setConnectionSrc(connectionSrc);

                fieldMatrix = query.getResultList(template,
                        queryParamList,
                        abstractFieldMetaList,
                        template.getQueryExpression(), sqlStyle);
            } else if (template.getSqlStatementType() == SQLStatementType.CALL_STATEMENT) {
                StoredProcedure procedure = new StoredProcedure();
                procedure.setProcedureName(template.getQueryExpression());

                for (QueryParameterEntity queryParam : template.getQueryParamList()) {
                    SPParameter spParam = new SPParameter();
                    spParam.setParameterName(queryParam.getParameterName());
                    spParam.setParameterMode(ParameterMode.IN);
                    spParam.setParameterType(queryParam.getParameterType());
                    spParam.setParameterValue(queryParam.getParamterValue());

                    procedure.getParamList().add(spParam);
                }

                fieldMatrix = executeSPForFieldMatrix(procedure, abstractFieldMetaList, sqlStyle);
            }
        } else {
            String queryString = "SELECT ";

            for (AbstractFieldMetaEntity fieldMeta : abstractFieldMetaList) {
                queryString += fieldMeta.getDatabaseFieldName() + ", ";
            }

            queryString = queryString.substring(0, queryString.length() - 2) + " FROM "
                    + template.getDatabaseTableName();

            if (queryParamList.size() > 0) {
                queryString += " WHERE ";
            }

            for (AbstractQueryParameter param : queryParamList) {
                queryString += param.getParameterName() + " " + param.getOperator() + " ? And ";
            }

            if (queryParamList.size() > 0) {
                queryString = queryString.substring(0, queryString.length() - 5);
            }

            System.out.println(">>>>>>>>>>>>>>>>>>>>>>> " + queryString);

            JDBCQuery query = new JDBCQuery();
            query.setConnectionName(connectionName);
            query.setConnectionSrc(connectionSrc);

            fieldMatrix = query.getResultList(template, queryParamList, abstractFieldMetaList, queryString, sqlStyle);
        }

        return fieldMatrix;
    }

    public List<AbstractEntity> findData(AbstractEntity entity) throws Exception {
        entity.encryptFields();
        
        new JDBCEntityRegressor(connectionName, connectionSrc, tnsConfig).performEntityRegression(entity);

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTnsConfig(tnsConfig);
        List<ClauseCondition> conditionList = null;
        List<FieldColumnName> columnList = null;

        conditionList = queryCriteria.buildJDBCQuery(entity);
        columnList = queryCriteria.getColumnList();

        QueryBuilder queryBuilder = new QueryBuilderImpl();
        DDLProcessor ddlProcessor = new DDLProcessor();
        String tableName = ddlProcessor.getTableNameFromEntity(entity);

        JDBCQuery query = getEntityManager().createQuery(queryBuilder.buildJDBCSelectQuery(
                conditionList,
                tableName,
                entity.getQueryGateOperators().createSubquery(
                        PersistenceProtocolType.JDBC_PROTOCOL_TYPE),
                columnList,
                tnsConfig.getDbmsName()));

        query = queryCriteria.populateJDBCQuery(entity, query);

        return query.getResultList();
    }

    public List<AbstractEntity> findMasterData(AbstractEntity entity) throws Exception {
        entity.encryptFields();

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTnsConfig(tnsConfig);
        List<ClauseCondition> conditionList = null;
        List<FieldColumnName> columnList = null;

        conditionList = queryCriteria.buildJDBCQuery(entity);
        columnList = queryCriteria.getColumnList();

        QueryBuilder queryBuilder = new QueryBuilderImpl();
        DDLProcessor ddlProcessor = new DDLProcessor();
        String tableName = ddlProcessor.getTableNameFromEntity(entity) + "_MAST";

        JDBCQuery query = getEntityManager().createQuery(queryBuilder.buildJDBCSelectQuery(
                conditionList,
                tableName,
                entity.getQueryGateOperators().createSubquery(
                        PersistenceProtocolType.JDBC_PROTOCOL_TYPE),
                columnList,
                tnsConfig.getDbmsName()));

        query = queryCriteria.populateJDBCQuery(entity, query);

        return query.getResultList();
    }

    public List<AbstractEntity> findMasterData(AbstractEntity entity, long entityMasterID) throws Exception {
        DDLProcessor ddlProcessor = new DDLProcessor();
        String tableName = ddlProcessor.getTableNameFromEntity(entity) + "_MAST";

        JDBCQuery query = getEntityManager().createQuery("SELECT * FROM " + tableName + " WHERE ENTITY_MASTER_ID = ?");
        query.setParameter("entityMasterID", entityMasterID);
        query.setEntity(entity);

        return query.getResultList();
    }

    public void deleteData(AbstractEntity entity) throws Exception {
        entity.encryptFields();

        QueryCriteria queryCriteria = new QueryCriteria();
        queryCriteria.setTnsConfig(tnsConfig);
        List<ClauseCondition> conditionList = null;

        conditionList = queryCriteria.buildJDBCQuery(entity);

        QueryBuilder queryBuilder = new QueryBuilderImpl();
        DDLProcessor ddlProcessor = new DDLProcessor();
        String tableName = ddlProcessor.getTableNameFromEntity(entity);

        JDBCQuery query = getEntityManager().createQuery(
                queryBuilder.buildJDBCDeleteQuery(conditionList, tableName, ""));

        query = queryCriteria.populateJDBCQuery(entity, query);
        query.executeUpdate();

        if ((CommonBean.AUDIT_TRAIL_SWITCH == AuditTrailSwitchType.ON) && (!entity.isSuppressAuditTrail())) {
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
                ex.printStackTrace();
            }

            getEntityManager().persistMaster(entity, ActionType.DELETE);
        }
    }

    public List<List<AbstractDataField>> executeSPForFieldMatrix(StoredProcedure procedure,
            List<AbstractFieldMetaEntity> abstractFieldMetaList,
            SQLStyle sqlStyle) throws Exception {
        List<List<AbstractDataField>> reportFieldDataMatrix = new ArrayList<List<AbstractDataField>>();

        Connection connection = JDBCConnectionManager.getInstance(
                getConnectionName(), getConnectionSrc()).getDatabaseConnection();

        CallableStatement cstmt = null;
        ResultSet resultSet = null;

        String callableQuery = "";
        if (sqlStyle == SQLStyle.MYSQL_STYLE) {
            callableQuery = "{call " + procedure.getProcedureName() + " (";
        } else if (sqlStyle == SQLStyle.ORACLE_STYLE) {
            callableQuery = "{? = call " + procedure.getProcedureName() + " (";
        }

        boolean hasParameters = false;
        for (SPParameter param : procedure.getParamList()) {
            callableQuery += "?, ";
            hasParameters = true;
        }

        if (hasParameters) {
            callableQuery = callableQuery.substring(0, callableQuery.length() - 2);
        }
        callableQuery += ")}";

        System.out.println("++++++++++++++++++++++++++++++++++++++++++ [1] callableQuery  ==>> " + callableQuery);

        try {
            cstmt = connection.prepareCall(callableQuery);
            int position = 1;

            if (sqlStyle == SQLStyle.ORACLE_STYLE) {
                cstmt.registerOutParameter(position++, OracleTypes.CURSOR);
            }

            for (SPParameter param : procedure.getParamList()) {
                if ((param.getParameterMode() == ParameterMode.IN)
                        || (param.getParameterMode() == ParameterMode.INOUT)) {
                    if (param.getParameterType() == Float.class) {
                        cstmt.setFloat(position, Float.parseFloat(param.getParameterValue().toString()));
                    } else if (param.getParameterType() == Double.class) {
                        cstmt.setDouble(position, Double.parseDouble(param.getParameterValue().toString()));
                    } else if (param.getParameterType() == Integer.class) {
                        cstmt.setInt(position, Integer.parseInt(param.getParameterValue().toString()));
                    } else if (param.getParameterType() == Long.class) {
                        cstmt.setLong(position, Long.parseLong(param.getParameterValue().toString()));
                    } else if (param.getParameterType() == String.class) {
                        cstmt.setString(position, param.getParameterValue().toString());
                    } else if (param.getParameterType() == BigDecimal.class) {
                        cstmt.setBigDecimal(position, BigDecimal.valueOf(Double.parseDouble(param.getParameterValue().toString())));
                    } else if (param.getParameterType() == java.util.Date.class) {
                        cstmt.setDate(position, DateUtil.toSQLDate((java.util.Date) param.getParameterValue()));
                    }

                    if (param.getParameterMode() == ParameterMode.INOUT) {
                        cstmt.registerOutParameter(position, param.getSqlParameterType());
                    }
                    position++;
                } else {
                    cstmt.registerOutParameter(position++, param.getSqlParameterType());
                }
            }

            if (sqlStyle == SQLStyle.MYSQL_STYLE) {
                resultSet = cstmt.executeQuery();
            } else if (sqlStyle == SQLStyle.ORACLE_STYLE) {
                cstmt.execute();
                resultSet = (ResultSet) cstmt.getObject(1);
            }

            while (resultSet.next()) {
                List<AbstractDataField> fieldList = new ArrayList<AbstractDataField>();
                for (AbstractFieldMetaEntity fieldMeta : abstractFieldMetaList) {
                    if ((fieldMeta.getDatabaseFieldName() != null) && (fieldMeta.getDatabaseFieldName().trim().length() > 0)) {
                        AbstractDataField field = new AbstractDataField();
                        field.setFieldName(fieldMeta.getDatabaseFieldName());
                        field.setFieldValue(resultSet.getObject(fieldMeta.getDatabaseFieldName()));
                        fieldList.add(field);
                    }
                }
                reportFieldDataMatrix.add(fieldList);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (cstmt != null) {
                    cstmt.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return reportFieldDataMatrix;
    }

    public AbstractAdaptableEntity executeSPForReport(StoredProcedure procedure,
            AbstractAdaptableEntity template,
            SQLStyle sqlStyle) throws Exception {
        Connection connection = JDBCConnectionManager.getInstance(
                getConnectionName(), getConnectionSrc()).getDatabaseConnection();

        CallableStatement cstmt = null;
        ResultSet resultSet = null;

        String callableQuery = "";
        if (sqlStyle == SQLStyle.MYSQL_STYLE) {
            callableQuery = "{call " + procedure.getProcedureName() + " (";
        } else if (sqlStyle == SQLStyle.ORACLE_STYLE) {
            callableQuery = "{? = call " + procedure.getProcedureName() + " (";
        }

        boolean hasParameters = false;
        for (SPParameter param : procedure.getParamList()) {
            callableQuery += "?, ";
            hasParameters = true;
        }

        if (hasParameters) {
            callableQuery = callableQuery.substring(0, callableQuery.length() - 2);
        }
        callableQuery += ")}";

        System.out.println("++++++++++++++++++++++++++++++++++++++++++ [3] callableQuery  ==>> " + callableQuery);

        try {
            cstmt = connection.prepareCall(callableQuery);
            int position = 1;

            if (sqlStyle == SQLStyle.ORACLE_STYLE) {
                cstmt.registerOutParameter(position++, OracleTypes.CURSOR);
            }

            for (SPParameter param : procedure.getParamList()) {
                if ((param.getParameterMode() == ParameterMode.IN)
                        || (param.getParameterMode() == ParameterMode.INOUT)) {
                    if (param.getParameterType() == Float.class) {
                        cstmt.setFloat(position, Float.parseFloat(param.getParameterValue().toString()));
                    } else if (param.getParameterType() == Double.class) {
                        cstmt.setDouble(position, Double.parseDouble(param.getParameterValue().toString()));
                    } else if (param.getParameterType() == Integer.class) {
                        cstmt.setInt(position, Integer.parseInt(param.getParameterValue().toString()));
                    } else if (param.getParameterType() == Long.class) {
                        cstmt.setLong(position, Long.parseLong(param.getParameterValue().toString()));
                    } else if (param.getParameterType() == String.class) {
                        cstmt.setString(position, param.getParameterValue().toString());
                    } else if (param.getParameterType() == BigDecimal.class) {
                        cstmt.setBigDecimal(position, BigDecimal.valueOf(Double.parseDouble(param.getParameterValue().toString())));
                    } else if (param.getParameterType() == java.util.Date.class) {
                        cstmt.setDate(position, DateUtil.toSQLDate((java.util.Date) param.getParameterValue()));
                    }

                    if (param.getParameterMode() == ParameterMode.INOUT) {
                        cstmt.registerOutParameter(position, param.getSqlParameterType());
                    }
                    position++;
                } else {
                    cstmt.registerOutParameter(position++, param.getSqlParameterType());
                }
            }

            if (sqlStyle == SQLStyle.MYSQL_STYLE) {
                resultSet = cstmt.executeQuery();
            } else if (sqlStyle == SQLStyle.ORACLE_STYLE) {
                cstmt.execute();
                resultSet = (ResultSet) cstmt.getObject(1);
            }

            if (template instanceof UlticoreReportEntity) {
                while (resultSet.next()) {
                    List<Object> fieldList = new ArrayList<Object>();
                    for (AbstractFieldMetaEntity fieldMeta : template.getAbstractFieldMetaList()) {
                        fieldList.add(resultSet.getObject(fieldMeta.getDatabaseFieldName()));
                    }
                    template.getObjectFieldList().add(fieldList);
                }
            } else {
                while (resultSet.next()) {
                    List<AbstractDataField> fieldList = new ArrayList<AbstractDataField>();
                    for (AbstractFieldMetaEntity fieldMeta : template.getAbstractFieldMetaList()) {
                        if ((fieldMeta.getDatabaseFieldName() != null) && (fieldMeta.getDatabaseFieldName().trim().length() > 0)) {
                            Object fieldValue = resultSet.getObject(fieldMeta.getDatabaseFieldName());
                            fieldList.add(new AbstractDataField(fieldMeta.getDatabaseFieldName(), fieldValue));
                        }
                    }
                    template.getAbstractFieldList().add(fieldList);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (cstmt != null) {
                    cstmt.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return template;
    }

    public void executeArbitraryRoutine(StoredProcedure procedure,
            SQLStyle sqlStyle) throws Exception {
        Connection connection = JDBCConnectionManager.getInstance(
                getConnectionName(), getConnectionSrc()).getDatabaseConnection();

        CallableStatement cstmt = null;

        String callableQuery = "";
        if (sqlStyle == SQLStyle.MYSQL_STYLE) {
            callableQuery = "{call " + procedure.getProcedureName() + " (";
        } else if (sqlStyle == SQLStyle.ORACLE_STYLE) {
            callableQuery = "{? = call " + procedure.getProcedureName() + " (";
        }

        boolean hasParameters = false;
        for (SPParameter param : procedure.getParamList()) {
            callableQuery += "?, ";
            hasParameters = true;
        }

        if (hasParameters) {
            callableQuery = callableQuery.substring(0, callableQuery.length() - 2);
        }
        callableQuery += ")}";

        System.out.println("++++++++++++++++++++++++++++++++++++++++++ [3] callableQuery  ==>> " + callableQuery);

        try {
            cstmt = connection.prepareCall(callableQuery);
            int position = 1;

            if (sqlStyle == SQLStyle.ORACLE_STYLE) {
                cstmt.registerOutParameter(position++, OracleTypes.CURSOR);
            }

            for (SPParameter param : procedure.getParamList()) {
                if ((param.getParameterMode() == ParameterMode.IN)
                        || (param.getParameterMode() == ParameterMode.INOUT)) {
                    if (param.getParameterType() == Float.class) {
                        cstmt.setFloat(position, Float.parseFloat(param.getParameterValue().toString()));
                    } else if (param.getParameterType() == Double.class) {
                        cstmt.setDouble(position, Double.parseDouble(param.getParameterValue().toString()));
                    } else if (param.getParameterType() == Integer.class) {
                        cstmt.setInt(position, Integer.parseInt(param.getParameterValue().toString()));
                    } else if (param.getParameterType() == Long.class) {
                        cstmt.setLong(position, Long.parseLong(param.getParameterValue().toString()));
                    } else if (param.getParameterType() == String.class) {
                        cstmt.setString(position, param.getParameterValue().toString());
                    } else if (param.getParameterType() == BigDecimal.class) {
                        cstmt.setBigDecimal(position, BigDecimal.valueOf(Double.parseDouble(param.getParameterValue().toString())));
                    } else if (param.getParameterType() == java.util.Date.class) {
                        cstmt.setDate(position, DateUtil.toSQLDate((java.util.Date) param.getParameterValue()));
                    }

                    if (param.getParameterMode() == ParameterMode.INOUT) {
                        cstmt.registerOutParameter(position, param.getSqlParameterType());
                    }
                    position++;
                } else {
                    cstmt.registerOutParameter(position++, param.getSqlParameterType());
                }
            }

            if (sqlStyle == SQLStyle.MYSQL_STYLE) {
                cstmt.executeQuery();
            } else if (sqlStyle == SQLStyle.ORACLE_STYLE) {
                cstmt.execute();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (cstmt != null) {
                    cstmt.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * @return the entityManager
     */
    public JDBCEntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * @param entityManager the entityManager to set
     */
    public void setEntityManager(JDBCEntityManager entityManager) {
        this.entityManager = entityManager;
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
}
