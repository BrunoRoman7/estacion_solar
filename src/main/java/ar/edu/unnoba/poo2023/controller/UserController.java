package ar.edu.unnoba.poo2023.controller;


import ar.edu.unnoba.poo.model.User;
import ar.edu.unnoba.poo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public String users(Model model){
        model.addAttribute("users", userService.getUsers());
        return "users/index";
    }
    @GetMapping("/new")
    public String newuser(Model model){
        model.addAttribute("user", new User());
        return "users/new";
    }


    @PostMapping
    public String create( @ModelAttribute("user") User user, Model model) {


        // Verificar si se completaron todos los campos
        if (user.getUsername() == "" || user.getApellido() == "" || user.getEmail() == "" || user.getPassword() == "") {
            model.addAttribute("error_incomplete", "Por favor, complete todos los campos.");
            return "users/new";
        }

        // Procesar y guardar el usuario si no hay errores de validación y se completaron todos los campos
        // Puedes agregar aquí la lógica para guardar el usuario en la base de datos
        userService.create(user);
        return "redirect:/users"; // Redirecciona a la página de usuarios u otra página de éxito
    }

    @GetMapping("/{id}")
    public String view(@PathVariable("id") int id, Model model){
        model.addAttribute("user", userService.getUserByTaxID(id));
        return "users/view";
    }

    /*

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") String id, Model model){
        model.addAttribute("user", userService.getUserByTaxID(id));
        return "edit";
    }


    @PostMapping("/{id}/update")
    public String update(@ModelAttribute User user, @PathVariable("id") int id, Model model){
        // Lógica para actualizar el usuario
        return "redirect:/user/index";
    }*/

    /*@GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id){
        // Lógica para eliminar el usuario
        return "redirect:/user/index";
    }
*/

}
