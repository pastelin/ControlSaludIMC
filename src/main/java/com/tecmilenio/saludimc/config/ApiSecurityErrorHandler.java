package com.tecmilenio.saludimc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;

/** Respuestas 401/403 de seguridad con negociacion basica JSON/XML. */
@Component
public class ApiSecurityErrorHandler implements AuthenticationEntryPoint,AccessDeniedHandler {
    private final ObjectMapper json;
    private final XmlMapper xml = new XmlMapper();
    public record ErrorSeguridad(int status,String error,String mensaje,String ruta,String fecha){}
    public ApiSecurityErrorHandler(ObjectMapper json){this.json=json;}
    @Override
    public void commence(HttpServletRequest req,HttpServletResponse res,AuthenticationException e) throws IOException {
        escribir(req,res,401,"Debes iniciar sesión para consultar este recurso");
    }
    @Override
    public void handle(HttpServletRequest req,HttpServletResponse res,AccessDeniedException e) throws IOException {
        if(req.getRequestURI().startsWith("/api/")) escribir(req,res,403,"Acceso prohibido o token CSRF faltante");
        else res.sendError(403);
    }
    private void escribir(HttpServletRequest req,HttpServletResponse res,int status,String mensaje) throws IOException {
        String accept=req.getHeader("Accept");
        boolean asXml=accept!=null&&(accept.contains("application/xml")||accept.contains("text/xml"));
        res.setStatus(status);
        res.setCharacterEncoding("UTF-8");
        res.setContentType(asXml?"application/xml":"application/json");
        res.setHeader("Cache-Control","no-store");
        ErrorSeguridad error=new ErrorSeguridad(status,"Error de seguridad",mensaje,req.getRequestURI(),LocalDateTime.now().toString());
        if(asXml)xml.writeValue(res.getOutputStream(),error);
        else json.writeValue(res.getOutputStream(),error);
    }
}
