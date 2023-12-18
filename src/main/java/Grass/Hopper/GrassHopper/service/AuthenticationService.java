package Grass.Hopper.GrassHopper.service;

import Grass.Hopper.GrassHopper.dto.LoginRequestDto;
import Grass.Hopper.GrassHopper.dto.LoginResponseDto;
import Grass.Hopper.GrassHopper.dto.RefreshTokenDto;
import Grass.Hopper.GrassHopper.dto.RegisterRequestDto;
import Grass.Hopper.GrassHopper.entity.UserEntity;
import Grass.Hopper.GrassHopper.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void signup(RegisterRequestDto signUpRequestDto){

        userRepository.save(
                UserEntity.builder()
                        .firstName(signUpRequestDto.getFirstName())
                        .lastName(signUpRequestDto.getLastName())
                        .username(signUpRequestDto.getUsername())
                        .email(signUpRequestDto.getEmail())
                        .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                        .profilePicturePath("C:/uploads/DummyProfilePicture.jpg")
                        .bannerPicturePath("C:/uploads/DummyBannerPicture.jpg")
                        .build()
        );

    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtService.generateToken(authentication);
        String refreshToken = jwtService.generateRefreshToken().getRefreshToken();
        String username = loginRequestDto.getUsername();

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(username)
                .expiresAt(Instant.now().plusMillis(jwtService.getJwtExpirationInMillis()))
                .build();
    }

    public void logout(RefreshTokenDto refreshTokenDto){
        jwtService.deleteRefreshToken(refreshTokenDto.getRefreshToken());
        SecurityContextHolder.clearContext();
    }

    
    

    public String extractUsernameFromRefreshToken(String refreshToken) {
        // Aquí debes implementar la lógica para extraer el nombre de usuario del token de actualización.
        // Puedes usar la misma lógica que utilizaste al generar el token originalmente.

        // Por ejemplo, si estás almacenando el nombre de usuario en el subject del token,
        // puedes usar el siguiente código:
        Jwt jwt = parseJwt(refreshToken);
        return jwt.getSubject();
    }

    private Jwt parseJwt(String token) {
        // Aquí debes implementar la lógica para parsear el token. Usa la biblioteca Nimbus JWT o la que estés utilizando.
        // La lógica dependerá de cómo estás almacenando la información en tus tokens.

        // Este es un ejemplo simple utilizando Nimbus JWT, pero necesitas ajustarlo según tu configuración:
        // Jwt jwt = Jwt.parse(token);
        // return jwt;

        return null; // Ajusta esto según tu implementación real.
    }

public LoginResponseDto refreshToken(RefreshTokenDto refreshTokenDto) {
        try {
            // Intenta validar el token de actualización
            jwtService.validateRefreshToken(refreshTokenDto.getRefreshToken());
    
            // Si no se lanza una excepción, el token de actualización es válido
            // Obtén el nombre de usuario del token actualizado
            String username = jwtService.extractUsernameFromRefreshToken(refreshTokenDto.getRefreshToken());
    
            // Genera un nuevo token de acceso con el nombre de usuario
            String accessToken = jwtService.generateTokenWithUsername(username);
    
            return LoginResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshTokenDto.getRefreshToken())
                    .username(username)
                    .expiresAt(Instant.now().plusMillis(jwtService.getJwtExpirationInMillis()))
                    .build();
        } catch (Exception e) {
            // Maneja la excepción (por ejemplo, loguea el error)
            e.printStackTrace(); // o utiliza un logger
            // Puedes devolver un objeto LoginResponseDto con valores predeterminados o nulos en caso de error.
            return LoginResponseDto.builder().build();
        }
    }


    public UserEntity getUserFromJwt(){
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findByUsername(principal.getSubject()).orElseThrow();
    }

    public List<String> findAllUsernames(){
        return this.userRepository.findAll().stream().map(user -> user.getUsername()).collect(Collectors.toList());
    }
}
