package com.tecmilenio.saludimc.api;

import com.tecmilenio.saludimc.dto.*;
import com.tecmilenio.saludimc.dto.Views.*;
import com.tecmilenio.saludimc.service.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping(value="/api/imc",produces={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
public class ImcApi {
    private final ImcService imc;
    private final UsuarioService usuarios;
    public ImcApi(ImcService imc,UsuarioService usuarios){this.imc=imc;this.usuarios=usuarios;}
    @GetMapping public Object historial(Authentication auth){
        var registros=imc.historial(usuarios.actual(auth.getName())).stream().map(ImcView::of).toList();
        return ApiSupport.response(registros,"self","/api/imc","GET","create","/api/imc","POST","user","/api/user/me","GET");
    }
    @GetMapping("/{id}") public Object buscar(Authentication auth,@PathVariable Long id){
        return ApiSupport.response(ImcView.of(imc.buscar(usuarios.actual(auth.getName()),id)),
             "self","/api/imc/"+id,"GET","collection","/api/imc","GET","delete","/api/imc/"+id,"DELETE");
    }
    @PostMapping(consumes={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> registrar(Authentication auth,@Valid @RequestBody Inputs.MedicionInput input){
        var m=imc.guardar(usuarios.actual(auth.getName()),input);
        String url="/api/imc/"+m.id;
        return ResponseEntity.created(URI.create(url)).body(ApiSupport.response(ImcView.of(m),
             "self",url,"GET","collection","/api/imc","GET","notifications","/api/notify","GET"));
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(Authentication auth,@PathVariable Long id){
        imc.eliminar(usuarios.actual(auth.getName()),id);
        return ResponseEntity.noContent().build();
    }
}
