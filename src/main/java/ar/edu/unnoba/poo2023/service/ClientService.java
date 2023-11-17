package ar.edu.unnoba.poo2023.service;

import ar.edu.unnoba.poo2023.model.Client;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ClientService {
    public void create(Client client);
    public void update(Client client, Long id);
    public void delete(Long id);
    public List<Client> getClients();
    public Client getClientById(Long id);
}
