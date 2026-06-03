package com.common.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.AntPathMatcher;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 字符串工具类
 *
 * @author Lion Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static final String SEPARATOR = ",";
    public static final String SQL_DELIMITER = ";";
    public static final String REDIS_DELIMITER = ":";
    public static final String COLUMN_PREFIX = "C_";
    private static final Map<Integer, String> numToChinese = new HashMap<>();
    private static final String[] units = {"", "十", "百", "千", "万", "亿"};
    private static final String zero = "零";

    static {
        numToChinese.put(0, "零");
        numToChinese.put(1, "一");
        numToChinese.put(2, "二");
        numToChinese.put(3, "三");
        numToChinese.put(4, "四");
        numToChinese.put(5, "五");
        numToChinese.put(6, "六");
        numToChinese.put(7, "七");
        numToChinese.put(8, "八");
        numToChinese.put(9, "九");
    }

    /**
     * 处理字符串列表
     * 更新字符串列表中，如果目标字符串不在列表中，则添加
     *
     * @param source 源
     * @param target 目标
     * @return {@link String }
     */
    public static String processStrList(String source, String target) {
        if (StringUtils.isBlank(source)) {
            source = target;
        } else {
            // 以分号分隔，判重
            if (!Arrays.asList(source.split(StringUtils.SQL_DELIMITER)).contains(target)) {
                source = target + StringUtils.SQL_DELIMITER + source;
            }
        }
        return source;
    }

    /**
     * 解析登录id为帐户id
     *
     * @param loginId 登录ID
     * @return {@link Long }
     */
    public static Long parseLoginId2AccountId(Object loginId) {
        String loginKey = String.valueOf(loginId);
        // 以:分割，获取最后一个即账号id
        return Long.parseLong(loginKey.substring(loginKey.lastIndexOf(StringUtils.REDIS_DELIMITER) + 1));
    }

    /**
     * 获取当月第一天的格式化字符串
     *
     * @return
     */
    public static String getFirstDayOfMonthFormatted() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return firstDayOfMonth.format(formatter);
    }

    /**
     * 获取当前月份格式化字符串
     *
     * @return
     */
    public static String getCurrentMonthFormatted() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return currentDate.format(formatter).substring(0, 6);
    }

    /**
     * 获取当前日期格式化字符串
     *
     * @return 当前日期格式化字符串
     */
    public static String getCurrentDateFormatted() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return currentDate.format(formatter);
    }

    /**
     * 从url提取oss路径
     *
     * @param url URL
     * @return {@link String }
     */
    public static String extractOssPathFromUrl(String url) {
        // 提取目录路径
        url = url.replace("//", "@");
        int targetIndex = url.indexOf('/');
        return url.substring(targetIndex + 1);
    }

    /**
     * 将字符串末尾分号去除
     *
     * @param str 待处理的字符串
     * @return 处理后的字符串
     */
    public static String removeLastSemicolon(String str) {
        if (str == null) {
            return null; // 如果输入为null，直接返回null
        }

        int length = str.length();
        if (length == 0) {
            return str; // 如果字符串为空，直接返回
        }

        // 检查最后一个字符是否为分号
        if (str.charAt(length - 1) == ';' || str.charAt(length - 1) == '；') {
            // 去除最后一个字符
            return str.substring(0, length - 1);
        } else {
            return str; // 如果没有分号，直接返回原字符串
        }
    }

    /**
     * 将字符串末尾分号去除
     */

    /**
     * 将数字转换为中文
     *
     * @param number number
     * @return {@link String }
     */
    public static String convertNumberToChinese(long number) {
        if (number == 0) {
            return numToChinese.get(0);
        }

        StringBuilder chineseNum = new StringBuilder();
        int unitIndex = 0;

        while (number > 0) {
            long digit = number % 10;
            number /= 10;

            if (digit != 0 || chineseNum.length() == 0 || !chineseNum.toString().endsWith(zero)) {
                chineseNum.insert(0, numToChinese.get((int) digit));
                chineseNum.insert(0, units[unitIndex]);
            } else if (!chineseNum.toString().startsWith(zero)) {
                chineseNum.insert(0, zero);
            }

            unitIndex++;
            if (unitIndex == units.length - 1) { // Reset unit index after '亿'
                unitIndex = 1;
            }
        }

        // Remove leading '零' if exists
        if (chineseNum.toString().startsWith(zero)) {
            chineseNum.delete(0, 1);
        }

        return chineseNum.toString();
    }

    /**
     * 格式化为Gbk
     *
     * @param str str
     * @return {@link String }
     */
    public static String format2ValidName(String str) {
        if (isBlank(str)) {
            return str;
        }
        // 使用 replaceAll() 方法去除所有空白字符
        String cleanName = format2Gbk(str);
        // 去除 除了'.' 和 '·'外的特殊字符
//        cleanName = cleanName.replaceAll("[^\\u4e00-\\u9fa5a-zA-Z0-9.·]", "");
        cleanName = cleanName.replaceAll("[^\\u4e00-\\u9fa5.·]", "");
        return cleanName;
    }

    /**
     * 格式化为Gbk
     *
     * @param str str
     * @return {@link String }
     */
    public static String format2Gbk(String str) {
        if (isBlank(str)) {
            return str;
        }
        // 使用 replaceAll() 方法去除所有空白字符
        String cleanName = str.replaceAll("\\s+", "");

        // 使用 replace() 方法将 '•' 替换为 '·'
        cleanName = cleanName.replace('•', '·');
        return cleanName;
    }

    /**
     * 删除空白字符串
     *
     * @param str str
     * @return {@link String }
     */
    public static String removeBlankStr(String str) {
        if (isBlank(str)) {
            return str;
        }
        // 使用 replaceAll() 方法去除所有空白字符
        return str.replaceAll("\\s+", "");
    }

    /**
     * 是否兼容gbk
     *
     * @param input 输入
     * @return boolean
     */
    public static boolean isGbkCompatible(String input) {
        try {
            // 将字符串编码为GBK字节数组
            byte[] gbkBytes = input.getBytes("GBK");
            // 再次将字节数组解码为字符串
            String decoded = new String(gbkBytes, "GBK");
            // 比较原始字符串和解码后的字符串是否一致
            return input.equals(decoded);
        } catch (UnsupportedEncodingException e) {
            // 如果捕获到异常，说明字符串中包含GBK不支持的字符
            return false;
        }
    }

    /**
     * 包含混合内容（中文，英文和数字）
     *
     * @param s S
     * @return boolean
     */
    public static boolean containsMixedContent(String s) {
        // 实现一个逻辑来判断字符串是否包含混合内容
        // 可以使用正则表达式或者其他方法
        return s.matches(".*[\\u4e00-\\u9fa5].*") && s.matches(".*[a-zA-Z0-9].*");
    }

    /**
     * 包含中文
     *
     * @param s S
     * @return boolean
     */
    public static boolean containsChinese(String s) {
        // 判断字符串是否包含中文字符
        return s.matches(".*[\\u4e00-\\u9fa5].*");
    }

    /**
     * 包含字母或数字
     *
     * @param str str
     * @return boolean
     */
    public static boolean containsLetterOrDigit(String str) {
        // 正则表达式检查字符串是否包含任何字母或数字
        String regex = "[a-zA-Z0-9]";

        // 检查字符串是否包含至少一个匹配项
        return str.matches(".*" + regex + ".*");
    }

    /**
     * 将秒转为时分秒字符串
     */
    public static String convertSecondsToTimeFormat(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    /**
     * 获取Controller类中的service对象
     *
     * @param className
     * @return
     */
    // 将类名转换为字段名
    public static String getServiceNameFromControllerName(String className) {
        // 去掉 "Controller" 后缀
        if (className.endsWith("Controller")) {
            className = className.substring(0, className.length() - "Controller".length());
        } else {
            throw new RuntimeException("输入的不是controller格式！");
        }

        // 首字母小写
        if (className.length() > 0) {
            className = Character.toLowerCase(className.charAt(0)) + className.substring(1);
        }

        // 添加 "Service" 后缀
        className += "Service";

        return className;
    }

    /**
     * 获取字符串最后一个”.“后的内容（用于获取实体类）
     *
     * @param input
     * @return
     */
    public static String getLastSegment(String input) {
        if (input == null || !input.contains(".")) {
            return input;
        }
        int lastDotIndex = input.lastIndexOf('.');
        return input.substring(lastDotIndex + 1);
    }

    /**
     * 拼接两个字符串并添加到指定层级构建Map
     *
     * @param name1       name1
     * @param name2       name2
     * @param level       层级
     * @param nameMapList Map列表
     */
    public static void transform2uniqueNameLevelMap(String name1, String name2, int level, List<HashMap<String, Integer>> nameMapList) {
        HashMap<String, Integer> uniqueNameMap = transform2uniqueNameLevelMap(name1, name2, level);
        nameMapList.add(uniqueNameMap);
    }

    /**
     * 拼接两个字符串并添加到指定层级构建Map
     * 1级设置无需设置name2
     *
     * @param name1 name1
     * @param name2 name2
     * @param level 层级
     * @return {@link HashMap }<{@link String }, {@link Integer }>
     */
    public static HashMap<String, Integer> transform2uniqueNameLevelMap(String name1, String name2, int level) {
        HashMap<String, Integer> map = new HashMap<>();
        if (StringUtils.isEmpty(name1)) {
            return map;
        }
        switch (level) {
            case 1:
                map.put(name1, 1);
                break;
            case 2:
                String rootName = concat(new String[]{name1, name2});
                // 注意：这里直接操作了传入对象的属性，这可能不是最佳实践，应根据实际情况调整
                map.put(rootName, 2);
                break;
            case 3:
                String[] parentNames = name1.split(StringUtils.SEPARATOR);
                String secondName = concat(new String[]{parentNames[parentNames.length - 1], name2});
                map.put(secondName, 3);
                break;
            default:
                String[] lastParentNames = name1.split(StringUtils.SEPARATOR);
                String otherName = concat(new String[]{lastParentNames[lastParentNames.length - 1], name2});
                map.put(otherName, level);
                break;
        }
        return map;
    }

    /**
     * 以,为分隔符拼接字符串
     *
     * @param sources 待拼接字符串数组
     * @return 拼接后的字符串
     */
    public static String concat(String[] sources) {
        return StrUtil.join(SEPARATOR, sources);
    }

    /**
     * 以separate为分隔符拼接字符串
     *
     * @param sources  待拼接字符串数组
     * @param separate 分隔符
     * @return 拼接后的字符串
     */
    public static String concat(String[] sources, String separate) {
        return StrUtil.join(separate, sources);
    }

    /**
     * 将包含字符串的数组转换为Long类型的数组
     *
     * @param stringIds 字符串数组
     * @return 转换后的Long数组
     */
    public static Long[] stringArrayToLongArray(String[] stringIds) {
        List<Long> longList = new ArrayList<>();
        for (String id : stringIds) {
            try {
                longList.add(Long.parseLong(id));
            } catch (NumberFormatException e) {
                // 处理无效的字符串，不能转换为Long
                System.err.println("Invalid ID: " + id + ", skipping...");
            }
        }
        return longList.toArray(new Long[0]);
    }

    /**
     * 所有字母大写并加上C_的前缀
     *
     * @param camelCaseName
     * @return
     */
    public static String convertToDbColumnName(String camelCaseName) {
        StringBuilder snakeCaseBuilder = new StringBuilder();
        if (!camelCaseName.isEmpty()) {
            snakeCaseBuilder.append("C_");
            for (char c : camelCaseName.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    snakeCaseBuilder.append('_');
                    snakeCaseBuilder.append(Character.toUpperCase(c));
                } else {
                    snakeCaseBuilder.append(Character.toUpperCase(c));
                }
            }
        }
        return snakeCaseBuilder.toString();
    }

    /**
     * 将字符串转换为数据表字段标准格式
     *
     * @param input 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String toSqlFieldName(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("C_"); // 添加前缀

        // 跟踪上一个字符是否大写
        boolean lastCharWasUpperCase = false;
        for (char c : input.toCharArray()) {
            if (Character.isUpperCase(c)) {
                // 如果当前字符是大写，并且上一个字符不是大写，则添加下划线
                if (!lastCharWasUpperCase) {
                    builder.append("_");
                }
                lastCharWasUpperCase = true;
            } else {
                // 如果当前字符是小写，则上一个字符是小写
                lastCharWasUpperCase = false;
            }
            builder.append(c);
        }
        // 以_分割字符并转换为大写字母后再以_合并
        List<String> stringList = Arrays.stream(builder.toString().split("_")).map(String::toUpperCase).collect(Collectors.toList());
        return String.join("_", stringList);
    }

    /**
     * 生成指定长度的随机字符串
     *
     * @param i 指定的随机字符串长度。
     * @return 一个长度为i的随机字符串，包含大小写字母和数字。
     */
    public static String getRandomString(int i) {
        // 生成指定length的随机字符串（A-Z，a-z，0-9）
//        return RandomStringUtils.randomAlphanumeric(i);
        return generateRandomString(i);
    }

    /**
     * 生成指定长度的随机字符串，不包含容易混淆的字符（如 O 和 0，I 和 1 等）。
     *
     * @param length 字符串的长度
     * @return 生成的随机字符串
     */
    public static String generateRandomString(int length) {
        // 允许使用的字符集合
        String allowedChars = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";

        // 创建随机数生成器
        Random random = new Random();

        // 生成随机字符串
        return IntStream.range(0, length)
            .mapToObj(i -> String.valueOf(allowedChars.charAt(random.nextInt(allowedChars.length()))))
            .collect(Collectors.joining());
    }

    /**
     * 生成指定长度的随机数字字符串
     *
     * @param i 指定的随机字符串长度。
     * @return 一个长度为i的随机字符串，只包含数字。
     */
    public static String getRandomNumbers(int i) {
        // 生成指定length的随机字符串（0-9）
        return RandomUtil.randomNumbers(i);
    }

    /**
     * 获取参数不为空值
     *
     * @param str defaultValue 要判断的value
     * @return value 返回值
     */
    public static String blankToDefault(String str, String defaultValue) {
        return StrUtil.blankToDefault(str, defaultValue);
    }

    /**
     * * 判断一个字符串是否为空串
     *
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str) {
        return StrUtil.isEmpty(str);
    }

    /**
     * * 判断一个字符串是否为非空串
     *
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 去空格
     */
    public static String trim(String str) {
        return StrUtil.trim(str);
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @return 结果
     */
    public static String substring(final String str, int start) {
        return substring(str, start, str.length());
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @param end   结束
     * @return 结果
     */
    public static String substring(final String str, int start, int end) {
        return StrUtil.sub(str, start, end);
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is {} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params   参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... params) {
        return StrUtil.format(template, params);
    }

    /**
     * 是否为http(s)://开头
     *
     * @param link 链接
     * @return 结果
     */
    public static boolean ishttp(String link) {
        return Validator.isUrl(link);
    }

    /**
     * 字符串转set
     *
     * @param str 字符串
     * @param sep 分隔符
     * @return set集合
     */
    public static Set<String> str2Set(String str, String sep) {
        return new HashSet<>(str2List(str, sep, true, false));
    }

    /**
     * 字符串转list
     *
     * @param str         字符串
     * @param sep         分隔符
     * @param filterBlank 过滤纯空白
     * @param trim        去掉首尾空白
     * @return list集合
     */
    public static List<String> str2List(String str, String sep, boolean filterBlank, boolean trim) {
        List<String> list = new ArrayList<>();
        if (isEmpty(str)) {
            return list;
        }

        // 过滤空白字符串
        if (filterBlank && isBlank(str)) {
            return list;
        }
        String[] split = str.split(sep);
        for (String string : split) {
            if (filterBlank && isBlank(string)) {
                continue;
            }
            if (trim) {
                string = trim(string);
            }
            list.add(string);
        }

        return list;
    }

    /**
     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串同时串忽略大小写
     *
     * @param cs                  指定字符串
     * @param searchCharSequences 需要检查的字符串数组
     * @return 是否包含任意一个字符串
     */
    public static boolean containsAnyIgnoreCase(CharSequence cs, CharSequence... searchCharSequences) {
        return StrUtil.containsAnyIgnoreCase(cs, searchCharSequences);
    }

    /**
     * 驼峰转下划线命名
     */
    public static String toUnderScoreCase(String str) {
        return StrUtil.toUnderlineCase(str);
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs) {
        return StrUtil.equalsAnyIgnoreCase(str, strs);
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String name) {
        return StrUtil.upperFirst(StrUtil.toCamelCase(name));
    }

    /**
     * 驼峰式命名法 例如：user_name->userName
     */
    public static String toCamelCase(String s) {
        return StrUtil.toCamelCase(s);
    }

    /**
     * 查找指定字符串是否匹配指定字符串列表中的任意一个字符串
     *
     * @param str  指定字符串
     * @param strs 需要检查的字符串数组
     * @return 是否匹配
     */
    public static boolean matches(String str, List<String> strs) {
        if (isEmpty(str) || CollUtil.isEmpty(strs)) {
            return false;
        }
        for (String pattern : strs) {
            if (isMatch(pattern, str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断url是否与规则配置:
     * ? 表示单个字符;
     * * 表示一层路径内的任意字符串，不可跨层级;
     * ** 表示任意层路径;
     *
     * @param pattern 匹配规则
     * @param url     需要匹配的url
     */
    public static boolean isMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    /**
     * 数字左边补齐0，使之达到指定长度。注意，如果数字转换为字符串后，长度大于size，则只保留 最后size个字符。
     *
     * @param num  数字对象
     * @param size 字符串指定长度
     * @return 返回数字的字符串格式，该字符串为指定长度。
     */
    public static String padl(final Number num, final int size) {
        return padl(num.toString(), size, '0');
    }

    /**
     * 字符串左补齐。如果原始字符串s长度大于size，则只保留最后size个字符。
     *
     * @param s    原始字符串
     * @param size 字符串指定长度
     * @param c    用于补齐的字符
     * @return 返回指定长度的字符串，由原字符串左补齐或截取得到。
     */
    public static String padl(final String s, final int size, final char c) {
        final StringBuilder sb = new StringBuilder(size);
        if (s != null) {
            final int len = s.length();
            if (s.length() <= size) {
                for (int i = size - len; i > 0; i--) {
                    sb.append(c);
                }
                sb.append(s);
            } else {
                return s.substring(len - size, len);
            }
        } else {
            for (int i = size; i > 0; i--) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 切分字符串(分隔符默认逗号)
     *
     * @param str 被切分的字符串
     * @return 分割后的数据列表
     */
    public static List<String> splitList(String str) {
        return splitTo(str, Convert::toStr);
    }

    /**
     * 切分字符串
     *
     * @param str       被切分的字符串
     * @param separator 分隔符
     * @return 分割后的数据列表
     */
    public static List<String> splitList(String str, String separator) {
        return splitTo(str, separator, Convert::toStr);
    }

    /**
     * 切分字符串自定义转换(分隔符默认逗号)
     *
     * @param str    被切分的字符串
     * @param mapper 自定义转换
     * @return 分割后的数据列表
     */
    public static <T> List<T> splitTo(String str, Function<? super Object, T> mapper) {
        return splitTo(str, SEPARATOR, mapper);
    }

    /**
     * 切分字符串自定义转换
     *
     * @param str       被切分的字符串
     * @param separator 分隔符
     * @param mapper    自定义转换
     * @return 分割后的数据列表
     */
    public static <T> List<T> splitTo(String str, String separator, Function<? super Object, T> mapper) {
        if (isBlank(str)) {
            return new ArrayList<>(0);
        }
        return StrUtil.split(str, separator)
            .stream()
            .filter(Objects::nonNull)
            .map(mapper)
            .collect(Collectors.toList());
    }

    /**
     * 分割字符串为数组，以,为分隔符
     * 并判断最后一个是否为空，防止异常
     *
     * @param str
     * @return
     */
    public static String[] splitStringToArray(String str) {
        if (isBlank(str)) {
            return new String[]{};
        }
        return str.split(SEPARATOR, -1);
    }

    /**
     * 生成播放ID
     *
     * @param viewerId  观看者id
     * @param startTime 开始时间
     * @return {@link String }
     */
    public static String generatePlayId(String viewerId, String channelId, Date startTime) {
        // 获取 startTime 的时间戳
        long startTimeMillis = startTime.getTime();
        String uniqueId = startTimeMillis + "C" + channelId + "S" + viewerId;
        return MD5.create().digestHex(uniqueId);
    }

    /**
     * 生成修改ID
     *
     * @param viewerId  观看者id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link String }
     */
    public static String generateModifiedId(String viewerId, Date startTime, Date endTime) {
        // 获取 startTime 的时间戳
        long startTimeMillis = startTime.getTime();
        // 获取 endTime 的时间戳
        long endTimeMillis = endTime.getTime();
        return startTimeMillis + "S" + viewerId + "E" + endTimeMillis;
    }

    /**
     * 字符串转换秒
     *
     * @param duration 时长字符串（持续时间的字符串表示，格式为"hh:mm:ss"，其中hh为小时，mm为分钟，ss为秒）
     * @return {@link Integer }
     */
    public static Integer convertDuration(String duration) {
        String[] parts = duration.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid duration format. Expected format: HH:mm:ss");
        }

        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);

        return hours * 3600 + minutes * 60 + seconds;
    }
}
