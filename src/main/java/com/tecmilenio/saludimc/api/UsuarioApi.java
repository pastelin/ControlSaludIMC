package com.tecmilenio.saludimc.api;

import com.tecmilenio.saludimc.dto.*;
import com.tecmilenio.saludimc.dto.Views.*;
import com.tecmilenio.saludimc.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping(value="/api/user",produces={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
public class UsuarioApi {
    private final UsuarioService service;
    public UsuarioApi(UsuarioService service){this.service=service;}
    @PostMapping(consumes={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroForm input){
        var u=service.registrar(input);
        return ResponseEntity.created(URI.create("/api/user/me"))
            .body(ApiSupport.response(UserView.of(u),"self","/api/user/me","GET","login","/login","GET"));
    }
    @GetMapping("/me") public Object perfil(Authentication auth){
        return ApiSupport.response(UserView.of(service.actual(auth.getName())),
             "self","/api/user/me","GET","update","/api/user/me","PUT","delete","/api/user/me","DELETE",
             "history","/api/imc","GET");
    }
    @PutMapping(value="/me",consumes={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public Object actualizar(Authentication auth,@Valid @RequestBody Inputs.PerfilInput input){
        var u=service.actual(auth.getName());
        return ApiSupport.response(UserView.of(service.actualizar(u,input)),"self","/api/user/me","GET");
    }
    @DeleteMapping("/me") public ResponseEntity<Void> eliminar(Authentication auth,HttpServletRequest request){
        service.eliminar(service.actual(auth.getName()));
        try{ request.logout(); }catch(jakarta.servlet.ServletException e){ request.getSession().invalidate(); }
        return ResponseEntity.noContent().build();
    }
}
