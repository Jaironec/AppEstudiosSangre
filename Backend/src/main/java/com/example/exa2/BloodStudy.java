package com.example.exa2;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import nonapi.io.github.classgraph.json.Id;

import java.util.Date;

@Document(collection = "bloodStudies")
public class BloodStudy {
    @Id
    private String id;

    @Field("nombreCompleto")
    private String nombreCompleto;

    @Field("porcentajeAzucar")
    private double porcentajeAzucar;

    @Field("porcentajeGrasa")
    private double porcentajeGrasa;

    @Field("porcentajeOxigeno")
    private double porcentajeOxigeno;

    @Field("nivelRiesgo")
    private String nivelRiesgo;

    @CreatedDate
    @Field("fechaCreacion")
    private Date fechaCreacion;

    @LastModifiedDate
    @Field("fechaActualizacion")
    private Date fechaActualizacion;

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    public double getPorcentajeAzucar() {
        return porcentajeAzucar;
    }
    
    public void setPorcentajeAzucar(double porcentajeAzucar) {
        this.porcentajeAzucar = porcentajeAzucar;
    }
    
    public double getPorcentajeGrasa() {
        return porcentajeGrasa;
    }
    
    public void setPorcentajeGrasa(double porcentajeGrasa) {
        this.porcentajeGrasa = porcentajeGrasa;
    }
    
    public double getPorcentajeOxigeno() {
        return porcentajeOxigeno;
    }
    
    public void setPorcentajeOxigeno(double porcentajeOxigeno) {
        this.porcentajeOxigeno = porcentajeOxigeno;
    }
    
    public String getNivelRiesgo() {
        return nivelRiesgo;
    }
    
    public void setNivelRiesgo(String nivelRiesgo) {
        this.nivelRiesgo = nivelRiesgo;
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }
    
    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    
}
