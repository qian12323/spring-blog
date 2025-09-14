// src/main/java/qian/blog_api/util/DateUtil.java
package qian.blog_api.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    // 定义默认日期格式
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // 格式化LocalDateTime为字符串
    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return FORMATTER.format(dateTime);
    }
}