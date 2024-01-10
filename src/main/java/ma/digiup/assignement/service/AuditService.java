package ma.digiup.assignement.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.digiup.assignement.domain.AuditDeposit;
import ma.digiup.assignement.domain.AuditTransfer;
import ma.digiup.assignement.repository.AuditDepositRepository;
import ma.digiup.assignement.repository.AuditTransferRepository;
import ma.digiup.assignement.domain.util.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class AuditService {
    private AuditTransferRepository auditTransferRepository;

    private AuditDepositRepository auditDepositRepository;




    public void auditTransfer(String message) {

        log.info("Audit de l'événement {}", EventType.TRANSFER);

        AuditTransfer audit = new AuditTransfer();
        audit.setEventType(EventType.TRANSFER);
        audit.setMessage(message);
        auditTransferRepository.save(audit);
    }


    public void auditDeposit(String message) {

        log.info("Audit de l'événement {}", EventType.DEPOSIT);

        AuditDeposit audit = new AuditDeposit();
        audit.setEventType(EventType.DEPOSIT);
        audit.setMessage(message);
        auditDepositRepository.save(audit);
    }
}
