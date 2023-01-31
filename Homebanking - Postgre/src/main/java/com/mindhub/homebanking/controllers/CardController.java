package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class CardController {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCards(@RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication){

        Client clientCard = clientRepository.findByEmail(authentication.getName());

        int cardCvv = getRandomNumber(100,999);

        String cardNumber = getRandomNumber(1000,9999) + "-" + getRandomNumber(1000,9999) + "-" + getRandomNumber(1000,9999) + "-" + getRandomNumber(1000,9999);

        if(cardType == null || cardColor == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if(clientCard.getCards().stream().filter(card -> card.getCardType().equals(cardType)).count() >= 3){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if(clientCard.getCards().stream().filter(card -> card.getCardColor().equals(cardColor) && card.getCardType().equals(cardType)).count() >= 1){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        cardRepository.save(new Card(clientCard, clientCard.toString(), cardType, cardColor, cardNumber,cardCvv,LocalDateTime.now(), LocalDateTime.now().plusYears(5)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}