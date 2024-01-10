package ma.digiup.assignement.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.digiup.assignement.domain.Compte;
import ma.digiup.assignement.domain.Deposit;
import ma.digiup.assignement.domain.Transfer;
import ma.digiup.assignement.domain.Utilisateur;
import ma.digiup.assignement.dto.DepositDto;
import ma.digiup.assignement.dto.TransferDto;
import ma.digiup.assignement.exceptions.CompteNonExistantException;
import ma.digiup.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.digiup.assignement.exceptions.TransactionException;
import ma.digiup.assignement.mapper.DtoMapper;
import ma.digiup.assignement.repository.CompteRepository;
import ma.digiup.assignement.repository.DepositRepository;
import ma.digiup.assignement.repository.TransferRepository;
import ma.digiup.assignement.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankServiceImpl implements BankService {

    private TransferRepository transferRepository;
    private DepositRepository depositRepository;
    private DtoMapper dtoMapper;
    private CompteRepository compteRepository;
    private UtilisateurRepository utilisateurRepository;

    public final int MONTANT_MAXIMAL = 10000;

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Compte save(Compte compte) {
        return compteRepository.save(compte);
    }

    @Override
    public List<Utilisateur> allUsers() {
        log.info("Liste des utilisateurs");
        List<Utilisateur> utilisateurs=utilisateurRepository.findAll();
        if(utilisateurs.isEmpty()) return null;
        return utilisateurs;
    }

    @Override
    public List<Compte> allComptes() {
        log.info("Liste des comptes");
        List<Compte> comptes=compteRepository.findAll();
        if(comptes.isEmpty()) return null;
        return comptes;
    }

    @Override
    public List<TransferDto> allTransferts() {
        log.info("Liste des transferts");
        List<Transfer> transfers = transferRepository.findAll();
        List<TransferDto> transferDtos = transfers.stream()
                .map(transfer -> dtoMapper.map(transfer))
                .collect(Collectors.toList());

        if(transferDtos.isEmpty()) return null;
        return transferDtos;
    }

    @Override
    public List<DepositDto> allDeposits() {
        log.info("Liste des depots");
        List<Deposit> deposits = depositRepository.findAll();
        List<DepositDto> depositDtos = deposits.stream()
                .map(deposit -> dtoMapper.map(deposit))
                .collect(Collectors.toList());

        if(depositDtos.isEmpty()) return null;
        return depositDtos;
    }

    @Override
    public void saveTransfer(TransferDto transferDto) throws CompteNonExistantException, TransactionException, SoldeDisponibleInsuffisantException {

        Compte compteEmetteur = compteRepository.findByNrCompte(transferDto.getNrCompteEmetteur());
        Compte compteBeneficiaire =compteRepository.findByNrCompte(transferDto.getNrCompteBeneficiaire());

        //Si le compte emetteur ou beneficiare n'existe pas
        if (compteEmetteur == null || compteBeneficiaire == null) {
            System.out.println("Compte Non existant");
            throw new CompteNonExistantException("Compte Non existant");
        }

        //contraintes des montant
        if (transferDto.getMontant().equals(null)) {
            System.out.println("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (transferDto.getMontant().intValue() == 0) {
            System.out.println("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (transferDto.getMontant().intValue() < 10) {
            System.out.println("Montant minimal de transfer non atteint");
            throw new TransactionException("Montant minimal de transfer non atteint");
        } else if (transferDto.getMontant().intValue() > MONTANT_MAXIMAL) {
            System.out.println("Montant maximal de transfer dépassé");
            throw new TransactionException("Montant maximal de transfer dépassé");
        }

        if (transferDto.getMotif().length() < 0) {
            System.out.println("Motif vide");
            throw new TransactionException("Motif vide");
        }

        if (compteEmetteur.getSolde().intValue() - transferDto.getMontant().intValue() < 0) {
            log.error("Solde insuffisant pour l'emetteur");
            throw new SoldeDisponibleInsuffisantException("Solde insuffisant pour l'emetteur");
        }
        //soustraire le montant de transfer du compte emetteur
        compteEmetteur.setSolde(compteEmetteur.getSolde().subtract(transferDto.getMontant()));
        //enregistrer la modification
        compteRepository.save(compteEmetteur);
        //ajouter le montant de transfert dans le compte beneficiaire et enregistrer dans la bdd
        compteBeneficiaire.setSolde(new BigDecimal(compteBeneficiaire.getSolde().intValue() + transferDto.getMontant().intValue()));
        compteRepository.save(compteBeneficiaire);

        Transfer transfer = new Transfer();
        transfer.setDateExecution(new Date());
        transfer.setCompteBeneficiaire(compteBeneficiaire);
        transfer.setCompteEmetteur(compteEmetteur);
        transfer.setMontantTransfer(transferDto.getMontant());
        transfer.setMotifTransfer(transferDto.getMotif());
        transferRepository.save(transfer);
    }

    @Override
    public void saveDeposit(DepositDto depositDto) throws CompteNonExistantException, TransactionException, SoldeDisponibleInsuffisantException{

        Compte compteBeneficiaire =compteRepository.findByRib(depositDto.getRibBeneficiaire());

        //Si le compte beneficiare n'existe pas
        if (compteBeneficiaire == null) {
            System.out.println("Compte Non existant");
            throw new CompteNonExistantException("Compte Non existant");
        }

        //contraintes des montant : on peux mieux structurer ca par factoriser les regles qui sont repetes dans les deux methodes
        if (depositDto.getMontant().equals(null)) {
            System.out.println("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (depositDto.getMontant().intValue() == 0) {
            System.out.println("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (depositDto.getMontant().intValue() < 10) {
            System.out.println("Montant minimal de transfer non atteint");
            throw new TransactionException("Montant minimal de transfer non atteint");
        } else if (depositDto.getMontant().intValue() > MONTANT_MAXIMAL) {
            System.out.println("Montant maximal de transfer dépassé");
            throw new TransactionException("Montant maximal de transfer dépassé");
        }

        if (depositDto.getMotif().length() < 0) {
            System.out.println("Motif vide");
            throw new TransactionException("Motif vide");
        }
        //ajouter le montant de transfert dans le compte beneficiaire et enregistrer dans la bdd
        compteBeneficiaire.setSolde(new BigDecimal(compteBeneficiaire.getSolde().intValue() + depositDto.getMontant().intValue()));
        compteRepository.save(compteBeneficiaire);

        Deposit deposit = new Deposit();
        deposit.setDateExecution(new Date());
        deposit.setCompteBeneficiaire(compteBeneficiaire);
        deposit.setNom_prenom_emetteur(depositDto.getNom_prenom_emetteur());
        deposit.setMontant(depositDto.getMontant());
        deposit.setMotifDeposit(depositDto.getMotif());

        depositRepository.save(deposit);
    }
}
