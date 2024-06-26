package com.sekolah.masterdata.dto;

public class CandidateResponseDTO {
    private Integer id;
    private String nama;
    private String tempatLahir;
    private String tanggalLahir;
    private String alamat;
    private String nomorKartuKeluarga;
    private String createdBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTempatLahir() {
        return tempatLahir;
    }

    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir = tempatLahir;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNomorKartuKeluarga() {
        return nomorKartuKeluarga;
    }

    public void setNomorKartuKeluarga(String nomorKartuKeluarga) {
        this.nomorKartuKeluarga = nomorKartuKeluarga;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}
