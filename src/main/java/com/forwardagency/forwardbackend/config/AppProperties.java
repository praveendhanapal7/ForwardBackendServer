package com.forwardagency.forwardbackend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Auth auth = new Auth();

    public Auth getAuth() {
        return auth;
    }

    public static class Auth {

        /**
         * Lifetime of a session token in hours. Defaults to 7 days.
         */
        private long sessionTtlHours = 24L * 7L;

        public long getSessionTtlHours() {
            return sessionTtlHours;
        }

        public void setSessionTtlHours(long sessionTtlHours) {
            this.sessionTtlHours = sessionTtlHours;
        }
    }
}
