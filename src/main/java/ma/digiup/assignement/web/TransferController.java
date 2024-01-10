package ma.digiup.assignement.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.digiup.assignement.domain.Utilisateur;
import ma.digiup.assignement.dto.DepositDto;
import ma.digiup.assignement.repository.UtilisateurRepository;
import ma.digiup.assignement.domain.Compte;
import ma.digiup.assignement.domain.Transfer;
import ma.digiup.assignement.dto.TransferDto;
import ma.digiup.assignement.exceptions.CompteNonExistantException;
import ma.digiup.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.digiup.assignement.exceptions.TransactionException;
import ma.digiup.assignement.repository.CompteRepository;
import ma.digiup.assignement.repository.TransferRepository;
import ma.digiup.assignement.service.AuditService;
import ma.digiup.assignement.service.BankService;
import ma.digiup.assignement.service.BankServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController(value = "/transfers")
@AllArgsConstructor
@Slf4j
class TransferController {

    private BankServiceImpl bankService;
    private AuditService auditService;


    @GetMapping("transferts")
    List<TransferDto> loadAll() {
        return bankService.allTransferts();
    }

    @GetMapping("deposits")
    List<DepositDto> loadAllDepots() {
        return bankService.allDeposits();
    }

    @GetMapping("comptes")
    List<Compte> loadAllCompte() {
        return bankService.allComptes();
    }

    @GetMapping("utilisateurs")
    List<Utilisateur> loadAllUtilisateur() {
        return bankService.allUsers();
    }

    @PostMapping("/transferts")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransaction(@RequestBody TransferDto transferDto)
            throws SoldeDisponibleInsuffisantException, CompteNonExistantException, TransactionException {

        bankService.saveTransfer(transferDto);
        auditService.auditTransfer("Transfer depuis " + transferDto.getNrCompteEmetteur() + " vers " + transferDto
                        .getNrCompteBeneficiaire() + " d'un montant de " + transferDto.getMontant()
                        .toString());
    }

    @PostMapping("/deposits")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransaction2(@RequestBody DepositDto depositDto)
            throws SoldeDisponibleInsuffisantException, CompteNonExistantException, TransactionException {

        bankService.saveDeposit(depositDto);
        auditService.auditDeposit("Depot depuis " + depositDto.getNom_prenom_emetteur() + " vers " +
                depositDto.getRibBeneficiaire() + " d'un montant de " + depositDto.getMontant()
                .toString());
    }


}
