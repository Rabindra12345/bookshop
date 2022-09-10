package com.rabindra.bookmarket.security;

import com.rabindra.bookmarket.model.Role;
import com.rabindra.bookmarket.security.jwt.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import com.bezkoder.spring.security.jwt.AuthEntryPointJwt;
//import com.rabindra.bookmarket.spring.security.jwt.AuthTokenFilter;

/**
 * @author Rabindra
 * @date 6.09.2022
 */
@Configuration
@EnableGlobalMethodSecurity(
    // securedEnabled = true,
    // jsr250Enabled = true,
    prePostEnabled = true)
public class SecurityConfig { // extends WebSecurityConfigurerAdapter {
  @Autowired
  CustomUserDetailsService userDetailsService;
  
  @Value("${authentication.internal-api-key}")
  private String internalApiKey;
//  @Autowired
//  private AuthEntryPointJwt unauthorizedHandler;
//  @Bean
//  public AuthTokenFilter authenticationJwtTokenFilter() {
//    return new AuthTokenFilter();
//  }
//  @Override
//  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//  }
  
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }
//  @Bean
//  @Override
//  public AuthenticationManager authenticationManagerBean() throws Exception {
//    return super.authenticationManagerBean();
//  }
  
  @Bean(BeanIds.AUTHENTICATION_MANAGER)
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
//  @Bean
//  public AuthTokenFilter authenticationJwtTokenFilter() {
//    return new AuthTokenFilter();
//  }
//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http.cors().and().csrf().disable()
//      .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//      .authorizeRequests().antMatchers("/api/auth/**").permitAll()
//      .antMatchers("/api/test/**").permitAll()
//      .anyRequest().authenticated();
//
//    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//  }
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable();
//        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
        .antMatchers("/api/authentication/**").permitAll()
        .antMatchers(HttpMethod.GET, "/api/book").permitAll()
        .antMatchers("/api/book/**").hasRole(Role.ADMIN.name())
        .antMatchers("/api/internal/**").hasRole(Role.SYSTEM_MANAGER.name())
        .anyRequest().authenticated();
    
    http.authenticationProvider(authenticationProvider());
    
    http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
    .addFilterBefore(internalApiAuthenticationFilter(), JwtAuthorizationFilter.class);    
    return http.build();
  }
  
  @Bean
  public InternalApiAuthenticationFilter internalApiAuthenticationFilter()
  {
      return new InternalApiAuthenticationFilter(internalApiKey);
  }
  
  @Bean
  public JwtAuthorizationFilter jwtAuthorizationFilter()
  {
      return new JwtAuthorizationFilter();
  }
  
  @Bean
  public WebMvcConfigurer corsConfigurer()
  {
      return new WebMvcConfigurer()
      {
          @Override
          public void addCorsMappings(CorsRegistry registry)
          {
              registry.addMapping("/**")
                      .allowedOrigins("*")
                      .allowedMethods("*");
          }
      };
  }
  
  //BELOW IS OLD SCHOOLD METHOD IF YOU WANT TO EXTEND..OLD SCHOOL METHOD STARTED ........... 
//  @Configuration
//  @EnableWebSecurity
//  public class SecurityConfig extends WebSecurityConfigurerAdapter
//  {
//      @Value("${authentication.internal-api-key}")
//      private String internalApiKey;
//
//      @Autowired
//      private CustomUserDetailsService userDetailsService;
//
//      @Override
//      protected void configure(AuthenticationManagerBuilder auth) throws Exception
//      {
//          auth.userDetailsService(userDetailsService)
//                  .passwordEncoder(passwordEncoder());
//      }
//
//      @Override
//      @Bean(BeanIds.AUTHENTICATION_MANAGER)
//      public AuthenticationManager authenticationManagerBean() throws Exception
//      {
//          return super.authenticationManagerBean();
//      }
//
//      @Override
//      protected void configure(HttpSecurity http) throws Exception
//      {
//          http.cors();
//          http.csrf().disable();
//          http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//          http.authorizeRequests()
//                  .antMatchers("/api/authentication/**").permitAll()
//                  .antMatchers(HttpMethod.GET, "/api/book").permitAll()
//                  .antMatchers("/api/book/**").hasRole(Role.ADMIN.name())
//                  .antMatchers("/api/internal/**").hasRole(Role.SYSTEM_MANAGER.name())
//                  .anyRequest().authenticated();
//
//          //jwt filter
//          //internal > jwt > authentication
//          http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
//                  .addFilterBefore(internalApiAuthenticationFilter(), JwtAuthorizationFilter.class);
//      }
//
//      @Bean
//      public InternalApiAuthenticationFilter internalApiAuthenticationFilter()
//      {
//          return new InternalApiAuthenticationFilter(internalApiKey);
//      }
//
//      @Bean
//      public JwtAuthorizationFilter jwtAuthorizationFilter()
//      {
//          return new JwtAuthorizationFilter();
//      }
//
//      @Bean
//      public PasswordEncoder passwordEncoder()
//      {
//          return new BCryptPasswordEncoder();
//      }
//
//      @Bean
//      public WebMvcConfigurer corsConfigurer()
//      {
//          return new WebMvcConfigurer()
//          {
//              @Override
//              public void addCorsMappings(CorsRegistry registry)
//              {
//                  registry.addMapping("/**")
//                          .allowedOrigins("*")
//                          .allowedMethods("*");
//              }
//          };
//      }
//  }
  
  //OLD SCHOOL METHOD ADDED COMPLETE .......................COMPLETE..............
}
