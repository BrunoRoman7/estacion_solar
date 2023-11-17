package ar.edu.unnoba.poo2023.controller;

import ar.edu.unnoba.poo2023.model.Client;
import ar.edu.unnoba.poo2023.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping("/new")
    public String newClient(Model model){
        model.addAttribute("client",new Client());
        return "clients/new";
    }

    @PostMapping
    public String create(@ModelAttribute Client client){
        clientService.create(client);
        return "redirect:/clients";
    }

    @GetMapping
    public String clients(Model model){
        model.addAttribute("clients",clientService.getClients());
        return "clients/index";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable("id") Long id,Model model){
        model.addAttribute("client",clientService.getClientById(id));
        return "clients/view";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id,Model model){
        model.addAttribute("client",clientService.getClientById(id));
        return "clients/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute Client client,@PathVariable("id") Long id,Model model){
        clientService.update(client,id);
        return "redirect:/clients";
    }

    @GetMapping("/{id}/delete")
    public String view(@PathVariable("id") Long id){
        clientService.delete(id);
        return "redirect:/clients";
    }
}
