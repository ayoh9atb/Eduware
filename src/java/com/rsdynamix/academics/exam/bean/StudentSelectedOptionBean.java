package com.rsdynamix.academics.exam.bean;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.academics.exam.entities.ExamPaperEntity;
import com.rsdynamix.academics.exam.entities.ExamQuestionEntity;
import com.rsdynamix.academics.exam.entities.QuestionOptionEntity;
import com.rsdynamix.academics.exam.entities.StudentSelectedOptionEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;

/**
 *
 * @author patushie@codrellica
 */
@SessionScoped
@ManagedBean(name = "studentSelectedOptionBean")
public class StudentSelectedOptionBean {

    private StudentSelectedOptionEntity studentSelectedOption;
    private List<StudentSelectedOptionEntity> studentselectedoptionList;
    //
    private int correctOptionCount;
    private int wrongOptionCount;
    private int totalOptionCount;
    private int totalNumberOfQuestions;
    private BigDecimal percentScore;

    public StudentSelectedOptionBean() {
        studentSelectedOption = new StudentSelectedOptionEntity();
        studentselectedoptionList = new ArrayList<StudentSelectedOptionEntity>();
    }

    public String searchStudentSelectedOption() {
        setCorrectOptionCount(0);
        setWrongOptionCount(0);
        setTotalOptionCount(0);
        setTotalNumberOfQuestions(0);
        setPercentScore(BigDecimal.ZERO);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
        StudentSelectedOptionEntity criteria = new StudentSelectedOptionEntity();

        ExamPaperBean examPaperBean = (ExamPaperBean) CommonBean.getBeanFromContext(
                "#{applicationScope.examPaperBean}", ExamPaperBean.class);
        if (examPaperBean == null) {
            examPaperBean = new ExamPaperBean();
            CommonBean.setBeanToContext("#{applicationScope.examPaperBean}", ExamPaperBean.class, examPaperBean);
        }

        ExamQuestionBean examQuestionBean = (ExamQuestionBean) CommonBean.getBeanFromContext(
                "#{sessionScope.examQuestionBean}", ExamQuestionBean.class);
        if (examQuestionBean == null) {
            examQuestionBean = new ExamQuestionBean();
            CommonBean.setBeanToContext("#{sessionScope.examQuestionBean}", ExamQuestionBean.class, examQuestionBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if ((studentSelectedOption.getExamPaperID() > 0)
                    && (!studentSelectedOption.getStudentNumber().trim().equals(""))) {
                criteria.setStudentNumber(studentSelectedOption.getStudentNumber());
                criteria.setExamPaperID(studentSelectedOption.getExamPaperID());
                ExamPaperEntity examPaper = examPaperBean.findExamPaperByID(studentSelectedOption.getExamPaperID());
                if (examPaper != null) {
                    setTotalNumberOfQuestions(examPaper.getNumberOfQuestion());
                }

                List<AbstractEntity> abstractOptionList = dataServer.findData(criteria);
                for (AbstractEntity entity : abstractOptionList) {
                    StudentSelectedOptionEntity submission = (StudentSelectedOptionEntity) entity;
                    submission.initializeTransientFields();

                    ExamQuestionEntity question = examQuestionBean.searchForQuestionByID(submission.getQuestionID());
                    if (question != null) {
                        submission.setQuestionNumber(question.getQuestionNumber());
                        submission.setQuestionStatement(question.getQuestionStatement());
                    }

                    QuestionOptionEntity submittedOption = examQuestionBean.searchForQuestionOptionByID(submission.getSelectedOptionID());
                    if (submittedOption != null) {
                        submission.setOptionLetter(submittedOption.getOptionLetter());
                        submission.setOptionText(submittedOption.getOptionText());
                        submission.setAnswer(submission.getQuestionOptionID() == submittedOption.getQuestionOptionID());
                        if (submission.isAnswer()) {
                            submission.setNumberOfPoints(1);
                            setCorrectOptionCount(getCorrectOptionCount() + 1);
                        } else {
                            setWrongOptionCount(getWrongOptionCount() + 1);
                        }
                        setTotalOptionCount(getTotalOptionCount() + 1);
                    }
                }
                setPercentScore(BigDecimal.valueOf((getCorrectOptionCount() * 100) / getTotalNumberOfQuestions()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public List<StudentSelectedOptionEntity> getStudentSelectedOptionList() {
        return this.getStudentselectedoptionList();
    }

    public void setStudentSelectedOptionList(List<StudentSelectedOptionEntity> studentselectedoptionList) {
        this.setStudentselectedoptionList(studentselectedoptionList);
    }

    /**
     * @return the studentSelectedOption
     */
    public StudentSelectedOptionEntity getStudentSelectedOption() {
        return studentSelectedOption;
    }

    /**
     * @param studentSelectedOption the studentSelectedOption to set
     */
    public void setStudentSelectedOption(StudentSelectedOptionEntity studentSelectedOption) {
        this.studentSelectedOption = studentSelectedOption;
    }

    /**
     * @return the studentselectedoptionList
     */
    public List<StudentSelectedOptionEntity> getStudentselectedoptionList() {
        return studentselectedoptionList;
    }

    /**
     * @param studentselectedoptionList the studentselectedoptionList to set
     */
    public void setStudentselectedoptionList(List<StudentSelectedOptionEntity> studentselectedoptionList) {
        this.studentselectedoptionList = studentselectedoptionList;
    }

    /**
     * @return the correctOptionCount
     */
    public int getCorrectOptionCount() {
        return correctOptionCount;
    }

    /**
     * @param correctOptionCount the correctOptionCount to set
     */
    public void setCorrectOptionCount(int correctOptionCount) {
        this.correctOptionCount = correctOptionCount;
    }

    /**
     * @return the wrongOptionCount
     */
    public int getWrongOptionCount() {
        return wrongOptionCount;
    }

    /**
     * @param wrongOptionCount the wrongOptionCount to set
     */
    public void setWrongOptionCount(int wrongOptionCount) {
        this.wrongOptionCount = wrongOptionCount;
    }

    /**
     * @return the totalOptionCount
     */
    public int getTotalOptionCount() {
        return totalOptionCount;
    }

    /**
     * @param totalOptionCount the totalOptionCount to set
     */
    public void setTotalOptionCount(int totalOptionCount) {
        this.totalOptionCount = totalOptionCount;
    }

    /**
     * @return the totalNumberOfQuestions
     */
    public int getTotalNumberOfQuestions() {
        return totalNumberOfQuestions;
    }

    /**
     * @param totalNumberOfQuestions the totalNumberOfQuestions to set
     */
    public void setTotalNumberOfQuestions(int totalNumberOfQuestions) {
        this.totalNumberOfQuestions = totalNumberOfQuestions;
    }

    /**
     * @return the percentScore
     */
    public BigDecimal getPercentScore() {
        return percentScore;
    }

    /**
     * @param percentScore the percentScore to set
     */
    public void setPercentScore(BigDecimal percentScore) {
        this.percentScore = percentScore;
    }
}
