package com.ex.sothat.global.auth;

import com.ex.sothat.domain.account.Account;
import com.ex.sothat.domain.account.AccountRepository;
import com.ex.sothat.domain.account.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AccountRepository accountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String email = getEmail(user, provider);

        Optional<Account> accountOptional = accountRepository.findByEmail(email);
        Account account;

        if (accountOptional.isPresent()) {
            account = accountOptional.get();
            if (!account.getProvider().equals(provider)) {
                throw new OAuth2AuthenticationException("Looks like you're signed up with " + account.getProvider() + " account. Please use your " + account.getProvider() + " account to login.");
            }
            account = updateExistingUser(account, user, provider);
        } else {
            account = registerNewUser(user, provider);
        }

        return AccountPrincipal.create(account, user.getAttributes());
    }

    private Account registerNewUser(OAuth2User oAuth2User, String provider) {
        Account account = Account.builder()
                .email(getEmail(oAuth2User, provider))
                .name(getName(oAuth2User, provider))
                .provider(provider)
                .build()
                .addRole(Authority.ROLE_USER);

        return accountRepository.save(account);
    }

    private Account updateExistingUser(Account account, OAuth2User oAuth2User, String provider) {
        account.setName(getName(oAuth2User, provider));
        return accountRepository.save(account);
    }

    private String getEmail(OAuth2User oAuth2User, String provider) {
        switch (provider) {
            case "google":
                return oAuth2User.getAttribute("email");
            // Add cases for other providers as needed
            default:
                throw new OAuth2AuthenticationException("Sorry! Login with " + provider + " is not supported yet.");
        }
    }

    private String getName(OAuth2User oAuth2User, String provider) {
        switch (provider) {
            case "google":
                return oAuth2User.getAttribute("name");
            // Add cases for other providers as needed
            default:
                throw new OAuth2AuthenticationException("Sorry! Login with " + provider + " is not supported yet.");
        }
    }
}