package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class CardController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCards(@RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication){

        Client clientCard = clientService.getClientsByEmail(authentication.getName());

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

        cardService.saveCard(new Card(clientCard, clientCard.toString(), cardType, cardColor, cardNumber,cardCvv,LocalDateTime.now(), LocalDateTime.now().plusYears(5),true));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PatchMapping("/api/client/current/cards/remove")
    public ResponseEntity<Object> removeCard (Authentication authentication,@RequestParam Long cardId ){

        Client authenticationClient = clientService.getClientsByEmail(authentication.getName());

        Card card = cardService.getCardsById(cardId);

        if(card == null){
            return new ResponseEntity<>("The card doesn´t exist", HttpStatus.FORBIDDEN);
        }
        if(authenticationClient == null){
            return new ResponseEntity<>("Client doesn't exist", HttpStatus.FORBIDDEN);
        }
        if(!authenticationClient.getCards().contains(card)){
            return new ResponseEntity<>("Card doesn´t exist", HttpStatus.FORBIDDEN);
        }

        cardService.removeCard(card);
        return new ResponseEntity<>("Card has been removed successfully", HttpStatus.ACCEPTED);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}