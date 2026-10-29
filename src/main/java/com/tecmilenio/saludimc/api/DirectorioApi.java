package com.tecmilenio.saludimc.api;

import com.tecmilenio.saludimc.domain.Directorio;
import com.tecmilenio.saludimc.dto.Inputs;
import com.tecmilenio.saludimc.dto.Views.*;
import com.tecmilenio.saludimc.repo.*;
import com.tecmilenio.saludimc.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.net.URI;

@RestController
@RequestMapping(value="/api/directory",produces={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
public class DirectorioApi {
    private final DirectorioRepository directorios;
    private final ArchivoRepository archivos;
    private final UsuarioService usuarios;
    public DirectorioApi(DirectorioRepository directorios,ArchivoRepository archivos,UsuarioService usuarios){
        this.directorios=directorios;this.archivos=archivos;this.usuarios=usuarios;
    }
    private Directorio buscar(Long userId,Long id){
        return directorios.findByIdAndUsuarioId(id,userId).orElseThrow(()->
            new ResponseStatusException(HttpStatus.NOT_FOUND,"Carpeta no encontrada"));
    }
    @GetMapping public Object listar(Authentication auth){
        var u=usuarios.actual(auth.getName());
        return ApiSupport.response(directorios.findByUsuarioIdOrderByIdAsc(u.id).stream().map(DirectoryView::of).toList(),
            "self","/api/directory","GET","create","/api/directory","POST");
    }
    @PostMapping(consumes={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> crear(Authentication auth,@Valid @RequestBody Inputs.NombreInput input){
        var u=usuarios.actual(auth.getName());
        var d=new Directorio();d.usuario=u;d.nombre=input.nombre().trim();directorios.save(d);
        String url="/api/directory/"+d.id;
        return ResponseEntity.created(URI.create(url)).body(ApiSupport.response(DirectoryView.of(d),
                "self",url,"GET","files","/api/file","GET"));
    }
    @GetMapping("/{id}") public Object uno(Authentication auth,@PathVariable Long id){
        var d=buscar(usuarios.actual(auth.getName()).id,id);
        String url="/api/directory/"+id;
        return ApiSupport.response(DirectoryView.of(d),"self",url,"GET","update",url,"PUT","delete",url,"DELETE");
    }
    @PutMapping(value="/{id}",consumes={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public Object editar(Authentication auth,@PathVariable Long id,@Valid @RequestBody Inputs.NombreInput input){
        var d=buscar(usuarios.actual(auth.getName()).id,id);d.nombre=input.nombre().trim();directorios.save(d);
        return ApiSupport.response(DirectoryView.of(d),"self","/api/directory/"+id,"GET");
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(Authentication auth,@PathVariable Long id){
        var d=buscar(usuarios.actual(auth.getName()).id,id);
        if(archivos.existsByDirectorioId(d.id)) throw new ResponseStatusException(HttpStatus.CONFLICT,"La carpeta tiene reportes; elimínalos antes");
        directorios.delete(d);return ResponseEntity.noContent().build();
    }
}
