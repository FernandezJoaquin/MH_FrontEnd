package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.UserRole;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientService {
    @Autowired
    ClientRepository clientRepo;

    @Override
    public List<ClientDTO> getClients() {
        return clientRepo.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @Override
    public ClientDTO getClientDTO(Long id) {
        return new ClientDTO(clientRepo.findById(id).orElse(null));
    }
    @Override
    public ClientDTO getClientDTO(String email) {
        return new ClientDTO(this.getClient(email));
    }
    @Override
    public Client getClient(String email) {
        return clientRepo.findByEmail(email);
    }

    @Override
    public Client generateNewClient(String firstName, String lastName, String email, String password) {
        return new Client(firstName, lastName, email, password, UserRole.CLIENT);
    }

    @Override
    public void saveClient(Client client) {
        clientRepo.save(client);
    }
}
