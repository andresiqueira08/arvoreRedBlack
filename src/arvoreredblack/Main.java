package arvoreredblack;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        RbTree arvore = new RbTree();
        int opcao;

        do {
            System.out.println("1 - Inserir");
            System.out.println("2 - Passeio em ordem");
            System.out.println("3 - Passeio por nível");
            System.out.println("4 - Remover");
            System.out.println("5 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();

            switch(opcao) {
                case 1:
                    System.out.print("Digite o valor a ser inserido: ");
                    int valor = sc.nextInt();
                    arvore.inserir(valor);
                    System.out.println("Valor inserido com sucesso!");
                    break;

                case 2:
                    System.out.println("\nExibição em ordem:");
                    arvore.emOrdem();
                    System.out.println();
                    break;

                case 3:
                    System.out.println("\nExibição por nível:");
                    arvore.porNivel();
                    System.out.println();
                    break;

                    case 4:
                    try {
                            System.out.print("Digite o valor a ser removido: ");
                            int valRem = sc.nextInt();
                            arvore.remover(valRem);
                            System.out.println("Remoção concluída!");
                        } catch (Exception e) {
                            System.out.println(" Erro ao remover: " + e.getMessage());
            }
                    break;

                case 5:
                    System.out.println("Saindo do programa...");
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
                    break;
            }
        } while(opcao != 4);

        sc.close();
    }
}
