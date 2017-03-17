package com.rsdynamix.dynamo.common.setup.beans;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamics.projects.field.operators.RelationshipFieldOperator;
import com.rsdynamics.projects.query.operators.QueryOperators;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.dynamo.setup.entities.RelationshipEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.tns.util.Constants;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;

/**
 *
 * @author patushie
 */
@SessionScoped
@ManagedBean(name = "relationshipBean")
public class RelationshipBean {

    public static final String WIFE_RELATIONSHIP_NAME = "WIFE";
    //
    public static final String HUSBAND_RELATIONSHIP_NAME = "HUSBAND";
    //
    private RelationshipEntity relationship;
    private List<RelationshipEntity> relationshipList;
    private List<SelectItem> relationshipItemList;

    public RelationshipBean() {
        relationship = new RelationshipEntity();
        loadRelationshipList();
    }

    public void loadRelationshipList() {
        if ((relationshipList == null) || (relationshipList.size() == 0)) {
            reloadRelationshipList();
        }
    }

    public void reloadRelationshipList() {
        relationshipList = new ArrayList<RelationshipEntity>();
        relationshipItemList = new ArrayList<SelectItem>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
        }

        MenuEntity privilege = privilegeBean.findPrivilegeByName(
                menuManagerBean.getSystemMap().get(MenuManagerBean.RELSHIP_MENU_ITEM));

        try {
            RelationshipEntity criteria = new RelationshipEntity();
            List<AbstractEntity> entityList = dataServer.findData(criteria);

            for (AbstractEntity entity : entityList) {
                RelationshipEntity bizDivEntity = (RelationshipEntity) entity;

                bizDivEntity.setActivated(bizDivEntity.getApprovalStatusID() >= privilege.getApprovedStatusID());
                relationshipList.add(bizDivEntity);
                addToSelectItemList(bizDivEntity);
            }

            Collections.sort(relationshipList, new Comparator<RelationshipEntity>() {

                public int compare(RelationshipEntity o1, RelationshipEntity o2) {
                    return (o1.getRelationshipName().compareTo(o2.getRelationshipName()));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public void loadRelationshipList(BusinessActionTrailEntity businessActionTrail) {
        relationshipList = new ArrayList<RelationshipEntity>();
        relationshipItemList = new ArrayList<SelectItem>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        try {
            RelationshipEntity criteria = new RelationshipEntity();
            List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());

            for (AbstractEntity entity : entityList) {
                RelationshipEntity bizDivEntity = (RelationshipEntity) entity;
                bizDivEntity.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());

                relationshipList.add(bizDivEntity);
            }

            Collections.sort(relationshipList, new Comparator<RelationshipEntity>() {

                public int compare(RelationshipEntity o1, RelationshipEntity o2) {
                    return (o1.getRelationshipName().compareTo(o2.getRelationshipName()));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public void addToSelectItemList(RelationshipEntity entity) {
        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
        }

        MenuEntity privilege = privilegeBean.findPrivilegeByName(
                menuManagerBean.getSystemMap().get(MenuManagerBean.RELSHIP_MENU_ITEM));

        if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getRelationshipID());
            item.setLabel(entity.getRelationshipName());
            relationshipItemList.add(item);
        }
    }

    public String addRelationship() {

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (relationship.getRelationshipName() == null || relationship.getRelationshipName().trim().isEmpty()) {
            applicationMessageBean.insertMessage("Specify Relationship Type", MessageType.ERROR_MESSAGE);
            return "";
        }
        if (!relationshipList.contains(relationship)) {
            relationshipList.add(relationship);
        } else {
            int index = relationshipList.indexOf(relationship);
            relationshipList.set(index, relationship);
        }
        relationship = new RelationshipEntity();

        return "";
    }

    public String saveRelationshipList() {
        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}",
                    ApplicationPropertyBean.class, appPropBean);
        }
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        dataServer.beginTransaction();
        try {
            for (RelationshipEntity relation : relationshipList) {
                if (relation.getRelationshipID() == 0) {
                    int bizDivisionID = Integer.parseInt(appPropBean.getValue(
                            FinultimateConstants.RELATIONSHIP_ID,
                            FinultimateConstants.ONE_STR, true));
                    relation.setRelationshipID(bizDivisionID);

                    dataServer.saveData(relation);
                } else {
                    dataServer.updateData(relation);
                }
            }
            applicationMessageBean.insertMessage("Relationship Type has been saved", MessageType.SUCCESS_MESSAGE);
        } catch (SQLException ex) {
            dataServer.rollbackTransaction();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        dataServer.endTransaction();

        return "";
    }

    public String activate() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);
        if (userManagerBean == null) {
            userManagerBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userManagerBean}",
                    UserManagerBean.class, userManagerBean);
        }

        BusinessActionTrailBean businessActionTrailBean = (BusinessActionTrailBean) CommonBean.getBeanFromContext(
                "#{sessionScope.businessActionTrailBean}", BusinessActionTrailBean.class);
        if (businessActionTrailBean == null) {
            businessActionTrailBean = new BusinessActionTrailBean();
            CommonBean.setBeanToContext("#{sessionScope.businessActionTrailBean}",
                    BusinessActionTrailBean.class, businessActionTrailBean);
        }

        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
        }

        MenuEntity privilege = privilegeBean.findPrivilegeByName(
                menuManagerBean.getSystemMap().get(MenuManagerBean.RELSHIP_MENU_ITEM));

        if (relationship.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((relationship.getApprovalStatusID() == 0) || (relationship.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(relationship.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        relationship.setApprovalStatusID(relationship.getApprovalStatusID() + 1);
                        relationship.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(relationship);
                        } catch (SQLException ex) {
                            dataServer.rollbackTransaction();
                            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                            ex.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        dataServer.endTransaction();
                        applicationMessageBean.insertMessage("Operation Activated Successfully!", MessageType.SUCCESS_MESSAGE);
                    } else if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            == creatorAcct.getRole().getApprovalLevelID()) {
                        applicationMessageBean.insertMessage("You cannot approve an operation performed "
                                + "by another user who has the same approval level as you!",
                                MessageType.ERROR_MESSAGE);
                    } else if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            < creatorAcct.getRole().getApprovalLevelID()) {
                        applicationMessageBean.insertMessage("You cannot approve an operation performed "
                                + "by another user who has a higher approval level as you!",
                                MessageType.ERROR_MESSAGE);
                    }
                } else if (relationship.getApprovalStatusID()
                        > userManagerBean.getUserAccount().getRole().getApprovalLevelID()) {
                    applicationMessageBean.insertMessage("This operation has been approved beyond "
                            + "your approval level, your approval is no longer effective!",
                            MessageType.ERROR_MESSAGE);
                }
            } else {
                applicationMessageBean.insertMessage("Access Denied: You do not have approval rights for this operation type!",
                        MessageType.ERROR_MESSAGE);
            }
        } else {
            applicationMessageBean.insertMessage("This operation is already approved. No action performed!",
                    MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String deleteRelationshipID() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (relationshipList.contains(relationship)) {

                if (relationship.getRelationshipID() > 0) {
                    if (relationship.isActivated()) {
                        applicationMessageBean.insertMessage("This Item has been Activated. Deletion of Activated Items is not allowed", MessageType.ERROR_MESSAGE);
                        return "";
                    }
                    FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

                    RelationshipEntity criteria = new RelationshipEntity();
                    criteria.setRelationshipID(relationship.getRelationshipID());
                    dataServer.beginTransaction();
                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();

                    for (SelectItem item : relationshipItemList) {
                        if (Integer.parseInt(item.getValue().toString()) == relationship.getRelationshipID()) {
                            relationshipItemList.remove(item);
                            break;
                        }
                    }
                }

                relationshipList.remove(relationship);
                relationship = new RelationshipEntity();
                applicationMessageBean.insertMessage("Relationship Type has been deleted", MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String findRelationshipTypeByCriteria() {

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        relationshipList.clear();
        boolean hasCriteria = false;

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        RelationshipEntity criteria = new RelationshipEntity();
        criteria.setQueryOperators(new RelationshipFieldOperator());

        if (!relationship.getRelationshipName().trim().isEmpty()) {
            ((RelationshipFieldOperator) criteria.getQueryOperators()).setRelationshipName(QueryOperators.LIKE);
            criteria.setRelationshipName("%" + relationship.getRelationshipName() + "%");
            hasCriteria = true;
        }

        try {
            if (hasCriteria) {
                List<AbstractEntity> entityList = dataServer.findData(criteria);

                MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                        "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
                if (menuManagerBean == null) {
                    menuManagerBean = new MenuManagerBean();
                    CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
                }

                PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                        "#{sessionScope.privilegeBean}", PrivilegeBean.class);
                if (privilegeBean == null) {
                    privilegeBean = new PrivilegeBean();
                    CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
                }

                MenuEntity privilege = privilegeBean.findPrivilegeByName(
                        menuManagerBean.getSystemMap().get(MenuManagerBean.RELSHIP_MENU_ITEM));

                RelationshipEntity businessDivisionEntity = null;
                for (AbstractEntity entity : entityList) {
                    businessDivisionEntity = (RelationshipEntity) entity;

                    businessDivisionEntity.setActivated(businessDivisionEntity.getApprovalStatusID() >= privilege.getApprovedStatusID());

                    relationshipList.add(businessDivisionEntity);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public RelationshipEntity relationshipItemSelected(ValueChangeEvent vce) {
        RelationshipEntity division = null;

        if ((vce != null) && (vce.getNewValue() != null)) {
            int divisionID = Integer.parseInt(vce.getNewValue().toString());
            division = findRelationshipByID(divisionID);
        }

        return division;
    }

    public void relationshipSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
        relationship = relationshipList.get(rowIndex);
    }

    public RelationshipEntity findRelationshipByID(long divisionID) {
        RelationshipEntity entity = null;

        for (RelationshipEntity relateEntity : relationshipList) {
            if (relateEntity.getRelationshipID() == divisionID) {
                entity = relateEntity;
                break;
            }
        }

        return entity;
    }

    public RelationshipEntity findRelationshipByName(final String name) {
        RelationshipEntity entity = null;

        for (RelationshipEntity relateEntity : relationshipList) {
            if (relateEntity.getRelationshipName().equals(name)) {
                entity = relateEntity;
                break;
            }
        }

        return entity;
    }

    public String loadPreviousHistoricalState() {
        BusinessActionTrailBean businessActionTrailBean = (BusinessActionTrailBean) CommonBean.getBeanFromContext(
                "#{sessionScope.businessActionTrailBean}", BusinessActionTrailBean.class);
        if (businessActionTrailBean == null) {
            businessActionTrailBean = new BusinessActionTrailBean();
            CommonBean.setBeanToContext("#{sessionScope.businessActionTrailBean}",
                    BusinessActionTrailBean.class, businessActionTrailBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (relationshipList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    relationshipList.get(0).getActionTrail());
            if (batEntity != null) {
                loadRelationshipList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadRelationshipList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        }

        return "";
    }

    public String loadNextHistoricalState() {
        BusinessActionTrailBean businessActionTrailBean = (BusinessActionTrailBean) CommonBean.getBeanFromContext(
                "#{sessionScope.businessActionTrailBean}", BusinessActionTrailBean.class);
        if (businessActionTrailBean == null) {
            businessActionTrailBean = new BusinessActionTrailBean();
            CommonBean.setBeanToContext("#{sessionScope.businessActionTrailBean}",
                    BusinessActionTrailBean.class, businessActionTrailBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (relationshipList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean
                    .loadNextHistoricalState(relationshipList.get(0).getActionTrail());
            if (batEntity != null) {
                loadRelationshipList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadRelationshipList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        }

        return "";
    }

    public String gotoAuditTrailPage() {
        String outcome = "";

        BusinessActionTrailBean businessActionTrailBean = (BusinessActionTrailBean) CommonBean.getBeanFromContext(
                "#{sessionScope.businessActionTrailBean}", BusinessActionTrailBean.class);
        if (businessActionTrailBean == null) {
            businessActionTrailBean = new BusinessActionTrailBean();
            CommonBean.setBeanToContext("#{sessionScope.businessActionTrailBean}",
                    BusinessActionTrailBean.class, businessActionTrailBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.RELATIONSHIP);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(relationship.getRelationshipID());
        businessActionTrailBean.loadBusinessActionTrailList();

        if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
            businessActionTrailBean.setBusinessActionTrail(
                    businessActionTrailBean.getBusinessActionTrailList().get(
                            businessActionTrailBean.getBusinessActionTrailList().size() - 1));

            loadRelationshipList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            outcome = "relationshipaudittrail.jsf";
        } else {
            applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
        }

        return outcome;
    }

    public String clearCache() {
        relationship = new RelationshipEntity();

        return "";
    }

    public RelationshipEntity getRelationship() {
        return this.relationship;
    }

    public void setRelationship(RelationshipEntity relationship) {
        this.relationship = relationship;
    }

    public List<RelationshipEntity> getRelationshipList() {
        return this.relationshipList;
    }

    public void setRelationshipList(List<RelationshipEntity> relationshipList) {
        this.relationshipList = relationshipList;
    }

    public List<SelectItem> getRelationshipItemList() {
        return this.relationshipItemList;
    }

    public void setRelationshipItemList(List<SelectItem> relationshipItemList) {
        this.relationshipItemList = relationshipItemList;
    }
}
