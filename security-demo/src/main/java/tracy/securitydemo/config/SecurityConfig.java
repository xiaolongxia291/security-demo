package tracy.securitydemo.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                //这个api对所有人可见
                .antMatchers("/hello").permitAll()
                //表示只有manager这个角色才能访问这个api
                .antMatchers("/manage/**").hasRole("manager");

        //没有权限默认会跳到登录页面
        http.formLogin();

        //开启了注销功能
        http.logout();//需要访问localhost:8080/logout才能注销
//        http.logout().deleteCookies("").invalidateHttpSession(false);//清除cookie
//        http.logout().logoutSuccessUrl("/hello");//注销后跳转至这个页面
    }

    //认证
    //密码需要编码，否则会报错
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //正常情况下，这些应该从数据库读取，但是这里简化了这个过程
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("manager1").password(new BCryptPasswordEncoder().encode("123456")).roles("manager")
                .and()
                .withUser("manager2").password(new BCryptPasswordEncoder().encode("123456")).roles("manager");
    }
}
