package org.wf.dp.dniprorada.engine.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.StringUtils;
import org.wf.dp.dniprorada.constant.Currency;
import org.wf.dp.dniprorada.constant.Language;
import org.wf.dp.dniprorada.liqPay.LiqBuy;

public abstract class BaseMailTaskWithAttachment implements JavaDelegate {

    private static final String TAG_PAYMENT_BUTTON_LIQPAY = "[paymentButton_LiqPay]";
    
    private static final String LIQPAY_CALLBACK_URL = "https://test.region.igov.org.ua/wf-central/service/setPaymentStatus_TaskActiviti?sID_Order={0}&sID_PaymentSystem=\"Liqpay\"&sData = \"\"";

    protected Expression sID_Merchant;
    protected Expression sSum;
    protected Expression sID_Currency;
    protected Expression sLanguage;
    protected Expression sDescription;
    protected Expression nID_Subject;
    
    protected String replaceTags(String textStr, DelegateExecution execution) throws Exception {
        if (textStr == null) {
            return null;
        }
        String textWithoutTags = textStr;
        if (textStr.contains(TAG_PAYMENT_BUTTON_LIQPAY)) {
            String sID_Merchant = getStringFromFieldExpression(this.sID_Merchant, execution);
            String sSum = getStringFromFieldExpression(this.sSum, execution);
            Currency sID_Currency = Currency.valueOf(getStringFromFieldExpression(this.sID_Currency, execution));
            Language sLanguage = LiqBuy.DEFAULT_LANG;
            String sDescription = getStringFromFieldExpression(this.sDescription, execution);
            String sID_Order = "TaskActiviti_" + execution.getId();  // TODO: not sure about id
            String sURL_CallbackStatusNew = StringUtils.replace(LIQPAY_CALLBACK_URL, "{0}", sID_Order); 
            String sURL_CallbackPaySuccess = null;
            Long nID_Subject = getLongFromFieldExpression(this.nID_Subject, execution);
            boolean bTest = true; // Impossible to get bTest in wf-base.
            String htmlButton = new LiqBuy().getPayButtonHTML_LiqPay(sID_Merchant, sSum, sID_Currency, sLanguage, sDescription, sID_Order, sURL_CallbackStatusNew, sURL_CallbackPaySuccess, nID_Subject, bTest);
            textWithoutTags = StringUtils.replace(textStr, TAG_PAYMENT_BUTTON_LIQPAY, htmlButton);
        }
        return textWithoutTags;
    }

    protected String getStringFromFieldExpression(Expression expression, DelegateExecution execution) {
        if (expression != null) {
            Object value = expression.getValue(execution);
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }
    
    protected Long getLongFromFieldExpression(Expression expression, DelegateExecution execution) {
        if (expression != null) {
            Object value = expression.getValue(execution);
            if (value != null) {
                return Long.valueOf(value.toString());
            }
        }
        return null;
    }

}
