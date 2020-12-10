package cn.dooling.samlazure.config.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTConfig {

    @Value("${jwt.header-name}")
    private String headerName;

    @Value("${jwt.schema}")
    private String schema;

    @Value("${jwt.iss}")
    private String iss;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.expiration-remember}")
    private long expirationRemember;

    @Value("${jwt.secret}")
    private String secret;


    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public long getExpirationRemember() {
        return expirationRemember;
    }

    public void setExpirationRemember(long expirationRemember) {
        this.expirationRemember = expirationRemember;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
