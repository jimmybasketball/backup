package com.sfebiz.supplychain.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author yanmingming
 * @since 15/12/26
 */
public class SimpleValidator {
    /**
     * 手机正则表达式
     */
    private static final Pattern MOBILE_PTN = Pattern.compile("^1[34578]\\d{9}$");
    /**
     * The constant IP_PTN.
     */
    private static final Pattern IP_PTN = Pattern.compile("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");
    /**
     * email 正则表达式
     */
    private static final Pattern EMAIL_PTN = Pattern
            .compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    /**
     * 非中文正则表达式
     */
    private static final Pattern NOT_CHINA_NAME = Pattern
            .compile("^[a-zA-Z0-9_]*$");
    /**
     * The Messages.
     */
    private final List<String> messages = new ArrayList<String>();

    /**
     * Instantiates a new Validator.
     */
    protected SimpleValidator() {
    }

    /**
     * Create verifier.
     *
     * @return the verifier
     */
    public static SimpleValidator create() {
        return new SimpleValidator();
    }

    /**
     * Verifies that the specified {@link String String} argument has non-blank text. If
     * the string is null or does not have any non-blank text the message is added to the list of
     * verification failure messages.
     * <p/>
     * <p>The validation error message is formatted using {@link String#format(String,
     * Object...) String.format(String, Object...)}*****</p>
     *
     * @param string the string to be validated
     * @param format verification failure message format
     * @param args   verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public SimpleValidator isNotBlank(String string, String format, Object... args) {
        boolean condition = (string != null) && (string.trim().length() > 0);
        isTrue(condition, format, args);

        return this;
    }

    /**
     * Verifies that the specified {@link Map Map} argument is empty. A map is empty if it
     * is null or if it has no entries. If the argument is not empty the message is added to the
     * list of verification failure messages.
     * <p/>
     * <p>The validation error message is formatted using {@link String#format(String,
     * Object...) String.format(String, Object...)}*****</p>
     *
     * @param map    the map to be validated
     * @param format verification failure message format
     * @param args   verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public SimpleValidator isEmpty(Map<?, ?> map, String format, Object... args) {
        boolean condition = (map == null) || (map.isEmpty());
        isTrue(condition, format, args);

        return this;
    }

    /**
     * Verifies that the specified {@link Collection Collection} argument is empty. A
     * collection is empty if it is not null or if it has no entries. If the argument is not empty
     * the message is added to the list of verification failure messages.
     * <p/>
     * <p>The validation error message is formatted using {@link String#format(String,
     * Object...) String.format(String, Object...)}*****</p>
     *
     * @param collection the collection to be validated
     * @param format     verification failure message format
     * @param args       verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public SimpleValidator isEmpty(Collection<?> collection, String format, Object... args) {
        boolean condition = (collection == null) || collection.isEmpty();
        isTrue(condition, format, args);

        return this;
    }

    /**
     * Verifies that the specified {@link String String} argument is empty. A string is
     * empty if it is null or if it has a length of zero. If the argument is not empty the message
     * is added to the list of verification failure messages.
     * <p/>
     * <p>The validation error message is formatted using {@link String#format(String,
     * Object...) String.format(String, Object...)}*****</p>
     *
     * @param string the string to be validated
     * @param format verification failure message format
     * @param args   verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public SimpleValidator isEmpty(String string, String format, Object... args) {
        boolean condition = (string != null) && (string.length() > 0);
        isFalse(condition, format, args);

        return this;
    }

    /**
     * Verifies that the specified condition is false. If the condition is not false the message is
     * added to the list of verification failure messages.
     * <p/>
     * <p>The validation error message is formatted using {@link String#format(String,
     * Object...) String.format(String, Object...)}*****</p>
     *
     * @param condition the condition to be validated
     * @param format    verification failure message format
     * @param args      verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public SimpleValidator isFalse(boolean condition, String format, Object... args) {
        if (condition) {
            messages.add(String.format(format, args));
        }

        return this;
    }

    /**
     * Verifies that the specified {@link String String} argument is not empty. A string
     * is not empty if it is not null and it has a length greater than zero. If the argument is
     * empty the message is added to the list of verification failure messages.
     * <p/>
     * <p>The validation error message is formatted using {@link String#format(String,
     * Object...) String.format(String, Object...)}*****</p>
     *
     * @param string the string to be validated
     * @param format verification failure message format
     * @param args   verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public SimpleValidator isNotEmpty(String string, String format, Object... args) {
        boolean condition = (string != null) && (string.length() > 0);
        isTrue(condition, format, args);

        return this;
    }

    /**
     * Verifies that the specified {@link Collection Collection} argument is not empty. A
     * collection is not empty if it is not null and has one or more entries. If the argument is
     * empty the message is added to the list of verification failure messages.
     * <p/>
     * <p>The validation error message is formatted using {@link String#format(String,
     * Object...) String.format(String, Object...)}*****</p>
     *
     * @param collection the collection to be validated
     * @param format     verification failure message format
     * @param args       verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public SimpleValidator isNotEmpty(Collection<?> collection, String format, Object... args) {
        boolean condition = (collection == null) || collection.isEmpty();
        isFalse(condition, format, args);

        return this;
    }

    /**
     * Verifies that the specified {@link Map Map} argument is not empty. A map is not
     * empty if it is not null and has one or more entries. If the argument is empty the message is
     * added to the list of verification failure messages.
     * <p/>
     * <p>The validation error message is formatted using {@link String#format(String,
     * Object...) String.format(String, Object...)}*****</p>
     *
     * @param map    the map to be validated
     * @param format verification failure message format
     * @param args   verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public SimpleValidator isNotEmpty(Map<?, ?> map, String format, Object... args) {
        boolean condition = (map == null) || (map.isEmpty());
        isFalse(condition, format, args);

        return this;
    }

    /**
     * Verifies that the specified object is not null. If the object is null the message is added to
     * the list of verification failure messages.
     * <p/>
     * <p>The validation error message is formatted using {@link String#format(String,
     * Object...) String.format(String, Object...)}*****</p>
     *
     * @param object the object to be validated
     * @param format verification failure message format
     * @param args   verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public SimpleValidator isNotNull(Object object, String format, Object... args) {
        isFalse((object == null), format, args);

        return this;
    }

    /**
     * Verifies that the specified object is null. If the object is not null the message is added to
     * the list of verification failure messages.
     * <p/>
     * <p>The validation error message is formatted using {@link String#format(String,
     * Object...) String.format(String, Object...)}*****</p>
     *
     * @param object the object to be validated
     * @param format verification failure message format
     * @param args   verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public SimpleValidator isNull(Object object, String format, Object... args) {
        isTrue((object == null), format, args);

        return this;
    }

    /**
     * Verifies that the specified condition is true. If the condition is not true the message is
     * added to the list of verification failure messages.
     * <p/>
     * <p>The validation error message is formatted using {@link String#format(String,
     * Object...) String.format(String, Object...)}*****</p>
     *
     * @param condition the condition to be validated
     * @param format    verification failure message format
     * @param args      verification failure message arguments
     * @return this object to facilitate fluent verification
     */
    public SimpleValidator isTrue(boolean condition, String format, Object... args) {
        if (!condition) {
            messages.add(String.format(format, args));
        }

        return this;
    }

    /**
     * Throw a verification exception when one or more verification tests had verification failures.
     * The exception message is the concatenated list of all of the verification failure messages.
     * <p/>
     * <p>Invokes {@link #throwIfError(String)} with an empty prefix message.</p>
     *
     * @return the verifier
     */
    public SimpleValidator throwIfError() {
        return throwIfError("");
    }

    /**
     * Throw a verification exception when one or more verification tests had verification failures.
     * The specified message is prefixed to the messages associated with each verification failure
     * message. The exception message is the concatenated list of all of the verification failure
     * messages.
     *
     * @param prefixMessage message is prefixed to the verification failure messages
     * @return the verifier
     * @throws IllegalArgumentException thrown if any verification tests have failed
     */
    public SimpleValidator throwIfError(String prefixMessage) {
        if (messages.size() > 0) {
            throw new IllegalArgumentException(getErrorMessage(prefixMessage));
        }

        return this;
    }

    public String getErrorMessage(String prefixMessage) {
        StringBuilder exceptionMessage = new StringBuilder((prefixMessage == null)
                ? "" : prefixMessage);
        String sp = (prefixMessage == null) ? "" : " ";

        for (String msg : messages) {
            exceptionMessage.append(sp)
                    .append(msg);
            sp = " ";
        }
        return exceptionMessage.toString();
    }

    /**
     * 是否有错误
     *
     * @return
     */
    public boolean hasError() {
        return messages.size() > 0;
    }

    /**
     * Is positive number.
     *
     * @param number the number
     * @param format the format
     * @param args   the args
     * @return the verifier
     */
    public SimpleValidator isPositiveNumber(Number number, String format, Object... args) {
        boolean condition = (number != null) && (number.intValue() > 0);
        isTrue(condition, format, args);
        return this;
    }

    /**
     * Is positive number.
     *
     * @param str    the str
     * @param format the format
     * @param args   the args
     * @return the verifier
     */
    public SimpleValidator isNumberic(String str, String format, Object... args) {
        boolean condition = str != null && str.length() > 0;
        isTrue(condition, format, args);
        if (!condition) {
            return this;
        }

        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57) {
                isTrue(false, format, args);
                break;
            }
        }
        return this;
    }

    /**
     * Is less than.
     *
     * @param number   the number
     * @param maxValue the max value
     * @param format   the format
     * @param args     the args
     * @return the verifier
     */
    public SimpleValidator isLessThan(Number number, int maxValue, String format, Object... args) {
        boolean condition = (number != null) && (number.intValue() > maxValue);
        isFalse(condition, format, args);
        return this;
    }

    /**
     * string is less than.
     *
     * @param str      the str
     * @param maxValue the max value
     * @param format   the format
     * @param args     the args
     * @return the verifier
     */
    public SimpleValidator isShortThan(String str, int maxValue, String format, Object... args) {
        boolean condition = (str != null) && (str.length() > maxValue);
        isFalse(condition, format, args);
        return this;
    }

    /**
     * Is not negative.
     *
     * @param number the number
     * @param format the format
     * @param args   the args
     * @return the verifier
     */
    public SimpleValidator isNotNegative(Number number, String format, Object... args) {
        boolean condition = (number != null) && (number.intValue() >= 0);
        isTrue(condition, format, args);
        return this;
    }

    /**
     * Is Null Or Positive number.
     *
     * @param number the number
     * @param format the format
     * @param args   the args
     * @return the verifier
     */
    public SimpleValidator isNullOrPositiveNumber(Number number, String format, Object... args) {
        boolean condition = (number == null) || (number.intValue() > 0);
        isTrue(condition, format, args);
        return this;
    }

    /**
     * Is mobile format.
     *
     * @param mobileString the mobile string
     * @param format       the format
     * @param args         the args
     * @return the verifier
     */
    public SimpleValidator isMobile(String mobileString, String format, Object... args) {
        boolean condition = (mobileString != null) && (MOBILE_PTN.matcher(mobileString).find());
        isTrue(condition, format, args);
        return this;
    }

    /**
     * Is mobile format.
     *
     * @param ip     the ip
     * @param format the format
     * @param args   the args
     * @return the verifier
     */
    public SimpleValidator isIPV4(String ip, String format, Object... args) {
        boolean condition = (ip != null) && (IP_PTN.matcher(ip).find());
        isTrue(condition, format, args);
        return this;
    }

    /**
     * Is mail format
     *
     * @param mailString the mail string
     * @param format     the format
     * @param args       the args
     * @return verifier
     */
    public SimpleValidator isMail(String mailString, String format, Object... args) {
        boolean condition = (mailString != null) && (EMAIL_PTN.matcher(mailString).find());
        isTrue(condition, format, args);
        return this;
    }

    /**
     * not china name
     *
     * @param name
     * @param format
     * @param args
     * @return
     */
    public SimpleValidator isNotChinaName(String name, String format, Object... args) {
        boolean condition = (StringUtils.isNotBlank(name) && (NOT_CHINA_NAME).matcher(name).find());
        isTrue(condition, format, args);
        return this;
    }

    /**
     * is not zero
     *
     * @param number
     * @param format
     * @param args
     * @return
     */
    public SimpleValidator isNotZero(long number, String format, Object... args){
        boolean condition = (number != 0);
        isTrue(condition, format, args);
        return this;
    }


}
