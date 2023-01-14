package com.shopme.setting;

import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {
    @Autowired private SettingRepository settingRepo;
    @Autowired private CurrencyRepository currencyRepo;

    public List<Setting> getGeneralSettings() {
        return settingRepo.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

    public EmailSettingTemplate getEmailSettings() {
        List<Setting> settings = settingRepo.findByCategory(SettingCategory.MAIL_SERVER);
        settings.addAll(settingRepo.findByCategory(SettingCategory.MAIL_TEMPLATES));

        return new EmailSettingTemplate(settings);
    }

    public CurrencySettingTemplate getCurrencySettings() {
        List<Setting> settings = settingRepo.findByCategory(SettingCategory.CURRENCY);
        return new CurrencySettingTemplate(settings);
    }

    public PaymentSettingTemplate getPaymentSettings() {
        List<Setting> settings = settingRepo.findByCategory(SettingCategory.PAYMENT);
        return new PaymentSettingTemplate(settings);
    }

    public String getCurrencyCode() {
        Setting setting = settingRepo.findByKey("CURRENCY_ID");
        Integer currencyId = Integer.parseInt(setting.getValue());
        Currency currency = currencyRepo.findById(currencyId).get();

        return currency.getCode();
    }
}
