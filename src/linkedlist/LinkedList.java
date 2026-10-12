package linkedlist;

/**
 *
 * @author edera
 */
public class LinkedList {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Listaenlazada<String> frutas = new Listaenlazada<>();
        frutas.append("manzana");
        frutas.append("pera");
        frutas.append("uva");

        frutas.insert("naranja", 1);
        System.out.println("Lista inicial: " + mostrar(frutas));
        System.out.println("Elemento en indice 1: " + frutas.get(1));
        System.out.println("Cantidad de elementos: " + frutas.size());

        System.out.println("Elemento eliminado: " + frutas.remove(2));
        System.out.println("Despues de remove: " + mostrar(frutas));

        frutas.clear();
        System.out.println("Despues de clear: tamaño=" + frutas.size()
                + ", vacia=" + frutas.empty());

        // ----- Puntos extra -----
        Listaenlazada<Integer> a = new Listaenlazada<>();
        for (int i = 1; i <= 4; i++) {
            a.append(i);
        }
        System.out.println("Lista a: " + mostrar(a));
        a.invertir();
        System.out.println("a invertida: " + mostrar(a));

        Listaenlazada<Integer> b = new Listaenlazada<>();
        b.append(10);
        b.append(20);
        a.concatenar(b);
        System.out.println("a + b: " + mostrar(a) + " (tamaño=" + a.size() + ")");

        // ----- Lista circular y doblemente enlazada -----
        System.out.println();
        System.out.println("=== Lista circular doblemente enlazada ===");
        Listaenlazada<String> dias = new Listaenlazada<>();
        dias.append("Lunes");
        dias.append("Martes");
        dias.append("Miercoles");
        dias.append("Jueves");
        System.out.println("Lista: " + mostrar(dias));

        // El cursor inicia en la cabeza
        dias.irInicio();
        System.out.println("irInicio()  -> " + dias.actual());

        // Avanzar con next(): al pasar de la cola vuelve a la cabeza
        System.out.println("Avanzando con next() 6 veces (da la vuelta):");
        for (int i = 0; i < 6; i++) {
            dias.next();
            System.out.println("  next()     -> " + dias.actual());
        }

        // Retroceder con previous(): desde la cabeza pasa a la cola
        dias.irInicio();
        System.out.println("irInicio()  -> " + dias.actual());
        dias.previous();
        System.out.println("previous() desde la cabeza -> " + dias.actual() + " (la cola)");

        dias.irFinal();
        System.out.println("irFinal()   -> " + dias.actual());
        dias.next();
        System.out.println("next() desde la cola       -> " + dias.actual() + " (la cabeza)");

        // Recorrer hacia atras toda la lista usando previous()
        dias.irFinal();
        StringBuilder reversa = new StringBuilder("Recorrido hacia atras: ");
        for (int i = 0; i < dias.size(); i++) {
            reversa.append(dias.actual());
            if (i < dias.size() - 1) {
                reversa.append(" <- ");
            }
            dias.previous();
        }
        System.out.println(reversa);

        // Con un solo elemento la lista se apunta a si misma
        Listaenlazada<Integer> uno = new Listaenlazada<>();
        uno.append(42);
        uno.next();
        System.out.println("Un solo elemento, next()     -> " + uno.actual());
        uno.previous();
        System.out.println("Un solo elemento, previous() -> " + uno.actual());

        // Si se elimina el elemento del cursor, este pasa al siguiente
        dias.irFinal();
        dias.remove(dias.size() - 1);
        System.out.println("Se elimino la cola, cursor -> " + dias.actual()
                + " en " + mostrar(dias));

        // Cursor en lista vacia
        dias.clear();
        try {
            dias.next();
        } catch (ListException e) {
            System.out.println("next() en lista vacia -> ListException: " + e.getMessage());
        }
    }

    private static <T> String mostrar(Listaenlazada<T> lista) {
        StringBuilder resultado = new StringBuilder("[");
        boolean primero = true;
        for (T elemento : lista) {
            if (!primero) {
                resultado.append(", ");
            }
            resultado.append(elemento);
            primero = false;
        }
        return resultado.append(']').toString();
    }
    
}
