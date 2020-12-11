package cn.dooling.samlazure.config.saml;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.MediaType;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticationRequestContext;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticationRequestFactory;
import org.springframework.security.saml2.provider.service.authentication.Saml2PostAuthenticationRequest;
import org.springframework.security.saml2.provider.service.authentication.Saml2RedirectAuthenticationRequest;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.Saml2MessageBinding;
import org.springframework.security.saml2.provider.service.servlet.filter.Saml2WebSsoAuthenticationRequestFilter;
import org.springframework.security.saml2.provider.service.web.Saml2AuthenticationRequestContextResolver;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomSaml2WebSsoAuthenticationRequestFilter extends Saml2WebSsoAuthenticationRequestFilter {

    private final Saml2AuthenticationRequestContextResolver authenticationRequestContextResolver;

    private Saml2AuthenticationRequestFactory authenticationRequestFactory;

    private RequestMatcher redirectMatcher = new AntPathRequestMatcher("/saml2/authenticate/{registrationId}");


    private String stateName = null;

    public CustomSaml2WebSsoAuthenticationRequestFilter(Saml2AuthenticationRequestContextResolver authenticationRequestContextResolver, Saml2AuthenticationRequestFactory authenticationRequestFactory) {
        super(authenticationRequestContextResolver, authenticationRequestFactory);
        this.authenticationRequestContextResolver = authenticationRequestContextResolver;
        this.authenticationRequestFactory = authenticationRequestFactory;
    }

    public CustomSaml2WebSsoAuthenticationRequestFilter(Saml2AuthenticationRequestContextResolver authenticationRequestContextResolver, Saml2AuthenticationRequestFactory authenticationRequestFactory, String stateName) {
        this(authenticationRequestContextResolver, authenticationRequestFactory);
        this.stateName = stateName;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        RequestMatcher.MatchResult matcher = this.redirectMatcher.matcher(request);
        if (!matcher.isMatch()) {
            filterChain.doFilter(request, response);
            return;
        }

        Saml2AuthenticationRequestContext context = this.authenticationRequestContextResolver.resolve(request);
        if (context == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        //添加cookie
        setStateCookie(request, response);
        RelyingPartyRegistration relyingParty = context.getRelyingPartyRegistration();
        if (relyingParty.getAssertingPartyDetails().getSingleSignOnServiceBinding() == Saml2MessageBinding.REDIRECT) {
            sendRedirect(response, context);
        } else {
            sendPost(response, context);
        }
    }

    private void sendRedirect(HttpServletResponse response, Saml2AuthenticationRequestContext context)
            throws IOException {
        Saml2RedirectAuthenticationRequest authenticationRequest = this.authenticationRequestFactory
                .createRedirectAuthenticationRequest(context);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(authenticationRequest.getAuthenticationRequestUri());
        addParameter("SAMLRequest", authenticationRequest.getSamlRequest(), uriBuilder);
        addParameter("RelayState", authenticationRequest.getRelayState(), uriBuilder);
        addParameter("SigAlg", authenticationRequest.getSigAlg(), uriBuilder);
        addParameter("Signature", authenticationRequest.getSignature(), uriBuilder);
        String redirectUrl = uriBuilder.build(true).toUriString();
        response.sendRedirect(redirectUrl);
    }

    private void addParameter(String name, String value, UriComponentsBuilder builder) {
        Assert.hasText(name, "name cannot be empty or null");
        if (StringUtils.hasText(value)) {
            builder.queryParam(UriUtils.encode(name, StandardCharsets.ISO_8859_1),
                    UriUtils.encode(value, StandardCharsets.ISO_8859_1));
        }
    }

    private void sendPost(HttpServletResponse response, Saml2AuthenticationRequestContext context) throws IOException {
        Saml2PostAuthenticationRequest authenticationRequest = this.authenticationRequestFactory
                .createPostAuthenticationRequest(context);
        String html = createSamlPostRequestFormData(authenticationRequest);
        response.setContentType(MediaType.TEXT_HTML_VALUE);
        response.getWriter().write(html);
    }

    private String createSamlPostRequestFormData(Saml2PostAuthenticationRequest authenticationRequest) {
        String authenticationRequestUri = authenticationRequest.getAuthenticationRequestUri();
        String relayState = authenticationRequest.getRelayState();
        String samlRequest = authenticationRequest.getSamlRequest();
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n").append("    <head>\n");
        html.append("        <meta charset=\"utf-8\" />\n");
        html.append("    </head>\n");
        html.append("    <body onload=\"document.forms[0].submit()\">\n");
        html.append("        <noscript>\n");
        html.append("            <p>\n");
        html.append("                <strong>Note:</strong> Since your browser does not support JavaScript,\n");
        html.append("                you must press the Continue button once to proceed.\n");
        html.append("            </p>\n");
        html.append("        </noscript>\n");
        html.append("        \n");
        html.append("        <form action=\"");
        html.append(authenticationRequestUri);
        html.append("\" method=\"post\">\n");
        html.append("            <div>\n");
        html.append("                <input type=\"hidden\" name=\"SAMLRequest\" value=\"");
        html.append(HtmlUtils.htmlEscape(samlRequest));
        html.append("\"/>\n");
        if (StringUtils.hasText(relayState)) {
            html.append("                <input type=\"hidden\" name=\"RelayState\" value=\"");
            html.append(HtmlUtils.htmlEscape(relayState));
            html.append("\"/>\n");
        }
        html.append("            </div>\n");
        html.append("            <noscript>\n");
        html.append("                <div>\n");
        html.append("                    <input type=\"submit\" value=\"Continue\"/>\n");
        html.append("                </div>\n");
        html.append("            </noscript>\n");
        html.append("        </form>\n");
        html.append("        \n");
        html.append("    </body>\n");
        html.append("</html>");
        return html.toString();
    }


    private void setStateCookie(HttpServletRequest request, HttpServletResponse response) {
        if (null != response && StrUtil.isNotBlank(stateName)) {
            String state = request.getParameter(stateName);
            Cookie cookie = new Cookie(stateName, state);
            cookie.setMaxAge(300);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
