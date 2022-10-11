package com.grupo.verival;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.grupo.verival.CentroDistribuicao.SITUACAO;
import com.grupo.verival.CentroDistribuicao.TIPOPOSTO;

public class CentroDistribuicaoTest {
    @ParameterizedTest
    @CsvSource({
            "500,10000,2500,NORMAL",
            "500,10000,1252,NORMAL",
            "500,10000,1250,NORMAL",
            "500,5001,2500,NORMAL",
            "500,5000,2500,NORMAL",
            "251,10000,2500,NORMAL",
            "250,10000,2500,NORMAL",
            "500,4999,2500,SOBRAVISO",
            "500,2500,2500,SOBRAVISO",
            "500,4999,2500,SOBRAVISO",
            "249,10000,2500,SOBRAVISO",
            "500,10000,1248,SOBRAVISO",
            "500,10000,624,EMERGENCIA",
            "500,2499,2500,EMERGENCIA",
            "124,10000,2500,EMERGENCIA",
    })
    public void testaSituacao(int aditivo, int gasolina, int alcool, SITUACAO esperado) {
        CentroDistribuicao cd = new CentroDistribuicao(aditivo, gasolina, alcool / 2, alcool / 2);
        cd.defineSituacao();
        assertEquals(esperado, cd.getSituacao());
    }

    @ParameterizedTest
    @CsvSource({
            "500,500,1000,500",
            "500,0,500,-1",
            "9999,2,10000,1",
    })
    public void testaAbastecimentoGasolina(int inicial, int adicional, int esperado, int resto) {
        CentroDistribuicao cd = new CentroDistribuicao(CentroDistribuicao.MAX_ADITIVO, inicial,
                CentroDistribuicao.MAX_ALCOOL / 2, CentroDistribuicao.MAX_ALCOOL / 2);
        int abastecido = cd.recebeGasolina(adicional);
        assertEquals(esperado, cd.gettGasolina());
        assertEquals(resto, abastecido);
    }

    @ParameterizedTest
    @CsvSource({
            "100,400,500,400",
            "100,0,100,-1",
            "499,2,500,1",
    })
    public void testaAbastecimentoAditivo(int inicial, int adicional, int esperado, int resto) {
        CentroDistribuicao cd = new CentroDistribuicao(inicial, CentroDistribuicao.MAX_GASOLINA,
                CentroDistribuicao.MAX_ALCOOL / 2, CentroDistribuicao.MAX_ALCOOL / 2);
        int abastecido = cd.recebeAditivo(adicional);
        assertEquals(esperado, cd.gettAditivo());
        assertEquals(resto, abastecido);
    }

    @ParameterizedTest
    @CsvSource({
            "1000,500,750,500",
            "500,0,250,-1",
            "2498,4,1250,2",
    })
    public void testaAbastecimentoAlcool(int inicial, int adicional, int esperado, int resto) {
        CentroDistribuicao cd = new CentroDistribuicao(CentroDistribuicao.MAX_ADITIVO, CentroDistribuicao.MAX_GASOLINA,
                inicial / 2, inicial / 2);
        int abastecido = cd.recebeAlcool(adicional);
        assertEquals(esperado, cd.gettAlcool1());
        assertEquals(esperado, cd.gettAlcool2());
        assertEquals(resto, abastecido);
    }

    @Test
    public void testaEncomendaCombustivelNormalComum() {
        CentroDistribuicao cd = new CentroDistribuicao(CentroDistribuicao.MAX_ADITIVO, CentroDistribuicao.MAX_GASOLINA,
                CentroDistribuicao.MAX_ALCOOL / 2, CentroDistribuicao.MAX_ALCOOL / 2);
        cd.defineSituacao();
        int quantidade = 1000;
        TIPOPOSTO tipo = TIPOPOSTO.COMUM;
        int[] esperado = { 0, 450, 9300, 1125, 1125 };
        int[] resultado = cd.encomendaCombustivel(quantidade, tipo);
        assertEquals(esperado[0], resultado[0]);
        assertEquals(esperado[1], resultado[1]);
        assertEquals(esperado[2], resultado[2]);
        assertEquals(esperado[3], resultado[3]);
        assertEquals(esperado[4], resultado[4]);
    }

    @Test
    public void testaEncomendaCombustivelNormalEstrategico() {
        CentroDistribuicao cd = new CentroDistribuicao(CentroDistribuicao.MAX_ADITIVO, CentroDistribuicao.MAX_GASOLINA,
                CentroDistribuicao.MAX_ALCOOL / 2, CentroDistribuicao.MAX_ALCOOL / 2);
        cd.defineSituacao();
        int quantidade = 1000;
        TIPOPOSTO tipo = TIPOPOSTO.ESTRATEGICO;
        int[] esperado = { 0, 450, 9300, 1125, 1125 };
        int[] resultado = cd.encomendaCombustivel(quantidade, tipo);
        assertEquals(esperado[0], resultado[0]);
        assertEquals(esperado[1], resultado[1]);
        assertEquals(esperado[2], resultado[2]);
        assertEquals(esperado[3], resultado[3]);
        assertEquals(esperado[4], resultado[4]);
    }

    @Test
    public void testaEncomendaCombustivelSobravisoComum() {
        CentroDistribuicao cd = new CentroDistribuicao((CentroDistribuicao.MAX_ADITIVO / 2) - 1, CentroDistribuicao.MAX_GASOLINA,
                CentroDistribuicao.MAX_ALCOOL / 2, CentroDistribuicao.MAX_ALCOOL / 2);
        cd.defineSituacao();
        int quantidade = 400;
        TIPOPOSTO tipo = TIPOPOSTO.COMUM;
        int[] esperado = { 0, 239, 9860, 1225, 1225 };
        int[] resultado = cd.encomendaCombustivel(quantidade, tipo);
        assertEquals(esperado[0], resultado[0]);
        assertEquals(esperado[1], resultado[1]);
        assertEquals(esperado[2], resultado[2]);
        assertEquals(esperado[3], resultado[3]);
        assertEquals(esperado[4], resultado[4]);
    }

    @Test
    public void testaEncomendaCombustivelSobravisoEstrategico() {
        CentroDistribuicao cd = new CentroDistribuicao((CentroDistribuicao.MAX_ADITIVO / 2) - 1, CentroDistribuicao.MAX_GASOLINA,
                CentroDistribuicao.MAX_ALCOOL / 2, CentroDistribuicao.MAX_ALCOOL / 2);
        cd.defineSituacao();
        int quantidade = 400;
        TIPOPOSTO tipo = TIPOPOSTO.ESTRATEGICO;
        int[] esperado = { 0, 229, 9720, 1200, 1200 };
        int[] resultado = cd.encomendaCombustivel(quantidade, tipo);
        assertEquals(esperado[0], resultado[0]);
        assertEquals(esperado[1], resultado[1]);
        assertEquals(esperado[2], resultado[2]);
        assertEquals(esperado[3], resultado[3]);
        assertEquals(esperado[4], resultado[4]);
    }

    @Test
    public void testaEncomendaCombustivelEmergenciaComum() {
        CentroDistribuicao cd = new CentroDistribuicao((CentroDistribuicao.MAX_ADITIVO / 4) - 1, CentroDistribuicao.MAX_GASOLINA,
                CentroDistribuicao.MAX_ALCOOL / 2, CentroDistribuicao.MAX_ALCOOL / 2);
        cd.defineSituacao();
        int quantidade = 400;
        TIPOPOSTO tipo = TIPOPOSTO.COMUM;
        int[] esperado = { -14, (CentroDistribuicao.MAX_ADITIVO / 4) - 1, CentroDistribuicao.MAX_GASOLINA, CentroDistribuicao.MAX_ALCOOL / 2, CentroDistribuicao.MAX_ALCOOL / 2 };
        int[] resultado = cd.encomendaCombustivel(quantidade, tipo);
        assertEquals(esperado[0], resultado[0]);
        assertEquals(esperado[1], resultado[1]);
        assertEquals(esperado[2], resultado[2]);
        assertEquals(esperado[3], resultado[3]);
        assertEquals(esperado[4], resultado[4]);
    }

    @Test
    public void testaEncomendaCombustivelEmergenciaEstrategico() {
        CentroDistribuicao cd = new CentroDistribuicao((CentroDistribuicao.MAX_ADITIVO / 4) - 1, CentroDistribuicao.MAX_GASOLINA,
                CentroDistribuicao.MAX_ALCOOL / 2, CentroDistribuicao.MAX_ALCOOL / 2);
        cd.defineSituacao();
        int quantidade = 400;
        TIPOPOSTO tipo = TIPOPOSTO.ESTRATEGICO;
        int[] esperado = { 0, 104, 9720, 1200, 1200 };
        int[] resultado = cd.encomendaCombustivel(quantidade, tipo);
        assertEquals(esperado[0], resultado[0]);
        assertEquals(esperado[1], resultado[1]);
        assertEquals(esperado[2], resultado[2]);
        assertEquals(esperado[3], resultado[3]);
        assertEquals(esperado[4], resultado[4]);
    }
}
