package com.apikey.apikey.config;

import com.apikey.apikey.repository.AuthApiKeyRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * This class is a filter for all the end-point in core-events api.
 */
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthApiKeyRepository authApiKeyRepository;

    /**
     * This function verifies if the request has a token or an apikey in the request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String apiKey = req.getHeader("API_KEY");

        if (StringUtils.hasText(apiKey)) {
            this.validateApiKey(req, res, chain);
        } else {
            this.validateJwt(req, res, chain);
        }
    }

    /**
     * This function validates the value of the apikey in the request.
     * It also verifies if the client that is making the request has the roles that are needed.
     */
    private void validateApiKey(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String apiKey = req.getHeader("API_KEY");

        final var authKey = authApiKeyRepository.findByApiKey(apiKey);

        if (authKey != null) {

            final var grantedAuthorities = authKey.getRoles().stream().map(a -> new SimpleGrantedAuthority("ROLE_" + a.getName())).collect(Collectors.toList());
            User user = new User(authKey.getClient(), apiKey, grantedAuthorities);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        }

        chain.doFilter(req, res);
    }

    /**
     * This function validates the secret of the token in the request.
     * It also verifies if the token is not expired.
     */
    private void validateJwt(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String authHeader = request.getHeader("authorization");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
        } else {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new NullPointerException("Token ", authHeader);
            }
        }
        final String token = authHeader.substring(7);
        Claims claims = Jwts.parser().setSigningKey("ZjBjdSQ=").parseClaimsJws(token).getBody();
        request.setAttribute("claims", claims);
        request.setAttribute("blog", servletRequest.getParameter("id"));
        filterChain.doFilter(request, response);
    }

}