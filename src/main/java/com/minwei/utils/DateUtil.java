package com.minwei.utils;

import com.minwei.enums.DateFormatEnum;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lmw
 */

@Slf4j
public class DateUtil {


    /**
     * 将字符串转为Date
     *
     * @param dateStr
     * @return
     */
    public static Date parseDate(String dateStr) {
        // 获取DateFormatEnum中的所有枚举值
        DateFormatEnum[] formats = DateFormatEnum.values();

        for (DateFormatEnum format : formats) {
            String formatValue = format.getValue();
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(formatValue);
                return formatter.parse(dateStr);
            } catch (Exception e) {
                log.error("日期：{},日期格式：{}，转换异常，正尝试下一日期格式", dateStr, formatValue);
            }
        }
        throw new RuntimeException("日期：" + dateStr + ",未找到匹配的日期格式");
    }

    public static String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }
}
