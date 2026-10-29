package com.tecmilenio.saludimc.service;

/** Calculo antropometrico de ejemplo: IMC=peso(kg)/estatura(m)^2. */
public final class BmiCalculator {
    private BmiCalculator(){}
    public static double calcular(double peso,double estatura){
        if (!Double.isFinite(peso)||!Double.isFinite(estatura)||peso<=0||estatura<1||estatura>2.5)
            throw new IllegalArgumentException("El peso debe ser positivo y la estatura entre 1 y 2.5 m");
        return Math.round((peso/(estatura*estatura))*100.0)/100.0;
    }
    public static String clasificar(double imc){
        if(imc<18.5)return "Bajo peso";
        if(imc<25)return "Saludable";
        if(imc<30)return "Sobrepeso";
        return "Obesidad";
    }
}
