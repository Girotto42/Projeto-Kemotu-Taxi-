package com.esf.frota.controller;

import com.esf.frota.model.Funcionario;
import com.esf.frota.repository.FuncionarioRepository;
import com.esf.frota.service.FuncionarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final FuncionarioRepository repository;
    private final FuncionarioService funcionarioService;
    private final PasswordEncoder passwordEncoder;
    private static final Pattern BCRYPT_HASH = Pattern.compile("\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}");

    public AuthController(AuthenticationManager authenticationManager,
                          FuncionarioRepository repository,
                          FuncionarioService funcionarioService,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.repository = repository;
        this.funcionarioService = funcionarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public UsuarioResposta login(@Valid @RequestBody LoginRequest request) {
        Funcionario funcionario = repository.findByEmail(request.email().trim().toLowerCase())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));
        if (BCRYPT_HASH.matcher(funcionario.getSenha()).matches()) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.email().trim().toLowerCase(), request.senha()));
        } else {
            boolean senhaLegadaCorreta = MessageDigest.isEqual(
                    funcionario.getSenha().getBytes(StandardCharsets.UTF_8),
                    request.senha().getBytes(StandardCharsets.UTF_8));
            if (!senhaLegadaCorreta) throw new BadCredentialsException("Credenciais inválidas");
            funcionario.setSenha(request.senha());
            funcionarioService.salvar(funcionario);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.email().trim().toLowerCase(), request.senha()));
        }
        return UsuarioResposta.de(funcionario);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResposta registrar(@Valid @RequestBody CadastroRequest request) {
        String email = request.email().trim().toLowerCase();
        String cpf = request.cpf().replaceAll("\\D", "");
        if (cpf.length() != 11) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF deve conter 11 dígitos");
        }
        if (repository.findByEmail(email).isPresent() || repository.findByCpf(cpf).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail ou CPF já cadastrado");
        }
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(request.nome().trim());
        funcionario.setEmail(email);
        funcionario.setCpf(cpf);
        funcionario.setCargo(request.cargo().trim());
        funcionario.setSenha(request.senha());
        funcionario.setSituacao(Funcionario.SituacaoFuncionario.Ativo);
        return UsuarioResposta.de(funcionarioService.salvar(funcionario));
    }

    public record LoginRequest(@NotBlank @Email String email, @NotBlank String senha) {}
    public record CadastroRequest(@NotBlank @Size(max = 100) String nome,
                                  @NotBlank @Email @Size(max = 100) String email,
                                  @NotBlank String cpf,
                                  @NotBlank @Size(max = 50) String cargo,
                                  @NotBlank @Size(min = 8, max = 72) String senha) {}
    public record UsuarioResposta(Long idFuncionario, String nome, String email, String cargo) {
        static UsuarioResposta de(Funcionario funcionario) {
            return new UsuarioResposta(funcionario.getIdFuncionario(), funcionario.getNome(),
                    funcionario.getEmail(), funcionario.getCargo());
        }
    }
}
