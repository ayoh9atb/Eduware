/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.dynamo.common.setup.beans;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.codrellica.projects.commons.DateUtil;
import com.rsdynamix.projects.commons.entities.BusinessDocumentEntity;
import com.rsdynamix.projects.commons.entities.DocumentImageEntity;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.tns.util.Constants;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;

/**
 *
 * @author p-aniah
 */
@SessionScoped
@ManagedBean(name = "documentManagerBean")
public class DocumentManagerBean implements Serializable {

    private static final String BIZ_DOC_ID_KEY = "biz_doc_id";
    /**/
    private static final String BIZ_DOC_ID_VALUE = "1";
    /**/
    private BusinessDocumentEntity document;
    private List<BusinessDocumentEntity> documentList;
    private UploadedFile uploadedFile;

    public DocumentManagerBean() {
        document = new BusinessDocumentEntity();
        documentList = new ArrayList<BusinessDocumentEntity>();

        loadDocumentInfo("");
    }

    public StreamedContent getImageContent(String imageFileName) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            BufferedImage img = ImageIO.read(new FileInputStream(imageFileName));

            int w = img.getWidth(null);
            int h = img.getHeight(null);

            // image is scaled two times at run time
            int scale = 2;

            BufferedImage bi = new BufferedImage(
                    (w * scale), (h * scale), BufferedImage.TYPE_INT_ARGB);

            Graphics g = bi.getGraphics();

            g.drawImage(img, 10, 10, (w * scale), (h * scale), null);

            ImageIO.write(bi, "png", bos);

            return new DefaultStreamedContent(
                    new ByteArrayInputStream(bos.toByteArray()), "image/png");
        }
    }

    public void loadDocumentInfo(String punNumber) {
        documentList = new ArrayList<BusinessDocumentEntity>();

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        BusinessDocumentEntity criteria = new BusinessDocumentEntity();
        criteria.setUniqueIDNumber(punNumber);

        FileInputStream fis = null;

        try {
            List<AbstractEntity> entityList = dataServer.findData(criteria);
            for (AbstractEntity entity : entityList) {
                BusinessDocumentEntity docEntity = (BusinessDocumentEntity) entity;

                File file = new File(FinultimateConstants.DOC_IMAGE_FOLDER
                        + File.separator + docEntity.getFileName());

                docEntity.setDocumentImg(new byte[(int) file.length()]);
                fis = new FileInputStream(file);
                fis.read(docEntity.getDocumentImg());

                documentList.add(docEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public BusinessDocumentEntity loadDocumentByPunNo(String punNumber) {
        documentList = new ArrayList<BusinessDocumentEntity>();

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        BusinessDocumentEntity criteria = new BusinessDocumentEntity();
        criteria.setUniqueIDNumber(punNumber);

        BusinessDocumentEntity docEntity = null;

        try {
            List<AbstractEntity> entityList = dataServer.findData(criteria);
            if (entityList.size() > 0) {
                docEntity = (BusinessDocumentEntity) entityList.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return docEntity;
    }

    public void documentSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
        document = getDocumentList().get(rowIndex);
        document.setSelected(Boolean.parseBoolean(vce.getNewValue().toString()));
        if (document.isSelected()) {
            processRequestToServlet(document);
            deselectOtherReservees(document);
        } else {
            document = new BusinessDocumentEntity();
        }
    }

    private void deselectOtherReservees(BusinessDocumentEntity bizDoc) {
        List<BusinessDocumentEntity> bizDocList = new ArrayList<BusinessDocumentEntity>();

        for (BusinessDocumentEntity bDoc : getDocumentList()) {
            if (bizDoc.getDocumentID() == bDoc.getDocumentID()) {
                bizDocList.add(bizDoc);
            } else {
                bDoc.setSelected(false);
                bizDocList.add(bDoc);
            }
        }

        setDocumentList(bizDocList);
    }

    public void documentTitleSpecified(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            document.setDocumentTitle(vce.getNewValue().toString());
        }
    }

    public void uploadHandler(FileUploadEvent event) {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            document.setDocumentImg(new byte[(int) event.getFile().getSize()]);
            event.getFile().getInputstream().read(document.getDocumentImg());

            document.setFileName(event.getFile().getFileName());

            document.setDateOfCapture(new Date());
            document.setDocumentMimeType(event.getFile().getContentType());

            addDocument();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public BusinessDocumentEntity uploadOneDocument(FileUploadEvent event) {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        BusinessDocumentEntity document = new BusinessDocumentEntity();

        try {
            document.setDocumentImg(new byte[(int) event.getFile().getSize()]);
            event.getFile().getInputstream().read(document.getDocumentImg());

            document.setFileName(event.getFile().getFileName());

            document.setDateOfCapture(new Date());
            document.setDocumentMimeType(event.getFile().getContentType());
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return document;
    }

    public void policyCorrespondenceUploadHandler(FileUploadEvent event) {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            String referenceNumber = "";//TODO: get from doc owner entity
            if (referenceNumber == null || referenceNumber.trim().isEmpty()) {
                throw new Exception("Reference Number cannot be empty");
            }
            document.setDocumentImg(new byte[(int) event.getFile().getSize()]);
            event.getFile().getInputstream().read(document.getDocumentImg());

            document.setFileName(event.getFile().getFileName());

            document.setDateOfCapture(new Date());
            document.setDocumentMimeType(event.getFile().getContentType());

            addDocument(referenceNumber);
            applicationMessageBean.insertMessage("Document has been saved", MessageType.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            applicationMessageBean.insertMessage("An error occured while uploading the document. Error:" + ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() + "/underwriting/createpolicy.jsf");
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void documentTitleEnteredEvent(ValueChangeEvent vce) {
        document.setDocumentTitle(vce.getNewValue().toString().trim());
    }

    public String addDocument(String referenceNumber) throws Exception {
        String fileName = "";

        if (document.getDocumentTitle() == null || document.getDocumentTitle().trim().isEmpty()) {
            throw new Exception("Specify a Title for the document");
        }

        if (!documentList.contains(document)) {
            fileName = saveDocument(document, referenceNumber);

            documentList.add(document);
            document = new BusinessDocumentEntity();
        }

        return fileName;
    }

    public String addDocument(
            BusinessDocumentEntity document, String referenceNumber) throws Exception {
        return saveDocument(document, referenceNumber);
    }

    public void addDocument() throws Exception {
        if (document.getDocumentTitle() == null || document.getDocumentTitle().trim().isEmpty()) {
            throw new Exception("Specify a Title for the document");
        }

        if (!documentList.contains(document)) {
            documentList.add(document);
        } else {
            int index = documentList.indexOf(document);
            documentList.set(index, document);
        }
        document = new BusinessDocumentEntity();
    }

    public String removeDocument() {
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
            if (getDocumentList().contains(getDocument())) {
                if (getDocument().getDocumentID() > 0) {
                    if (getDocument().isActivated()) {
                        applicationMessageBean.insertMessage(
                                "This Item has been Activated. Deletion of Activated Items is not allowed",
                                MessageType.ERROR_MESSAGE);
                        return "";
                    }
                    dataServer.beginTransaction();
                    dataServer.deleteData(getDocument());
                    dataServer.endTransaction();
                }
                getDocumentList().remove(getDocument());
                applicationMessageBean.insertMessage("Document has been deleted", MessageType.INFORMATION_MESSAGE);
                document = new BusinessDocumentEntity();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String saveDocument() {
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
            for (BusinessDocumentEntity document : documentList) {
                if (document.getUniqueIDNumber().trim().length() == 0) {
                    saveDocument(document, "");
                } else {
                    for (BusinessDocumentEntity bizDoc : getDocumentList()) {
                        if (document.getDocumentID() == 0) {
                            long docID = Long.parseLong(appPropBean.getValue(BIZ_DOC_ID_KEY,
                                    BIZ_DOC_ID_VALUE, true));
                            document.setDocumentID(docID);

                            dataServer.saveData(document);
                        } else {
                            dataServer.updateData(document);
                        }
                    }
                }
            }

            dataServer.endTransaction();

            applicationMessageBean.insertMessage(
                    "Entity list saved successfully.", MessageType.SUCCESS_MESSAGE);
        } catch (SQLException ex) {
            dataServer.rollbackTransaction();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return "";
    }

    public String saveDocument(BusinessDocumentEntity document, String referenceNumber) throws SQLException, Exception {
        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}",
                    ApplicationPropertyBean.class, appPropBean);
        }

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
        FileOutputStream fos = null;

        try {
            dataServer.beginTransaction();
            if (document.getDocumentID() == 0) {
                long docID = Long.parseLong(appPropBean.getValue(BIZ_DOC_ID_KEY,
                        BIZ_DOC_ID_VALUE, true));

                document.setDocumentID(docID);

                if (referenceNumber.trim().length() > 0) {
                    document.setUniqueIDNumber(referenceNumber);
                } else {
                    referenceNumber = generateDocUniqueNumber(docID, appPropBean);
                    document.setUniqueIDNumber(referenceNumber);
                }

                int fileIndex = 0;

                String fileName = "";

                if (referenceNumber.indexOf("/") > -1) {
                    fileName = referenceNumber.replace("/", "_");
                } else {
                    fileName = referenceNumber;
                }

                String docMimeType = "";
                if ((document.getDocumentMimeType().trim().length() > 0)
                        && (document.getDocumentMimeType().indexOf("/") > -1)) {
                    docMimeType = document.getDocumentMimeType().split("/")[1];
                } else {
                    docMimeType = document.getDocumentMimeType();
                }

                File file = new File(FinultimateConstants.DOC_IMAGE_FOLDER
                        + File.separator + fileName + "_" + String.valueOf(fileIndex++)
                        + "." + docMimeType);
                boolean fileExists = file.exists();

                while (fileExists) {
                    file = new File(FinultimateConstants.DOC_IMAGE_FOLDER + File.separator
                            + fileName + "_" + String.valueOf(fileIndex++) + "." + docMimeType);

                    fileExists = file.exists();
                }

                document.setFileName(file.getName());
                file.createNewFile();
                fos = new FileOutputStream(file);
                fos.write(document.getDocumentImg());

                dataServer.saveData(document);
            } else {
                dataServer.updateData(document);
            }
            dataServer.endTransaction();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }

        return document.getFileName();
    }

    public String saveDocumentWithGeneratedRef(BusinessDocumentEntity document) throws SQLException, Exception {
        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}",
                    ApplicationPropertyBean.class, appPropBean);
        }

        String referenceNumber = "";

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
        FileOutputStream fos = null;

        try {
            dataServer.beginTransaction();
            if (document.getDocumentID() == 0) {
                long docID = Long.parseLong(appPropBean
                        .getValue(BIZ_DOC_ID_KEY, BIZ_DOC_ID_VALUE, true));

                document.setDocumentID(docID);

                referenceNumber = generateDocUniqueNumber(docID, appPropBean);
                document.setUniqueIDNumber(referenceNumber);

                int fileIndex = 0;

                String fileName = "";

                if (referenceNumber.indexOf("/") > -1) {
                    fileName = referenceNumber.replace("/", "_");
                } else {
                    fileName = referenceNumber;
                }

                String docMimeType = "";
                if ((document.getDocumentMimeType().trim().length() > 0)
                        && (document.getDocumentMimeType().indexOf("/") > -1)) {
                    docMimeType = document.getDocumentMimeType().split("/")[1];
                } else {
                    docMimeType = document.getDocumentMimeType();
                }

                File file = new File(FinultimateConstants.DOC_IMAGE_FOLDER
                        + File.separator + fileName + "_" + String.valueOf(fileIndex++)
                        + "." + docMimeType);
                boolean fileExists = file.exists();

                while (fileExists) {
                    file = new File(FinultimateConstants.DOC_IMAGE_FOLDER + File.separator
                            + fileName + "_" + String.valueOf(fileIndex++) + "." + docMimeType);

                    fileExists = file.exists();
                }

                document.setFileName(file.getName());
                file.createNewFile();
                fos = new FileOutputStream(file);
                fos.write(document.getDocumentImg());

                dataServer.saveData(document);
            } else {
                dataServer.updateData(document);
            }
            dataServer.endTransaction();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }

        return referenceNumber;
    }

    public String generateDocUniqueNumber(
            long invNoSerial, ApplicationPropertyBean appPropBean) throws SQLException, Exception {
        long invNoCharLen = Long.parseLong(appPropBean.getValue(FinultimateConstants.DOCUMENT_NO_SERIAL_LEN,
                FinultimateConstants.DOCUMENT_NO_SERIAL_LEN_VALUE, false));

        String invNoPrefix = appPropBean.getValue(FinultimateConstants.DOCUMENT_NO_PREFIX,
                FinultimateConstants.DOCUMENT_NO_PREFIX_VALUE, false);

        String invNoStr = String.valueOf(invNoSerial);
        for (int i = 1; i <= (invNoCharLen - String.valueOf(invNoSerial).length()); i++) {
            invNoStr = "0" + invNoStr;
        }

        invNoStr += String.valueOf(new Date().getTime());

        return invNoPrefix + invNoStr;
    }

    public void processRequestToServlet(BusinessDocumentEntity bizDoc) {
        FacesContext context = FacesContext.getCurrentInstance();

        HttpSession servletSession = (HttpSession) context.getExternalContext().getSession(false);
        servletSession.setAttribute(String.valueOf(document.getDocumentID()), bizDoc);
    }

    public String clearCache() {
        document = new BusinessDocumentEntity();
        documentList = new ArrayList<BusinessDocumentEntity>();

        return "";
    }

    /**
     * @return the uploadedFile
     */
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    /**
     * @param uploadedFile the uploadedFile to set
     */
    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    /**
     * @return the document
     */
    public BusinessDocumentEntity getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(BusinessDocumentEntity document) {
        this.document = document;
    }

    /**
     * @return the documentList
     */
    public List<BusinessDocumentEntity> getDocumentList() {
        return documentList;
    }

    /**
     * @param documentList the documentList to set
     */
    public void setDocumentList(List<BusinessDocumentEntity> documentList) {
        this.documentList = documentList;
    }

}
