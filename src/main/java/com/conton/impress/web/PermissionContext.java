package com.conton.impress.web;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限上下文信息，用以存储当前用户相关联的信息
 *
 *
 */
public class PermissionContext {

        private static ThreadLocal<Object> threadLocal = new ThreadLocal<Object>();

        public static void clearThreadVariable() {
            threadLocal.remove();
        }

        public static void setUser(Long userId) {
            Map map = (Map) threadLocal.get();
            if (map == null) {
                map = new HashMap();
            }
            map.put(WebConstants.LOGIN_SESSION_ID_USER, userId);
            threadLocal.set(map);
        }

        public static Long getUserId() {
            Map map = (Map) threadLocal.get();
            if (map != null) {
                return (Long) map.get(WebConstants.LOGIN_SESSION_ID_USER);
            }
            return null;
        }
}
