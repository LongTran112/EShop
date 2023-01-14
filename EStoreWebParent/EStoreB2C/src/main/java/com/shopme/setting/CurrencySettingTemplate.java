package com.shopme.setting;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingTemplate;

import java.util.List;

public class CurrencySettingTemplate extends SettingTemplate {

    public CurrencySettingTemplate(List<Setting> listSettings) {
        super(listSettings);
    }

    public String getSymbol() {
        return super.getValue("CURRENCY_SYMBOL");
    }

    public String getSymbolPosition() {
        return super.getValue("CURRENCY_SYMBOL_POSITION");
    }

    public String getDecimalPointType() {
        return super.getValue("DECIMAL_POINT_TYPE");
    }

    public String getThousandPointType() {
        return super.getValue("THOUSANDS_POINT_TYPE");
    }

    public int getDecimalDigits() {
        return Integer.parseInt(super.getValue("DECIMAL_DIGITS"));
    }
}
