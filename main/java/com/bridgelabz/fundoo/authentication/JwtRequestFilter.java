package com.bridgelabz.fundoo.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bridgelabz.fundoo.services.UserService;
import com.bridgelabz.fundoo.util.JwtUtil;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserService userService;
   
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
           jwtToken = authHeader.substring(7);
           username = jwtUtil.extractUsername(jwtToken);
       }

       if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
           UserDetails userDetails = userService.loadUserByUsername(username);

           if (jwtUtil.validateToken(jwtToken, userDetails)) {

             UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
               token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(token);
           } else {
        	   System.out.println("Invalid Token");
           }
       }
       chain.doFilter(request, response);
   }
}
