package com.acrobat.shiro;

import org.junit.Test;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.nio.charset.Charset;

/**
 * @author xutao
 * @date 2018-12-03 15:49
 */
public class TempTest {

    @Test
    public void test1() {
        String str = "\\xac\\xed\\x00\\x05sr\\x00*org.apache.shiro.session.mgt.SimpleSession\\x9d\\x1c\\xa1\\xb8\\xd5\\x8cbn\\x03\\x00\\x00xpw\\x02\\x00\\xdbt\\x00$b50807d5-8002-442e-81ba-eb7344e2a8f6sr\\x00\\x0ejava.util.Datehj\\x81\\x01KYt\\x19\\x03\\x00\\x00xpw\\b\\x00\\x00\\x01gs\\xa2K\\xf8xq\\x00~\\x00\\x04w\\x19\\x00\\x00\\x00\\x00\\x00\\x1bw@\\x00\\x0f0:0:0:0:0:0:0:1sr\\x00\\x11java.util.HashMap\\x05\\a\\xda\\xc1\\xc3\\x16`\\xd1\\x03\\x00\\x02F\\x00\\nloadFactorI\\x00\\tthresholdxp?@\\x00\\x00\\x00\\x00\\x00\\x0cw\\b\\x00\\x00\\x00\\x10\\x00\\x00\\x00\\x02t\\x00Porg.apache.shiro.subject.support.DefaultSubjectContext_AUTHENTICATED_SESSION_KEYsr\\x00\\x11java.lang.Boolean\\xcd r\\x80\\xd5\\x9c\\xfa\\xee\\x02\\x00\\x01Z\\x00\\x05valuexp\\x01t\\x00Morg.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEYsr\\x002org.apache.shiro.subject.SimplePrincipalCollection\\xa8\\x7fX%\\xc6\\xa3\\bJ\\x03\\x00\\x01L\\x00\\x0frealmPrincipalst\\x00\\x0fLjava/util/Map;xpsr\\x00\\x17java.util.LinkedHashMap4\\xc0N\\\\\\x10l\\xc0\\xfb\\x02\\x00\\x01Z\\x00\\x0baccessOrderxq\\x00~\\x00\\x05?@\\x00\\x00\\x00\\x00\\x00\\x0cw\\b\\x00\\x00\\x00\\x10\\x00\\x00\\x00\\x01t\\x00'com.acrobat.shiro.realm.MyShiroRealm_10sr\\x00\\x17java.util.LinkedHashSet\\xd8l\\xd7Z\\x95\\xdd*\\x1e\\x02\\x00\\x00xr\\x00\\x11java.util.HashSet\\xbaD\\x85\\x95\\x96\\xb8\\xb74\\x03\\x00\\x00xpw\\x0c\\x00\\x00\\x00\\x02?@\\x00\\x00\\x00\\x00\\x00\\x01t\\x00\\x0211xx\\x00w\\x01\\x01q\\x00~\\x00\\x0fxxx";

        JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
        Object o = serializer.deserialize(str.getBytes(Charset.forName("UTF-8")));

        System.out.println();
    }

    @Test
    public void test2() {
        String prefix = "prefix:";
        String str = prefix + "abc";

        System.out.println(str.substring(prefix.length()));
        System.out.println(prefix.substring(1, prefix.length() - 1));
    }
}
