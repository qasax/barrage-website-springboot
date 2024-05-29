package com.bs.barragewebsitespringboot.config;

import com.bs.barragewebsitespringboot.filter.JwtFilter;
import com.bs.barragewebsitespringboot.security.*;
import com.bs.barragewebsitespringboot.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
    public class WebSecurityConfig   {
    @Autowired
    DBUserDetailsManager dbUserDetailsManager;
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    AccessDeniedHandlerImpl accessDeniedHandler;
    @Autowired
    AuthenticationEntryPointImpl authenticationEntryPoint;
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(dbUserDetailsManager);
        return provider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //authorizeRequests()：开启授权保护
        //anyRequest()：对所有请求开启授权保护
        //authenticated()：已认证请求会自动被授权
        String[] PERMITTED_PATHS = {//直接放行路径
                "/login",
                "/register",
                "/validateUsername",
                "/registerNormal",
                "/website/**",
                "/video/getPageVideoBySearch",
                "/video/getPageVideoByType",
                "/video/getUserPageVideo",
                "/video/getVideoDetail",
                "/video/getVideo",
                "/video/getVideoPic",
                "/video/getSubtitle",
                "/website/getCarouselFileName",
                "/website/getCarouselImg",
                "/barrage/v3/",
                "/comment/getPageComment",
                "/user/getAvatarByUserId",
                "/userFollow/getUserPageFollow",
                "/userFollow/getUserPageFans",
        };
        http.formLogin(AbstractHttpConfigurer::disable)//取消默认登录页面的使用
                .logout(AbstractHttpConfigurer::disable)//取消默认登出页面的使用
                .authenticationProvider(authenticationProvider())//将自己配置的PasswordEncoder放入SecurityFilterChain中
                .csrf(AbstractHttpConfigurer::disable)//禁用csrf保护，前后端分离不需要
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//禁用session，因为我们已经使用了JWT
                .httpBasic(AbstractHttpConfigurer::disable)//禁用httpBasic，因为我们传输数据用的是post，而且请求体是JSON
                //requestMatchers(HttpMethod.POST,）只管理post请求
                .authorizeHttpRequests(request -> request.requestMatchers(PERMITTED_PATHS).permitAll().anyRequest().authenticated())//开放两个接口，一个注册，一个登录，其余均要身份认证
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)//将用户授权时用到的JWT校验过滤器添加进SecurityFilterChain中，并放在UsernamePasswordAuthenticationFilter的前面.
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler);
        return http.build();
       /* http
                .formLogin()
                .loginProcessingUrl("/login").permitAll() //登录页面无需授权即可访问
                .usernameParameter("username") //自定义表单用户名参数，默认是username
                .passwordParameter("password") //自定义表单密码参数，默认是password
                .failureHandler(new MyAuthenticationFailureHandler()) //认证失败时的处理
                .successHandler(new MyAuthenticationSuccessHandler())
                .and().authorizeHttpRequests()
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable(); //认证成功时的处理
                //表单授权方式*/
       /* http.csrf((csrf) -> {
            try {
                csrf.disable()
                .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        http.logout(logout -> {
            logout.logoutSuccessHandler(new MyLogoutSuccessHandler()); //注销成功时的处理
        });
        http.sessionManagement(session -> {
            session
                    .maximumSessions(1)
                    .expiredSessionStrategy(new MySessionInformationExpiredStrategy());
        });*/
    }

    //配置采用哪种密码加密算法
    @Bean
    public PasswordEncoder passwordEncoder() {
        //不使用密码加密
        //return NoOpPasswordEncoder.getInstance();

        //使用默认的BCryptPasswordEncoder加密方案
        return new BCryptPasswordEncoder();

        //strength=10，即密钥的迭代次数(strength取值在4~31之间，默认为10)
        //return new BCryptPasswordEncoder(10);

        //利用工厂类PasswordEncoderFactories实现,工厂类内部采用的是委派密码编码方案.
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    }

