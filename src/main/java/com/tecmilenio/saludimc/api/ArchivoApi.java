package com.tecmilenio.saludimc.api;

import com.tecmilenio.saludimc.domain.*;
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
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping(value="/api/file",produces={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
public class ArchivoApi {
    private final ArchivoRepository archivos;
    private final DirectorioRepository directorios;
    private final MedicionRepository mediciones;
    private final UsuarioService usuarios;
    public ArchivoApi(ArchivoRepository archivos,DirectorioRepository directorios,
                      MedicionRepository mediciones,UsuarioService usuarios){
        this.archivos=archivos;this.directorios=directorios;this.mediciones=mediciones;this.usuarios=usuarios;
    }
    private Archivo buscar(Long u,Long id){
        return archivos.findByIdAndUsuarioId(id,u).orElseThrow(()->
            new ResponseStatusException(HttpStatus.NOT_FOUND,"Reporte no encontrado"));
    }
    @GetMapping public Object listar(Authentication auth){
        var u=usuarios.actual(auth.getName());
        return ApiSupport.response(archivos.findByUsuarioIdOrderByFechaDesc(u.id).stream().map(FileView::of).toList(),
            "self","/api/file","GET","create","/api/file","POST","directory","/api/directory","GET");
    }
    @PostMapping(consumes={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> crear(Authentication auth,@Valid @RequestBody Inputs.ArchivoInput input){
        var u=usuarios.actual(auth.getName());
        var d=directorios.findByIdAndUsuarioId(input.directorioId(),u.id).orElseThrow(()->
            new ResponseStatusException(HttpStatus.NOT_FOUND,"Carpeta no encontrada"));
        List<Medicion> historial=mediciones.findByUsuarioIdOrderByFechaDesc(u.id);
        StringBuilder csv=new StringBuilder("Fecha,Peso (kg),Estatura (m),IMC,Estado\r\n");
        for(Medicion m:historial){
            csv.append(m.fecha).append(',').append(m.peso).append(',').append(m.estatura)
               .append(',').append(m.valorImc).append(',').append(m.estado).append("\r\n");
        }
        var a=new Archivo();a.usuario=u;a.directorio=d;a.nombre=input.nombre().trim();a.contenido=csv.toString();
        archivos.save(a);
        String url="/api/file/"+a.id;
        return ResponseEntity.created(URI.create(url)).body(ApiSupport.response(FileView.of(a),
              "self",url,"GET","download",url+"/download","GET","collection","/api/file","GET"));
    }
    @GetMapping("/{id}") public Object ver(Authentication auth,@PathVariable Long id){
        return ApiSupport.response(FileView.of(buscar(usuarios.actual(auth.getName()).id,id)),
               "self","/api/file/"+id,"GET","download","/api/file/"+id+"/download","GET",
               "update","/api/file/"+id,"PUT","delete","/api/file/"+id,"DELETE");
    }
    @PutMapping(value="/{id}",consumes={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public Object cambiarNombre(Authentication auth,@PathVariable Long id,@Valid @RequestBody Inputs.NombreInput input){
        var a=buscar(usuarios.actual(auth.getName()).id,id);
        a.nombre=input.nombre().trim();archivos.save(a);
        return ApiSupport.response(FileView.of(a),"self","/api/file/"+id,"GET");
    }
    @GetMapping(value="/{id}/download",produces="text/csv")
    public ResponseEntity<byte[]> descargar(Authentication auth,@PathVariable Long id){
        var a=buscar(usuarios.actual(auth.getName()).id,id);
        String nombre=a.nombre.replaceAll("[^a-zA-Z0-9._-]","_");
        if(!nombre.toLowerCase().endsWith(".csv"))nombre+=".csv";
        return ResponseEntity.ok()
           .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+nombre+"\"")
           .contentType(new MediaType("text","csv",StandardCharsets.UTF_8))
           .body(("\uFEFF"+a.contenido).getBytes(StandardCharsets.UTF_8));
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(Authentication auth,@PathVariable Long id){
        archivos.delete(buscar(usuarios.actual(auth.getName()).id,id));
        return ResponseEntity.noContent().build();
    }
}
