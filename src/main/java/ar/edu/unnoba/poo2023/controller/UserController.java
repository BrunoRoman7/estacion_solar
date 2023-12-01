package ar.edu.unnoba.poo2023.controller;

import ar.edu.unnoba.poo2023.model.User;
import ar.edu.unnoba.poo2023.service.CSVDataReader;
import ar.edu.unnoba.poo2023.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CSVDataReader csvdataReader;



    @GetMapping
    public String users(Model model){
        model.addAttribute("users", userService.getUsers());
        return "users/usuarios";
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


        userService.create(user);
        return "redirect:/users";
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
