package com.erd.nextdev2016.helper;

/**
 * Created by Ersa Rizki Dimitri on 17/05/2015.
 */
public class Restaurant {
    public int id;
    public String judul;
    public String snippet;
    public String isi_berita;
    public String gambar;

    public Restaurant() {
    }

    public Restaurant(int id, String judul, String snippet, String isi_berita, String gambar) {
        this.id = id;
        this.judul = judul;
        this.snippet = snippet;
        this.isi_berita = isi_berita;
        this.gambar = gambar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getIsi_berita() {
        return isi_berita;
    }

    public void setIsi_berita(String isi_berita) {
        this.isi_berita = isi_berita;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
