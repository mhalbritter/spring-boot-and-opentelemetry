package com.example.shared;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Logs the headers of the incoming requests to debug level.
 */
class HeaderLoggerFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderLoggerFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logHeaders(request);
        filterChain.doFilter(request, response);
    }

    private void logHeaders(HttpServletRequest request) {
        request.getHeaderNames().asIterator().forEachRemaining(header -> {
            List<String> values = new ArrayList<>();
            request.getHeaders(header).asIterator().forEachRemaining(values::add);
            LOGGER.debug("{}: {}", header, values.size() == 1 ? values.getFirst() : values);
        });
    }
}
