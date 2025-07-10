package lms.com.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lms.com.common.Constant;
import lms.com.common.LMSResponse;
import lms.com.config.JwtService;
import lms.com.dtos.UserDTO;
import lms.com.entity.Token;
import lms.com.entity.User;
import lms.com.entity.enums.Role;
import lms.com.entity.enums.TokenType;
import lms.com.exceptions.BadRequestException;
import lms.com.exceptions.DuplicateException;
import lms.com.exceptions.EntityCreationException;
import lms.com.mapper.UserMapper;
import lms.com.repository.TokenRepository;
import lms.com.repository.UserRepository;
import lms.com.utils.DateUtil;
import lms.com.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${user.file.path.absolutePath}")
    private String absolutePath;

    @Value(("${user.file.path.relativePath}"))
    private String relativePath;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final FileUtil fileUtil;

    public LMSResponse register(UserDTO userDTO) throws IOException {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateException(Constant.USER_REGISTERED + userDTO.getEmail());
        }

        User user = UserMapper.dtoToEntity(userDTO, passwordEncoder);
        User savedUser = userRepository.save(user);

        Long userId = savedUser.getId();
        if (userDTO.getImage() != null && !userDTO.getImage().isEmpty()) {
            try {
                String imagePath = fileUtil.writeMediaFile(userDTO.getImage(), absolutePath, relativePath, userId);
                savedUser.setImagePath(imagePath);
                userRepository.save(savedUser);
            } catch (IOException ex) {
                throw new EntityCreationException("User image upload failed.");
            }
        }

        return LMSResponse.success(Constant.USER_REGISTER_SUCCESS, UserMapper.entityToDto(savedUser));
    }

    public LMSResponse authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );
        Optional<User> userOpt = userRepository.findByEmail(authRequest.getEmail());
        if (userOpt.isEmpty() || !userOpt.get().isEnabled()) {
            return LMSResponse.fail(Constant.USER_NOT_FOUND, "User is enabled or not found");
        }

        User user = userOpt.get();

        // Generate JWT tokens for the authenticated user
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        Role userRole = null;
        if (user.getUserRole() != null && !user.getUserRole().name().isEmpty()) {
            userRole = user.getUserRole();
        }
        return LMSResponse.success(Constant.AUTH_SUCCESS, AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .role(userRole)
                .userId(user.getId())
                .build()
        );
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .loginAt(DateUtil.getNowDate())
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
            token.setLogoutAt(DateUtil.getNowDate());
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader =request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new BadRequestException("Refresh token is missing or malformed.");
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            Optional<User> userOpt = this.userRepository.findByEmail(userEmail);

            if (userOpt.isPresent()) {
                User user = userOpt.get();

                if (jwtService.isTokenValid(refreshToken, user)) {
                    String accessToken = jwtService.generateToken(user);

                    revokeAllUserTokens(user);
                    saveUserToken(user, accessToken);

                    AuthResponse authResponse = AuthResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();

                    response.setContentType("application/json");
                    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                }
            }
        }
    }
}
