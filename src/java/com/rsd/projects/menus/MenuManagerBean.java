package com.rsd.projects.menus;

import com.rsdynamix.dynamo.common.entities.UserAdminSwitchType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.hrms.kpi.beans.EmployeeAppraisalBean;
import com.rsdynamix.hrms.kpi.entities.AppraisalScoreType;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.bean.SessionDataReinitBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.projects.web.commons.bean.LinkEntry;
import com.rsdynamix.projects.web.commons.bean.LinkStack;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author patushie
 */
@SessionScoped
@ManagedBean(name = "menuManagerBean")
public class MenuManagerBean {

    public static final String SETUP_MENU_ITEM = "SETUP_MENU_ITEM";
    //
    public static final String COMPANY_ID_MENU_ITEM = "COMPANY_ID_MENU_ITEM";
    //
    public static final String ORG_SETUP_MENU_ITEM = "ORG_SETUP_MENU_ITEM";
    //
    public static final String BRANCHES_MENU_ITEM = "BRANCHES_MENU_ITEM";
    //
    public static final String DIVISION_MENU_ITEM = "DIVISION_MENU_ITEM";
    //
    public static final String DEPT_MENU_ITEM = "DEPT_MENU_ITEM";
    //
    public static final String RELSHIP_MENU_ITEM = "RELSHIP_MENU_ITEM";
    //
    public static final String TITLES_MENU_ITEM = "TITLES_MENU_ITEM";
    //
    public static final String LOCATION_MENU_ITEM = "LOCATION_MENU_ITEM";
    //
    public static final String COUNTRIES_MENU_ITEM = "COUNTRIES_MENU_ITEM";
    //
    public static final String STATES_MENU_ITEM = "STATES_MENU_ITEM";
    //
    public static final String CITIES_MENU_ITEM = "CITIES_MENU_ITEM";
    //
    public static final String CURRENCIES_MENU_ITEM = "CURRENCIES_MENU_ITEM";
    //
    public static final String SERV_PROVIDRS_MENU_ITEM = "SERV_PROVIDRS_MENU_ITEM";
    //
    public static final String BANKS_MENU_ITEM = "BANKS_MENU_ITEM";
    //
    public static final String PFAS_MENU_ITEM = "PFAS_MENU_ITEM";
    //
    public static final String TRUSTEES_MENU_ITEM = "TRUSTEES_MENU_ITEM";
    //
    public static final String EMPLOYERS_MENU_ITEM = "EMPLOYERS_MENU_ITEM";
    //
    public static final String UNDERWRITER_MENU_ITEM = "UNDERWRITER_MENU_ITEM";
    //
    public static final String MESSAGING_MENU_ITEM = "MESSAGING_MENU_ITEM";
    //
    public static final String ACCTING_MENU_ITEM = "ACCTING_MENU_ITEM";
    //
    public static final String FINANCE_SETP_MENU_ITEM = "FINANCE_SETP_MENU_ITEM";
    //
    public static final String ACCT_SETP_MENU_ITEM = "ACCT_SETP_MENU_ITEM";
    //
    public static final String TRAN_SRC_MENU_ITEM = "TRAN_SRC_MENU_ITEM";
    //
    public static final String ACCT_SECT_MENU_ITEM = "ACCT_SECT_MENU_ITEM";
    //
    public static final String ACCT_GRP_MENU_ITEM = "ACCT_GRP_MENU_ITEM";
    //
    public static final String EXCHANGE_RATE_MENU_ITEM = "EXCHANGE_RATE_MENU_ITEM";
    //
    public static final String ACCT_CHRT_MENU_ITEM = "ACCT_CHRT_MENU_ITEM";
    //
    public static final String SUBLEDGR_ACCT_CHRT_MENU_ITEM = "SUBLEDGR_ACCT_CHRT_MENU_ITEM";
    //
    public static final String PERIODS_MENU_ITEM = "PERIODS_MENU_ITEM";
    //
    public static final String FIN_YR_MENU_ITEM = "FIN_YR_MENU_ITEM";
    //
    public static final String BANK_STATEMENT_CREATE_MENU_ITEM = "BANK_STATEMENT_CREATE_MENU_ITEM";
    //
    public static final String BANK_STATEMENT_VIEW_MENU_ITEM = "BANK_STATEMENT_VIEW_MENU_ITEM";
    //
    public static final String ACCT_PRIOD_MENU_ITEM = "ACCT_PRIOD_MENU_ITEM";
    //
    public static final String TRN_TYP_MENU_ITEM = "TRN_TYP_MENU_ITEM";
    //
    public static final String BIZ_TYP_MP_MENU_ITEM = "BIZ_TYP_MP_MENU_ITEM";
    //
    public static final String BUDGET_MENU_ITEM = "BUDGET_MENU_ITEM";
    //
    public static final String VIEW_ACCT_BUDGET_MENU_ITEM = "VIEW_ACCT_BUDGET_MENU_ITEM";
    //
    public static final String CR8_ACCT_BUDGET_MENU_ITEM = "CR8_ACCT_BUDGET_MENU_ITEM";
    //
    public static final String POSTING_MENU_ITEM = "POSTING_MENU_ITEM";
    //
    public static final String JNL_ENTRY_MENU_ITEM = "JNL_ENTRY_MENU_ITEM";
    //
    public static final String INQ_N_REPRT_MENU_ITEM = "INQ_N_REPRT_MENU_ITEM";
    //
    public static final String GL_TRN_MENU_ITEM = "GL_TRN_MENU_ITEM";
    //
    public static final String SUBLDGR_GL_TRN_MENU_ITEM = "SUBLDGR_GL_TRN_MENU_ITEM";
    //
    public static final String CASH_BOOK_GL_TRN_MENU_ITEM = "CASH_BOOK_GL_TRN_MENU_ITEM";
    //
    public static final String BANK_RECONCILIATION_MENU_ITEM = "BANK_RECONCILIATION_MENU_ITEM";
    //
    public static final String BAL_SHT_MENU_ITEM = "BAL_SHT_MENU_ITEM";
    //
    public static final String TRIAL_BAL_MENU_ITEM = "TRIAL_BAL_MENU_ITEM";
    //
    public static final String P_N_L_MENU_ITEM = "P_N_L_MENU_ITEM";
    //
    public static final String INCOME_STMT_MENU_ITEM = "INCOME_STMT_MENU_ITEM";
    //
    public static final String SUSPENSE_REPRT_MENU_ITEM = "SUSPENSE_REPRT_MENU_ITEM";
    //
    public static final String FOREX_REVALUATION_MENU_ITEM = "FOREX_REVALUATION_MENU_ITEM";
    //
    public static final String PERFORM_FX_REVALUATION_MENU_ITEM = "PERFORM_FX_REVALUATION_MENU_ITEM";
    //
    public static final String VIEW_FX_REVALUATION_MENU_ITEM = "VIEW_FX_REVALUATION_MENU_ITEM";
    //
    public static final String CLIENTS_MENU_ITEM = "CLIENTS_MENU_ITEM";
    //
    public static final String ELEMT_TYP_MENU_ITEM = "ELEMT_TYP_MENU_ITEM";
    //
    public static final String IDENT_TYP_MENU_ITEM = "IDENT_TYP_MENU_ITEM";
    //
    public static final String ADDR_PRF_TYP_MENU_ITEM = "ADDR_PRF_TYP_MENU_ITEM";
    //
    public static final String CLIENT_SETP_MENU_ITEM = "CLIENT_SETP_MENU_ITEM";
    //
    public static final String CR8_CLIENT_MENU_ITEM = "CR8_CLIENT_MENU_ITEM";
    //
    public static final String VIEW_CLIENT_MENU_ITEM = "VIEW_CLIENT_MENU_ITEM";
    //
    public static final String AGENTS_MENU_ITEM = "AGENTS_MENU_ITEM";
    //
    public static final String AGENT_SETP_MENU_ITEM = "AGENT_SETP_MENU_ITEM";
    //
    public static final String AGENT_TYP_MENU_ITEM = "AGENT_TYP_MENU_ITEM";
    //
    public static final String CR8_AGENT_MENU_ITEM = "CR8_AGENT_MENU_ITEM";
    //
    public static final String AGENT_MGT_MENU_ITEM = "AGENT_MGT_MENU_ITEM";
    //
    public static final String AGENT_COMMSN_MENU_ITEM = "AGENT_COMMSN_MENU_ITEM";
    //
    public static final String AGENT_PERF_MENU_ITEM = "AGENT_PERF_MENU_ITEM";
    //
    public static final String PRODT_MENU_ITEM = "PRODT_MENU_ITEM";
    //
    public static final String INSUR_SETP_MENU_ITEM = "INSUR_SETP_MENU_ITEM";
    //
    public static final String PRODT_TYP_SETP_MENU_ITEM = "PRODT_TYP_SETP_MENU_ITEM";
    //
    public static final String TAX_SETP_MENU_ITEM = "TAX_SETP_MENU_ITEM";
    //
    public static final String TAX_AUTH_SETP_MENU_ITEM = "TAX_AUTH_SETP_MENU_ITEM";
    //
    public static final String DISCNT_SETP_MENU_ITEM = "DISCNT_SETP_MENU_ITEM";
    //
    public static final String UWRITING_MENU_ITEM = "UWRITING_MENU_ITEM";
    //
    public static final String PRICING_MENU_ITEM = "PRICING_MENU_ITEM";
    //
    public static final String PRICING_SETP_MENU_ITEM = "PRICING_SETP_MENU_ITEM";
    //
    public static final String RATES_MENU_ITEM = "RATES_MENU_ITEM";
    //
    public static final String ADD_TRFF_RATES_MENU_ITEM = "ADD_TRFF_RATES_MENU_ITEM";
    //
    public static final String MNTN_TRFF_RATES_MENU_ITEM = "MNTN_TRFF_RATES_MENU_ITEM";
    //
    public static final String GENDER_MORT_RATES_MENU_ITEM = "GENDER_MORT_RATES_MENU_ITEM";
    //
    public static final String QK_QUOTE_MENU_ITEM = "QK_QUOTE_MENU_ITEM";
    //
    public static final String CR8_QUOTE_MENU_ITEM = "CR8_QUOTE_MENU_ITEM";
    //
    public static final String POLICIES_MENU_ITEM = "POLICIES_MENU_ITEM";
    //
    public static final String CR8_SGL_PROP_MENU_ITEM = "CR8_SGL_PROP_MENU_ITEM";
    //
    public static final String EDIT_PROP_MENU_ITEM = "EDIT_PROP_MENU_ITEM";
    //
    public static final String UPLOAD_PROP_MENU_ITEM = "UPLOAD_PROP_MENU_ITEM";
    //
    public static final String MNTN_PROP_MENU_ITEM = "MNTN_PROP_MENU_ITEM";
    //
    public static final String MNTN_PROP_ACTIVATE_MENU_ITEM = "MNTN_PROP_ACTIVATE_MENU_ITEM";
    //
    public static final String MNTN_PROP_COLLECTNS_MENU_ITEM = "MNTN_PROP_COLLECTNS_MENU_ITEM";
    //
    public static final String MNTN_PROP_PAYOUT_MENU_ITEM = "MNTN_PROP_PAYOUT_MENU_ITEM";
    //
    public static final String MNTN_PROP_DOCUMENT_MENU_ITEM = "MNTN_PROP_DOCUMENT_MENU_ITEM";
    //
    public static final String MNTN_PROP_ENDORSE_MENU_ITEM = "MNTN_PROP_ENDORSE_MENU_ITEM";
    //
    public static final String UW_RPRT_MENU_ITEM = "UW_RPRT_MENU_ITEM";
    //
    public static final String DAILY_PDTN_MENU_ITEM = "DAILY_PDTN_MENU_ITEM";
    //
    public static final String CLMS_N_SURNDR_MENU_ITEM = "CLMS_N_SURNDR_MENU_ITEM";
    //
    public static final String CLMS_MENU_ITEM = "CLMS_MENU_ITEM";
    //
    public static final String REG_CLM_MENU_ITEM = "REG_CLM_MENU_ITEM";
    //
    public static final String CLM_MNTN_MENU_ITEM = "CLM_MNTN_MENU_ITEM";
    //
    public static final String SURNDRS_MENU_ITEM = "SURNDRS_MENU_ITEM";
    //
    public static final String REG_SURNDR_MENU_ITEM = "REG_SURNDR_MENU_ITEM";
    //
    public static final String SURNDR_MNTN_MENU_ITEM = "SURNDR_MNTN_MENU_ITEM";
    //
    public static final String FINANCIALS_COLLECTION_MENU_ITEM = "FINANCIALS_COLLECTION_MENU_ITEM";
    //
    public static final String FINANCIALS_PAYMENT_MENU_ITEM = "FINANCIALS_PAYMENT_MENU_ITEM";
    //
    public static final String PAY_COLL_MENU_ITEM = "PAY_COLL_MENU_ITEM";
    //
    public static final String CR8_RCPT_MENU_ITEM = "CR8_RCPT_MENU_ITEM";
    //
    public static final String SRCH_RCPT_MENU_ITEM = "SRCH_RCPT_MENU_ITEM";
    //
    public static final String RCPT_DAILY_PRD_MENU_ITEM = "RCPT_DAILY_PRD_MENU_ITEM";
    //
    public static final String COLL_SETP_MENU_ITEM = "COLL_SETP_MENU_ITEM";
    //
    public static final String BIZ_MODL_SETP_MENU_ITEM = "BIZ_MODL_SETP_MENU_ITEM";
    //
    public static final String SALES_PROMOTION_MENU_ITEM = "SALES_PROMOTION_MENU_ITEM";
    //
    public static final String COLL_MOD_SETP_MENU_ITEM = "COLL_MOD_SETP_MENU_ITEM";
    //
    public static final String COLL_SRC_SETP_MENU_ITEM = "COLL_SRC_SETP_MENU_ITEM";
    //
    public static final String APPROVLS_MENU_ITEM = "APPROVLS_MENU_ITEM";
    //
    public static final String APPROVLS_STP_MENU_ITEM = "APPROVLS_STP_MENU_ITEM";
    //
    public static final String APPROVLS_LIM_MENU_ITEM = "APPROVLS_LIM_MENU_ITEM";
    //
    public static final String PAY_REQ_MENU_ITEM = "PAY_REQ_MENU_ITEM";
    //
    public static final String PAY_MONTR_MENU_ITEM = "PAY_MONTR_MENU_ITEM";
    //
    public static final String VIEW_PAYMT_MENU_ITEM = "VIEW_PAYMT_MENU_ITEM";
    //
    public static final String SECURITY_MENU_ITEM = "SECURITY_MENU_ITEM";
    //
    public static final String NEW_ENTITIES_MENU_ITEM = "NEW_ENTITIES_MENU_ITEM";
    //
    public static final String NEW_USR_ACCT_MENU_ITEM = "NEW_USR_ACCT_MENU_ITEM";
    //
    public static final String NEW_USR_ROLS_MENU_ITEM = "NEW_USR_ROLS_MENU_ITEM";
    //
    public static final String NEW_PRIVLG_MENU_ITEM = "NEW_PRIVLG_MENU_ITEM";
    //
    public static final String NEW_SUBSYS_MENU_ITEM = "NEW_SUBSYS_MENU_ITEM";
    //
    public static final String NEW_APPLIC_SYS_MENU_ITEM = "NEW_APPLIC_SYS_MENU_ITEM";
    //
    public static final String NEW_SYS_ROLS_MENU_ITEM = "NEW_SYS_ROLS_MENU_ITEM";
    //
    public static final String AUDIT_TRAIL_ADM_MENU_ITEM = "AUDIT_TRAIL_ADM_MENU_ITEM";
    //
    public static final String AUDIT_TRAIL_MENU_ITEM = "AUDIT_TRAIL_MENU_ITEM";
    //
    public static final String REPORTS_MENU_ITEM = "REPORTS_MENU_ITEM";
    //
    public static final String RPTS_GRPING_MENU_ITEM = "RPTS_GRPING_MENU_ITEM";
    //
    public static final String RPTS_CATGRY_MENU_ITEM = "RPTS_CATGRY_MENU_ITEM";
    //
    public static final String RPTS_N_CHRTS_MENU_ITEM = "RPTS_N_CHRTS_MENU_ITEM";
    //
    public static final String SETP_RPTS_MENU_ITEM = "SETP_RPTS_MENU_ITEM";
    //
    public static final String SETP_CHRTS_MENU_ITEM = "SETP_CHRTS_MENU_ITEM";
    //
    public static final String RPTS_AUTMTN_MENU_ITEM = "RPTS_AUTMTN_MENU_ITEM";
    //
    public static final String RPTS_TRGRS_MENU_ITEM = "RPTS_TRGRS_MENU_ITEM";
    //
    public static final String MSSG_TRGRS_MENU_ITEM = "MSSG_TRGRS_MENU_ITEM";
    //
    public static final String RPTS_ALRT_SCHD_MENU_ITEM = "RPTS_ALRT_SCHD_MENU_ITEM";
    //
    public static final String REFERRER_MENU_ITEM = "REFERRER_MENU_ITEM";
    //
    public static final String CR8_REFERRER_TYPE_MENU_ITEM = "CR8_REFERRER_TYPE_MENU_ITEM";
    //
    public static final String ESCALATION_RT_SETP_MENU_ITEM = "ESCALATION_RT_SETP_MENU_ITEM";
    //
    public static final String MANAGE_USR_ACCT_MENU_ITEM = "MANAGE_USR_ACCT_MENU_ITEM";
    //
    public static final String VIEW_LOGGEDIN_USERS_MENU_ITEM = "VIEW_LOGGEDIN_USERS_MENU_ITEM";
    //
    public static final String PAYOUT_CHANNEL_MENU_ITEM = "PAYOUT_CHANNEL_MENU_ITEM";
    //
    public static final String VIEW_PAYOUT_TRANS_MENU_ITEM = "VIEW_PAYOUT_TRANS_MENU_ITEM";
    //
    public static final String INVESTMENT_MENU_ITEM = "INVESTMENT_MENU_ITEM";
    //
    public static final String INVESTMT_STUP_MENU_ITEM = "INVESTMT_STUP_MENU_ITEM";
    //
    public static final String INVESTMT_STUP_STOCKS_MENU_ITEM = "INVESTMT_STUP_STOCKS_MENU_ITEM";
    //
    public static final String INVESTMT_STUP_BNK_ACCT_MENU_ITEM = "INVESTMT_STUP_BNK_ACCT_MENU_ITEM";
    //
    public static final String INVESTMT_STUP_INSTIT_MENU_ITEM = "INVESTMT_STUP_INSTIT_MENU_ITEM";
    //
    public static final String INVESTMT_STUP_PLAYR_MENU_ITEM = "INVESTMT_STUP_PLAYR_MENU_ITEM";
    //
    public static final String INVEST_PORTFOLIO_MGT = "INVEST_PORTFOLIO_MGT";
    //
    public static final String INVESTMT_STUP_PTFOLIO_MENU_ITEM = "INVESTMT_STUP_PTFOLIO_MENU_ITEM";
    //
    public static final String INVESTMT_STUP_ASSETYP_MENU_ITEM = "INVESTMT_STUP_ASSETYP_MENU_ITEM";
    //
    public static final String INVESTMT_STUP_BROKR_MENU_ITEM = "INVESTMT_STUP_BROKR_MENU_ITEM";
    //
    public static final String INVESTMT_STUP_COMPNY_MENU_ITEM = "INVESTMT_STUP_COMPNY_MENU_ITEM";
    //
    public static final String INVESTMT_STUP_CHRGE_MENU_ITEM = "INVESTMT_STUP_CHRGE_MENU_ITEM";
    //
    public static final String INVESTMT_STUP_BROKER_COMM_MENU_ITEM = "INVESTMT_STUP_BROKER_COMM_MENU_ITEM";
    //
    public static final String INVESTMT_STUP_FUND_MENU_ITEM = "INVESTMT_STUP_FUND_MENU_ITEM";
    //
    public static final String INVESTMT_FUND_TRANS_MENU_ITEM = "INVESTMT_FUND_TRANS_MENU_ITEM";
    //
    public static final String INVESTMT_STUP_SECTR_MENU_ITEM = "INVESTMT_STUP_SECTR_MENU_ITEM";
    //
    public static final String INVESTMT_STUP_ANNUAL_DAYS_TYPE_MENU_ITEM = "INVESTMT_STUP_ANNUAL_DAYS_TYPE_MENU_ITEM";
    //
    public static final String INVESTMT_STUP_INVEST_TYPE_MENU_ITEM = "INVESTMT_STUP_INVEST_TYPE_MENU_ITEM";
    //
    public static final String INVESTMT_STUP_POSTIN_GRP_MENU_ITEM = "INVESTMT_STUP_POSTIN_GRP_MENU_ITEM";
    //
    public static final String INVESTMT_INV_POOL_MENU_ITEM = "INVESTMT_INV_POOL_MENU_ITEM";
    //
    public static final String INVESTMT_ASSET_MGT = "INVESTMT_ASSET_MGT";
    //
    public static final String INVESTMT_EQTY_MENU_ITEM = "INVESTMT_EQTY_MENU_ITEM";
    //
    public static final String INVESTMT_EQTY_SETP_MENU_ITEM = "INVESTMT_EQTY_SETP_MENU_ITEM";
    //
    public static final String INVESTMT_EQTY_STOCK_MENU_ITEM = "INVESTMT_EQTY_STOCK_MENU_ITEM";
    //
    public static final String INVESTMT_EQTY_STOCK_PRICE_MENU_ITEM = "INVESTMT_EQTY_STOCK_PRICE_MENU_ITEM";
    //
    public static final String INVESTMT_EQTY_TRN_MENU_ITEM = "INVESTMT_EQTY_TRN_MENU_ITEM";
    //
    public static final String INVESTMT_EQTY_FUND_RQST_MENU_ITEM = "INVESTMT_EQTY_FUND_RQST_MENU_ITEM";
    //
    public static final String INVESTMT_EQTY_DIVIDEND_MENU_ITEM = "INVESTMT_EQTY_DIVIDEND_MENU_ITEM";
    //
    public static final String INVESTMT_EQTY_BONUS_MENU_ITEM = "INVESTMT_EQTY_BONUS_MENU_ITEM";
    //
    public static final String INVESTMT_EQUITY_PLACEMT_MENU_ITEM = "INVESTMT_EQUITY_PLACEMT_MENU_ITEM";
    //
    public static final String INVESTMT_EQUITY_MNG_PLACEMT_MENU_ITEM = "INVESTMT_EQUITY_MNG_PLACEMT_MENU_ITEM";
    //
    public static final String INVESTMT_EQTY_PURCHS_TRN_MENU_ITEM = "INVESTMT_EQTY_PURCHS_TRN_MENU_ITEM";
    //
    public static final String INVESTMT_EQTY_CORP_ACTN_MENU_ITEM = "INVESTMT_EQTY_CORP_ACTN_MENU_ITEM";
    //
    public static final String INVESTMT_EQTY_PDIC_ACT_MENU_ITEM = "INVESTMT_EQTY_PDIC_ACT_MENU_ITEM";
    //
    public static final String INVESTMT_BOND_MENU_ITEM = "INVESTMT_BOND_MENU_ITEM";
    //
    public static final String INVESTMT_BOND_TRN_MENU_ITEM = "INVESTMT_BOND_TRN_MENU_ITEM";
    //
    public static final String INVESTMT_BOND_FUND_REQ_MENU_ITEM = "INVESTMT_BOND_FUND_REQ_MENU_ITEM";
    //
    public static final String INVESTMT_BOND_PLACEMT_MENU_ITEM = "INVESTMT_BOND_PLACEMT_MENU_ITEM";
    //
    public static final String INVESTMT_BOND_MNG_PLACEMT_MENU_ITEM = "INVESTMT_BOND_MNG_PLACEMT_MENU_ITEM";
    //
    public static final String INVESTMT_BOND_CALC_MENU_ITEM = "INVESTMT_BOND_CALC_MENU_ITEM";
    //
    public static final String INVESTMT_BOND_CORPACT_MENU_ITEM = "INVESTMT_BOND_CORPACT_MENU_ITEM";
    //
    public static final String INVESTMT_BOND_PRDICACT_MENU_ITEM = "INVESTMT_BOND_PRDICACT_MENU_ITEM";
    //
    public static final String INVESTMT_MONEYMRKT_MENU_ITEM = "INVESTMT_MONEYMRKT_MENU_ITEM";
    //
    public static final String INVESTMT_MONEYMRKT_TRN_MENU_ITEM = "INVESTMT_MONEYMRKT_TRN_MENU_ITEM";
    //
    public static final String INVESTMT_MONEYMRKT_FUND_REQ_MENU_ITEM = "INVESTMT_MONEYMRKT_FUND_REQ_MENU_ITEM";
    //
    public static final String INVESTMT_MONEYMRKT_PLACEMT_MENU_ITEM = "INVESTMT_MONEYMRKT_PLACEMT_MENU_ITEM";
    //
    public static final String INVESTMT_MONEYMRKT_MNG_PLACEMT_MENU_ITEM = "INVESTMT_MONEYMRKT_MNG_PLACEMT_MENU_ITEM";
    //
    public static final String INVESTMT_MONEYMRKT_ACTNS_MENU_ITEM = "INVESTMT_MONEYMRKT_ACTNS_MENU_ITEM";
    //
    public static final String INVESTMT_MONEYMRKT_DEP_MENU_ITEM = "INVESTMT_MONEYMRKT_DEP_MENU_ITEM";
    //
    public static final String INVESTMT_MONEYMRKT_CALLDEP_MENU_ITEM = "INVESTMT_MONEYMRKT_CALLDEP_MENU_ITEM";
    //
    public static final String INVESTMT_MONEYMRKT_SECURITIES_MENU_ITEM = "INVESTMT_MONEYMRKT_SECURITIES_MENU_ITEM";
    //
    public static final String INVESTMT_MONEYMRKT_PRDIC_ACT_MENU_ITEM = "INVESTMT_MONEYMRKT_PRDIC_ACT_MENU_ITEM";
    //
    public static final String INVESTMT_PLCEMNT_MNG_TERMINATION_MENU_ITEM = "INVESTMT_PLCEMNT_MNG_TERMINATION_MENU_ITEM";
    //
    public static final String INVESTMT_PPT_MENU_ITEM = "INVESTMT_PPT_MENU_ITEM";
    //
    public static final String INVESTMT_PPT_TRN_MENU_ITEM = "INVESTMT_PPT_TRN_MENU_ITEM";
    //
    public static final String INVESTMT_PPT_FUND_REQ_MENU_ITEM = "INVESTMT_PPT_FUND_REQ_MENU_ITEM";
    //
    public static final String INVESTMT_PPT_PLACEMT_MENU_ITEM = "INVESTMT_PPT_PLACEMT_MENU_ITEM";
    //
    public static final String INVESTMT_PPT_PRICE_REVAL_MENU_ITEM = "INVESTMT_PPT_PRICE_REVAL_MENU_ITEM";
    //
    public static final String INVESTMT_PPT_MNG_PLACEMT_MENU_ITEM = "INVESTMT_PPT_MNG_PLACEMT_MENU_ITEM";
    //
    public static final String INVESTMT_TRAN_MENU_ITEM = "INVESTMT_TRAN_MENU_ITEM";
    //
    public static final String INVESTMT_COMPLIANCE_RT_MENU_ITEM = "INVESTMT_COMPLIANCE_RT_MENU_ITEM";
    //
    public static final String INVESTMT_DEPOSIT_TYP_MENU_ITEM = "INVESTMT_DEPOSIT_TYP_MENU_ITEM";
    //
    public static final String INVESTMT_TRANSACTN_INTEREST_MENU_ITEM = "INVESTMT_TRANSACTN_INTEREST_MENU_ITEM";
    //
    public static final String UNDERWRITING_VALUATION_MENU_ITEM = "UNDERWRITING_VALUATION_MENU_ITEM";
    //
    public static final String ASSET_LIABILITY_MATCHING_MENU_ITEM = "ASSET_LIABILITY_MATCHING_MENU_ITEM";
    //
    public static final String ALM_GENDER_MORTALITY_MENU_ITEM = "ALM_GENDER_MORTALITY_MENU_ITEM";
    //
    public static final String ALM_SETUP_MENU_ITEM = "ALM_SETUP_MENU_ITEM";
    //
    public static final String ALM_PA90_CUMMUTATION_MENU_ITEM = "ALM_PA90_CUMMUTATION_MENU_ITEM";
    //
    public static final String UW_VALUATION_RPT_MENU_ITEM = "UW_VALUATION_RPT_MENU_ITEM";
    //
    public static final String ALM_VALUATION_RPT_MENU_ITEM = "ALM_VALUATION_RPT_MENU_ITEM";
    //
    public static final String ASSET_VALUATION_MENU_ITEM = "ASSET_VALUATION_MENU_ITEM";
    //
    public static final String EQUITY_VALUATION_MENU_ITEM = "EQUITY_VALUATION_MENU_ITEM";
    //
    public static final String BOND_VALUATION_MENU_ITEM = "BOND_VALUATION_MENU_ITEM";
    //
    public static final String MONEYMKT_VALUATION_MENU_ITEM = "MONEYMKT_VALUATION_MENU_ITEM";
    //
    public static final String PROPERTY_VALUATION_MENU_ITEM = "PROPERTY_VALUATION_MENU_ITEM";
    //
    public static final String INVESTMT_CSCSID_NO_MENU_ITEM = "INVESTMT_CSCSID_NO_MENU_ITEM";
    //
    public static final String INVESTMT_EQUITY_EXEC_ORDER_MENU_ITEM = "INVESTMT_EQUITY_EXEC_ORDER_MENU_ITEM";
    //
    public static final String INVEST_PORTFOLIO_SUMRY = "INVEST_PORTFOLIO_SUMRY";
    //
    public static final String VIEW_INVEST_PORTFOLIO_SUMRY = "VIEW_INVEST_PORTFOLIO_SUMRY";
    //
    public static final String INVESTMT_EQUITY_VIEW_EXECUTED_MENU_ITEM = "INVESTMT_EQUITY_VIEW_EXECUTED_MENU_ITEM";
    //
    //
    //
    //
    //
    public static final String ASSET_MANAGEMENT_GENERAL_SETUP_MENU_ITEM = "ASSET_MANAGEMENT_GENERAL_SETUP_MENU_ITEM";
    //
    public static final String ASSET_MANAGEMENT_MENU_ITEM = "ASSET_MANAGEMENT_MENU_ITEM";
    //
    public static final String ASSET_CATEGORY_MENU_ITEM = "ASSET_CATEGORY_MENU_ITEM";
    //
    public static final String ASSET_DEPARTMENT_MENU_ITEM = "ASSET_DEPARTMENT_MENU_ITEM";
    //
    public static final String ASSET_DEPRECIATE_PERIOD_TYPE_MENU_ITEM = "ASSET_DEPRECIATE_PERIOD_TYPE_MENU_ITEM";
    //
    public static final String ASSET_CONTRACT_TYPE_MENU_ITEM = "ASSET_CONTRACT_TYPE_MENU_ITEM";
    //
    public static final String ASSET_INSPECTOR_MENU_ITEM = "ASSET_INSPECTOR_MENU_ITEM";
    //
    public static final String ASSET_EXTERNAL_HOLDER_MENU_ITEM = "ASSET_EXTERNAL_HOLDER_MENU_ITEM";
    //
    public static final String ASSET_BIDDER_MENU_ITEM = "ASSET_BIDDER_MENU_ITEM";

    //
    public static final String ASSET_INSURANCE_MGT_MENU_ITEM = "ASSET_INSURANCE_MGT_MENU_ITEM";
    //
    public static final String ASSET_INSURANCE_TYPE_MENU_ITEM = "ASSET_INSURANCE_TYPE_MENU_ITEM";
    //
    public static final String ASSET_INSURANCE_POLICY_CLASS_MENU_ITEM = "ASSET_INSURANCE_POLICY_CLASS_MENU_ITEM";
    //
    public static final String ASSET_INSURANCE_PRODUCT_MENU_ITEM = "ASSET_INSURANCE_PRODUCT_MENU_ITEM";
    //
    public static final String ASSET_INSURANCE_COVER_TYPE_MENU_ITEM = "ASSET_INSURANCE_COVER_TYPE_MENU_ITEM";
    //
    public static final String ASSET_INSURANCE_PRODUCT_COVER_TYPE_MENU_ITEM = "ASSET_INSURANCE_PRODUCT_COVER_TYPE_MENU_ITEM";

    //
    public static final String ASSET_TYPE_MGT_MENU_ITEM = "ASSET_TYPE_MGT_MENU_ITEM";
    //
    public static final String ASSET_TYPE_MENU_ITEM = "ASSET_TYPE_MENU_ITEM";
    //
    public static final String ASSET_TYPE_DEPRECIATE_RATE_MENU_ITEM = "ASSET_TYPE_DEPRECIATE_RATE_MENU_ITEM";
    //
    public static final String ASSET_TYPE_ELEMENT_MENU_ITEM = "ASSET_TYPE_ELEMENT_MENU_ITEM";
    //
    public static final String ASSET_TYPE_BENCHMARK_MENU_ITEM = "ASSET_TYPE_BENCHMARK_MENU_ITEM";
    //
    public static final String ASSET_TYPE_ATTRIBUTE_MENU_ITEM = "ASSET_TYPE_ATTRIBUTE_MENU_ITEM";
    //
    public static final String ASSET_TYPE_LEASE_PENALTY_MENU_ITEM = "ASSET_TYPE_LEASE_PENALTY_MENU_ITEM";

    //
    public static final String ASSET_INFO_MGT_MENU_ITEM = "ASSET_INFO_MGT_MENU_ITEM";
    //
    public static final String ASSET_ITEM_DETAIL_MENU_ITEM = "ASSET_ITEM_DETAIL_MENU_ITEM";
    //
    public static final String ASSET_CUSTODY_TRAIL_MENU_ITEM = "ASSET_CUSTODY_TRAIL_MENU_ITEM";
    //
    public static final String ASSET_CONTRACT_DOCUMENT_MENU_ITEM = "ASSET_CONTRACT_DOCUMENT_MENU_ITEM";
    //
    public static final String ASSET_INSURANCE_ENTRY_MENU_ITEM = "ASSET_INSURANCE_ENTRY_MENU_ITEM";
    //
    public static final String ASSET_DEPRECIATE_TRAIL_MENU_ITEM = "ASSET_DEPRECIATE_TRAIL_MENU_ITEM";
    //
    public static final String ASSET_DISPOSITION_MENU_ITEM = "ASSET_DISPOSITION_MENU_ITEM";
    //
    public static final String ASSET_DISPOSITION_BID_MENU_ITEM = "ASSET_DISPOSITION_BID_MENU_ITEM";
    //
    public static final String ASSET_BID_DEFAULT_TRAIL_MENU_ITEM = "ASSET_BID_DEFAULT_TRAIL_MENU_ITEM";
    //
    public static final String ASSET_LEASE_DETAIL_MENU_ITEM = "ASSET_LEASE_DETAIL_MENU_ITEM";
    //
    public static final String ASSET_INSPECT_REGIME_MENU_ITEM = "ASSET_INSPECT_REGIME_MENU_ITEM";
    //
    public static final String ASSET_INSPECT_ELEMENT_MENU_ITEM = "ASSET_INSPECT_ELEMENT_MENU_ITEM";
    //
    //
    //
    //
    public static final String HRM_SALARY_SCHEME_MENU_ITEM = "HRM_SALARY_SCHEME_MENU_ITEM";
    //
    public static final String HUMAN_RESOURCE_MENU_ITEM = "HUMAN_RESOURCE_MENU_ITEM";
    //
    public static final String CUSTOMER_RELATIONS_MENU_ITEM = "CUSTOMER_RELATIONS_MENU_ITEM";
    //
    public static final String GENERAL_APPLICATIONS_MENU_ITEM = "GENERAL_APPLICATIONS_MENU_ITEM";
    //
    public static final String FINANCIALS_MENU_ITEM = "FINANCIALS_MENU_ITEM";
    //
    public static final String PROCUREMENT_MENU_ITEM = "PROCUREMENT_MENU_ITEM";
    //
    public static final String INVENTORY_MENU_ITEM = "INVENTORY_MENU_ITEM";
    //
    public static final String FIXED_ASSET_MENU_ITEM = "FIXED_ASSET_MENU_ITEM";
    //
    public static final String INVESTMENT_SYS_MENU_ITEM = "INVESTMENT_SYS_MENU_ITEM";
    //
    public static final String ELEARNING_SYS_MENU_ITEM = "ELEARNING_SYS_MENU_ITEM";
    //
    public static final String INSURANCE_SYSTEM_MENU_ITEM = "INSURANCE_SYSTEM_MENU_ITEM";
    //
    public static final String FINANCIAL_ASSETS_SYSTEM_MENU_ITEM = "FINANCIAL_ASSETS_SYSTEM_MENU_ITEM";
    //
    public static final String EDUCATION_SYSTEM_MENU_ITEM = "EDUCATION_SYSTEM_MENU_ITEM";
    //
    public static final String ACADEMIC_RECORD_SYS_MENU_ITEM = "ACADEMIC_RECORD_SYS_MENU_ITEM";
    //
    public static final String HEALTH_SYSTEM_MENU_ITEM = "HEALTH_SYSTEM_MENU_ITEM";
    //
    public static final String HEALTH_INFO_SYSTEM_MENU_ITEM = "HEALTH_INFO_SYSTEM_MENU_ITEM";
    //
    //
    //
    //
    //
    public static final String STOCK_TRANSFER_REQ_MENU_ITEM = "STOCK_TRANSFER_REQ_MENU_ITEM";
    //
    //
    //
    //
    public static final String PROCURE_PRICING_N_QUOTES_MENU_ITEM = "PROCURE_PRICING_N_QUOTES_MENU_ITEM";
    //
    public static final String PROCURE_PROD_PRICE_LIST_MENU_ITEM = "PROCURE_PROD_PRICE_LIST_MENU_ITEM";
    //
    public static final String PROCURE_SUPPLIER_QUOTATIONS_MENU_ITEM = "PROCURE_SUPPLIER_QUOTATIONS_MENU_ITEM";
    //
    public static final String PROCURE_CR8_QUOTATION_MENU_ITEM = "PROCURE_CR8_QUOTATION_MENU_ITEM";
    //
    public static final String PROCURE_MNG_QUOTATION_MENU_ITEM = "PROCURE_MNG_QUOTATION_MENU_ITEM";
    //
    public static final String PROCURE_CR8_MRKT_PRICE_MENU_ITEM = "PROCURE_CR8_MRKT_PRICE_MENU_ITEM";
    //
    public static final String PROCURE_MNG_MRKT_PRICE_MENU_ITEM = "PROCURE_MNG_MRKT_PRICE_MENU_ITEM";
    //
    public static final String PROCURE_PURCHASE_MGT_MENU_ITEM = "PROCURE_PURCHASE_MGT_MENU_ITEM";
    //
    public static final String PROCURE_SUPPLIER_SETP_MENU_ITEM = "PROCURE_SUPPLIER_SETP_MENU_ITEM";
    //
    public static final String PROCURE_PRODUCT_LINE_MENU_ITEM = "PROCURE_PRODUCT_LINE_MENU_ITEM";
    //
    public static final String PROCURE_PURCHASE_ORDER_MENU_ITEM = "PROCURE_PURCHASE_ORDER_MENU_ITEM";
    //
    public static final String PROCURE_PURCHASE_ORDER_CR8_MENU_ITEM = "PROCURE_PURCHASE_ORDER_CR8_MENU_ITEM";
    //
    public static final String PROCURE_PURCHASE_ORDER_MNG_MENU_ITEM = "PROCURE_PURCHASE_ORDER_MNG_MENU_ITEM";
    //
    public static final String CLIENT_TYP_MENU_ITEM = "CLIENT_TYP_MENU_ITEM";
    //
    public static final String POLICY_LOANS_MENU_ITEM = "POLICY_LOANS_MENU_ITEM";
    //
    public static final String PRODUCT_LOAN_SETUP_MENU_ITEM = "PRODUCT_LOAN_SETUP_MENU_ITEM";
    //
    public static final String PRODUCT_SURRENDER_PENALTY_MENU_ITEM = "PRODUCT_SURRENDER_PENALTY_MENU_ITEM";
    //
    public static final String POLICY_LOAN_REQUEST_MENU_ITEM = "POLICY_LOAN_REQUEST_MENU_ITEM";
    //
    public static final String POLICY_LOAN_ADMIN_MENU_ITEM = "POLICY_LOAN_ADMIN_MENU_ITEM";
    //
    public static final String POLICY_LOAN_MAINTAIN_MENU_ITEM = "POLICY_LOAN_MAINTAIN_MENU_ITEM";
    //
    //
    //
    //
    public static final String BUDGET_4CAST_MENU_ITEM = "BUDGET_4CAST_MENU_ITEM";
    //
    public static final String BUDGET_LINE_ITEM_MENU_ITEM = "BUDGET_LINE_ITEM_MENU_ITEM";
    //
    public static final String BUDGET_ENTRY_MENU_ITEM = "BUDGET_ENTRY_MENU_ITEM";
    //
    public static final String BUDGET_MANAGE_MENU_ITEM = "BUDGET_MANAGE_MENU_ITEM";
    //
    public static final String CHANGE_VOTE_OF_CHARGE_MENU_ITEM = "CHANGE_VOTE_OF_CHARGE_MENU_ITEM";
    //
    public static final String MANAGE_VOTE_OF_CHARGE_MENU_ITEM = "MANAGE_VOTE_OF_CHARGE_MENU_ITEM";
    //
    public static final String ADDITIONAL_BUDGET_MENU_ITEM = "ADDITIONAL_BUDGET_MENU_ITEM";
    //
    public static final String MAINTAIN_ADDITIONAL_BUDGET_MENU_ITEM = "MAINTAIN_ADDITIONAL_BUDGET_MENU_ITEM";
    //
    public static final String LIFE_INSURANCE_MENU_ITEM = "LIFE_INSURANCE_MENU_ITEM";
    //
    public static final String ANNUITY_INSURANCE_MENU_ITEM = "ANNUITY_INSURANCE_MENU_ITEM";
    //
    public static final String NONLIFE_INSURANCE_MENU_ITEM = "NONLIFE_INSURANCE_MENU_ITEM";
    //
    public static final String LIBRARY_SYS_MGT_MENU_ITEM = "LIBRARY_SYS_MGT_MENU_ITEM";
    //
    public static final String HEALTH_INSURANCE_MENU_ITEM = "HEALTH_INSURANCE_MENU_ITEM";
    //
    public static final String FACULTY_SETP_MENU_ITEM = "FACULTY_SETP_MENU_ITEM";

    //Multitenancy variable
    private boolean multi_tenancy_enabled;

    private boolean reportPanelCollapsed;
    private LinkStack linkStack;
    private Map<String, String> systemMap;
    //
    private ActiveSubsystemType activeSubsystem;
    private String menuOfSubsystem;
    private String subsystemName;

    private boolean changedLocale;

    public MenuManagerBean() {
        activeSubsystem = ActiveSubsystemType.EDUWARE;
        reportPanelCollapsed = true;
        buildSystemMenuMap();
    }

    public String gotoHRMSSubsystem() {
        activeSubsystem = ActiveSubsystemType.HRMS;
        return "/hrmshome.jsf";
    }

    public String gotoCRMSubsystem() {
        activeSubsystem = ActiveSubsystemType.CRM;
        return "/crmhome.jsf";
    }

    public String gotoFINSubsystem() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/home.jsf";
    }

    public String selectFinancialMenu() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/home.jsf";
    }

    public String selectHumanResourcesMenu() {
        activeSubsystem = ActiveSubsystemType.HRMS;
        return "/home.jsf";
    }

    public String selectCustomerRelationsMenu() {
        activeSubsystem = ActiveSubsystemType.CRM;
        return "/home.jsf";
    }

    public String selectProcurementMenu() {
        activeSubsystem = ActiveSubsystemType.PROCUREMENT;
        return "/home.jsf";
    }

    public String selectInventoryMenu() {
        activeSubsystem = ActiveSubsystemType.INVENTORY;
        return "/home.jsf";
    }

    public String selectInvestmentMenu() {
        activeSubsystem = ActiveSubsystemType.INVESTMENT;
        return "/home.jsf";
    }

    public String selectFixedAssetMenu() {
        activeSubsystem = ActiveSubsystemType.FIXED_ASSET;
        return "/home.jsf";
    }

    public String selectEduwareMenu() {
        activeSubsystem = ActiveSubsystemType.EDUWARE;
        return "/home.jsf";
    }

    public String selectAcademicRecordsMenu() {
        activeSubsystem = ActiveSubsystemType.ACADEMIC_RECORDS;
        return "/home.jsf";
    }

    public String selectLibraryManagementMenu() {
        activeSubsystem = ActiveSubsystemType.LIBRARY_MANAGEMENT;
        return "/home.jsf";
    }

    public String selectLifeInsuranceMenu() {
        activeSubsystem = ActiveSubsystemType.LIFE_INSURANCE;
        return "/home.jsf";
    }

    public String selectNonlifeInsuranceMenu() {
        activeSubsystem = ActiveSubsystemType.NONLIFE_INSURANCE;
        return "/home.jsf";
    }

    public String selectHealthInsuranceMenu() {
        activeSubsystem = ActiveSubsystemType.HEALTH_INSURANCE;
        return "/home.jsf";
    }

    public String selectHealthInformationSysMenu() {
        activeSubsystem = ActiveSubsystemType.HEALTH_INFO_SYSTEM;
        return "/home.jsf";
    }

    public String selectAnnuityInsuranceMenu() {
        activeSubsystem = ActiveSubsystemType.ANNUITY_INSURANCE;
        return "/home.jsf";
    }

    public String getMenuOfSubsystem() {
        menuOfSubsystem = "";

        if (activeSubsystem == ActiveSubsystemType.HRMS) {
            menuOfSubsystem = "/hrmsmenus.xhtml";
        } else if (activeSubsystem == ActiveSubsystemType.CRM) {
            menuOfSubsystem = "/crmmenus.xhtml";
        } else if (activeSubsystem == ActiveSubsystemType.ACCOUNTING) {
            menuOfSubsystem = "/menus.xhtml";
        } else if (activeSubsystem == ActiveSubsystemType.FIXED_ASSET) {
            menuOfSubsystem = "/fixedassetmenus.xhtml";
        } else if (activeSubsystem == ActiveSubsystemType.INVENTORY) {
            menuOfSubsystem = "/inventorymenus.xhtml";
        } else if (activeSubsystem == ActiveSubsystemType.PROCUREMENT) {
            menuOfSubsystem = "/purchasemenus.xhtml";
        } else if (activeSubsystem == ActiveSubsystemType.EDUWARE) {
            menuOfSubsystem = "/elearningmenus.xhtml";
        } else if (activeSubsystem == ActiveSubsystemType.ACADEMIC_RECORDS) {
            menuOfSubsystem = "/academicsmenus.xhtml";
        } else if (activeSubsystem == ActiveSubsystemType.LIBRARY_MANAGEMENT) {
            menuOfSubsystem = "/librarysystemmenus.xhtml";
        } else if (activeSubsystem == ActiveSubsystemType.NONLIFE_INSURANCE) {
            menuOfSubsystem = "/generalmenus.xhtml";
        } else if (activeSubsystem == ActiveSubsystemType.LIFE_INSURANCE) {
            menuOfSubsystem = "/lifemenus.xhtml";
        } else if (activeSubsystem == ActiveSubsystemType.ANNUITY_INSURANCE) {
            menuOfSubsystem = "/annuitymenus.xhtml";
        } else if (activeSubsystem == ActiveSubsystemType.HEALTH_INSURANCE) {
            menuOfSubsystem = "/healthinsuremenus.xhtml";
        } else if (activeSubsystem == ActiveSubsystemType.HEALTH_INFO_SYSTEM) {
            menuOfSubsystem = "/healthinfosysmenus.xhtml";
        } else if (activeSubsystem == ActiveSubsystemType.INVESTMENT) {
            menuOfSubsystem = "/investmenus.xhtml";
        } else {
            menuOfSubsystem = "/menus.xhtml";
        }

        return menuOfSubsystem;
    }

    public String getSubsystemName() {
        String subsysName = "";

        if (activeSubsystem != null) {
            subsysName = activeSubsystem.toString();
        } else {
            subsysName = ActiveSubsystemType.ACCOUNTING.toString();
        }

        return subsysName;
    }

    public void buildSystemMenuMap() {
        systemMap = new HashMap<String, String>();

        systemMap.put(GENERAL_APPLICATIONS_MENU_ITEM, "General Applications");
        systemMap.put(FINANCIALS_MENU_ITEM, "Financials");
        systemMap.put(HUMAN_RESOURCE_MENU_ITEM, "Human Resources");
        systemMap.put(CUSTOMER_RELATIONS_MENU_ITEM, "CRM");
        systemMap.put(PROCUREMENT_MENU_ITEM, "Procurement");
        systemMap.put(INVENTORY_MENU_ITEM, "Inventory");
        systemMap.put(FIXED_ASSET_MENU_ITEM, "Fixed Asset Management");
        systemMap.put(INVENTORY_MENU_ITEM, "Inventory");
        systemMap.put(FIXED_ASSET_MENU_ITEM, "Fixed Asset Management");
        systemMap.put(INSURANCE_SYSTEM_MENU_ITEM, "Fixed Asset Management");
        systemMap.put(LIFE_INSURANCE_MENU_ITEM, "Life Insurance");
        systemMap.put(ANNUITY_INSURANCE_MENU_ITEM, "Annuity Insurance");
        systemMap.put(NONLIFE_INSURANCE_MENU_ITEM, "Nonlife Insurance");
        systemMap.put(HEALTH_INSURANCE_MENU_ITEM, "Health Insurance");
        systemMap.put(FINANCIAL_ASSETS_SYSTEM_MENU_ITEM, "Financial Assets");
        systemMap.put(INVESTMENT_SYS_MENU_ITEM, "Investment &amp; Portfolio Mgt");
        systemMap.put(EDUCATION_SYSTEM_MENU_ITEM, "Education");
        systemMap.put(ACADEMIC_RECORD_SYS_MENU_ITEM, "Academic Record Management");
        systemMap.put(LIBRARY_SYS_MGT_MENU_ITEM, "Library Management");
        systemMap.put(ELEARNING_SYS_MENU_ITEM, "E-Learning");
        systemMap.put(HEALTH_SYSTEM_MENU_ITEM, "Health");
        systemMap.put(HEALTH_INFO_SYSTEM_MENU_ITEM, "Health Information System");

        systemMap.put(SETUP_MENU_ITEM, "Setup");
        systemMap.put(COMPANY_ID_MENU_ITEM, "Company Identity");
        systemMap.put(ORG_SETUP_MENU_ITEM, "Organizational Setup");
        systemMap.put(BRANCHES_MENU_ITEM, "Branches");
        systemMap.put(DIVISION_MENU_ITEM, "Divisions");
        systemMap.put(DEPT_MENU_ITEM, "Departments");
        systemMap.put(RELSHIP_MENU_ITEM, "Relationship Types");
        systemMap.put(TITLES_MENU_ITEM, "Titles");

        systemMap.put(LOCATION_MENU_ITEM, "Location");
        systemMap.put(COUNTRIES_MENU_ITEM, "Countries");
        systemMap.put(STATES_MENU_ITEM, "States/Provinces");
        systemMap.put(CITIES_MENU_ITEM, "Cities");
        systemMap.put(CURRENCIES_MENU_ITEM, "Currencies");

        systemMap.put(SERV_PROVIDRS_MENU_ITEM, "Service Providers");
        systemMap.put(BANKS_MENU_ITEM, "Banks");
        systemMap.put(PFAS_MENU_ITEM, "PFAs");
        systemMap.put(TRUSTEES_MENU_ITEM, "Trustees");
        systemMap.put(EMPLOYERS_MENU_ITEM, "Employers");
        systemMap.put(REFERRER_MENU_ITEM, "Referrals");
        systemMap.put(UNDERWRITER_MENU_ITEM, "Insurance Companies");
        systemMap.put(MESSAGING_MENU_ITEM, "Messaging");

        systemMap.put(FINANCE_SETP_MENU_ITEM, "Finance Setup");
        systemMap.put(ACCTING_MENU_ITEM, "Accounting");
        systemMap.put(ACCT_SETP_MENU_ITEM, "Account Setups");
        systemMap.put(TRAN_SRC_MENU_ITEM, "Transaction Source");
        systemMap.put(ACCT_SECT_MENU_ITEM, "Account Sections");
        systemMap.put(ACCT_GRP_MENU_ITEM, "Account Groups");
        systemMap.put(ACCT_CHRT_MENU_ITEM, "Chart of Accounts");
        systemMap.put(SUBLEDGR_ACCT_CHRT_MENU_ITEM, "Subledger Accounts");
        systemMap.put(PERIODS_MENU_ITEM, "Periods");
        systemMap.put(FIN_YR_MENU_ITEM, "Financial Year");
        systemMap.put(ACCT_PRIOD_MENU_ITEM, "Accounting Period");
        systemMap.put(TRN_TYP_MENU_ITEM, "Transaction Types");
        systemMap.put(BIZ_TYP_MP_MENU_ITEM, "Business Type Mapping");
        systemMap.put(BUDGET_MENU_ITEM, "GL Account Budget");
        systemMap.put(CR8_ACCT_BUDGET_MENU_ITEM, "Create Budget");
        systemMap.put(VIEW_ACCT_BUDGET_MENU_ITEM, "View Budget");
        systemMap.put(POSTING_MENU_ITEM, "Postings");
        systemMap.put(JNL_ENTRY_MENU_ITEM, "Journal Entry");
        systemMap.put(INQ_N_REPRT_MENU_ITEM, "Inquiries And Reports");
        systemMap.put(GL_TRN_MENU_ITEM, "GL Transactions");
        systemMap.put(SUBLDGR_GL_TRN_MENU_ITEM, "Subledger Transactions");
        systemMap.put(CASH_BOOK_GL_TRN_MENU_ITEM, "View Cash Book");
        systemMap.put(BANK_STATEMENT_CREATE_MENU_ITEM, "Create Bank Statement");
        systemMap.put(BANK_STATEMENT_VIEW_MENU_ITEM, "View Bank Statement");

        systemMap.put(BAL_SHT_MENU_ITEM, "Balance Sheet");
        systemMap.put(TRIAL_BAL_MENU_ITEM, "Trial Balance");
        systemMap.put(P_N_L_MENU_ITEM, "Profit And Loss Statement");
        systemMap.put(INCOME_STMT_MENU_ITEM, "Income Statement");
        systemMap.put(SUSPENSE_REPRT_MENU_ITEM, "Suspense Account");
        systemMap.put(EXCHANGE_RATE_MENU_ITEM, "Exchange Rate");
        systemMap.put(FOREX_REVALUATION_MENU_ITEM, "Forex Revaluation");
        systemMap.put(PERFORM_FX_REVALUATION_MENU_ITEM, "Perform Revaluation");
        systemMap.put(VIEW_FX_REVALUATION_MENU_ITEM, "View Revaluation");

        systemMap.put(CLIENTS_MENU_ITEM, "Clients");
        systemMap.put(ELEMT_TYP_MENU_ITEM, "Element Types");
        systemMap.put(IDENT_TYP_MENU_ITEM, "Identification Types");
        systemMap.put(ADDR_PRF_TYP_MENU_ITEM, "Proof of Address Types");
        systemMap.put(CR8_REFERRER_TYPE_MENU_ITEM, "Setup Referral Types");
        systemMap.put(CLIENT_SETP_MENU_ITEM, "Client Setup");
        systemMap.put(CR8_CLIENT_MENU_ITEM, "Create Client");
        systemMap.put(VIEW_CLIENT_MENU_ITEM, "View Client");

        systemMap.put(AGENTS_MENU_ITEM, "Agents");
        systemMap.put(AGENT_SETP_MENU_ITEM, "Agent Setup");
        systemMap.put(AGENT_TYP_MENU_ITEM, "Agent Types");
        systemMap.put(CR8_AGENT_MENU_ITEM, "Create Agents");
        systemMap.put(AGENT_MGT_MENU_ITEM, "Agent Management");
        systemMap.put(AGENT_COMMSN_MENU_ITEM, "Agent Commissions");
        systemMap.put(AGENT_PERF_MENU_ITEM, "Agent Performance");

        systemMap.put(PRODT_MENU_ITEM, "Products");
        systemMap.put(INSUR_SETP_MENU_ITEM, "Insurance Setup");
        systemMap.put(ESCALATION_RT_SETP_MENU_ITEM, "Escalation Rate Setup");
        systemMap.put(PRODT_TYP_SETP_MENU_ITEM, "Product Type Setup");
        systemMap.put(TAX_SETP_MENU_ITEM, "Tax Setup");
        systemMap.put(TAX_AUTH_SETP_MENU_ITEM, "Tax Authority Setup");
        systemMap.put(DISCNT_SETP_MENU_ITEM, "Discount Setup");

        systemMap.put(UWRITING_MENU_ITEM, "Underwriting");
        systemMap.put(PRICING_MENU_ITEM, "Pricing");
        systemMap.put(PRICING_SETP_MENU_ITEM, "Pricing Setup");
        systemMap.put(RATES_MENU_ITEM, "Rates");
        systemMap.put(ADD_TRFF_RATES_MENU_ITEM, "Add Tariff Rates");
        systemMap.put(MNTN_TRFF_RATES_MENU_ITEM, "Maintain Tariff Rates");
        systemMap.put(GENDER_MORT_RATES_MENU_ITEM, "Gender Mortality Rates");
        systemMap.put(QK_QUOTE_MENU_ITEM, "Quick Quote");
        systemMap.put(CR8_QUOTE_MENU_ITEM, "Create Quote");
        systemMap.put(POLICIES_MENU_ITEM, "Policies");
        systemMap.put(CR8_SGL_PROP_MENU_ITEM, "Create Single Proposal");
        systemMap.put(EDIT_PROP_MENU_ITEM, "Edit Proposal");
        systemMap.put(UPLOAD_PROP_MENU_ITEM, "Upload Proposals");
        systemMap.put(MNTN_PROP_MENU_ITEM, "Maintain Proposals/Policies");
        systemMap.put(MNTN_PROP_ACTIVATE_MENU_ITEM, "Activate Policy");
        systemMap.put(UW_RPRT_MENU_ITEM, "Reports");
        systemMap.put(DAILY_PDTN_MENU_ITEM, "Daily Production");
        systemMap.put(MNTN_PROP_COLLECTNS_MENU_ITEM, "Collections");
        systemMap.put(MNTN_PROP_PAYOUT_MENU_ITEM, "Pay Outs");
        systemMap.put(MNTN_PROP_DOCUMENT_MENU_ITEM, "Documents");
        systemMap.put(MNTN_PROP_ENDORSE_MENU_ITEM, "Endorsements");

        systemMap.put(CLMS_N_SURNDR_MENU_ITEM, "Claims And Surrenders");
        systemMap.put(CLMS_MENU_ITEM, "Claims");
        systemMap.put(REG_CLM_MENU_ITEM, "Register Claim");
        systemMap.put(CLM_MNTN_MENU_ITEM, "Claim Maintenance");
        systemMap.put(SURNDRS_MENU_ITEM, "Surrenders");
        systemMap.put(REG_SURNDR_MENU_ITEM, "Register Surrender");
        systemMap.put(SURNDR_MNTN_MENU_ITEM, "Surrender Maintenance");

        systemMap.put(FINANCIALS_COLLECTION_MENU_ITEM, "Collections");
        systemMap.put(FINANCIALS_PAYMENT_MENU_ITEM, "Payments");
        systemMap.put(PAY_COLL_MENU_ITEM, "Payment Collections");
        systemMap.put(CR8_RCPT_MENU_ITEM, "Create Receipt");
        systemMap.put(SRCH_RCPT_MENU_ITEM, "Search for Receipts");
        systemMap.put(RCPT_DAILY_PRD_MENU_ITEM, "Daily Production");
        systemMap.put(COLL_SETP_MENU_ITEM, "Collections Setup");
        systemMap.put(BIZ_MODL_SETP_MENU_ITEM, "Business Module Setup");
        systemMap.put(COLL_MOD_SETP_MENU_ITEM, "Collection Mode Setup");
        systemMap.put(COLL_SRC_SETP_MENU_ITEM, "Collection Source Setup");
        systemMap.put(PAYOUT_CHANNEL_MENU_ITEM, "Payout Channel Setup");
        systemMap.put(APPROVLS_MENU_ITEM, "Approvals");
        systemMap.put(APPROVLS_STP_MENU_ITEM, "Approval Step");
        systemMap.put(APPROVLS_LIM_MENU_ITEM, "Approval Limit");
        systemMap.put(PAY_REQ_MENU_ITEM, "Payment Requisition");
        systemMap.put(PAY_MONTR_MENU_ITEM, "Requisition Monitor");
        systemMap.put(VIEW_PAYMT_MENU_ITEM, "View Payment");
        systemMap.put(VIEW_PAYOUT_TRANS_MENU_ITEM, "View Payout Status");

        systemMap.put(SECURITY_MENU_ITEM, "Security");
        systemMap.put(NEW_ENTITIES_MENU_ITEM, "New Entities");
        systemMap.put(NEW_USR_ACCT_MENU_ITEM, "New User Account");
        systemMap.put(MANAGE_USR_ACCT_MENU_ITEM, "Manage User Account");
        systemMap.put(VIEW_LOGGEDIN_USERS_MENU_ITEM, "Active User Sessions");
        systemMap.put(NEW_USR_ROLS_MENU_ITEM, "New User Roles");
        systemMap.put(NEW_PRIVLG_MENU_ITEM, "New Privilege/Menu");
        systemMap.put(NEW_APPLIC_SYS_MENU_ITEM, "New Application System");
        systemMap.put(NEW_SUBSYS_MENU_ITEM, "New Subsystem");
        systemMap.put(NEW_SYS_ROLS_MENU_ITEM, "Define/Build Roles");
        systemMap.put(AUDIT_TRAIL_ADM_MENU_ITEM, "Audit Trail Admin");
        systemMap.put(AUDIT_TRAIL_MENU_ITEM, "Audit Trail");

        systemMap.put(REPORTS_MENU_ITEM, "Reports");
        systemMap.put(RPTS_GRPING_MENU_ITEM, "Groupings");
        systemMap.put(RPTS_CATGRY_MENU_ITEM, "Report Categories");
        systemMap.put(RPTS_N_CHRTS_MENU_ITEM, "Reports & Charts");
        systemMap.put(SETP_RPTS_MENU_ITEM, "Setup Reports");
        systemMap.put(SETP_CHRTS_MENU_ITEM, "Setup Charts");
        systemMap.put(RPTS_AUTMTN_MENU_ITEM, "Automation");
        systemMap.put(RPTS_TRGRS_MENU_ITEM, "Report Triggers");
        systemMap.put(MSSG_TRGRS_MENU_ITEM, "Message Triggers");
        systemMap.put(RPTS_ALRT_SCHD_MENU_ITEM, "Notification Schedules");

        systemMap.put(POLICY_LOANS_MENU_ITEM, "Policy Loan");
        systemMap.put(PRODUCT_LOAN_SETUP_MENU_ITEM, "Loan Setup");
        systemMap.put(PRODUCT_SURRENDER_PENALTY_MENU_ITEM, "Surrender Penalty");
        systemMap.put(POLICY_LOAN_ADMIN_MENU_ITEM, "Loan Administration");
        systemMap.put(POLICY_LOAN_REQUEST_MENU_ITEM, "Loan Application");
        systemMap.put(POLICY_LOAN_MAINTAIN_MENU_ITEM, "Manage Loan");

        systemMap.put(INVESTMENT_MENU_ITEM, "Investment Setup");
        systemMap.put(INVESTMT_STUP_MENU_ITEM, "Types Setup");
        systemMap.put(INVESTMT_COMPLIANCE_RT_MENU_ITEM, "Compliance Rates");
        systemMap.put(INVESTMT_DEPOSIT_TYP_MENU_ITEM, "Deposit Types");
        systemMap.put(INVESTMT_STUP_ANNUAL_DAYS_TYPE_MENU_ITEM, "Annual Days Types");
        systemMap.put(INVESTMT_STUP_BNK_ACCT_MENU_ITEM, "Bank Accounts");
        systemMap.put(INVESTMT_STUP_INSTIT_MENU_ITEM, "Institutions");
        systemMap.put(INVESTMT_STUP_PLAYR_MENU_ITEM, "Partner Types");
        systemMap.put(INVEST_PORTFOLIO_MGT, "Portfolio Management");
        systemMap.put(INVESTMT_STUP_PTFOLIO_MENU_ITEM, "Portfolios");
        systemMap.put(INVESTMT_STUP_ASSETYP_MENU_ITEM, "Asset Types");
        systemMap.put(INVESTMT_STUP_BROKR_MENU_ITEM, "Stock Brokers");
        systemMap.put(INVESTMT_STUP_BROKER_COMM_MENU_ITEM, "Broker Commission");
        systemMap.put(INVESTMT_STUP_COMPNY_MENU_ITEM, "Investment Companies");
        systemMap.put(INVESTMT_STUP_CHRGE_MENU_ITEM, "Charges");
        systemMap.put(INVESTMT_STUP_FUND_MENU_ITEM, "Funds");
        systemMap.put(INVESTMT_FUND_TRANS_MENU_ITEM, "Add Credit");
        systemMap.put(INVESTMT_STUP_SECTR_MENU_ITEM, "Sectors");
        systemMap.put(INVESTMT_STUP_INVEST_TYPE_MENU_ITEM, "Investment Types");
        systemMap.put(INVESTMT_STUP_POSTIN_GRP_MENU_ITEM, "Posting Group");
        systemMap.put(INVESTMT_INV_POOL_MENU_ITEM, "Investment Pool");
        systemMap.put(INVESTMT_ASSET_MGT, "Order Management");
        systemMap.put(INVESTMT_EQTY_MENU_ITEM, "Equities");
        systemMap.put(INVESTMT_EQTY_SETP_MENU_ITEM, "Equities Setup");
        systemMap.put(INVESTMT_EQTY_TRN_MENU_ITEM, "Equities Transactions");
        systemMap.put(INVESTMT_EQTY_STOCK_MENU_ITEM, "Create Stock");
        systemMap.put(INVESTMT_EQTY_STOCK_PRICE_MENU_ITEM, "Manage Stock Prices");
        systemMap.put(INVESTMT_EQTY_FUND_RQST_MENU_ITEM, "Place Order");
        systemMap.put(INVESTMT_EQUITY_PLACEMT_MENU_ITEM, "Settle Order");
        systemMap.put(INVESTMT_EQUITY_MNG_PLACEMT_MENU_ITEM, "Manage Equities");
        systemMap.put(INVESTMT_EQTY_CORP_ACTN_MENU_ITEM, "Corporate Action");
        systemMap.put(INVESTMT_EQTY_DIVIDEND_MENU_ITEM, "Manage Dividends");
        systemMap.put(INVESTMT_EQTY_BONUS_MENU_ITEM, "Manage Bonuses");
        systemMap.put(INVESTMT_EQTY_PDIC_ACT_MENU_ITEM, "Periodic Activities");
        systemMap.put(INVESTMT_BOND_MENU_ITEM, "Bonds");
        systemMap.put(INVESTMT_BOND_TRN_MENU_ITEM, "Bond Transactions");
        systemMap.put(INVESTMT_BOND_FUND_REQ_MENU_ITEM, "Place Order");
        systemMap.put(INVESTMT_BOND_PLACEMT_MENU_ITEM, "Settle Order");
        systemMap.put(INVESTMT_BOND_MNG_PLACEMT_MENU_ITEM, "Manage Bonds");
        systemMap.put(INVESTMT_BOND_CALC_MENU_ITEM, "Bond Calculator");
        systemMap.put(INVESTMT_BOND_CORPACT_MENU_ITEM, "Corperate Actions");
        systemMap.put(INVESTMT_BOND_PRDICACT_MENU_ITEM, "Periodic Activities");
        systemMap.put(INVESTMT_MONEYMRKT_MENU_ITEM, "Money Markets");
        systemMap.put(INVESTMT_MONEYMRKT_TRN_MENU_ITEM, "MM Transactions");
        systemMap.put(INVESTMT_MONEYMRKT_FUND_REQ_MENU_ITEM, "Place Order");
        systemMap.put(INVESTMT_MONEYMRKT_PLACEMT_MENU_ITEM, "Settle Order");
        systemMap.put(INVESTMT_MONEYMRKT_MNG_PLACEMT_MENU_ITEM, "Manage MM Trans.");
        systemMap.put(INVESTMT_PLCEMNT_MNG_TERMINATION_MENU_ITEM, "Manage Terminations");

        systemMap.put(INVESTMT_MONEYMRKT_ACTNS_MENU_ITEM, "Actions");
        systemMap.put(INVESTMT_MONEYMRKT_DEP_MENU_ITEM, "Deposits");
        systemMap.put(INVESTMT_MONEYMRKT_CALLDEP_MENU_ITEM, "Call Deposits");
        systemMap.put(INVESTMT_MONEYMRKT_SECURITIES_MENU_ITEM, "Securities");
        systemMap.put(INVESTMT_MONEYMRKT_PRDIC_ACT_MENU_ITEM, "Periodic Activities");
        systemMap.put(INVESTMT_PPT_MENU_ITEM, "Property");
        systemMap.put(INVESTMT_PPT_TRN_MENU_ITEM, "Property Transactions");
        systemMap.put(INVESTMT_PPT_FUND_REQ_MENU_ITEM, "Place Order");
        systemMap.put(INVESTMT_PPT_PLACEMT_MENU_ITEM, "Settle Order");
        systemMap.put(INVESTMT_PPT_PRICE_REVAL_MENU_ITEM, "Property Revaluation");
        systemMap.put(INVESTMT_PPT_MNG_PLACEMT_MENU_ITEM, "Manage Property");

        systemMap.put(UNDERWRITING_VALUATION_MENU_ITEM, "Valuation");
        systemMap.put(ALM_SETUP_MENU_ITEM, "Setup");
        systemMap.put(ALM_GENDER_MORTALITY_MENU_ITEM, "Gender Mortality (Val.)");
        systemMap.put(ALM_PA90_CUMMUTATION_MENU_ITEM, "PA90 Cummutation");
        systemMap.put(UW_VALUATION_RPT_MENU_ITEM, "Perform Valuation");

        systemMap.put(ASSET_LIABILITY_MATCHING_MENU_ITEM, "Asset/Liability");
        systemMap.put(ASSET_VALUATION_MENU_ITEM, "Asset Valuation");
        systemMap.put(EQUITY_VALUATION_MENU_ITEM, "Equity Valuation");
        systemMap.put(BOND_VALUATION_MENU_ITEM, "Bond Valuation");
        systemMap.put(MONEYMKT_VALUATION_MENU_ITEM, "Money Markets Val.");
        systemMap.put(PROPERTY_VALUATION_MENU_ITEM, "Property Valuation");
        systemMap.put(ALM_VALUATION_RPT_MENU_ITEM, "Asset/Liability Matching");

        systemMap.put(INVESTMT_CSCSID_NO_MENU_ITEM, "CSCS ID");
        systemMap.put(INVESTMT_EQUITY_EXEC_ORDER_MENU_ITEM, "Execute Order");
        systemMap.put(INVEST_PORTFOLIO_SUMRY, "Portfolio Summary");
        systemMap.put(VIEW_INVEST_PORTFOLIO_SUMRY, "View Portfolio Summary");
        systemMap.put(INVESTMT_EQUITY_VIEW_EXECUTED_MENU_ITEM, "View Executed Orders");

        systemMap.put(ASSET_MANAGEMENT_MENU_ITEM, "Fixed Assets");
        systemMap.put(ASSET_MANAGEMENT_GENERAL_SETUP_MENU_ITEM, "General Asset Setup");

        systemMap.put(ASSET_CATEGORY_MENU_ITEM, "Asset Category");
        systemMap.put(ASSET_CONTRACT_TYPE_MENU_ITEM, "Contract Type");
        systemMap.put(ASSET_INSPECTOR_MENU_ITEM, "Asset Inspector");
        systemMap.put(ASSET_EXTERNAL_HOLDER_MENU_ITEM, "External Holder");
        systemMap.put(ASSET_BIDDER_MENU_ITEM, "Asset Bidder");
        systemMap.put(ASSET_DEPARTMENT_MENU_ITEM, "Department");
        systemMap.put(ASSET_DEPRECIATE_PERIOD_TYPE_MENU_ITEM, "Depreciation Period Type");

        systemMap.put(ASSET_TYPE_MGT_MENU_ITEM, "Asset Type Management");
        systemMap.put(ASSET_TYPE_MENU_ITEM, "Asset Type");
        systemMap.put(ASSET_TYPE_DEPRECIATE_RATE_MENU_ITEM, "Depreciation Rate(s)");
        systemMap.put(ASSET_TYPE_ELEMENT_MENU_ITEM, "Element(s)");
        systemMap.put(ASSET_TYPE_BENCHMARK_MENU_ITEM, "Benchmark(s)");
        systemMap.put(ASSET_TYPE_ATTRIBUTE_MENU_ITEM, "Attribute(s)");
        systemMap.put(ASSET_TYPE_LEASE_PENALTY_MENU_ITEM, "Lease Penalty");

        systemMap.put(ASSET_INSURANCE_MGT_MENU_ITEM, "Insurance Setup");

        //systemMap.put(ASSET_INSURANCE_TYPE_MENU_ITEM, "Insurance Type");
        systemMap.put(ASSET_INSURANCE_POLICY_CLASS_MENU_ITEM, "Policy Class");
        systemMap.put(ASSET_INSURANCE_PRODUCT_MENU_ITEM, "Insurance Product");
        systemMap.put(ASSET_INSURANCE_COVER_TYPE_MENU_ITEM, "Insurance Cover Type");
        systemMap.put(ASSET_INSURANCE_PRODUCT_COVER_TYPE_MENU_ITEM, "Cover Type Mapping");

        //
        systemMap.put(ASSET_INFO_MGT_MENU_ITEM, "Asset Info Management");
        systemMap.put(ASSET_ITEM_DETAIL_MENU_ITEM, "Asset(s) Details");

        systemMap.put(ASSET_CUSTODY_TRAIL_MENU_ITEM, "Custody Trail(s)");
        systemMap.put(ASSET_CONTRACT_DOCUMENT_MENU_ITEM, "Contract Document(s)");
        systemMap.put(ASSET_INSURANCE_ENTRY_MENU_ITEM, "Insurance Cover(s)");
        systemMap.put(ASSET_DEPRECIATE_TRAIL_MENU_ITEM, "Depreciation Trail(s)");
        systemMap.put(ASSET_DISPOSITION_MENU_ITEM, "Asset Disposition(s)");
        systemMap.put(ASSET_DISPOSITION_BID_MENU_ITEM, "Disposition Bid(s)");
        systemMap.put(ASSET_BID_DEFAULT_TRAIL_MENU_ITEM, "Bid Default Trail(s)");
        systemMap.put(ASSET_LEASE_DETAIL_MENU_ITEM, "Lease(s) History ");
        systemMap.put(ASSET_INSPECT_REGIME_MENU_ITEM, "Inspection Regime(s)");
        systemMap.put(ASSET_INSPECT_ELEMENT_MENU_ITEM, "Inspection Regime Element(s)");

        systemMap.put(HRM_SALARY_SCHEME_MENU_ITEM, "Salary Scheme Setup");

        systemMap.put(STOCK_TRANSFER_REQ_MENU_ITEM, "Transfer Inventory");

        systemMap.put(PROCURE_PRICING_N_QUOTES_MENU_ITEM, "Pricing & Quotations");
        systemMap.put(PROCURE_PROD_PRICE_LIST_MENU_ITEM, "Product Price List");
        systemMap.put(PROCURE_SUPPLIER_QUOTATIONS_MENU_ITEM, "Supplier Quotations");
        systemMap.put(PROCURE_CR8_QUOTATION_MENU_ITEM, "Create Quotation");
        systemMap.put(PROCURE_MNG_QUOTATION_MENU_ITEM, "Manage Quotations");
        systemMap.put(PROCURE_CR8_MRKT_PRICE_MENU_ITEM, "Create Price List");
        systemMap.put(PROCURE_MNG_MRKT_PRICE_MENU_ITEM, "Manage Price List");

        systemMap.put(PROCURE_PURCHASE_MGT_MENU_ITEM, "Purchases");
        systemMap.put(PROCURE_SUPPLIER_SETP_MENU_ITEM, "Supplier Setup");
        systemMap.put(PROCURE_PRODUCT_LINE_MENU_ITEM, "Product Line");
        systemMap.put(PROCURE_PURCHASE_ORDER_MENU_ITEM, "Purchase Order");
        systemMap.put(PROCURE_PURCHASE_ORDER_CR8_MENU_ITEM, "Create Purchase Order");
        systemMap.put(PROCURE_PURCHASE_ORDER_MNG_MENU_ITEM, "Maintain Purchase Orders");

        systemMap.put(CLIENT_TYP_MENU_ITEM, "Client Type Setup");

        systemMap.put(BUDGET_4CAST_MENU_ITEM, "Budget & Forecast");
        systemMap.put(BUDGET_LINE_ITEM_MENU_ITEM, "Budget Line Setup");
        systemMap.put(BUDGET_ENTRY_MENU_ITEM, "Create Budget");
        systemMap.put(BUDGET_MANAGE_MENU_ITEM, "Manage Budget");
        systemMap.put(CHANGE_VOTE_OF_CHARGE_MENU_ITEM, "Create Re-allocation");
        systemMap.put(MANAGE_VOTE_OF_CHARGE_MENU_ITEM, "Manage Re-allocation");
        systemMap.put(ADDITIONAL_BUDGET_MENU_ITEM, "Create Additional Budget");
        systemMap.put(MAINTAIN_ADDITIONAL_BUDGET_MENU_ITEM, "Manage Additional Budget");

        systemMap.put(FACULTY_SETP_MENU_ITEM, "Faculty Setup");

    }

    //Return the current year
    public int getCurrentYear() {
        return java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
    }

    //SetMultitenancy
    public boolean isMultiTenancyEnabled() {
        setMulti_tenancy_enabled(false);
        return isMulti_tenancy_enabled();
    }

    public String hideReportPanel() {
        reportPanelCollapsed = true;
        return "";
    }

    public String showReportPanel() {
        reportPanelCollapsed = false;
        return "";
    }

    public String backToCallerPage() {
        return FinultimateCommons.retrieveLastRequestingResource();
    }

    public String performSystemStateResetForMenuSelect(String pageUrl) {
        String outcome = "";

        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);

        if ((userManagerBean == null)
                || (userManagerBean.getUserAccount() == null)
                || (userManagerBean.getUserAccount().getUserName().trim().equals(""))) {
            try {
                outcome = userManagerBean.logOutUser();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
            if (applicationMessageBean == null) {
                applicationMessageBean = new ApplicationMessageBean();
                CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                        ApplicationMessageBean.class, applicationMessageBean);
            }

            applicationMessageBean.insertMessage("", MessageType.NONE);

            SessionDataReinitBean sessionDataReinitBean = (SessionDataReinitBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.sessionDataReinitBean}", SessionDataReinitBean.class);
            if (sessionDataReinitBean == null) {
                sessionDataReinitBean = new SessionDataReinitBean();
                CommonBean.setBeanToContext("#{sessionScope.sessionDataReinitBean}",
                        SessionDataReinitBean.class, sessionDataReinitBean);
            }
            sessionDataReinitBean.reinitSessionData();
            clearLinkStack();

            outcome = pageUrl;
        }

        return outcome;
    }

    public static String performSystemStateResetForButtonClick(String pageUrl) {
        String outcome = "";

        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);

        if ((userManagerBean == null)
                || (userManagerBean.getUserAccount() == null)
                || (userManagerBean.getUserAccount().getUserName().trim().equals(""))) {
            try {
                outcome = userManagerBean.logOutUser();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
            if (applicationMessageBean == null) {
                applicationMessageBean = new ApplicationMessageBean();
                CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                        ApplicationMessageBean.class, applicationMessageBean);
            }

            applicationMessageBean.insertMessage("", MessageType.NONE);

            outcome = pageUrl;
        }

        return outcome;
    }

    public void insertRequestingResource(String requestingResourceLink) {
        if (linkStack == null) {
            linkStack = new LinkStack();
        }

        LinkEntry entry = new LinkEntry();
        entry.setLinkUrl(requestingResourceLink);
        linkStack.put(entry);
    }

    public String retrieveLastRequestingResource() {
        LinkEntry entry = linkStack.pop();
        return entry.getLinkUrl();
    }

    public void captureRequestingResource() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String contextPath = request.getContextPath();

        insertRequestingResource(request.getRequestURI().substring(contextPath.length()));
    }

    public void captureRequestingResource(String contextPath) {
        insertRequestingResource(contextPath);
    }

    public void clearLinkStack() {
        linkStack = new LinkStack();
    }

    public boolean hasPrivilege(String privilegeName) {
        boolean hasPriv = false;
        if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            hasPriv = true;
        } else {
            UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.userManagerBean}", UserManagerBean.class);
            if (userManagerBean == null) {
                userManagerBean = new UserManagerBean();
                CommonBean.setBeanToContext("#{sessionScope.userManagerBean}",
                        UserManagerBean.class, userManagerBean);
            }

            hasPriv = userManagerBean.getUserAccount().getRole().hasPrivilege(privilegeName);
        }

        return hasPriv;
    }

    public boolean hasRWPrivilege(String privilegeName) {
        boolean hasPriv = false;

        if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            hasPriv = true;
        } else {
            UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.userManagerBean}", UserManagerBean.class);
            if (userManagerBean == null) {
                userManagerBean = new UserManagerBean();
                CommonBean.setBeanToContext("#{sessionScope.userManagerBean}",
                        UserManagerBean.class, userManagerBean);
            }
            hasPriv = userManagerBean.getUserAccount().getRole().hasRWPrivilege(privilegeName);
        }

        return hasPriv;
    }

    public String gotoCompanyIdentity() {
        return "/setup/companyidentity.jsf";
    }

    public boolean hasCompanyIdentity() {
        return hasPrivilege("/setup/companyidentity.jsf");
    }

    public String gotoBranchSetup() {
        return "/setup/branchsetup.jsf";
    }

    public String gotoBusinessDivision() {
        return "/setup/businessdivision.jsf";
    }

    public String gotoDepartmentSetup() {
        return "/setup/departmentsetup.jsf";
    }

    public String gotoRelationshipSetup() {
        return "/setup/relationshipsetup.jsf";
    }

    public String gotoTitleSetup() {
        return "/setup/titlesetup.jsf";
    }

    public String gotoCountrySetup() {
        return "/setup/countrysetup.jsf";
    }

    public String gotoStateSetup() {
        return "/setup/statesetup.jsf";
    }

    public String gotoCitySetup() {
        return "/setup/citysetup.jsf";
    }

    public String gotoCurrencySetup() {
        return "/setup/currencysetup.jsf";
    }

    public String gotoBankSetup() {
        return "/setup/banksetup.jsf";
    }

    public String gotoPfaSetup() {
        return "/clients/pfasetup.jsf";
    }

    public String gotoTrusteeSetup() {
        return "/clients/trusteesetup.jsf";
    }

    public String gotoEmployersSetup() {
        return "/clients/employersetup.jsf";
    }

    public String gotoReferrerSetup() {
        return "/clients/referrersetup.jsf";
    }

    public String gotoUnderwriterSetup() {
        return "/setup/underwritersetup.jsf";
    }

    public String gotoMailSetup() {
        return "/messaging/mailsetup.jsf";
    }

    public String gotoMedicalTestSetup() {
        return "/setup/medicaltestsetup.jsf";
    }

    public String gotoTransactionSource() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/transactionsource.jsf";
    }

    public String gotoExchangeRatePage() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/exchangerate.jsf";
    }

    public String gotoForexConverterPage() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/forexconverter.jsf";
    }

    public String gotoAccountSection() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/accountsections.jsf";
    }

    public String gotoAccountGroup() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/accountgroups.jsf";
    }

    public String gotoChartOfAccounts() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/chartofaccounts.jsf";
    }

    public String gotoSubledgerChartOfAccounts() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/subledgerchartofaccounts.jsf";
    }

    public String gotoPerformFXRevaluationPage() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/performfxrevaluation.jsf";
    }

    public String gotoViewFXRevaluationPage() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/viewfxrevaluation.jsf";
    }

    public String gotoBudgetLineItem() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/budget/budgetlineitem.jsf";
    }

    public String gotoBudgetCreation() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/budget/budgetentry.jsf";
    }

    public String gotoBudgetManagement() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/budget/managebudget.jsf";
    }

    public String gotoChangeVoteOfCharge() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/budget/changevoteofcharge.jsf";
    }

    public String gotoCreateAdditionalBudget() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/budget/createsupplementbudget.jsf";
    }

    public String gotoManageAdditionalBudget() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/budget/managesupplementbudget.jsf";
    }

    public String gotoManageVoteOfCharge() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/budget/managevoteofcharge.jsf";
    }

    public String gotoFinancialYear() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/financialyear.jsf";
    }

    public String gotoAccountingPeriod() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/accountingperiod.jsf";
    }

    public String gotoTransactionTypeSetup() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/transactiontypesetup.jsf";
    }

    public String gotoTranTypeBizActMappingSetup() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/trantypebizmappingsetup.jsf";
    }

    public String gotoPartnerTypeAccountSetup() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/partnerglgrpacctcodesetup.jsf";
    }

    public String gotoViewAccountBudget() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/budget/viewaccountsbudget.jsf";
    }

    public String gotoCreateAccountBudget() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/budget/createaccountsbudget.jsf";
    }

    public String gotoGlJournal() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/gljournalentry.jsf";
    }

    public String gotoViewGlJournal() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/gljournalmanage.jsf";
    }

    public String gotoAccountTransactions() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/accounttransactions.jsf";
    }

    public String gotoSubledgerAccountTransactions() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/subledgeraccounttransactions.jsf";
    }

    public String gotoBalanceSheet() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/balancesheetreport.jsf";
    }

    public String gotoTrialBalance() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/trialbalance.jsf";
    }

    public String gotoProfitAndLoss() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/profitandlossreport.jsf";
    }

    public String gotoIncomeStatement() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/incomestatement.jsf";
    }

    public String gotoSuspenseAcctStmt() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/suspenseacctstmt.jsf";
    }

    public String gotoCashBookTransactions() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/cashbooktransactions.jsf";
    }

    public String gotoCreateBankTransactions() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/createbanktransactions.jsf";
    }

    public String gotoViewBankTransactions() {
        activeSubsystem = ActiveSubsystemType.ACCOUNTING;
        return "/gledger/viewbankstatements.jsf";
    }

    public String gotoIdentityTypeSetup() {
        return "/clients/identitytypesetup.jsf";
    }

    public String gotoProofOfAddressTypeSetup() {
        return "/clients/proofofaddrtypesetup.jsf";
    }

    public String gotoReferralTypeSetup() {
        return "/clients/referraltypesetup.jsf";
    }

    public String gotoClientSetup() {
        return "/clients/clientsetup.jsf";
    }

    public String gotoClientSearch() {
        return "/clients/clientsearch.jsf";
    }

    public String gotoAgentType() {
        return "/agents/agenttype.jsf";
    }

    public String gotoAgency() {
        return "/agents/agency.jsf";
    }

    public String gotoAgentSearch() {
        return "/agents/agentsearch.jsf";
    }

    public String gotoAgentPerformanceTree() {
        return "/agents/agentperformancetree.jsf";
    }

    public String gotoEscalationRateSetup() {
        return "/insurancesetup/escalationratesetup.jsf";
    }

    public String gotoProductTypeSetup() {
        return "/insurancesetup/producttypesetup.jsf";
    }

    public String gotoTaxAuthoritiesSetup() {
        return "/sales/taxauthorities.jsf";
    }

    public String gotoTaxSetup() {
        return "/sales/taxsetup.jsf";
    }

    public String gotoSalesDiscountSetup() {
        return "/sales/salesdiscount.jsf";
    }

    public String gotoGenderMortality() {
        return "/underwriting/gendermortality.jsf";
    }

    public String gotoPricingSetup() {
        return "/underwriting/pricingsetup.jsf";
    }

    public String gotoTariffRatePage() {
        return "/underwriting/computedtariffrate.jsf";
    }

    public String gotoAddTariffRatePage() {
        return "/underwriting/computedtariffratecreate.jsf";
    }

    public String gotoCreateQuote() {
        return "/underwriting/createquote.jsf";
    }

    public String gotoCreatePolicy() {
        return "/underwriting/createpolicy.jsf";
    }

    public String gotoEditProposal() {
        return "/underwriting/editproposal.jsf";
    }

    public String gotoPolicyUpload() {
        return "/underwriting/policyupload.jsf";
    }

    public String gotoDailyProduction() {
        return "/receivables/common/dailyproduction.jsf";
    }

    public String gotoDiscountInterestRate() {
        return "/claims/discountinterestratesetup.jsf";
    }

    public String gotoRegisterClaim() {
        return "/claims/registerclaim.jsf";
    }

    public String gotoMaintainClaim() {
        return "/claims/maintainclaim.jsf";
    }

    public String gotoRegisterSurrender() {
        return "/surrender/registersurrender.jsf";
    }

    public String gotoMaintainSurrender() {
        return "/surrender/maintainsurrender.jsf";
    }

    public String gotoReceipt() {
        return "/receivables/common/createreceipt.jsf";
    }

    public String gotoReceiptSearch() {
        return "/receivables/common/receiptsearch.jsf";
    }

    public String gotoReceiptProduction() {
        return "/receivables/common/dailyproduction.jsf";
    }

    public String gotoBusinessModule() {
        return "/receivables/common/businessmodule.jsf";
    }

    public String gotoCollectionModeSetup() {
        return "/receivables/common/collectionmodesetup.jsf";
    }

    public String gotoCollectionSourceSetup() {
        return "/receivables/common/collectionsourcesetup.jsf";
    }

    public String gotoPayoutChannelSetup() {
        return "/setup/payoutchannelsetup.jsf";
    }

    public String gotoRequisitionMonitor() {
        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);
        userManagerBean.gotoReqApprovialPage();
        return "/requisition/requisitionmonitor.jsf";
    }

    public String gotoWorkflowActionStepPage() {
        return "/requisition/workflowactionstep.jsf";
    }

    public String gotoWorkflowApprovalLimitPage() {
        return "/requisition/workflowapprovallimit.jsf";
    }

    public String gotoAccountCodeingPage() {
        return "/requisition/accountcodingpage.jsf";
    }

    public String gotoPaymentView() {
        return "/requisition/paymentview.jsf";
    }

    public String gotoPayoutStatusView() {
        return "/requisition/payouttransactionsearch.jsf";
    }

    public String gotoChangePassword() {
        return "/security/changepassword.jsf";
    }

    public String gotoCreateUser() {
        return "/security/createuser.jsf";
    }

    public String gotoManageUserAccount() {
        return "/security/manageuseraccount.jsf";
    }

    public String gotoCreateRole() {
        return "/security/createrole.jsf";
    }

    public String gotoCreateMenu() {
        return "/security/createmenu.jsf";
    }

    public String gotoCreateSubsystem() {
        return "/security/createsubsystem.jsf";
    }

    public String gotoCreateApplication() {
        return "/security/createapplicationsystem.jsf";
    }

    public String gotoUserRole() {
        return "/security/roles.jsf";
    }

    public String gotoLoggedOnUsers() {
        return "/security/loggedonuseraccount.jsf";
    }

    public String gotoAuditTrail() {
        return "/security/audittrailadmin.jsf";
    }

    public String gotoRoleSwitcher() {
        return "/security/switchuserrole.jsf";
    }

    public String gotoReportCategorySetup() {
        return "/setup/reportcategorysetup.jsf";
    }

    public String gotoReportConfigurer() {
        return "/reports/reportconfigurer.jsf";
    }

    public String gotoChartConfigurer() {
        return "/reports/chartconfigurer.jsf";
    }

    public String gotoReportTriggerSetup() {
        return "/reports/reporttriggersetup.jsf";
    }

    public String gotoReportScheduleSetup() {
        return "/reports/reportschedulesetup.jsf";
    }

    public String gotoMessagingTriggerSetup() {
        return "/messaging/messagetriggersetup.jsf";
    }

    public String gotoMessagingScheduleSetup() {
        return "/messaging/messageschedulesetup.jsf";
    }

    public String gotoExprFunctionSetup() {
        return "/reports/expressionfunction.jsf";
    }

    public String gotoSelfAppraisalPage() {
        EmployeeAppraisalBean employeeAppraisalBean = (EmployeeAppraisalBean) CommonBean.getBeanFromContext(
                "#{sessionScope.employeeAppraisalBean}", EmployeeAppraisalBean.class);
        if (employeeAppraisalBean == null) {
            employeeAppraisalBean = new EmployeeAppraisalBean();
            CommonBean.setBeanToContext(
                    "#{sessionScope.employeeAppraisalBean}", EmployeeAppraisalBean.class, employeeAppraisalBean);
        }

        employeeAppraisalBean.setSupervisorAppraisal(false);
        employeeAppraisalBean.employeeSelectedFromLogin();
        return performSystemStateResetForMenuSelect("/hrm/staffmgt/kpi/employeeappraisal.jsf");
    }

    public String gotoSupervisorAppraisalPage() {
        EmployeeAppraisalBean employeeAppraisalBean = (EmployeeAppraisalBean) CommonBean.getBeanFromContext(
                "#{sessionScope.employeeAppraisalBean}", EmployeeAppraisalBean.class);
        if (employeeAppraisalBean == null) {
            employeeAppraisalBean = new EmployeeAppraisalBean();
            CommonBean.setBeanToContext(
                    "#{sessionScope.employeeAppraisalBean}", EmployeeAppraisalBean.class, employeeAppraisalBean);
        }

        employeeAppraisalBean.setSupervisorAppraisal(true);
        employeeAppraisalBean.setAppraisalScoreType(AppraisalScoreType.APPRAISAL);
        return performSystemStateResetForMenuSelect("/hrm/staffmgt/kpi/employeeappraisal.jsf");
    }

    public String gotoFinalAppraisalPage() {
        EmployeeAppraisalBean employeeAppraisalBean = (EmployeeAppraisalBean) CommonBean.getBeanFromContext(
                "#{sessionScope.employeeAppraisalBean}", EmployeeAppraisalBean.class);
        if (employeeAppraisalBean == null) {
            employeeAppraisalBean = new EmployeeAppraisalBean();
            CommonBean.setBeanToContext(
                    "#{sessionScope.employeeAppraisalBean}", EmployeeAppraisalBean.class, employeeAppraisalBean);
        }

        employeeAppraisalBean.setSupervisorAppraisal(true);
        employeeAppraisalBean.setAppraisalScoreType(AppraisalScoreType.FINAL);
        return performSystemStateResetForMenuSelect("/hrm/staffmgt/kpi/employeefinalappraisal.jsf");
    }

    public String gotoFaculty() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        return "/academics/setup/faculty.jsf";
    }

    /**
     * @return the linkStack
     */
    public LinkStack getLinkStack() {
        return linkStack;
    }

    /**
     * @param linkStack the linkStack to set
     */
    public void setLinkStack(LinkStack linkStack) {
        this.linkStack = linkStack;
    }

    /**
     * @return the reportPanelCollapsed
     */
    public boolean isReportPanelCollapsed() {
        return reportPanelCollapsed;
    }

    /**
     * @param reportPanelCollapsed the reportPanelCollapsed to set
     */
    public void setReportPanelCollapsed(boolean reportPanelCollapsed) {
        this.reportPanelCollapsed = reportPanelCollapsed;
    }

    /**
     * @return the systemMap
     */
    public Map<String, String> getSystemMap() {
        return systemMap;
    }

    /**
     * @param systemMap the systemMap to set
     */
    public void setSystemMap(Map<String, String> systemMap) {
        this.systemMap = systemMap;
    }

    /**
     * @param subsystemName the subsystemName to set
     */
    public void setSubsystemName(String subsystemName) {
        this.subsystemName = subsystemName;
    }

    /**
     * @return the multi_tenancy_enabled
     */
    public boolean isMulti_tenancy_enabled() {
        return multi_tenancy_enabled;
    }

    /**
     * @param multi_tenancy_enabled the multi_tenancy_enabled to set
     */
    public void setMulti_tenancy_enabled(boolean multi_tenancy_enabled) {
        this.multi_tenancy_enabled = multi_tenancy_enabled;
    }

    /**
     * @return the changedLocale
     */
    public boolean isChangedLocale() {
        return changedLocale;
    }

    /**
     * @param changedLocale the changedLocale to set
     */
    public void setChangedLocale(boolean changedLocale) {
        this.changedLocale = changedLocale;
    }

}
