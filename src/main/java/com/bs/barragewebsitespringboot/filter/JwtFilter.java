package com.bs.barragewebsitespringboot.filter;

import com.bs.barragewebsitespringboot.pojo.LoginUser;
import com.bs.barragewebsitespringboot.security.DBUserDetailsManager;
import com.bs.barragewebsitespringboot.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    DBUserDetailsManager dbUserDetailsManager;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 如果用户携带token，且redis的token在有效期（不在有效期也进入默认认证），则进入手动授权流程。
     * 无token则进入SpringSecurity的默认认证流程。（无法通过）
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("Authorization");
        httpServletResponse.setContentType("text/json;charset=utf-8");
        if (StringUtils.hasLength(token)) {
            String username = null;
            Integer userId=null;
            String redisToken=null;
            try {
                username = (String) JwtUtils.getClaims(token).get("username");
                userId = Integer.parseInt((String) JwtUtils.getClaims(token).get("userId")) ;
                redisToken= stringRedisTemplate.opsForValue().get(String.valueOf(userId));//经过反序列化后，key必须为String value则是Object
            } catch (ExpiredJwtException e) {
                logger.info("失效身份");
            }
            if (username != null&& redisToken!=null) {
                String role = (String) JwtUtils.getClaims(token).get("role");
                UserDetails userDetails = dbUserDetailsManager.loadUserByUsername(username);
                LoginUser model = (LoginUser) userDetails;
                List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(role);//设定该账户的权限
                //System.out.println("用户权限等级"+role);
                UsernamePasswordAuthenticationToken authenticationFilter = new UsernamePasswordAuthenticationToken(model, null, grantedAuthorities);
                authenticationFilter.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationFilter);
                //下面两行将用户id和名称放在 请求域中 --》fileController中使用
                httpServletRequest.setAttribute("username",username);
                httpServletRequest.setAttribute("userId",userId);
                httpServletRequest.setAttribute("role",role);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
//添加redis后使用
/*
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request,response);
            return;
        }
        //解析token
        String userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //从redis中获取用户信息
        String redisKey = "login:" + userId;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("用户未登录");
        }
        //存入SecurityContextHolder
        //TODO 获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request,response);
    }

}*/
