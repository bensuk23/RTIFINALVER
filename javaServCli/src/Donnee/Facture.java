package Donnee;

import java.io.Serializable;
import java.sql.Date;

public class Facture implements Serializable
{
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public boolean isPaye() {
        return paye;
    }

    public void setPaye(boolean paye) {
        this.paye = paye;
    }

    private int id;
    private int idClient;
    private Date date;
    private double montant;
    private boolean paye;

    public Facture(int id, int idClient, Date date, double montant, boolean paye)
    {
        this.id = id;
        this.idClient = idClient;
        this.date = date;
        this.montant = montant;
        this.paye = paye;
    }

    @Override
    public String toString() {
        return "Facture{" +
                "id=" + id +
                ", idClient=" + idClient +
                ", date=" + date +
                ", montant=" + montant +
                ", paye=" + paye +
                '}';
    }
}