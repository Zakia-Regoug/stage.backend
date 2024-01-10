package ma.digiup.assignement.service;

import ma.digiup.assignement.domain.Compte;
import ma.digiup.assignement.domain.Utilisateur;
import ma.digiup.assignement.dto.DepositDto;
import ma.digiup.assignement.dto.TransferDto;
import ma.digiup.assignement.exceptions.CompteNonExistantException;
import ma.digiup.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.digiup.assignement.exceptions.TransactionException;

import java.util.List;

public interface BankService {

   Utilisateur save(Utilisateur utilisateur);
   Compte save(Compte compte);

   List<Utilisateur> allUsers();
   List<Compte> allComptes();
   List<TransferDto> allTransferts();
   List<DepositDto> allDeposits();

   void saveTransfer(TransferDto transferDto) throws CompteNonExistantException, TransactionException, SoldeDisponibleInsuffisantException;
   void saveDeposit(DepositDto depositDto) throws CompteNonExistantException, TransactionException, SoldeDisponibleInsuffisantException;
}
