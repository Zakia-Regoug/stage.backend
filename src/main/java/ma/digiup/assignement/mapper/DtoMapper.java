package ma.digiup.assignement.mapper;

import ma.digiup.assignement.domain.Deposit;
import ma.digiup.assignement.domain.Transfer;
import ma.digiup.assignement.dto.DepositDto;
import ma.digiup.assignement.dto.TransferDto;
import org.springframework.stereotype.Service;

@Service
public class DtoMapper {

    public TransferDto map(Transfer transfer) {
        TransferDto transferDto=new TransferDto();
        transferDto.setNrCompteEmetteur(transfer.getCompteEmetteur().getNrCompte());
        transferDto.setNrCompteBeneficiaire(transfer.getCompteBeneficiaire().getNrCompte());
        transferDto.setDate(transfer.getDateExecution());
        transferDto.setMotif(transfer.getMotifTransfer());
        transferDto.setMontant(transfer.getMontantTransfer());
        return transferDto;

    }

    public DepositDto map(Deposit deposit) {
        DepositDto depositDto=new DepositDto();
        depositDto.setNom_prenom_emetteur(deposit.getNom_prenom_emetteur());
        depositDto.setRibBeneficiaire(deposit.getCompteBeneficiaire().getRib());
        depositDto.setDate(deposit.getDateExecution());
        depositDto.setMotif(deposit.getMotifDeposit());
        depositDto.setMontant(deposit.getMontant());

        return depositDto;

    }
}
