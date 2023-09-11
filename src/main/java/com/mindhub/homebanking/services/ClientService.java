package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getClients();
    ClientDTO getClientDTO(Long id);
    ClientDTO getClientDTO(String email);
    Client getClient(String email);;
    Client generateNewClient(String firstName, String lastName, String email, String password);
    void saveClient(Client client);
}
