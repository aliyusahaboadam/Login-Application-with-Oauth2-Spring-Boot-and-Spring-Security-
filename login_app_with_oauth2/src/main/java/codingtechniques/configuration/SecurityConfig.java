package codingtechniques.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
	
	
	
	@Bean
	public ClientRegistrationRepository registrationRepository () {
		return new InMemoryClientRegistrationRepository(this.clientRegistration());
	}
	
	
	
	private ClientRegistration clientRegistration () {
		return ClientRegistration.withRegistrationId("github")
				.clientId("e2340c067b61e041868d")
				.clientSecret("fdffaf0c87ad1f9d05bcf72730dd8c110e61886f")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.scope("read")
				.authorizationUri("https://github.com/login/oauth/authorize")
				.tokenUri("https://github.com/login/oauth/access_token")
				.userInfoUri("https://api.github.com/user")
				.userNameAttributeName("id")
				.clientName("Coding Techniques")
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
				.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService () {
		UserDetails user = User.withUsername("Aliyu")
				          .password("1234")
				          .authorities("read")
				          .build();
		return new InMemoryUserDetailsManager(user);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder () {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
		http.csrf(
				
				c -> c.disable()
				
				)
		
		.authorizeHttpRequests(
				
				request -> request.requestMatchers("/css/**", "/oauth2/**").permitAll().anyRequest().authenticated()
				
				)
		
		.formLogin(
				form -> form.loginPage("/login").permitAll()
				    .loginProcessingUrl("/login")
				    .defaultSuccessUrl("/home")
			
				)
		
		.oauth2Login(
				
				form -> form.loginPage("/login").permitAll().defaultSuccessUrl("/home")
				
				)
		
		
		
		.logout(
				
			 form -> form.invalidateHttpSession(true).clearAuthentication(true)
			  .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			  .logoutSuccessUrl("/login?logout")	
			  .permitAll()
				);
		
		
		
		
		return http.build();
		
		
	}
	
	
	

}
