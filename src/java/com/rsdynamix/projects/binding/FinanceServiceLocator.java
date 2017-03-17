package com.rsdynamix.projects.binding;

import com.codrellica.mail.beans.SendMessageBean;
import com.codrellica.mail.beans.SendMessageRemote;
import com.rsdynamics.persistence.config.PersistenceProtocolType;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;
import com.rsdynamix.tns.util.Constants;
import javax.naming.InitialContext;

/**
 *
 * This class locates and instantiates the Dynamo persistence service. In this
 * instance, the service is colocated with the rest of the Dynamo architecture,
 * so a simple instantiation will do.
 *
 * @author p-aniah
 */
public class FinanceServiceLocator {

    /**
     * Creates a new instance of FinanceServiceLocator
     */
    private FinanceServiceLocator() {
    }

    public static FinultimatePersistenceRemote locateFinancePersistenceManager() {
        FinultimatePersistenceRemote dataServer = (FinultimatePersistenceRemote) getJNDIBinding(
                "com.rsdynamics.projects.eao.FinultimatePersistenceRemote");

        dataServer.setPersistenceProtocolType(PersistenceProtocolType.JDBC_PROTOCOL_TYPE);
        dataServer.setSelectedTnsName(Constants.FINULTIMATE_PU);

        return dataServer;
    }

    public static FinultimatePersistenceRemote locateCommonsPersistenceManager() {
        FinultimatePersistenceRemote dataServer = (FinultimatePersistenceRemote) getJNDIBinding(
                "com.rsdynamics.projects.eao.FinultimatePersistenceRemote");
        dataServer.setPersistenceProtocolType(PersistenceProtocolType.JDBC_PROTOCOL_TYPE);
        dataServer.setSelectedTnsName(Constants.FINULTIMATE_PU);

        return dataServer;
    }

    public static FinultimatePersistenceRemote getChurchPersistenceManager() {
        FinultimatePersistenceRemote dataServer = (FinultimatePersistenceRemote) getJNDIBinding(
                "com.rsdynamics.projects.eao.FinultimatePersistenceRemote");
        dataServer.setPersistenceProtocolType(PersistenceProtocolType.JDBC_PROTOCOL_TYPE);
        dataServer.setSelectedTnsName(Constants.CHURCH_PU);

        return dataServer;
    }

    public static FinultimatePersistenceRemote getAcademixPersistenceManager() {
        FinultimatePersistenceRemote dataServer = (FinultimatePersistenceRemote) getJNDIBinding(
                "com.rsdynamics.projects.eao.FinultimatePersistenceRemote");
        dataServer.setPersistenceProtocolType(PersistenceProtocolType.JDBC_PROTOCOL_TYPE);
        dataServer.setSelectedTnsName(Constants.ACADEMIX_PU);

        return dataServer;
    }
    
    public static FinultimatePersistenceRemote getHealthPersistenceManager() {
        FinultimatePersistenceRemote dataServer = (FinultimatePersistenceRemote) getJNDIBinding(
                "com.rsdynamics.projects.eao.FinultimatePersistenceRemote");
        dataServer.setPersistenceProtocolType(PersistenceProtocolType.JDBC_PROTOCOL_TYPE);
        dataServer.setSelectedTnsName(Constants.HEALTH_INF_MGT_PU);

        return dataServer;
    }
    
    public static FinultimatePersistenceRemote getInsurancePersistenceManager() {
        FinultimatePersistenceRemote dataServer = (FinultimatePersistenceRemote) getJNDIBinding(
                "com.rsdynamics.projects.eao.FinultimatePersistenceRemote");
        dataServer.setPersistenceProtocolType(PersistenceProtocolType.JDBC_PROTOCOL_TYPE);
        dataServer.setSelectedTnsName(Constants.INSURE_PU);

        return dataServer;
    }
    
    public static SendMessageRemote locateMailMessageServer(){
        return new SendMessageBean();
    }

    private static Object getJNDIBinding(String jndiName) {
        Object handle = null;
        try {
            InitialContext context = new InitialContext();
            handle = context.lookup(jndiName);
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }

        return handle;
    }

}
