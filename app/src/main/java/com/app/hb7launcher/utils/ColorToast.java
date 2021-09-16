package com.app.hb7launcher.utils;

/*
 * Created by Issam ELGUERCH on 10/8/2020.
 * mail: issamelguerch@gmail.com
 * All rights reserved.
 */


public enum ColorToast {
    GREEN(""), RED(""), GREY("");

    private final String valeur;

    private ColorToast(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return this.valeur;
    }
}