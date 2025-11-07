package arvoreredblack;

import java.util.LinkedList;
import java.util.Queue;

class RbTree {
    private Node root;

    public void inserir(int valor) {
        Node novo = new Node(valor);

        // Caso 1: árvore vazia: novo nó vira raiz preta
        if (root == null) {
            root = novo;
            root.setRed(false);
            return;
        }

        // Busca a posição correta para inserir o novo nó
        Node atual = root;
        Node pai = null;

        while (atual != null) {
            pai = atual;
            if (valor < atual.getInfo()) {
                atual = atual.getLeft();
            } else if (valor > atual.getInfo()) {
                atual = atual.getRight();
            } else {
                // Valor já existe: nada a fazer
                return;
            }
        }

        // Define o pai
        novo.setParent(pai);

        // Insere o nó
        if (valor < pai.getInfo()) {
            pai.setLeft(novo);
        } else {
            pai.setRight(novo);
        }

        // Corrige propriedades da Red-Black Tree
        corrigirInsercao(novo);
    }

    private void corrigirInsercao(Node novo) {
        while (novo != root && novo.getParent().isRed()) {
            Node pai = novo.getParent();
            Node avo = pai.getParent();

            if (avo == null) {
                break;
            }

            // Pai está à esquerda do avô
            if (pai == avo.getLeft()) {
                Node tio = avo.getRight();

                // Caso 1: tio vermelho → recoloração
                if (tio != null && tio.isRed()) {
                    pai.setRed(false);
                    tio.setRed(false);
                    avo.setRed(true);
                    novo = avo;
                } else {
                    // Caso 2: nó é filho direito → rotação esquerda
                    if (novo == pai.getRight()) {
                        novo = pai;
                        rotacaoEsquerda(novo);
                        pai = novo.getParent();
                    }
                    // Caso 3: nó é filho esquerdo → rotação direita
                    pai.setRed(false);
                    avo.setRed(true);
                    rotacaoDireita(avo);
                }
            } 
            // Pai está à direita do avô
            else {
                Node tio = avo.getLeft();

                if (tio != null && tio.isRed()) {
                    pai.setRed(false);
                    tio.setRed(false);
                    avo.setRed(true);
                    novo = avo;
                } else {
                    if (novo == pai.getLeft()) {
                        novo = pai;
                        rotacaoDireita(novo);
                        pai = novo.getParent();
                    }
                    pai.setRed(false);
                    avo.setRed(true);
                    rotacaoEsquerda(avo);
                }
            }
        }

        root.setRed(false); // raiz sempre preta
    }

    private void rotacaoEsquerda(Node x) {
        Node y = x.getRight();
        x.setRight(y.getLeft());
        if (y.getLeft() != null) y.getLeft().setParent(x);

        y.setParent(x.getParent());
        if (x.getParent() == null) {
            root = y;
        } else if (x == x.getParent().getLeft()) {
            x.getParent().setLeft(y);
        } else {
            x.getParent().setRight(y);
        }

        y.setLeft(x);
        x.setParent(y);
    }

    private void rotacaoDireita(Node y) {
        Node x = y.getLeft();
        y.setLeft(x.getRight());
        if (x.getRight() != null) x.getRight().setParent(y);

        x.setParent(y.getParent());
        if (y.getParent() == null) {
            root = x;
        } else if (y == y.getParent().getLeft()) {
            y.getParent().setLeft(x);
        } else {
            y.getParent().setRight(x);
        }

        x.setRight(y);
        y.setParent(x);
    }

    
    //Remoção:
    public void remover(int valor) {
        Node z = buscarNode(root, valor); // busca o nó com o valor desejado
        if (z == null) return; // se não encontrou, nada é feito

        Node y = z; // y será usado para armazenar o nó que realmente será removido
        boolean yOriginalCorVermelha = y.isRed(); // guarda a cor original do nó y
        Node x; // x será o nó que vai substituir y na árvore (pode ser null)

        // Caso 1: nó tem no máximo 1 filho à direita
        if (z.getLeft() == null) {
            x = z.getRight(); // filho direito (pode ser null)
            transplantar(z, z.getRight()); // substitui z por seu filho direito
        }
        // Caso 2: nó tem apenas filho à esquerda
        else if (z.getRight() == null) {
            x = z.getLeft(); // filho esquerdo (pode ser null)
            transplantar(z, z.getLeft()); // substitui z por seu filho esquerdo
        }
        // Caso 3: nó tem dois filhos → substitui pelo sucessor
        else {
            y = minimo(z.getRight()); // encontra o menor nó da subárvore direita (sucessor)
            yOriginalCorVermelha = y.isRed(); // guarda cor original do sucessor
            x = y.getRight(); // pode ser null

            if (y.getParent() == z) { // se o sucessor é filho direto de z
                if (x != null)
                    x.setParent(y); // atualiza o pai de x
            } else {
                transplantar(y, y.getRight()); // move o filho direito do sucessor para o lugar dele
                y.setRight(z.getRight()); // conecta o sucessor ao filho direito de z
                y.getRight().setParent(y); // atualiza o pai do filho direito
            }

            transplantar(z, y); // substitui z por y (o sucessor)
            y.setLeft(z.getLeft()); // conecta o filho esquerdo de z em y
            y.getLeft().setParent(y); // atualiza o pai do filho esquerdo
            y.setRed(z.isRed()); // mantém a cor original de z
        }

        // Se o nó removido (ou o substituto) era preto, corrige a árvore
        if (!yOriginalCorVermelha) {
            corrigirRemocao(x);
        }
    }

    // ---------------------------------------------------
    // MÉTODO PARA "TRANSPLANTAR" NÓS (substitui um nó por outro)
    // ---------------------------------------------------
    private void transplantar(Node u, Node v) {
        if (u.getParent() == null) {
            root = v; // se u era a raiz, v vira a nova raiz
        } else if (u == u.getParent().getLeft()) {
            u.getParent().setLeft(v); // se u era filho esquerdo, substitui à esquerda
        } else {
            u.getParent().setRight(v); // se u era filho direito, substitui à direita
        }

        if (v != null) {
            v.setParent(u.getParent()); // atualiza o pai do novo nó v
        }
    }

    // ---------------------------------------------------
    // CORREÇÃO APÓS REMOÇÃO (caso o nó removido fosse preto)
    // ---------------------------------------------------
    private void corrigirRemocao(Node x) {
    // Enquanto não for raiz e (x é preto ou nulo)
    while (x != root && (x == null || !x.isRed())) {
        Node pai = (x != null) ? x.getParent() : null; // evita NullPointer

        // Se x for nulo, precisamos de uma referência ao pai para continuar
        if (pai == null) break;

        if (x == pai.getLeft()) { // x é filho esquerdo
            Node w = pai.getRight(); // irmão de x

            if (w != null && w.isRed()) { // Caso 1: irmão vermelho
                w.setRed(false);
                pai.setRed(true);
                rotacaoEsquerda(pai);
                w = pai.getRight();
            }

            if (w == null) {
                x = pai;
                continue;
            }

            // Caso 2: ambos os filhos do irmão são pretos
            if ((w.getLeft() == null || !w.getLeft().isRed()) &&
                (w.getRight() == null || !w.getRight().isRed())) {
                w.setRed(true);
                x = pai;
            } else {
                // Caso 3: irmão tem filho esquerdo vermelho e direito preto
                if (w.getRight() == null || !w.getRight().isRed()) {
                    if (w.getLeft() != null) w.getLeft().setRed(false);
                    w.setRed(true);
                    rotacaoDireita(w);
                    w = pai.getRight();
                }

                // Caso 4: irmão tem filho direito vermelho
                w.setRed(pai.isRed());
                pai.setRed(false);
                if (w.getRight() != null) w.getRight().setRed(false);
                rotacaoEsquerda(pai);
                x = root;
            }
        } else { // x é filho direito (espelho)
            Node w = pai.getLeft();

            if (w != null && w.isRed()) {
                w.setRed(false);
                pai.setRed(true);
                rotacaoDireita(pai);
                w = pai.getLeft();
            }

            if (w == null) {
                x = pai;
                continue;
            }

            if ((w.getRight() == null || !w.getRight().isRed()) &&
                (w.getLeft() == null || !w.getLeft().isRed())) {
                w.setRed(true);
                x = pai;
            } else {
                if (w.getLeft() == null || !w.getLeft().isRed()) {
                    if (w.getRight() != null) w.getRight().setRed(false);
                    w.setRed(true);
                    rotacaoEsquerda(w);
                    w = pai.getLeft();
                }

                w.setRed(pai.isRed());
                pai.setRed(false);
                if (w.getLeft() != null) w.getLeft().setRed(false);
                rotacaoDireita(pai);
                x = root;
            }
        }
    }

    if (x != null) x.setRed(false); // garante que o nó final é preto
}


    // ---------------------------------------------------
    // FUNÇÃO AUXILIAR: BUSCA DE UM NÓ PELO VALOR
    // ---------------------------------------------------
    private Node buscarNode(Node raiz, int valor) {
        while (raiz != null) {
            if (valor < raiz.getInfo()) raiz = raiz.getLeft();
            else if (valor > raiz.getInfo()) raiz = raiz.getRight();
            else return raiz; // encontrou
        }
        return null;
    }

    // ---------------------------------------------------
    // FUNÇÃO AUXILIAR: ENCONTRA O MENOR NÓ (SUCESSOR)
    // ---------------------------------------------------
    private Node minimo(Node no) {
        while (no.getLeft() != null) {
            no = no.getLeft();
        }
        return no;
    }

    // Passeios:

    public void emOrdem() {
        percorrerEmOrdem(root);
        System.out.println();
    }

    private void percorrerEmOrdem(Node r) {
        if (r != null) {
            percorrerEmOrdem(r.getLeft());
            System.out.print(r.getInfo() + (r.isRed() ? "(RED) " : "(BLACK) "));
            percorrerEmOrdem(r.getRight());
        }
    }

    public void porNivel() {
        if (root == null) return;
        Queue<Node> fila = new LinkedList<>();
        fila.add(root);
        while (!fila.isEmpty()) {
            Node atual = fila.poll();
            System.out.print(atual.getInfo() + (atual.isRed() ? "(RED) " : "(BLACK) "));
            if (atual.getLeft() != null){ 
                fila.add(atual.getLeft());
            }
            if (atual.getRight() != null){ 
                fila.add(atual.getRight());
            }
        }
        System.out.println();
    }
}