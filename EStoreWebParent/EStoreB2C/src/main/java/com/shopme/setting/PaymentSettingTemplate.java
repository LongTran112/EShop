package com.shopme.setting;


import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingTemplate;

import javax.servlet.http.PushBuilder;
import java.util.List;

public class PaymentSettingTemplate extends SettingTemplate {

    public PaymentSettingTemplate(List<Setting> listSettings) {
        super(listSettings);
    }

    public String getUrl(){
        return super.getValue("PAYPAL_API_BASE_URL");
    }

    public String getClientId(){
        return super.getValue("PAYPAL_API_CLIENT_ID");
    }

    public String getClientSecret(){
        return super.getValue("PAYPAL_API_CLIENT_SECRET");
    }


}
