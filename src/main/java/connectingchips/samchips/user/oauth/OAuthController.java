package connectingchips.samchips.user.oauth;

import connectingchips.samchips.commons.dto.BasicResponse;
import connectingchips.samchips.user.domain.SocialType;
import jakarta.persistence.Basic;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/{socialType}")
    public BasicResponse redirectAuthCodeRequestUrl(@PathVariable SocialType socialType, HttpServletResponse response) throws IOException {
        String redirectUrl = oAuthService.getAuthCodeRequestUrl(socialType);
        response.sendRedirect(redirectUrl);

        return BasicResponse.of(HttpStatus.OK);
    }
}
