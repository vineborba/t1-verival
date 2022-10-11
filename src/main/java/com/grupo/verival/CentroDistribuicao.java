package com.grupo.verival;

// References: EXTREME GO HORSE

public class CentroDistribuicao {
    public enum SITUACAO {
        NORMAL, SOBRAVISO, EMERGENCIA
    }

    public enum TIPOPOSTO {
        COMUM, ESTRATEGICO
    }

    public static final int MAX_ADITIVO = 500;
    public static final int MAX_ALCOOL = 2500;
    public static final int MAX_GASOLINA = 10000;

    private int aditivo;
    private int gasolina;
    private int alcool;
    private int alcool2;
    private SITUACAO situacao;

    public CentroDistribuicao(int tAditivo, int tGasolina, int tAlcool1, int tAlcool2) {
        if (validaEntradas(tAditivo, tGasolina, tAlcool1, tAlcool2)) {
            throw new IllegalArgumentException("Valores de entrada invalidos");
        }
        this.aditivo = tAditivo;
        this.gasolina = tGasolina;
        this.alcool = tAlcool1;
        this.alcool2 = tAlcool2;

        defineSituação();
    }

    public void defineSituação() {
        if ((this.aditivo >= (MAX_ADITIVO * 0.5)) &&
                (this.gasolina >= (MAX_GASOLINA * 0.5)) &&
                (this.alcool >= (MAX_ALCOOL * 0.5)) &&
                (this.alcool2 >= (MAX_ALCOOL * 0.5))) {
            this.situacao = SITUACAO.NORMAL;
        } else if ((this.aditivo < (MAX_ADITIVO * 0.5) && this.aditivo >= (MAX_ADITIVO * 0.25)) ||
                (this.gasolina < (MAX_GASOLINA * 0.5) && this.gasolina >= (MAX_GASOLINA * 0.25)) ||
                (this.alcool < (MAX_ALCOOL * 0.5) && this.alcool >= (MAX_ALCOOL * 0.25)) ||
                (this.alcool2 < (MAX_ALCOOL * 0.5) && this.alcool2 >= (MAX_ALCOOL * 0.25))) {
            this.situacao = SITUACAO.SOBRAVISO;
        } else if ((this.aditivo < (MAX_ADITIVO * 0.25)) ||
                (this.gasolina < (MAX_GASOLINA * 0.25)) ||
                (this.alcool < (MAX_ALCOOL * 0.25)) ||
                (this.alcool2 < (MAX_ALCOOL * 0.25))) {
            this.situacao = SITUACAO.EMERGENCIA;
        }
    }

    private int[] getQuantidadeTanques() {
        int[] tanques = { aditivo, gasolina, alcool, alcool2 };
        return tanques;
    }

    private boolean encomenda(int qtdAditivo, int qtdGasolina, int qtdAlcool, int qtdAlcool2, int multiplicador) {
        int aditivo = this.aditivo - qtdAditivo * multiplicador / 100;
        int gasolina = this.gasolina - qtdGasolina * multiplicador / 100;
        int alcool = this.alcool - qtdAlcool / 2 * multiplicador / 100;
        int alcool2 = this.alcool2 - qtdAlcool / 2 * multiplicador / 100;

        if (aditivo < 0 || gasolina < 0 || alcool < 0 || alcool2 < 0)
            return false;

        this.aditivo = aditivo;
        this.gasolina = gasolina;
        this.alcool = alcool;
        this.alcool2 = alcool2;
        return true;
    }

    public int[] encomendaCombustivel(int qtdade, TIPOPOSTO tipoPosto) {
        if (qtdade < 0) {
            int[] fezBosta = { -7 };
            return fezBosta;
        }

        int qtdAditivo = qtdade * 5 / 100;
        int qtdGasolina = qtdade * 70 / 100;
        int qtdAlcool = qtdade * 25 / 100;

        boolean res = false;
        if (this.situacao == SITUACAO.NORMAL) {
            res = this.encomenda(qtdAditivo, qtdGasolina, qtdAlcool / 2, qtdAlcool / 2, 100);
        } else if ((this.situacao == SITUACAO.SOBRAVISO) && (tipoPosto == TIPOPOSTO.COMUM)) {
            this.encomenda(qtdAditivo, qtdGasolina, qtdAlcool / 2, qtdAlcool / 2, 50);
        } else if ((this.situacao == SITUACAO.SOBRAVISO) && (tipoPosto == TIPOPOSTO.ESTRATEGICO)) {
            this.encomenda(qtdAditivo, qtdGasolina, qtdAlcool / 2, qtdAlcool / 2, 100);
        } else if ((this.situacao == SITUACAO.EMERGENCIA) && (tipoPosto == TIPOPOSTO.COMUM)) {
            int[] fodeo = { -14 };
            return fodeo;
        } else if ((this.situacao == SITUACAO.EMERGENCIA) && (tipoPosto == TIPOPOSTO.ESTRATEGICO)) {
            this.encomenda(qtdAditivo, qtdGasolina, qtdAlcool / 2, qtdAlcool / 2, 50);
        }
        this.defineSituação();

        if (res) {
            return this.getQuantidadeTanques();
        }
        int[] numDeu = { -21 };
        return numDeu;

    }

    public SITUACAO getSituacao() {
        return situacao;
    }

    public int getAditivo() {
        return aditivo;
    }

    public int getGasolina() {
        return gasolina;
    }

    public int getAlcool() {
        return alcool;
    }

    public int getAlcool2() {
        return alcool2;
    }

    public int recebeAditivo(int qtdade) {
        if ((this.aditivo + qtdade) > MAX_ADITIVO) {
            int sobra = MAX_ADITIVO - this.aditivo;
            this.aditivo += sobra;
            return sobra;
        } else if (qtdade < 0) {
            return -1;
        } else {
            this.aditivo += qtdade;
            return qtdade;
        }
    }

    public int recebeGasolina(int qtdade) {
        if ((this.gasolina + qtdade) > MAX_GASOLINA) {
            int sobra = MAX_GASOLINA - this.gasolina;
            this.gasolina += sobra;
            return sobra;
        } else if (qtdade < 0) {
            return -1;
        } else {
            this.gasolina += qtdade;
            return qtdade;
        }
    }

    public int recebeAlcool(int qtdade) {
        if ((this.alcool + qtdade) > MAX_ALCOOL) {
            int sobra = MAX_ALCOOL - this.alcool;
            this.alcool += sobra;
            this.alcool2 += sobra;
            return sobra;
        } else if (qtdade < 0) {
            return -1;
        } else {
            this.alcool += qtdade;
            this.alcool2 += qtdade;
            return qtdade;
        }
    }

    private boolean validaEntradas(int aditivo, int gasolina, int alcool, int alcool2) {
        return (alcool != alcool2) ||
                (gasolina > MAX_GASOLINA || gasolina <= 0) ||
                (aditivo > MAX_ADITIVO || aditivo <= 0) ||
                (alcool > MAX_ALCOOL || alcool <= 0);
    }
}