package com.tecmilenio.saludimc.web;

import com.tecmilenio.saludimc.dto.RegistroForm;
import com.tecmilenio.saludimc.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;

@Controller
public class PaginasController {
    private final UsuarioService usuarios;
    public PaginasController(UsuarioService usuarios){this.usuarios=usuarios;}
    @GetMapping("/") public String inicio(){return "redirect:/login";}
    @GetMapping("/login") public String login(Authentication auth){
        if (auth!=null&&auth.isAuthenticated()) return "redirect:/imc";
        return "login";
    }
    @GetMapping("/registro") public String registro(Model model){
        model.addAttribute("registroForm",new RegistroForm());return "registro";
    }
    @PostMapping("/registro") public String crear(@Valid @ModelAttribute("registroForm") RegistroForm registro,
                                                      BindingResult errores,Model model){
        if(errores.hasErrors()){model.addAttribute("mensaje","Verifica los datos: edad mínima 15 años, estatura de 1 a 2.5 m y contraseña de 8 caracteres.");return "registro";}
        try{usuarios.registrar(registro);return "redirect:/login?registrado";}
        catch(ResponseStatusException e){model.addAttribute("mensaje",e.getReason());return "registro";}
    }
    @GetMapping("/imc") public String panel(Model model,Authentication auth){
        var u=usuarios.actual(auth.getName());
        model.addAttribute("usuario",u.nombreCompleto);
        model.addAttribute("estatura",u.estatura);
        return "imc";
    }
}
