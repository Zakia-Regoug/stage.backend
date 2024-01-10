package ma.digiup.assignement.dto;

import java.math.BigDecimal;
import java.util.Date;

public class DepositDto {

  private String nom_prenom_emetteur;
  private String ribBeneficiaire;
  private String motif;
  private BigDecimal montant;
  private Date date;



  //getters and setters
  public String getNom_prenom_emetteur() {
    return nom_prenom_emetteur;
  }

  public void setNom_prenom_emetteur(String nom_prenom_emetteur) {
    this.nom_prenom_emetteur = nom_prenom_emetteur;
  }

  public String getRibBeneficiaire() {
    return ribBeneficiaire;
  }

  public void setRibBeneficiaire(String ribBeneficiaire) {
    this.ribBeneficiaire = ribBeneficiaire;
  }

  public BigDecimal getMontant() {
    return montant;
  }

  public void setMontant(BigDecimal montant) {
    this.montant = montant;
  }

  public String getMotif() {
    return motif;
  }

  public void setMotif(String motif) {
    this.motif = motif;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}
