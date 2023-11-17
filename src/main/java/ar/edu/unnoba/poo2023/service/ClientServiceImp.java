package ar.edu.unnoba.poo2023.service;

import ar.edu.unnoba.poo2023.model.Client;
import ar.edu.unnoba.poo2023.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImp implements ClientService{

    private ClientRepository clientRepository;
    @Autowired
    public ClientServiceImp(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }
    @Override
    public void create(Client client) {
        clientRepository.save(client);
    }

    @Override
    public void update(Client client, Long id) {
        Client clientDB = clientRepository.findById(id).get();
        clientDB.setName(client.getName());
        clientDB.setTaxID(client.getTaxID());
        clientRepository.save(clientDB);
    }

    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id).get();
    }
}
