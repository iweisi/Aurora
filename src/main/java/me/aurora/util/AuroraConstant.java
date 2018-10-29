package me.aurora.util;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * 常用静态常量
 * @author 郑杰
 * @date 2018/09/29 12:52:06
 */
public class AuroraConstant {

    /**
     * 在添加时，如需手动添加密码，请使用js的MD5进行加密，参考登录页面，后台再次进行加密，保存到数据库，默认密码：123456
     */
    public static final String PWD = "36318d4cc46eb68050b8b81ebc620f05";

    public static final String SUCCESS = "success";

    /**
     * 用于排序
     */
    public static class Soft{

        public static final String DESC = "desc";

        public static final String ASC = "asc";
    }

    /**
     * 用于分页
     */
    public static class Page{

        public static final String CODE = "code";

        public static final String MSG = "msg";

        public static final String COUNT = "count";

        public static final String DATA = "data";
    }

    /**
     * 用于七牛云zone与机房对应关系
     */
    public static class QiNiu{

        public static final String HUAD = "华东";

        public static final String HUAB = "华北";

        public static final String HUAN = "华南";

        public static final String BEIM = "北美";

        public static final String DNY = "东南亚";
    }

    /**
     * 用于IP定位转换
     */
    public static final String REGION = "内网IP|内网IP";

    /**
     * 常用接口
     */
    public static class Url{
        public static final String SM_MS_URL = "https://sm.ms/api/upload";
    }

    /**
     * 阿里云短信所用到的错误码列表
     */
    public enum Sms{
        RAM_PERMISSION_DENY("isp.RAM_PERMISSION_DENY","RAM权限DENY"),
        OUT_OF_SERVICE("isv.OUT_OF_SERVICE","业务停机"),
        PRODUCT_UN_SUBSCRIPT("isv.PRODUCT_UN_SUBSCRIPT","未开通云通信产品的阿里云客户"),
        PRODUCT_UNSUBSCRIBE("isv.PRODUCT_UNSUBSCRIBE","产品未开通"),
        ACCOUNT_NOT_EXISTS("isv.ACCOUNT_NOT_EXISTS","账户不存在"),
        ACCOUNT_ABNORMAL("isv.ACCOUNT_ABNORMAL","账户异常"),
        SMS_TEMPLATE_ILLEGAL("isv.SMS_TEMPLATE_ILLEGAL","短信模板不合法"),
        SMS_SIGNATURE_ILLEGAL("isv.SMS_SIGNATURE_ILLEGAL","短信签名不合法"),
        INVALID_PARAMETERS("isv.INVALID_PARAMETERS","参数异常"),
        SYSTEM_ERROR("isv.SYSTEM_ERROR","系统错误"),
        MOBILE_NUMBER_ILLEGAL("isv.MOBILE_NUMBER_ILLEGAL","非法手机号"),
        MOBILE_COUNT_OVER_LIMIT("isv.MOBILE_COUNT_OVER_LIMIT","手机号码数量超过限制"),
        TEMPLATE_MISSING_PARAMETERS("isv.TEMPLATE_MISSING_PARAMETERS","模板缺少变量"),
        BUSINESS_LIMIT_CONTROL("isv.BUSINESS_LIMIT_CONTROL","业务限流"),
        INVALID_JSON_PARAM("isv.INVALID_JSON_PARAM","JSON参数不合法，只接受字符串值"),
        BLACK_KEY_CONTROL_LIMIT("isv.BLACK_KEY_CONTROL_LIMIT","黑名单管控"),
        PARAM_LENGTH_LIMIT("isv.PARAM_LENGTH_LIMIT","参数超出长度限制"),
        PARAM_NOT_SUPPORT_URL("isv.PARAM_NOT_SUPPORT_URL","不支持URL"),
        AMOUNT_NOT_ENOUGH("isv.AMOUNT_NOT_ENOUGH","账户余额不足");

        private final String key;
        private final String value;

        Sms(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public static String get(String key) {
            Sms sms = resolve(key);
            if (sms == null) {
                throw new IllegalArgumentException("No matching constant for [" + sms + "]");
            } else {
                return sms.value;
            }
        }

        public static Sms resolve(String key) {
            Sms[] var1 = values();
            for(int var = 0; var < var1.length; ++var) {
                Sms sms = var1[var];
                if (sms.key == key) {
                    return sms;
                }
            }
            return null;
        }
    }
}
