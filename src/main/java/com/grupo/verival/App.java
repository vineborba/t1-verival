package com.grupo.verival;

public class App {
    public static void main(String[] args) {
        CentroDistribuicao cd = new CentroDistribuicao(CentroDistribuicao.MAX_ADITIVO, CentroDistribuicao.MAX_GASOLINA,
                CentroDistribuicao.MAX_ALCOOL / 2, CentroDistribuicao.MAX_ALCOOL / 2);

        System.out.println(
                cd.gettAditivo());

    }
}
