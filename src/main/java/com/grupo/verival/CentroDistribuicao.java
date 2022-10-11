package com.grupo.verival;

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

    private SITUACAO situacao;
    private int gasolina;
    private int aditivo;
    private int alcool1;
    private int alcool2;

    public CentroDistribuicao(int tAditivo, int tGasolina, int tAlcool1, int tAlcool2) {
        this.situacao = SITUACAO.NORMAL;
        this.aditivo = tAditivo;
        this.gasolina = tGasolina;
        this.alcool1 = tAlcool1;
        this.alcool2 = tAlcool2;
    }

    private SITUACAO verificaGasolina() {
        double volume = this.gasolina / (MAX_GASOLINA * 1.0);
        if (volume >= 0.5) {
            return SITUACAO.NORMAL;
        }
        if (volume >= 0.25) {
            return SITUACAO.SOBRAVISO;
        }
        return SITUACAO.EMERGENCIA;
    }

    private SITUACAO verificaAditivo() {
        double volume = this.aditivo / (MAX_ADITIVO * 1.0);
        if (volume >= 0.5) {
            return SITUACAO.NORMAL;
        }
        if (volume >= 0.25) {
            return SITUACAO.SOBRAVISO;
        }
        return SITUACAO.EMERGENCIA;
    }

    private SITUACAO verificaAlcool() {
        double volume = (this.alcool1 + this.alcool2) / (MAX_ALCOOL* 1.0);
        if (volume >= 0.5) {
            return SITUACAO.NORMAL;
        }
        if (volume >= 0.25) {
            return SITUACAO.SOBRAVISO;
        }
        return SITUACAO.EMERGENCIA;
    }

    public void defineSituacao() {
        SITUACAO[] situacoes = {
                this.verificaGasolina(),
                this.verificaAditivo(),
                this.verificaAlcool()
        };
        if (situacoes[0] == SITUACAO.EMERGENCIA || situacoes[1] == SITUACAO.EMERGENCIA
                || situacoes[2] == SITUACAO.EMERGENCIA) {
            this.situacao = SITUACAO.EMERGENCIA;
        } else if (situacoes[0] == SITUACAO.SOBRAVISO || situacoes[1] == SITUACAO.SOBRAVISO
                || situacoes[2] == SITUACAO.SOBRAVISO) {
            this.situacao = SITUACAO.SOBRAVISO;
        } else {
            this.situacao = SITUACAO.NORMAL;
        }
    }

    public SITUACAO getSituacao() {
        return this.situacao;
    }

    public int gettGasolina() {
        return this.gasolina;
    }

    public int gettAditivo() {
        return this.aditivo;
    }

    public int gettAlcool1() {
        return this.alcool1;
    }

    public int gettAlcool2() {
        return this.alcool2;
    }

    public int recebeAditivo(int qtdade) {
        if (qtdade < 0) {
            throw new IllegalArgumentException("Quantidade invalida");
        }
        if (qtdade == 0) {
            return -1;
        }

        int restante = CentroDistribuicao.MAX_ADITIVO - this.aditivo;
        if (restante >= qtdade) {
            this.aditivo += qtdade;
            defineSituacao();
            return qtdade;
        }
        int resto = qtdade - restante;
        int carga = qtdade - resto;
        this.aditivo += carga;
        defineSituacao();
        return carga;
    }

    public int recebeGasolina(int qtdade) {
        if (qtdade < 0) {
            throw new IllegalArgumentException("Quantidade invalida");
        }
        if (qtdade == 0) {
            return -1;
        }

        int restante = CentroDistribuicao.MAX_GASOLINA - this.gasolina;
        if (restante >= qtdade) {
            this.gasolina += qtdade;
            defineSituacao();
            return qtdade;
        }
        int resto = qtdade - restante;
        int carga = qtdade - resto;
        this.gasolina += carga;
        defineSituacao();
        return carga;
    }

    public int recebeAlcool(int qtdade) {
        if (qtdade < 0) {
            throw new IllegalArgumentException("Quantidade invalida");
        }
        if (qtdade == 0) {
            return -1;
        }
        int qtdade100 = qtdade * 100;
        int restante = CentroDistribuicao.MAX_ALCOOL - (this.alcool1 + this.alcool2);
        int restante100 = restante * 100;
        if (restante100 > qtdade100) {
            int metade100 = qtdade100 / 2;
            int metade = (int) Math.floor(metade100 / 100.0);
            this.alcool1 += metade;
            this.alcool2 += metade;
            defineSituacao();
            return metade * 2;
        }
        int resto100 = qtdade100 - restante100;
        int carga100 = qtdade100 - resto100;
        int metade100 = carga100 / 2;
        int metade = (int) Math.floor(metade100 / 100.0);
        this.alcool1 += metade;
        this.alcool2 += metade;
        defineSituacao();
        return metade * 2;
    }

    private void retiraGasolina(int quantidade) throws Exception {
        double quantidadeRetirada = 0.7 * quantidade;
        if (quantidadeRetirada > this.gasolina) {
            throw new Exception();
        }
        this.gasolina = (int) Math.ceil(this.gasolina - quantidadeRetirada);
    }

    private void retiraAditivo(int quantidade) throws Exception {
        double quantidadeRetirada = 0.05 * quantidade;
        if (quantidadeRetirada > this.aditivo) {
            throw new Exception();
        }
        this.aditivo = (int) Math.ceil(this.aditivo - quantidadeRetirada);
    }

    private void retiraAlcool(int quantidade) throws Exception {
        double quantidadeRetirada = (0.25 / 2) * quantidade;
        if (quantidadeRetirada > (this.alcool1 + this.alcool2)) {
            throw new Exception();
        }
        this.alcool1 = (int) Math.ceil(this.alcool1 - quantidadeRetirada);
        this.alcool2 = (int) Math.ceil(this.alcool2 - quantidadeRetirada);
    }

    public int[] encomendaCombustivel(int qtdade, TIPOPOSTO tipoPosto) {
        int[] quantidades = new int[5];
        double multiplicadorNormal = 1.0;
        double multiplicadorEstrategico = 1.0;
        int codigoErro = 0;

        // ! No caso de ser recebido um valor inválido por parâmetro deve-se retornar “-7”
        if (qtdade <= 0) {
            codigoErro = -7;
        }

        // ! se o pedido não puder ser atendido em função da “situação” retorna-se “-14”
        if (this.situacao == SITUACAO.EMERGENCIA && tipoPosto == TIPOPOSTO.COMUM) {
            codigoErro = -14;
            multiplicadorNormal = 0.0;
            multiplicadorEstrategico = 0.5;
        }

        if (this.situacao == SITUACAO.SOBRAVISO) {
            multiplicadorNormal = 0.5;
            multiplicadorEstrategico = 1.0;
        }

        double multiplicador = tipoPosto == TIPOPOSTO.COMUM ? multiplicadorNormal : multiplicadorEstrategico;
        int quantidadeCalculada = (int) Math.floor(qtdade * multiplicador);
        int bkpGasolina = this.gasolina;
        int bkpAditivo = this.aditivo;
        int bkpAlcool1 = this.alcool1;
        int bkpAlcool2 = this.alcool2;
        try {
            this.retiraAditivo(quantidadeCalculada);
            this.retiraAlcool(quantidadeCalculada);
            this.retiraGasolina(quantidadeCalculada);
        } catch (Exception e) {
            codigoErro = -21;
            this.gasolina = bkpGasolina;
            this.aditivo = bkpAditivo;
            this.alcool1 = bkpAlcool1;
            this.alcool2 = bkpAlcool2;
        }

        //! erro, aditivo, gasolina, álcool T1 e álcool T2
        quantidades[0] = codigoErro;
        quantidades[1] = this.aditivo;
        quantidades[2] = this.gasolina;
        quantidades[3] = this.alcool1;
        quantidades[4] = this.alcool2;
        defineSituacao();
        return quantidades;
    }
}