package linkedlist;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Lista circular doblemente enlazada.
 *
 * Cada nodo tiene un apuntador al siguiente (sig) y otro al anterior (ant).
 * La lista es circular: la cola apunta con sig a la cabeza y la cabeza
 * apunta con ant a la cola. Por eso solo se guarda "inicio" (cabeza) y la
 * cola siempre es inicio.ant.
 *
 * Ademas de las operaciones de iList, la lista mantiene un cursor interno
 * (actual) que se mueve con next(), previous(), irInicio() e irFinal().
 * Al avanzar desde la cola el cursor vuelve a la cabeza, y al retroceder
 * desde la cabeza el cursor pasa a la cola.
 *
 * @author edera
 */
public class Listaenlazada<T> implements iList<T>, Iterable<T>{
protected NodoDoble<T> inicio;   // cabeza
protected NodoDoble<T> actual;   // cursor de iteracion
protected int nElementos;

    public Listaenlazada(){
        inicio = null;
        actual = null;
        nElementos = 0;
    }

    private static class NodoDoble<E>{
        private E dato;
        private NodoDoble<E> sig;
        private NodoDoble<E> ant;

        public NodoDoble(E dato){
            this.dato = dato;
        }
    }

    @Override
    public void append(T elemento) throws ListException {
        NodoDoble<T> nodoNuevo = new NodoDoble<>(elemento);

        if(inicio == null){
            // unico nodo: se apunta a si mismo en ambas direcciones
            nodoNuevo.sig = nodoNuevo;
            nodoNuevo.ant = nodoNuevo;
            inicio = nodoNuevo;
            actual = nodoNuevo;
        }else{
            NodoDoble<T> cola = inicio.ant;
            nodoNuevo.ant = cola;
            nodoNuevo.sig = inicio;
            cola.sig = nodoNuevo;
            inicio.ant = nodoNuevo;
        }

        nElementos++;
    }

    @Override
    public void insert(T elemento, int index) throws ListException {
        if (index < 0 || index > nElementos) {
            throw new ListException("Indice de insercion fuera de rango: " + index);
        }

        if (index == nElementos) {
            append(elemento);   // incluye el caso de lista vacia
            return;
        }

        NodoDoble<T> nodoNuevo = new NodoDoble<>(elemento);
        NodoDoble<T> siguiente = nodoEn(index);   // el nodo que quedara despues del nuevo
        NodoDoble<T> anterior = siguiente.ant;

        nodoNuevo.sig = siguiente;
        nodoNuevo.ant = anterior;
        anterior.sig = nodoNuevo;
        siguiente.ant = nodoNuevo;

        if (index == 0) {
            inicio = nodoNuevo;
        }
        nElementos++;
    }

    @Override
    public T remove(int index) throws ListException {
        validarIndice(index);
        NodoDoble<T> eliminado = nodoEn(index);

        if (nElementos == 1) {
            inicio = null;
            actual = null;
        } else {
            eliminado.ant.sig = eliminado.sig;
            eliminado.sig.ant = eliminado.ant;
            if (eliminado == inicio) {
                inicio = eliminado.sig;
            }
            if (eliminado == actual) {
                actual = eliminado.sig;   // el cursor avanza al siguiente
            }
        }
        nElementos--;
        return eliminado.dato;
    }

    @Override
    public boolean removeObj(T elemento) throws ListException {
        int index = indexOf(elemento);
        if (index < 0) {
            return false;
        }
        remove(index);
        return true;
    }

    @Override
    public int indexOf(T elemento) {
        NodoDoble<T> nodo = inicio;
        for (int i = 0; i < nElementos; i++) {   // circular: se recorre por cantidad, no hasta null
            if (Objects.equals(nodo.dato, elemento)) {
                return i;
            }
            nodo = nodo.sig;
        }
        return -1;
    }

    @Override
    public T get(int index) throws ListException {
        validarIndice(index);
        return nodoEn(index).dato;
    }

    @Override
    public void set(T elemento, int index) throws ListException {
        validarIndice(index);
        nodoEn(index).dato = elemento;
    }

    @Override
    public void clear() {
        inicio = null;
        actual = null;
        nElementos = 0;
    }

    @Override
    public boolean empty() {
        return nElementos == 0;
    }

    @Override
    public int size() {
        return nElementos;
    }

    // ------------------------------------------------------------------
    // Iteracion con cursor circular
    // ------------------------------------------------------------------

    /**
     * Coloca el cursor en el primer elemento (cabeza).
     */
    @Override
    public void irInicio() {
        actual = inicio;
    }

    /**
     * Coloca el cursor en el ultimo elemento (cola).
     */
    @Override
    public void irFinal() {
        actual = (inicio == null) ? null : inicio.ant;
    }

    /**
     * Mueve el cursor al elemento siguiente. Si estaba en la cola, vuelve
     * a la cabeza.
     */
    @Override
    public void next() throws ListException {
        validarCursor();
        actual = actual.sig;
    }

    /**
     * Mueve el cursor al elemento anterior. Si estaba en la cabeza, pasa
     * a la cola.
     */
    @Override
    public void previous() throws ListException {
        validarCursor();
        actual = actual.ant;
    }

    /**
     * Devuelve el elemento al que apunta el cursor (no lo mueve).
     */
    @Override
    public T actual() throws ListException {
        validarCursor();
        return actual.dato;
    }

    @Override
    public java.util.Iterator<T> Iterator() {
        return iterator();
    }

    /**
     * Iterador para el for-each. Aunque la lista es circular, recorre cada
     * elemento una sola vez (de la cabeza a la cola) y termina. No usa ni
     * modifica el cursor de la lista.
     */
    @Override
    public java.util.Iterator<T> iterator() {
        return new Iterator<T>() {
            private NodoDoble<T> nodo = inicio;
            private int recorridos = 0;

            @Override
            public boolean hasNext() {
                return recorridos < nElementos;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T dato = nodo.dato;
                nodo = nodo.sig;
                recorridos++;
                return dato;
            }
        };
    }

    /**
     * Invierte el orden de los elementos de la lista (en sitio, O(n)).
     * Se intercambian los apuntadores sig/ant de cada nodo y la nueva
     * cabeza es la antigua cola. El cursor sigue en el mismo elemento.
     */
    @Override
    public void invertir() {
        if (nElementos < 2) {
            return;
        }
        NodoDoble<T> nodo = inicio;
        for (int i = 0; i < nElementos; i++) {
            NodoDoble<T> siguiente = nodo.sig;
            nodo.sig = nodo.ant;
            nodo.ant = siguiente;
            nodo = siguiente;
        }
        inicio = inicio.sig;   // tras el intercambio, inicio.sig es la antigua cola
    }

    /**
     * Agrega al final todos los elementos de otra lista.
     * La otra lista no se modifica (se copian sus datos en nodos nuevos).
     * Si se concatena una lista consigo misma, se duplica su contenido.
     */
    @Override
    public void concatenar(Listaenlazada<T> otraLista) throws ListException {
        if (otraLista == null) {
            throw new ListException("La lista a concatenar es null");
        }
        int cantidad = otraLista.nElementos; // tamaño fijo: evita ciclo infinito si otraLista == this
        NodoDoble<T> origen = otraLista.inicio;
        for (int i = 0; i < cantidad; i++) {
            append(origen.dato);
            origen = origen.sig;
        }
    }

    private void validarIndice(int index) {
        if (index < 0 || index >= nElementos) {
            throw new ListException("Indice fuera de rango: " + index);
        }
    }

    private void validarCursor() {
        if (actual == null) {
            throw new ListException("La lista esta vacia");
        }
    }

    /**
     * Busca el nodo en la posicion index. Al ser doblemente enlazada y
     * circular, se recorre desde la cabeza o desde la cola segun cual
     * quede mas cerca.
     */
    private NodoDoble<T> nodoEn(int index) {
        NodoDoble<T> nodo;
        if (index <= nElementos / 2) {
            nodo = inicio;
            for (int i = 0; i < index; i++) {
                nodo = nodo.sig;
            }
        } else {
            nodo = inicio.ant;
            for (int i = nElementos - 1; i > index; i--) {
                nodo = nodo.ant;
            }
        }
        return nodo;
    }

}
