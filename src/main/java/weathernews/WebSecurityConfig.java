package weathernews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity // Spring Securityの基本設定を有効にする
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

// 	@Bean
// 	PasswordEncoder passwordEncoder() {
// 		return new BCryptPasswordEncoder();
//}

@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("js/**", "css/**", "weathernews/**").permitAll() // /js以下と/css以下へのアクセスは常に許可する
            .antMatchers("weathernews/admin/**").authenticated(); // admin以下へのアクセスは認証を要求する
         //    .and()
        // .formLogin()
        //    .loginPage("admin/login")
        //    .loginProcessingUrl("admin/loginProceccing")
        //    .usernameParameter("id") // ログイン画面の項目に指定したname
        //     .passwordParameter("pass") // ログイン画面の項目に指定したname
        //    .defaultSuccessUrl("admin/systemlog", true)
        //    .failureUrl("admin/login") // ログイン画面、認証URL、認証失敗時の繊維先へのアクセスは常に許可する
        //     .and();
        // .logout().permitAll(); // ログアウト機能を有効にする 
}

 }